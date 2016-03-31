package org.jimple.planner.storage;

import static org.jimple.planner.Constants.ALL_ARRAY_SIZE;
import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT_TEMP;
import static org.jimple.planner.Constants.PROPERTIES_KEY_CURRENT_SAVEPATH;
import static org.jimple.planner.Constants.PROPERTIES_KEY_PREV_SAVEPATH;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_BLUE_1;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_GREEN_2;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_YELLOW_3;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_ORANGE_4;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_RED_5;
import static org.jimple.planner.Constants.PROPERTIES_VALUE_COLOUR_PURPLE_6;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_TO_CWD;
import static org.jimple.planner.Constants.DEFAULT_FILE_DIRECTORY;

import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_BLUE_1;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_GREEN_2;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_YELLOW_3;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_ORANGE_4;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_RED_5;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_PURPLE_6;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;

public class StorageProperties implements StorageTools{
	private StorageSave storageSave;
	private StorageLoad storageLoad;
	//private Properties storageProperties = null;
	private static HashMap<String, Integer> colourToId = new HashMap<String, Integer>();
	
	public StorageProperties(){
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		initialiseHashMap();
	}
	
	private void initialiseHashMap(){
		colourToId.put(PROPERTIES_VALUE_COLOUR_BLUE_1, TASK_LABEL_COLOUR_BLUE_1);
		colourToId.put(PROPERTIES_VALUE_COLOUR_GREEN_2, TASK_LABEL_COLOUR_GREEN_2);
		colourToId.put(PROPERTIES_VALUE_COLOUR_YELLOW_3, TASK_LABEL_COLOUR_YELLOW_3);
		colourToId.put(PROPERTIES_VALUE_COLOUR_ORANGE_4, TASK_LABEL_COLOUR_ORANGE_4);
		colourToId.put(PROPERTIES_VALUE_COLOUR_RED_5, TASK_LABEL_COLOUR_RED_5);
		colourToId.put(PROPERTIES_VALUE_COLOUR_PURPLE_6, TASK_LABEL_COLOUR_PURPLE_6);
	}
	
	public boolean setPath(String pathName){
		Properties storageProperties = storageLoad.loadProperties();
		boolean setStatus = false;
		if(isFilePathValid(pathName)){
			if(isKeyChanged(pathName)){
				setStatus = copyToNewLocation();
				deleteResidualDirectory();
			}
		}
		storageSave.saveProperties(storageProperties);
		return setStatus;
	}
	
