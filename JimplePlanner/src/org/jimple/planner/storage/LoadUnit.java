package org.jimple.planner.storage;

import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.FILEPATH_CONFIG;
import static org.jimple.planner.Constants.FILEPATH_TEST;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_TO_CWD;
import static org.jimple.planner.Constants.TAGS_CATEGORY;
import static org.jimple.planner.Constants.TAGS_DESCRIPTION;
import static org.jimple.planner.Constants.TAGS_FROM_TIME;
import static org.jimple.planner.Constants.TAGS_LINE_FIELD_SEPARATOR;
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

import org.jimple.planner.Task;

public class LoadUnit implements StorageLoad{
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
		assignTaskIds(allTasksLists);
		return allTasksLists;
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
	
	public Task testGetTaskFromLine(String fileLineContent){
		return getTaskFromLine(fileLineContent);
	}
	
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
	
	public Properties loadProperties(){
		String configPath = FILEPATH_CONFIG;
		BufferedReader configFileReader = createFileReader(configPath);
		Properties property = new Properties();;
		try {
			property.load(configFileReader);
				if(property.isEmpty()){
					propertyKeysInitialise(property);
					//storeProperties(property);
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
}
