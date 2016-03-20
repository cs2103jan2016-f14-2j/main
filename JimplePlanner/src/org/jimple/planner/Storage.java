package org.jimple.planner;

import static org.jimple.planner.Constants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Properties;

public class Storage {
	private static Properties properties = null;

	//Constructor
	public Storage(){
		properties = loadProperties();
	}
	
	public boolean setPath(String pathName){
		boolean setStatus = false;
		if(checkFilePath(pathName)){
			if(updateKeys(pathName)){
				storeProperties(properties);
				setStatus = copyToNewLocation();
			}
		}
		return setStatus;
	}
	
	//True if and only if the new key updated is the same
	private boolean updateKeys(String pathName){
		String previousPath = properties.getProperty(PROPERTIES_SAVEPATH_KEY_NAME);
		if(previousPath.equals(pathName)){
			return false;
		}
		properties.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, previousPath);
		properties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, pathName);
		return true;
	}
	
	private boolean copyToNewLocation(){
		String newDir = getCurrentFileDirectory();
		String oldDir = getOldFileDirectory();
		
		String newPath = getActualFilePath(newDir);
		String oldPath = getActualFilePath(oldDir);
		boolean saveStatus= false;
		
		ArrayList<ArrayList<Task>> consolidatedTasks = getConsolidatedTasks(newPath, oldPath);
		saveStatus = isSavedSelect(consolidatedTasks, newPath, oldPath);
		return saveStatus;
	}

	private ArrayList<ArrayList<Task>> getConsolidatedTasks(String newPath, String oldPath){
		ArrayList<ArrayList<Task>> oldPathTasks = getTaskSelect(oldPath);
		ArrayList<ArrayList<Task>> newPathTasks = getTaskSelect(newPath);
		ArrayList<ArrayList<Task>> consolidatedTasks = new ArrayList<ArrayList<Task>>();
		for(int i = 0; i < ALL_ARRAY_SIZE; i++){
			ArrayList<Task> newTasksByType = newPathTasks.get(i);
			ArrayList<Task> oldTasksByType = oldPathTasks.get(i);
			oldTasksByType.addAll(newTasksByType);
			
			HashSet<Task> removeDuplicatedTasksByType = new HashSet<Task>(oldTasksByType);
			ArrayList<Task> consolidatedTasksByType = new ArrayList<Task>(removeDuplicatedTasksByType);
			consolidatedTasks.add(consolidatedTasksByType);
		}
		return consolidatedTasks;
	}
	
	private String getFileDirectory(String filePath){
		String fileDir = properties.getProperty(filePath);
		if(fileDir.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			fileDir = EMPTY_STRING;
		}
		return fileDir;
	}
	
	
	
	private String getCurrentFileDirectory(){
		return getFileDirectory(PROPERTIES_SAVEPATH_KEY_NAME);
	}
	
	private String getOldFileDirectory(){
		return getFileDirectory(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME);
	}
	
	private String getFullFilePath(String fileSaveDir, String fileName) {
		String fileString = EMPTY_STRING;
		if(fileSaveDir.equals(EMPTY_STRING) || fileSaveDir.endsWith(File.separator)){
			fileString = fileSaveDir + fileName;
		} else {
			fileString = fileSaveDir + File.separator + fileName;
		}
		return fileString;
	}
	
	private String getActualFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT);
	}
	
	private String getActualTempFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT_TEMP);
	}
	
	private void storeProperties(Properties property){
		BufferedWriter configFileWriter = createFileWriter(FILEPATH_CONFIG);
		try {
			property.store(configFileWriter, PROPERTIES_COMMENT_HEADER);
			configFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkFilePath(String filePath){
		if(filePath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			return true;
		}
        try {
            Paths.get(filePath);
            File fileDir = new File(filePath);
            return fileDir.isDirectory();
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
	
	private Properties loadProperties(){
		String configPath = FILEPATH_CONFIG;
		BufferedReader configFileReader = createFileReader(configPath);
		Properties property = new Properties();;
		try {
			property.load(configFileReader);
				if(property.isEmpty()){
					propertyKeysInitialise(property);
					storeProperties(property);
				}
				configFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return property;
	}
	
	private void propertyKeysInitialise(Properties property){
		if(property.getProperty(PROPERTIES_SAVEPATH_KEY_NAME) == null){
			property.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, PROPERTIES_SAVEPATH_TO_CWD);
		}
		if(property.getProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME) == null){
			property.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, PROPERTIES_SAVEPATH_TO_CWD);
		}
	}
	
	private File createFile(String fileName) {
		File file = new File(fileName);
		
		assert !file.isDirectory();
		
		String dirPath = file.getAbsolutePath().replaceAll(file.getName(), EMPTY_STRING);
		File dir = new File(dirPath);
		dir.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	//Methods for creating file readers and file writers
	private BufferedReader createFileReader(String fileName){
		BufferedReader reader = null;
		try {
			File file = createFile(fileName);
			FileInputStream fileIn = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
			reader = new BufferedReader(inputStreamReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return reader;
	}
	
	private BufferedWriter createFileWriter(String fileName){
		BufferedWriter writer = null;
		try {
			File file = createFile(fileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
			writer = new BufferedWriter(outputStreamWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return writer;
	}
	
	private boolean isSavedSelect(ArrayList<ArrayList<Task>> allTaskLists, String filePath, String tempFilePath){
		assert allTaskLists.size() == 3;
		sortBeforeWritngToFile(allTaskLists);
		writeTasksToFile(allTaskLists, tempFilePath);
		boolean saveStatus = isSaveToFile(filePath, tempFilePath);
		return saveStatus;
	}
	
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists) {
		String fileSaveDir = getCurrentFileDirectory();
		String fileName = getActualFilePath(fileSaveDir);
		String tempFileName = getActualTempFilePath(fileSaveDir);
		return isSavedSelect(allTaskLists, fileName, tempFileName);
	}
	
	public boolean isSavedTest(ArrayList<ArrayList<Task>> allTaskLists){
		return isSavedSelect(allTaskLists, FILEPATH_TEST, FILEPATH_TEST_TEMP);
	}
	
	private void writeTasksToFile(ArrayList<ArrayList<Task>> allTaskLists, String tempFilePath){
		BufferedWriter tempWriter = createFileWriter(tempFilePath);
		try {
			for(ArrayList<Task> taskList: allTaskLists){
				for(Task task: taskList){
					String lineString = extractTaskToString(task);
					tempWriter.write(lineString);
					tempWriter.newLine();
				}
			}
			tempWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isSaveToFile(String filePath, String tempFilePath){
		File file = createFile(filePath);
		File tempFile = createFile(tempFilePath);
		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	//This method extracts all relevant fields from an Task and stores them as a String, each String line is an Task
		private String extractTaskToString(Task task){
			String lineString = formatToSaveString(TAGS_TITLE + task.getTitle());
			if(isDescriptionExist(task)){
				String descriptionString = formatToSaveString(TAGS_DESCRIPTION + task.getDescription());
				lineString = lineString + descriptionString;
			} 
			if(isCategoryExist(task)) {
				String categoryString = formatToSaveString(TAGS_CATEGORY + task.getCategory());
				lineString = lineString + categoryString;
			} 
			if (isFromTimeExist(task)){
				String fromTimeString = formatToSaveString(TAGS_FROM_TIME + task.getFromTime());
				lineString = lineString + fromTimeString;
			} 
			if (isToTimeExist(task)){
				String fromToString = formatToSaveString(TAGS_TO_TIME + task.getToTime());
				lineString = lineString + fromToString;
			}
			return lineString;
		}
		
	private boolean isDescriptionExist(Task task){
		return !(task.getDescription().length()==0);
	}
	
	private boolean isCategoryExist(Task task){
		return !(task.getCategory().length()==0);
	}

	private boolean isFromTimeExist(Task task){
		return !(task.getFromTimeString().length()==0);
	}
	
	private boolean isToTimeExist(Task task){
		return !(task.getToTimeString().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "/"
	private String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	
	private void sortBeforeWritngToFile(ArrayList<ArrayList<Task>> allTaskLists){
		sortDeadlines(allTaskLists);
		sortEvents(allTaskLists);
	}
	
	private void sortDeadlines(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> toDateComparator = Task.getToDateComparator();
		Collections.sort(allTaskLists.get(1), toDateComparator);
	}
	
	private void sortEvents(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> fromDateComparator = Task.getFromDateComparator();
		Collections.sort(allTaskLists.get(2), fromDateComparator);
	}
	
	//this handles the deletion of files and the subsequent renaming of temporary file to the default filename
	
	private ArrayList<ArrayList<Task>> getTaskSelect(String filePath){
		BufferedReader defaultFileReader = createFileReader(filePath);
		ArrayList<ArrayList<Task>> allTasksLists = populateArrayList();
		String fileLineContent;
		try {
			while ((fileLineContent = defaultFileReader.readLine()) != null) {
				if(!fileLineContent.equals(EMPTY_STRING)){
					Task task = getTaskFromLine(fileLineContent);
					allocateTaskToArrayList(task, allTasksLists);
				}
			}
			defaultFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allTasksLists;
	}
	
	public ArrayList<ArrayList<Task>> getTasks(){
		String fileSaveDir = getCurrentFileDirectory();
		String fileName = getActualFilePath(fileSaveDir);
		return getTaskSelect(fileName);
	}
	
	public ArrayList<ArrayList<Task>> getTestTasks(){
		return getTaskSelect(FILEPATH_TEST);
	}
	
	private ArrayList<ArrayList<Task>> populateArrayList(){
		ArrayList<ArrayList<Task>> allTasksLists = new ArrayList<ArrayList<Task>>();
		allTasksLists.add(new ArrayList<Task>());
		allTasksLists.add(new ArrayList<Task>());
		allTasksLists.add(new ArrayList<Task>());
		return allTasksLists;
	}
	
	private ArrayList<String> getSeparateFields(String fileLineContent){
		ArrayList<String> separatedContents = new ArrayList<String>(Arrays.asList(fileLineContent.split(TAGS_LINE_FIELD_SEPARATOR)));
		return separatedContents;
	}
	
	private Task getTaskFromLine(String fileLineContent){
		ArrayList<String> fileLineContentSeparated = getSeparateFields(fileLineContent);
		Task task = new Task(EMPTY_STRING);
		for(String field: fileLineContentSeparated){
			setFields(task, field);
		}
		return task;
	}
	
	//This method is purely for test purposes only
	public Task testGetTaskFromLine(String fileLineContent){
		return getTaskFromLine(fileLineContent);
	}
	
	//This method is used for unit test in StorageTest
	public String[] testExtractTasksToStringArray(Task task){
		String[] result = new String[6];
		result[0] = task.getTitle();
		result[1] = task.getDescription();
		result[2] = task.getCategory();
		result[3] = task.getFromTimeString();
		result[4] = task.getToTimeString();
		result[5] = task.getType();
		return result;
	}
	
	private void setFields(Task task, String field){
		if(isTitle(field)){
			String titleString = getRemovedTitleTagString(field);
			task.setTitle(titleString);
		} else if(isCategory(field)){
			String catField = getRemovedCategoryTagString(field);
			task.setCategory(catField);
		} else if(isDescription(field)){
			String descField = getRemovedDescriptionTagString(field);
			task.setDescription(descField);
		} else if(isFromTime(field)){
			String fromField = getRemovedFromTagString(field);
			task.setFromDate(fromField);
		} else if (isToTime(field)){
			String toField = getRemovedToTagString(field);
			task.setToDate(toField);
		}
	}
	
	private boolean isTitle(String field){
		return field.contains(TAGS_TITLE);
	}
	
	private boolean isCategory(String field){
		return field.contains(TAGS_CATEGORY);
	}
	
	private boolean isDescription(String field){
		return field.contains(TAGS_DESCRIPTION);
	}
	
	private boolean isFromTime(String field){
		return field.contains(TAGS_FROM_TIME);
	}
	
	private boolean isToTime(String field){
		return field.contains(TAGS_TO_TIME);
	}
	
	private String getRemovedTitleTagString(String field){
		String removedTag = field.replace(TAGS_TITLE, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedCategoryTagString(String field){
		String removedTag = field.replace(TAGS_CATEGORY, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedDescriptionTagString(String field){
		String removedTag = field.replace(TAGS_DESCRIPTION, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedFromTagString(String field){
		String removedTag = field.replace(TAGS_FROM_TIME, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedToTagString(String field){
		String removedTag = field.replace(TAGS_TO_TIME, EMPTY_STRING);
		return removedTag;
	}
	
	private void allocateTaskToArrayList(Task task, ArrayList<ArrayList<Task>> allTasksLists){
		String taskType = task.getType();
		switch(taskType){
		case TYPE_TODO:
			allTasksLists.get(0).add(task);
			break;
		case TYPE_DEADLINE:
			allTasksLists.get(1).add(task);
			break;
		case TYPE_EVENT:
			allTasksLists.get(2).add(task);
			break;
		}
	}
}
