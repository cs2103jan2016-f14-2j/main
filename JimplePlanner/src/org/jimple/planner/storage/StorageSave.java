package org.jimple.planner.storage;

import static org.jimple.planner.Constants.FILEPATH_CONFIG;
import static org.jimple.planner.Constants.FILEPATH_TEST;
import static org.jimple.planner.Constants.FILEPATH_TEST_TEMP;
import static org.jimple.planner.Constants.PROPERTIES_COMMENT_HEADER;
import static org.jimple.planner.Constants.TAGS_CATEGORY;
import static org.jimple.planner.Constants.TAGS_DESCRIPTION;
import static org.jimple.planner.Constants.TAGS_FROM_TIME;
import static org.jimple.planner.Constants.TAGS_LINE_FIELD_SEPARATOR;
import static org.jimple.planner.Constants.TAGS_TITLE;
import static org.jimple.planner.Constants.TAGS_TO_TIME;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

import org.jimple.planner.Task;

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
	
	public boolean isSavedSelect(ArrayList<ArrayList<Task>> allTaskLists, String filePath, String tempFilePath){
		assert allTaskLists.size() == 3;
		sortBeforeWritngToFile(allTaskLists);
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
	
	public void sortBeforeWritngToFile(ArrayList<ArrayList<Task>> allTaskLists){
		sortById(allTaskLists);
		sortDeadlines(allTaskLists);
		sortEvents(allTaskLists);
	}
	
	private void sortDeadlines(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> fromDateComparator = Task.getFromDateTimeComparator();
		Collections.sort(allTaskLists.get(1), fromDateComparator);
	}
	
	private void sortEvents(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> fromDateComparator = Task.getFromDateTimeComparator();
		Collections.sort(allTaskLists.get(2), fromDateComparator);
	}
	
	private void sortById(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> taskIdComparator = Task.getTaskIdComparator();
		for(ArrayList<Task> taskList: allTaskLists){
			Collections.sort(taskList, taskIdComparator);
		}
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
	public boolean isSavedTest(ArrayList<ArrayList<Task>> allTaskLists){
		return isSavedSelect(allTaskLists, FILEPATH_TEST, FILEPATH_TEST_TEMP);
	}
	
	public void testWriteTasks(ArrayList<ArrayList<Task>> allTaskLists, BufferedWriter tempWriter) {
		sortBeforeWritngToFile(allTaskLists);
		writeTasksUsingWriter(allTaskLists, tempWriter);
	}
}
