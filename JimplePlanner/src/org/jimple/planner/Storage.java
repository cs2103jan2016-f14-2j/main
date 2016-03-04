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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Storage {
	private static final String DEFAULT_FILE_DIRECTORY = "jimpleFiles"+File.separator;
	private static final String DEFAULT_FILE_NAME = "planner.jim";
	private static final String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	private static final String TAGS_CATEGORY = ":cat:";
	private static final String TAGS_DESCRIPTION = ":desc:";
	private static final String TAGS_FROM_TIME = ":from:";
	private static final String TAGS_TO_TIME = ":to:";
	private static final String TAGS_TITLE = ":title:";
	private static final String TAGS_LINE_FIELD_SEPARATOR = "/";
	private static final String EMPTY_STRING = "";
	private static final String FILE_SECTION_SEPARATOR = ">>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<";
	
	private File createFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	//Methods for creating filereaders and filewriters
	private BufferedReader createDefaultFileReader() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+DEFAULT_FILE_NAME;
		File file = createFile(filePath);
		FileInputStream fileIn = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		return reader;
	}
	
	private BufferedWriter createTempFileWriter() throws FileNotFoundException {
		String filePath = DEFAULT_FILE_DIRECTORY+DEFAULT_TEMP_FILE_NAME;
		File file = createFile(filePath);
		FileOutputStream fileOut = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		return writer;
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
		return !(task.getFromTime().length()==0);
	}
	
	private boolean isToTimeExist(Task task){
		return !(task.getToTime().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "/"
	private String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists) throws IOException{
		writeToFile(allTaskLists);
		boolean saveStatus = isSaveToFile();
		return saveStatus;
	}
	
	private void writeToFile(ArrayList<ArrayList<Task>> allTaskLists) throws IOException  {
		BufferedWriter tempWriter = createTempFileWriter();
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
	
	//this handles the deletion of files and the subsequent renaming of temporary file to the default filename
	private boolean isSaveToFile(){
		File file = createFile(DEFAULT_FILE_NAME);
		File tempFile = createFile(DEFAULT_TEMP_FILE_NAME);

		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	public ArrayList<ArrayList<Task>> getTasks() throws IOException{
		BufferedReader defaultFileReader = createDefaultFileReader();
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
