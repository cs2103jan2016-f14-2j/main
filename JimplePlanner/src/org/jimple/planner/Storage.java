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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Storage {
	private static final String DEFAULT_FILE_DIRECTORY = "jimpleFiles"+File.separator;
	private static final String DEFAULT_FILE_NAME = "planner.jim";
	private static final String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	private static final String TEST_FILE_NAME = "testplanner.jim";
	private static final String TEST_TEMP_FILE_NAME = "testtempplanner.jim";
	private static final String TAGS_CATEGORY = ":cat:";
	private static final String TAGS_DESCRIPTION = ":desc:";
	private static final String TAGS_FROM_TIME = ":from:";
	private static final String TAGS_TO_TIME = ":to:";
	private static final String TAGS_TITLE = ":title:";
	private static final String TAGS_LINE_FIELD_SEPARATOR = "/";
	private static final String EMPTY_STRING = "";
	private static final String FILE_SECTION_SEPARATOR = ">>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<";
	private static final boolean IS_TEST = true;
	private static final boolean IS_NOT_TEST = false;
	
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
	
	//Methods for creating filereaders and filewriters
	private BufferedReader createFileReader(String fileName) throws FileNotFoundException {
		File file = createFile(fileName);
		FileInputStream fileIn = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		return reader;
	}
	
	private BufferedWriter createFileWriter(String fileName) throws FileNotFoundException {
		File file = createFile(fileName);
		FileOutputStream fileOut = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		return writer;
	}
	
	private BufferedReader createDefaultFileReader() throws FileNotFoundException {
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
	private BufferedReader createTestFileReader() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+TEST_FILE_NAME;
		return createFileReader(filePath);
	}
	
	private BufferedWriter createTestTempFileWriter() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+TEST_TEMP_FILE_NAME;
		return createFileWriter(filePath);
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
			tempWriter.write(FILE_SECTION_SEPARATOR);
			tempWriter.newLine();
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
		ArrayList<ArrayList<Task>> allTasksLists = new ArrayList<ArrayList<Task>>();
		String fileLineContent;
		ArrayList<Task> taskList = new ArrayList<Task>();
		while ((fileLineContent = defaultFileReader.readLine()) != null) {
			if(!fileLineContent.equals(FILE_SECTION_SEPARATOR)){
				Task task = getTaskFromLine(fileLineContent);
				taskList.add(task);
			} else {
				allTasksLists.add(taskList);
				taskList = new ArrayList<Task>();
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
}
