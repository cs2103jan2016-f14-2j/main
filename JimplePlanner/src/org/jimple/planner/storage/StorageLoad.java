package org.jimple.planner.storage;

import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.FILEPATH_CONFIG;
import static org.jimple.planner.Constants.FILEPATH_TEST;
import static org.jimple.planner.Constants.FILEPATH_ARCHIVE;
import static org.jimple.planner.Constants.PROPERTIES_KEY_CURRENT_SAVEPATH;
import static org.jimple.planner.Constants.PROPERTIES_KEY_PREV_SAVEPATH;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_TO_CWD;
import static org.jimple.planner.Constants.TAGS_LABEL;
import static org.jimple.planner.Constants.TAGS_DESCRIPTION;
import static org.jimple.planner.Constants.TAGS_FROM_TIME;
import static org.jimple.planner.Constants.TAGS_LINE_FIELD_SEPARATOR;
import static org.jimple.planner.Constants.TAGS_LABEL_FIELD_SEPARATOR;
import static org.jimple.planner.Constants.TAGS_TITLE;
import static org.jimple.planner.Constants.TAGS_TO_TIME;
import static org.jimple.planner.Constants.TYPE_DEADLINE;
import static org.jimple.planner.Constants.TYPE_EVENT;
import static org.jimple.planner.Constants.TYPE_TODO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;

public class StorageLoad implements StorageLoadInterface{
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
	
	public ArrayList<ArrayList<Task>> getTaskSelect(String filePath){
		BufferedReader defaultFileReader = createFileReader(filePath);
		return getTaskByReader(defaultFileReader);
	}

	public ArrayList<Task> getArchivedTask(){
		String archivePath = FILEPATH_ARCHIVE;
		BufferedReader defaultFileReader = createFileReader(archivePath);
		ArrayList<Task> archivedTasks = new ArrayList<Task>();

		String fileLineContent;
		try {
			while ((fileLineContent = defaultFileReader.readLine()) != null) {
				if(!fileLineContent.equals(EMPTY_STRING)){
					Task task = getTaskFromLine(fileLineContent);
					checkTaskValidity(task);
					archivedTasks.add(task);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return archivedTasks;
	}
	
	private ArrayList<ArrayList<Task>> getTaskByReader(BufferedReader defaultFileReader) {
		ArrayList<ArrayList<Task>> allTasksLists = populateArrayList();
		String fileLineContent;
		try {
			while ((fileLineContent = defaultFileReader.readLine()) != null) {
				if(!fileLineContent.equals(EMPTY_STRING)){
					Task task = getTaskFromLine(fileLineContent);
					checkTaskValidity(task);
					allocateTaskToArrayList(task, allTasksLists);
				}
			}
			defaultFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Task.sortTasks(allTasksLists);
		return allTasksLists;
	}
	
	private ArrayList<ArrayList<Task>> populateArrayList(){
		ArrayList<ArrayList<Task>> allTasksLists = new ArrayList<ArrayList<Task>>();
		allTasksLists.add(new ArrayList<Task>());
		allTasksLists.add(new ArrayList<Task>());
		allTasksLists.add(new ArrayList<Task>());
		return allTasksLists;
	}
	
	private ArrayList<String> getSeparateFields(String fileLineContent){
		Pattern pattern = Pattern.compile(TAGS_LINE_FIELD_SEPARATOR);
		String[] separatedContentsArray = pattern.split(fileLineContent);
		ArrayList<String> separatedContents = new ArrayList<String>(Arrays.asList(separatedContentsArray));
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
	
	private void setFields(Task task, String field){
		if(isTitle(field)){
			String titleString = getRemovedTitleTagString(field);
			task.setTitle(titleString);
		} else if(isLabel(field)){
			//TODO label field
			ArrayList<String> labelStringArray = getRemovedLabelTagStringArray(field);
			TaskLabel taskLabel = TaskLabel.getDummyLabel(labelStringArray.get(0), Integer.parseInt(labelStringArray.get(1)));
			task.setTaskLabel(taskLabel);
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
	
	private boolean isLabel(String field){
		return field.contains(TAGS_LABEL);
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
	
	private ArrayList<String> getRemovedLabelTagStringArray(String field){
		String removedTag = field.replace(TAGS_LABEL, EMPTY_STRING);
		Pattern pattern = Pattern.compile(TAGS_LABEL_FIELD_SEPARATOR);
		String[] splitLabelFields = pattern.split(removedTag);
		ArrayList<String> splitLabelFieldsWithNoEmptyString = new ArrayList<String>();
		for(String string: splitLabelFields){
			if(string.equals(EMPTY_STRING) || string==null){
				continue;
			}
			splitLabelFieldsWithNoEmptyString.add(string);
		}
		return splitLabelFieldsWithNoEmptyString;
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
	
	public Properties loadProperties(){
		String configPath = FILEPATH_CONFIG;
		BufferedReader configFileReader = createFileReader(configPath);
		Properties property = new Properties();
		try {
			property.load(configFileReader);
				if(property.isEmpty()){
					propertyKeysInitialise(property);
				}
				configFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return property;
	}
	
	private void propertyKeysInitialise(Properties property){
		setKeys(property, PROPERTIES_KEY_CURRENT_SAVEPATH, PROPERTIES_SAVEPATH_TO_CWD);
		setKeys(property, PROPERTIES_KEY_PREV_SAVEPATH, PROPERTIES_SAVEPATH_TO_CWD);
	}
	
	private void setKeys(Properties property, String key, String value){
		if(property.getProperty(key) == null){
			property.setProperty(key, value);
		}
	}
	
	/*
	 * ALL TEST METHODS HERE
	 */
	public ArrayList<ArrayList<Task>> getTestTasks(){
		return getTaskSelect(FILEPATH_TEST);
	}
	
	public Task testGetTaskFromLine(String fileLineContent){
		return getTaskFromLine(fileLineContent);
	}
	
	public String[] testExtractTasksToStringArray(Task task){
		String[] result = new String[6];
		result[0] = task.getTitle();
		result[1] = task.getDescription();
		TaskLabel taskLabel = task.getTaskLabel();
		result[2] = taskLabel.getLabelName() + " " + String.valueOf(taskLabel.getColourId());
		result[3] = task.getFromTimeString();
		result[4] = task.getToTimeString();
		result[5] = task.getType();
		return result;
	}
}
