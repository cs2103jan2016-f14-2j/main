package org.jimple.planner.storage;

import static org.jimple.planner.constants.Constants.DEFAULT_FILE_DIRECTORY;
import static org.jimple.planner.constants.Constants.EMPTY_STRING;
import static org.jimple.planner.constants.Constants.FILEPATH_DEFAULT;
import static org.jimple.planner.constants.Constants.FILEPATH_DEFAULT_TEMP;
import static org.jimple.planner.constants.Constants.PROPERTIES_KEY_CURRENT_SAVEPATH;
import static org.jimple.planner.constants.Constants.PROPERTIES_KEY_PREV_SAVEPATH;
import static org.jimple.planner.constants.Constants.PROPERTIES_SAVEPATH_TO_CWD;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_BLUE_1;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_GREEN_2;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_ORANGE_4;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_PURPLE_6;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_RED_5;
import static org.jimple.planner.constants.Constants.PROPERTIES_VALUE_COLOUR_YELLOW_3;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_BLUE_1;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_GREEN_2;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_ORANGE_4;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_PURPLE_6;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_RED_5;
import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_YELLOW_3;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0135808B
public class StorageProperties implements StorageToolsInterface{
	private StorageSave storageSave;
	private StorageLoad storageLoad;
	private final static Logger LOGGER = Logger.getLogger(StorageProperties.class.getName());
	private static HashMap<String, Integer> colourToId = new HashMap<String, Integer>();
	//@@author A0135808B
	public StorageProperties(){
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		initialiseHashMap();
	}
	//@@author A0135808B
	private void initialiseHashMap(){
		colourToId.put(PROPERTIES_VALUE_COLOUR_BLUE_1, TASK_LABEL_COLOUR_BLUE_1);
		colourToId.put(PROPERTIES_VALUE_COLOUR_GREEN_2, TASK_LABEL_COLOUR_GREEN_2);
		colourToId.put(PROPERTIES_VALUE_COLOUR_YELLOW_3, TASK_LABEL_COLOUR_YELLOW_3);
		colourToId.put(PROPERTIES_VALUE_COLOUR_ORANGE_4, TASK_LABEL_COLOUR_ORANGE_4);
		colourToId.put(PROPERTIES_VALUE_COLOUR_RED_5, TASK_LABEL_COLOUR_RED_5);
		colourToId.put(PROPERTIES_VALUE_COLOUR_PURPLE_6, TASK_LABEL_COLOUR_PURPLE_6);
	}
	//@@author A0135808B
	public boolean setPath(String pathName){
		Properties storageProperties = storageLoad.loadProperties();
		boolean setStatus = false;
		if(isFilePathValid(pathName)){
			if(isKeyChanged(pathName, storageProperties)){
				setStatus = copyToNewLocation(storageProperties);
				deleteResidualDirectory(storageProperties);
			}
		}
		storageSave.saveProperties(storageProperties);
		return setStatus;
	}
	//@@author A0135808B
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
	//@@author A0135808B
	private boolean isKeyChanged(String pathName, Properties storageProperties){
		String previousPath = storageProperties.getProperty(PROPERTIES_KEY_CURRENT_SAVEPATH);
		if(previousPath.equals(pathName)){
			return false;
		} else if (isValueSame(pathName, previousPath)){ //This is mostly only to check if both prev and current are origin
			setIfOrigin(pathName, previousPath);
			return false;
		} else{
			storageProperties.setProperty(PROPERTIES_KEY_PREV_SAVEPATH, previousPath);
			storageProperties.setProperty(PROPERTIES_KEY_CURRENT_SAVEPATH, pathName);
			return true;
		}
	}
	//@@author A0135808B
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
	//@@author A0135808B
	private boolean isValueSame(String pathName, String previousPath) {
		String previousFileDir = (new File(getFileDirectory(previousPath))).getAbsolutePath();
		String currentFileDir = (new File(getFileDirectory(pathName))).getAbsolutePath();
		return (previousFileDir.equals(currentFileDir));
	}
	//@@author A0135808B
	private boolean copyToNewLocation(Properties storageProperties){
		String newDir = getCurrentFileDirectory(storageProperties);
		String oldDir = getOldFileDirectory(storageProperties);
		
		String newPath = getFilePath(newDir);
		String oldPath = getFilePath(oldDir);
		
		ArrayList<ArrayList<Task>> consolidatedTasks = getConsolidatedTasks(newPath, oldPath);
		boolean saveStatus = storageSave.isSavedTasksSelect(consolidatedTasks, newPath, oldPath);
		return saveStatus;
	}
	//@@author A0135808B
	private ArrayList<ArrayList<Task>> getConsolidatedTasks(String newPath, String oldPath){
		ArrayList<ArrayList<Task>> oldPathTasks = storageLoad.getTaskSelect(oldPath);
		ArrayList<ArrayList<Task>> newPathTasks = storageLoad.getTaskSelect(newPath);
		ArrayList<ArrayList<Task>> consolidatedTasks = removeDuplicateTasks(oldPathTasks, newPathTasks);
		Task.sortTasks(consolidatedTasks);
		return consolidatedTasks;
	}
	//@@author A0135808B
	private ArrayList<ArrayList<Task>> removeDuplicateTasks(ArrayList<ArrayList<Task>> oldPathTasks,
			ArrayList<ArrayList<Task>> newPathTasks) {
		ArrayList<ArrayList<Task>> consolidatedTasks = new ArrayList<ArrayList<Task>>();
		for(int i = 0; i < 4; i++){
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
	//@@author A0135808B
	private void deleteResidualDirectory(Properties storageProperties){
		String oldFileDirPath = storageProperties.getProperty(PROPERTIES_KEY_PREV_SAVEPATH);
		oldFileDirPath = getFullFilePath(oldFileDirPath, DEFAULT_FILE_DIRECTORY);
		if(!oldFileDirPath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			File oldFileDir = new File(oldFileDirPath);
			if(oldFileDir.isDirectory()){
				oldFileDir.delete();
			}
		}
	}
	//@@author A0135808B
	private String getFileDirectoryFromProperties(Properties storageProperties, String key){
		String fileDir = storageProperties.getProperty(key);
		fileDir = getFileDirectory(fileDir);
		return fileDir;
	}
	//@@author A0135808B
	private String getFileDirectory(String fileDir) {
		if(fileDir.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			fileDir = EMPTY_STRING;
		}
		return fileDir;
	}
	//@@author A0135808B
	private String getCurrentFileDirectory(Properties storageProperties){
		return getFileDirectoryFromProperties(storageProperties, PROPERTIES_KEY_CURRENT_SAVEPATH);
	}
	//@@author A0135808B
	private String getOldFileDirectory(Properties storageProperties){
		return getFileDirectoryFromProperties(storageProperties, PROPERTIES_KEY_PREV_SAVEPATH);
	}
	//@@author A0135808B
	private String getFullFilePath(String fileSaveDir, String fileName) {
		String fileString = EMPTY_STRING;
		if(fileSaveDir.equals(EMPTY_STRING) || fileSaveDir.endsWith(File.separator)){
			fileString = fileSaveDir + fileName;
		} else {
			fileString = fileSaveDir + File.separator + fileName;
		}
		return fileString;
	}
	//@@author A0135808B
	private String getFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT);
	}
	//@@author A0135808B
	private String getTempFilePath(String fileSaveDir){
		return getFullFilePath(fileSaveDir, FILEPATH_DEFAULT_TEMP);
	}
	//@@author A0135808B
	private String getCurrentFilePath(Properties storageProperties){
		String currentFileDir = getCurrentFileDirectory(storageProperties);
		return getFilePath(currentFileDir);
	}
	//@@author A0135808B
	private String getCurrentTempFilePath(Properties storageProperties){
		String currentFileDir = getCurrentFileDirectory(storageProperties);
		return getTempFilePath(currentFileDir);
	}
	//@@author A0135808B
	public String getCurrentSaveFilePath(){
		Properties storageProperties = storageLoad.loadProperties();
		return getCurrentFilePath(storageProperties);
	}
	//@@author A0135808B
	public String getCurrentTempSaveFilePath(){
		Properties storageProperties = storageLoad.loadProperties();
		return getCurrentTempFilePath(storageProperties);
	}
	//@@author A0135808B
	public String checkPath(){
		Properties storageProperties = storageLoad.loadProperties();
		String currentPath = getCurrentFileDirectory(storageProperties);
		if(currentPath.equals(EMPTY_STRING)){
			File currentPathFile = new File(currentPath);
			currentPath = currentPathFile.getAbsolutePath();
		} 
		return currentPath;
	}
	//@@author A0135808B
	public boolean isSavedLabels(ArrayList<TaskLabel> labelLists){
		Properties newStorageProperties = getPathProperties();
		for(TaskLabel taskLabel: labelLists){
			if(taskLabel.equals(TaskLabel.createDefaultLabel())){
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
				LOGGER.warning("Error, no label should have its colour id be invalid");
				LOGGER.warning("label name: "+taskLabel.getLabelName());
				LOGGER.warning("label colourId: "+taskLabel.getColourId());
				return false;
			}
			newStorageProperties.setProperty(labelName, labelColourString);
		}
		storageSave.saveProperties(newStorageProperties);
		return true;
	}
	//@@author A0135808B
	private Properties getPathProperties() {
		Properties storageProperties = storageLoad.loadProperties();
		Properties newStorageProperties = new Properties();
		String currentPath = storageProperties.getProperty(PROPERTIES_KEY_CURRENT_SAVEPATH);
		String prevPath = storageProperties.getProperty(PROPERTIES_KEY_PREV_SAVEPATH);
		newStorageProperties.setProperty(PROPERTIES_KEY_CURRENT_SAVEPATH, currentPath);
		newStorageProperties.setProperty(PROPERTIES_KEY_PREV_SAVEPATH, prevPath);
		return newStorageProperties;
	}
	//@@author A0135808B
	public ArrayList<TaskLabel> getLabels(){
		Properties storageProperties = storageLoad.loadProperties();
		ArrayList<TaskLabel> labelList = new ArrayList<TaskLabel>();
		labelList.add(TaskLabel.createDefaultLabel()); //1st element of labelList is always default label
		
		Set<Object> propertiesKeys = storageProperties.keySet();
		boolean removed_savepath_key = propertiesKeys.remove((Object)PROPERTIES_KEY_CURRENT_SAVEPATH);
		boolean removed_prev_savepath_key = propertiesKeys.remove((Object)PROPERTIES_KEY_PREV_SAVEPATH);
		assert removed_prev_savepath_key;
		assert removed_savepath_key;
		for(Object labelNames: propertiesKeys){
			String labelNamesString = (String) labelNames;
			String labelColourString = storageProperties.getProperty(labelNamesString);
			int labelColourId = colourToId.get(labelColourString);
			TaskLabel taskLabel = TaskLabel.createNewLabel(labelNamesString, labelColourId);
			labelList.add(taskLabel);
		}
		return labelList;
	}
	
	/*
	 * ALL TEST METHODS ARE HERE
	 */
	//@@author A0135808B
	public boolean testIsFilePathValid(String filePath){
		return isFilePathValid(filePath);
	}
}
