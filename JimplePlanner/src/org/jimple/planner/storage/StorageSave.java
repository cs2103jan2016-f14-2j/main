package org.jimple.planner.storage;

import static org.jimple.planner.Constants.FILEPATH_CONFIG;
import static org.jimple.planner.Constants.FILEPATH_TEST;
import static org.jimple.planner.Constants.FILEPATH_TEST_TEMP;
import static org.jimple.planner.Constants.PROPERTIES_COMMENT_HEADER;
import static org.jimple.planner.Constants.TAGS_LABEL;
import static org.jimple.planner.Constants.TAGS_DESCRIPTION;
import static org.jimple.planner.Constants.TAGS_FROM_TIME;
import static org.jimple.planner.Constants.TAGS_LINE_FIELD_SEPARATOR;
import static org.jimple.planner.Constants.TAGS_TITLE;
import static org.jimple.planner.Constants.TAGS_TO_TIME;
import static org.jimple.planner.Constants.TAGS_LABEL_FIELD_SEPARATOR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;

public class StorageSave implements StorageSaveInterface{
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
	
	public boolean isSavedTasksSelect(ArrayList<ArrayList<Task>> allTaskLists, String filePath, String tempFilePath){
		assert allTaskLists.size() == 3;
		Task.sortTasks(allTaskLists);
		writeTasksToFile(allTaskLists, tempFilePath);
		boolean saveStatus = isSaveToFile(filePath, tempFilePath);
		return saveStatus;
	}
	
	private void writeTasksToFile(ArrayList<ArrayList<Task>> allTaskLists, String tempFilePath){
		BufferedWriter tempWriter = createFileWriter(tempFilePath);
		writeTasksUsingWriter(allTaskLists, tempWriter);
	}

	private void writeTasksUsingWriter(ArrayList<ArrayList<Task>> allTaskLists, BufferedWriter tempWriter) {
		try {
			for(ArrayList<Task> taskList: allTaskLists){
				for(Task task: taskList){
					checkTaskValidity(task);
					String lineString = extractTaskToString(task);
					tempWriter.write(lineString);
					tempWriter.newLine();
				}
			}
			tempWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
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
	
	private String extractTaskToString(Task task){
		String lineString = formatToSaveString(TAGS_TITLE + task.getTitle());
		if(isDescriptionExist(task)){
			String descriptionString = formatToSaveString(TAGS_DESCRIPTION + task.getDescription());
			lineString = lineString + descriptionString;
		} 
		if (isFromTimeExist(task)){
			String fromTimeString = formatToSaveString(TAGS_FROM_TIME + task.getFromTime());
			lineString = lineString + fromTimeString;
		} 
		if (isToTimeExist(task)){
			String fromToString = formatToSaveString(TAGS_TO_TIME + task.getToTime());
			lineString = lineString + fromToString;
		}
		//TODO label field implemented but need to test
		TaskLabel taskLabel = task.getTaskLabel();
		if(taskLabel.equals(TaskLabel.getDefaultLabel())){
			String taskLabelToString = formatToTaskLabel(taskLabel);
			lineString = lineString + taskLabelToString;
		}
		return lineString;
	}
	
	private boolean isDescriptionExist(Task task){
		return !(task.getDescription().length()==0);
	}
	
	private boolean isFromTimeExist(Task task){
		return !(task.getFromTimeString().length()==0);
	}
	
	private boolean isToTimeExist(Task task){
		return !(task.getToTimeString().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "[/s/]"
	private String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	
	private String formatToTaskLabel(TaskLabel taskLabel){
		String labelName = formatLabelFields(taskLabel.getLabelName());
		String labelColourIdString = formatLabelFields(String.valueOf(taskLabel.getColourId()));
		String formattedString = TAGS_LABEL + labelName + labelColourIdString;
		return formattedString;
	}
	
	private String formatLabelFields(String string){
		return TAGS_LABEL_FIELD_SEPARATOR + string + TAGS_LABEL_FIELD_SEPARATOR;
	}
	
	public void saveProperties(Properties property){
		BufferedWriter configFileWriter = createFileWriter(FILEPATH_CONFIG);
		try {
			property.store(configFileWriter, PROPERTIES_COMMENT_HEADER);
			configFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ALL TEST METHODS ARE HERE
	 */
	public boolean isSavedTasksTest(ArrayList<ArrayList<Task>> allTaskLists){
		return isSavedTasksSelect(allTaskLists, FILEPATH_TEST, FILEPATH_TEST_TEMP);
	}
	
	public void testWriteTasks(ArrayList<ArrayList<Task>> allTaskLists, BufferedWriter tempWriter) {
		Task.sortTasks(allTaskLists);
		writeTasksUsingWriter(allTaskLists, tempWriter);
	}
}
