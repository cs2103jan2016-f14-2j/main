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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.exceptions.InvalidTaskException;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0135808B
public class StorageSave implements StorageSaveInterface{
	private final static Logger LOGGER = Logger.getLogger(StorageSave.class.getName());

	private BufferedWriter createFileWriter(String fileName){
		BufferedWriter writer = null;
		try {
			File file = createFile(fileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
			writer = new BufferedWriter(outputStreamWriter);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		}
		return writer;
	}

	public boolean isSavedTasksSelect(ArrayList<ArrayList<Task>> allTaskLists, String filePath, String tempFilePath){
		assert allTaskLists.size() == 4;
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
				writeArrayOfTasksUsingWriters(taskList, tempWriter);
			}
			tempWriter.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		} catch (NullPointerException e){
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		}
	}

	private void writeArrayOfTasksUsingWriters(ArrayList<Task> taskList, BufferedWriter tempWriter){
			for(Task task: taskList){
				try {
					checkTaskValidity(task);
				} catch (InvalidTaskException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					LOGGER.severe(e.getMessage());
					LOGGER.severe("task title:" + task.getTitle());
					LOGGER.severe("task's fromTime is: "+task.getFromTime());
					LOGGER.severe("task's toTime is: "+task.getToTime());
					e.printStackTrace();
				}
				String lineString = extractTaskToString(task);
				try {
					tempWriter.write(lineString);
					tempWriter.newLine();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.toString(), e);
					e.printStackTrace();
				}
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
		if(!taskLabel.equals(TaskLabel.createDefaultLabel())){
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
	
	//Minor formatting of string such that each "field" is enclosed with a "/s/"
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
			LOGGER.log(Level.SEVERE, e.toString(), e);
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
