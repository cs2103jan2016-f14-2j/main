package org.jimple.planner.storage;

import static org.jimple.planner.constants.Constants.FILEPATH_CONFIG;
import static org.jimple.planner.constants.Constants.FILEPATH_TEST;
import static org.jimple.planner.constants.Constants.FILEPATH_TEST_TEMP;
import static org.jimple.planner.constants.Constants.PROPERTIES_COMMENT_HEADER;
import static org.jimple.planner.constants.Constants.TAGS_DESCRIPTION;
import static org.jimple.planner.constants.Constants.TAGS_FROM_TIME;
import static org.jimple.planner.constants.Constants.TAGS_ISDONE;
import static org.jimple.planner.constants.Constants.TAGS_LABEL;
import static org.jimple.planner.constants.Constants.TAGS_LABEL_FIELD_SEPARATOR;
import static org.jimple.planner.constants.Constants.TAGS_LINE_FIELD_SEPARATOR;
import static org.jimple.planner.constants.Constants.TAGS_TITLE;
import static org.jimple.planner.constants.Constants.TAGS_TO_TIME;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0135808B
public class StorageSave implements StorageSaveInterface{
	//@@author A0135808B
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
	//@@author A0135808B
	public boolean isSavedTasksSelect(ArrayList<ArrayList<Task>> allTaskLists, String filePath, String tempFilePath){
		assert allTaskLists.size() == 4;
		Task.sortTasks(allTaskLists);
		writeTasksToFile(allTaskLists, tempFilePath);
		boolean saveStatus = isSaveToFile(filePath, tempFilePath);
		return saveStatus;
	}
	//@@author A0135808B
	private void writeTasksToFile(ArrayList<ArrayList<Task>> allTaskLists, String tempFilePath){
		BufferedWriter tempWriter = createFileWriter(tempFilePath);
		writeTasksUsingWriter(allTaskLists, tempWriter);
	}
	//@@author A0135808B
	private void writeTasksUsingWriter(ArrayList<ArrayList<Task>> allTaskLists, BufferedWriter tempWriter) {
		try {
			for(ArrayList<Task> taskList: allTaskLists){
				writeArrayOfTasksUsingWriters(taskList, tempWriter);
			}
			tempWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
	//@@author A0135808B
	private void writeArrayOfTasksUsingWriters(ArrayList<Task> taskList, BufferedWriter tempWriter){
		try {
			for(Task task: taskList){
				checkTaskValidity(task);
				String lineString = extractTaskToString(task);
				tempWriter.write(lineString);
				tempWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//@@author A0135808B
	private boolean isSaveToFile(String filePath, String tempFilePath){
		File file = createFile(filePath);
		File tempFile = createFile(tempFilePath);
		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	//@@author A0135808B
	private String extractTaskToString(Task task){
		System.out.println(task.getIsDone());
		String lineString = formatToSaveString(TAGS_ISDONE + Boolean.toString(task.getIsDone()));
		String titleString = formatToSaveString(TAGS_TITLE + task.getTitle());
		lineString = lineString + titleString;
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
		TaskLabel taskLabel = task.getTaskLabel();
		if(!taskLabel.equals(TaskLabel.getDefaultLabel())){
			String taskLabelToString = formatToTaskLabel(taskLabel);
			lineString = lineString + taskLabelToString;
		}
		return lineString;
	}
	//@@author A0135808B
	private boolean isDescriptionExist(Task task){
		return !(task.getDescription().length()==0);
	}
	//@@author A0135808B
	private boolean isFromTimeExist(Task task){
		return !(task.getFromTimeString().length()==0);
	}
	//@@author A0135808B
	private boolean isToTimeExist(Task task){
		return !(task.getToTimeString().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "/s/"
	//@@author A0135808B
	private String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	//@@author A0135808B
	private String formatToTaskLabel(TaskLabel taskLabel){
		String labelName = formatLabelFields(taskLabel.getLabelName());
		String labelColourIdString = formatLabelFields(String.valueOf(taskLabel.getColourId()));
		String formattedString = TAGS_LABEL + labelName + labelColourIdString;
		return formattedString;
	}
	//@@author A0135808B
	private String formatLabelFields(String string){
		return TAGS_LABEL_FIELD_SEPARATOR + string + TAGS_LABEL_FIELD_SEPARATOR;
	}
	//@@author A0135808B
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
	//@@author A0135808B
	public boolean isSavedTasksTest(ArrayList<ArrayList<Task>> allTaskLists){
		return isSavedTasksSelect(allTaskLists, FILEPATH_TEST, FILEPATH_TEST_TEMP);
	}
	//@@author A0135808B
	public void testWriteTasks(ArrayList<ArrayList<Task>> allTaskLists, BufferedWriter tempWriter) {
		Task.sortTasks(allTaskLists);
		writeTasksUsingWriter(allTaskLists, tempWriter);
	}
}
