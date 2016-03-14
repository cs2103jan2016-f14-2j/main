package org.jimple.planner;
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
import java.util.Properties;

public class Storage {
	private static final String DEFAULT_FILE_DIRECTORY = "jimpleFiles"+File.separator;
	private static final String DEFAULT_FILE_NAME = "planner.jim";
	private static final String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	private static final String TEST_FILE_NAME = "testplanner.jim";
	private static final String TEST_TEMP_FILE_NAME = "testtempplanner.jim";
	private static final String PROPERTIES_CONFIG_FILE_NAME = "jimpleConfig.properties";
	private static final String PROPERTIES_SAVEPATH_KEY_NAME = "savepath";
	
	private static final String FILEPATH_DEFAULT = DEFAULT_FILE_DIRECTORY + DEFAULT_FILE_NAME;
	private static final String FILEPATH_DEFAULT_TEMP = DEFAULT_FILE_DIRECTORY + DEFAULT_TEMP_FILE_NAME;
	private static final String FILEPATH_TEST = DEFAULT_FILE_DIRECTORY + TEST_FILE_NAME;
	private static final String FILEPATH_TEST_TEMP = DEFAULT_FILE_DIRECTORY + TEST_TEMP_FILE_NAME;
	private static final String FILEPATH_CONFIG = DEFAULT_FILE_DIRECTORY+PROPERTIES_CONFIG_FILE_NAME;
	
	private static final String TAGS_CATEGORY = ":cat:";
	private static final String TAGS_DESCRIPTION = ":desc:";
	private static final String TAGS_FROM_TIME = ":from:";
	private static final String TAGS_TO_TIME = ":to:";
	private static final String TAGS_TITLE = ":title:";
	private static final String TAGS_LINE_FIELD_SEPARATOR = "/";
	
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "floating";
	private static final String TYPE_DEADLINE = "deadline";
	private static final String EMPTY_STRING = "";
	private static final boolean IS_TEST = true;
	private static final boolean IS_NOT_TEST = false;
	
	private static Properties properties = null;
	private static String fileSavePath = null;
	
	//Constructor
	public Storage(){
		properties = loadProperties();
		fileSavePath = getSavePath();
	}
	
	public boolean setPath(String pathName){
		boolean setStatus = false;
		if(checkFilePath(pathName)){
			properties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, pathName);
			storeProperties();
			setStatus = true;
			//TODO Copy over the tasks current to new path
		}
		return setStatus;
	}
	
	private void storeProperties(){
		BufferedWriter configFileWriter = createFileWriter(FILEPATH_CONFIG);
		try {
			properties.store(configFileWriter, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getSavePath(){
		String savedPath = properties.getProperty(PROPERTIES_SAVEPATH_KEY_NAME);
		if(savedPath != null){
			savedPath += FILEPATH_DEFAULT;
		}
		return savedPath;
	}
	
	private boolean checkFilePath(String filePath){
        try {
            Paths.get(filePath);
            File fileDir = new File(filePath);
            return fileDir.isDirectory();
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
	
	private Properties loadProperties(){
		try {
			String configPath = FILEPATH_CONFIG;
			BufferedReader configFileReader = createFileReader(configPath);
			Properties properties = new Properties();
			properties.load(configFileReader);
			configFileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	private File createFile(String fileName) {
		File file = new File(fileName);
		File dir = new File(DEFAULT_FILE_DIRECTORY);
		try {
			dir.mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	/*private BufferedReader createDefaultFileReader() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+DEFAULT_FILE_NAME;
		return createFileReader(filePath);
	}
	
	private BufferedWriter createTempFileWriter() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+DEFAULT_TEMP_FILE_NAME;
		return createFileWriter(filePath);
	} 
	
	/*
	 * The following 2 methods are just used for Testing purposes
	 */
	/*
	private BufferedReader createTestFileReader() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+TEST_FILE_NAME;
		return createFileReader(filePath);
	}
	
	private BufferedWriter createTestTempFileWriter() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+TEST_TEMP_FILE_NAME;
		return createFileWriter(filePath);
	} */
	
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
	
	private boolean isSavedSelect(ArrayList<ArrayList<Task>> allTaskLists, boolean isTest) throws IOException{
		sortBeforeWritngToFile(allTaskLists);
		writeToFile(allTaskLists, isTest);
		boolean saveStatus = isSaveToFile(isTest);
		return saveStatus;
	}
	
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists) throws IOException{
		return isSavedSelect(allTaskLists, IS_NOT_TEST);
	}
	
	public boolean isSavedTest(ArrayList<ArrayList<Task>> allTaskLists) throws IOException{
		return isSavedSelect(allTaskLists, IS_TEST);
	}
	
	private void writeToFile(ArrayList<ArrayList<Task>> allTaskLists, boolean isTest) throws IOException  {
		BufferedWriter tempWriter = null;
		if(isTest){
			tempWriter = createTestTempFileWriter();
		} else {
			tempWriter = createTempFileWriter();
		}
		for(ArrayList<Task> taskList: allTaskLists){
			for(Task task: taskList){
				String lineString = extractTaskToString(task);
				tempWriter.write(lineString);
				tempWriter.newLine();
			}
		}
		tempWriter.close();
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
	private boolean isSaveToFile(boolean isTest){
		String filePath = null;
		String tempFilePath = null;
		if(isTest){
			filePath = DEFAULT_FILE_DIRECTORY+TEST_FILE_NAME;
			tempFilePath = DEFAULT_FILE_DIRECTORY+TEST_TEMP_FILE_NAME;
		} else {
			filePath = DEFAULT_FILE_DIRECTORY+DEFAULT_FILE_NAME;
			tempFilePath = DEFAULT_FILE_DIRECTORY+DEFAULT_TEMP_FILE_NAME;
		}
		File file = createFile(filePath);
		File tempFile = createFile(tempFilePath);
		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	private ArrayList<ArrayList<Task>> getTaskSelect(boolean isTest) throws IOException{
		BufferedReader defaultFileReader = null;
		if(isTest){
			defaultFileReader = createTestFileReader();
		} else {
			defaultFileReader = createDefaultFileReader();
		}
		ArrayList<ArrayList<Task>> allTasksLists = populateArrayList();
		String fileLineContent;
		while ((fileLineContent = defaultFileReader.readLine()) != null) {
			if(!fileLineContent.equals(EMPTY_STRING)){
				Task task = getTaskFromLine(fileLineContent);
				allocateTaskToArrayList(task, allTasksLists);
			}
		}
		defaultFileReader.close();
		return allTasksLists;
	}
	
	public ArrayList<ArrayList<Task>> getTasks() throws IOException{
		return getTaskSelect(IS_NOT_TEST);
	}
	
	public ArrayList<ArrayList<Task>> getTestTasks() throws IOException{
		return getTaskSelect(IS_TEST);
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