	private boolean isFilePathValid(String filePath){
		if(filePath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			return true;
		} else if(filePath.equals(EMPTY_STRING)){
			return false;
		}
        try {
            Paths.get(filePath);
            File fileDir = new File(filePath);
            return fileDir.isDirectory();
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
	
	//TODO Refactor this method
	private boolean isKeyChanged(String pathName){
		Properties storageProperties = storageLoad.loadProperties();
		String previousPath = storageProperties.getProperty(PROPERTIES_KEY_CURRENT_SAVEPATH);
		if(previousPath.equals(pathName)){
			return false;
		} else if (isValueSame(pathName, previousPath)){ 
			setIfOrigin(pathName, previousPath);
			return false;
		} else{
			storageProperties.setProperty(PROPERTIES_KEY_PREV_SAVEPATH, previousPath);
			storageProperties.setProperty(PROPERTIES_KEY_CURRENT_SAVEPATH, pathName);
			return true;
		}
	}

	private void setIfOrigin(String pathName, String previousPath) {
		Properties storageProperties = storageLoad.loadProperties();
		if(pathName.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			storageProperties.setProperty(PROPERTIES_KEY_CURRENT_SAVEPATH, pathName);
			storageProperties.setProperty(PROPERTIES_KEY_PREV_SAVEPATH, pathName);
		} else if(previousPath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			storageProperties.setProperty(PROPERTIES_KEY_CURRENT_SAVEPATH, previousPath);
			storageProperties.setProperty(PROPERTIES_KEY_PREV_SAVEPATH, previousPath);
		}
	}

	private boolean isValueSame(String pathName, String previousPath) {
		String previousFileDir = (new File(getFileDirectory(previousPath))).getAbsolutePath();
		String currentFileDir = (new File(getFileDirectory(pathName))).getAbsolutePath();
		return (previousFileDir.equals(currentFileDir));
	}
	
	private boolean copyToNewLocation(){
		String newDir = getCurrentFileDirectory();
		String oldDir = getOldFileDirectory();
		
		String newPath = getFilePath(newDir);
		String oldPath = getFilePath(oldDir);
		
		ArrayList<ArrayList<Task>> consolidatedTasks = getConsolidatedTasks(newPath, oldPath);
		boolean saveStatus = storageSave.isSavedTasksSelect(consolidatedTasks, newPath, oldPath);
		return saveStatus;
	}

	private ArrayList<ArrayList<Task>> getConsolidatedTasks(String newPath, String oldPath){
		ArrayList<ArrayList<Task>> oldPathTasks = storageLoad.getTaskSelect(oldPath);
		ArrayList<ArrayList<Task>> newPathTasks = storageLoad.getTaskSelect(newPath);
		ArrayList<ArrayList<Task>> consolidatedTasks = removeDuplicateTasks(oldPathTasks, newPathTasks);
		Task.sortTasks(consolidatedTasks);
		return consolidatedTasks;
	}

	private ArrayList<ArrayList<Task>> removeDuplicateTasks(ArrayList<ArrayList<Task>> oldPathTasks,
			ArrayList<ArrayList<Task>> newPathTasks) {
		ArrayList<ArrayList<Task>> consolidatedTasks = new ArrayList<ArrayList<Task>>();
		for(int i = 0; i < ALL_ARRAY_SIZE; i++){
			ArrayList<Task> newTasksByType = newPathTasks.get(i);
			ArrayList<Task> oldTasksByType = oldPathTasks.get(i);
			oldTasksByType.addAll(newTasksByType);
			assignTaskIds(oldPathTasks);
			HashSet<Task> removeDuplicatedTasksByType = new HashSet<Task>(oldTasksByType);
			ArrayList<Task> consolidatedTasksByType = new ArrayList<Task>(removeDuplicatedTasksByType);
			consolidatedTasks.add(consolidatedTasksByType);
		}
		return consolidatedTasks;
	}
	
	private void deleteResidualDirectory(){
		Properties storageProperties = storageLoad.loadProperties();
		String oldFileDirPath = storageProperties.getProperty(PROPERTIES_KEY_PREV_SAVEPATH);
		oldFileDirPath = getFullFilePath(oldFileDirPath, DEFAULT_FILE_DIRECTORY);
		if(!oldFileDirPath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			File oldFileDir = new File(oldFileDirPath);
			if(oldFileDir.isDirectory()){
				oldFileDir.delete();
			}
		}
	}
	
	private String getFileDirectoryFromProperties(String key){
		Properties storageProperties = storageLoad.loadProperties();
		String fileDir = storageProperties.getProperty(key);
		fileDir = getFileDirectory(fileDir);
		return fileDir;
	}

	private String getFileDirectory(String fileDir) {
		if(fileDir.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			fileDir = EMPTY_STRING;
		}
		return fileDir;
	}
	
	private String getCurrentFileDirectory(){
		return getFileDirectoryFromProperties(PROPERTIES_KEY_CURRENT_SAVEPATH);
	}
	
	private String getOldFileDirectory(){
		return getFileDirectoryFromProperties(PROPERTIES_KEY_PREV_SAVEPATH);
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
	
	private String getFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT);
	}
	
	private String getTempFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT_TEMP);
	}
	
	public String getCurrentFilePath(){
		String currentFileDir = getCurrentFileDirectory();
		return getFilePath(currentFileDir);
	}
	
	public String getCurrentTempFilePath(){
		String currentFileDir = getCurrentFileDirectory();
		return getTempFilePath(currentFileDir);
	}
	
	public String checkPath(){
		String currentPath = getCurrentFileDirectory();
		if(currentPath.equals(EMPTY_STRING)){
			File currentPathFile = new File(currentPath);
			currentPath = currentPathFile.getAbsolutePath();
		} 
		return currentPath;
	}
	//TODO remove all other properties or just create a new property and get prevsavepath and savepath
	public boolean isSavedLabels(ArrayList<TaskLabel> labelLists){
		Properties storageProperties = storageLoad.loadProperties();
		for(TaskLabel taskLabel: labelLists){
			if(taskLabel.equals(TaskLabel.getDefaultLabel())){
				continue;
			}
			String labelName = taskLabel.getLabelName();
			int labelColourId = taskLabel.getColourId();
			String labelColourString = null;
			switch(labelColourId){
			case TASK_LABEL_COLOUR_BLUE_1:
				labelColourString = PROPERTIES_VALUE_COLOUR_BLUE_1;
				break;
				
			case TASK_LABEL_COLOUR_GREEN_2:
				labelColourString = PROPERTIES_VALUE_COLOUR_GREEN_2;
				break;
				
			case TASK_LABEL_COLOUR_YELLOW_3:
				labelColourString = PROPERTIES_VALUE_COLOUR_YELLOW_3;
				break;
			
			case TASK_LABEL_COLOUR_ORANGE_4:
				labelColourString = PROPERTIES_VALUE_COLOUR_ORANGE_4;
				break;
			
			case TASK_LABEL_COLOUR_RED_5:
				labelColourString = PROPERTIES_VALUE_COLOUR_RED_5;
				break;
			
			case TASK_LABEL_COLOUR_PURPLE_6:
				labelColourString = PROPERTIES_VALUE_COLOUR_PURPLE_6;
				break;
			default:
				System.out.println("Error, no label should be null here");
				return false;
			}
			storageProperties.setProperty(labelName, labelColourString);
		}
		storageSave.saveProperties(storageProperties);
		return true;
	}
	
	public ArrayList<TaskLabel> getLabels(){
		Properties storageProperties = storageLoad.loadProperties();
		ArrayList<TaskLabel> labelList = new ArrayList<TaskLabel>();
		labelList.add(TaskLabel.getDefaultLabel()); //1st element of labelList is always default label
		
		Set<Object> propertiesKeys = storageProperties.keySet();
		boolean removed_savepath_key = propertiesKeys.remove((Object)PROPERTIES_KEY_CURRENT_SAVEPATH);
		boolean removed_prev_savepath_key = propertiesKeys.remove((Object)PROPERTIES_KEY_PREV_SAVEPATH);
		assert removed_prev_savepath_key;
		assert removed_savepath_key;
		for(Object labelNames: propertiesKeys){
			String labelNamesString = (String) labelNames;
			String labelColourString = storageProperties.getProperty(labelNamesString);
			int labelColourId = colourToId.get(labelColourString);
			TaskLabel taskLabel = TaskLabel.getNewLabel(labelNamesString, labelColourId);
			labelList.add(taskLabel);
		}
		return labelList;
	}
	
	/*
	 * ALL TEST METHODS ARE HERE
	 */
	public boolean testIsFilePathValid(String filePath){
		return isFilePathValid(filePath);
	}
}
