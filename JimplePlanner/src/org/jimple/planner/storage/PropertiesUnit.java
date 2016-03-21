package org.jimple.planner.storage;

import static org.jimple.planner.Constants.ALL_ARRAY_SIZE;
import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT_TEMP;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_TO_CWD;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import org.jimple.planner.Task;

public class PropertiesUnit {
	private static SaveUnit saveUnit = null;
	private static LoadUnit loadUnit = null;
	private Properties storageProperties = null;
	
	public PropertiesUnit(){
		saveUnit = new SaveUnit();
		loadUnit = new LoadUnit();
		storageProperties = loadUnit.loadProperties();
	}
	
	public boolean setPath(String pathName){
		boolean setStatus = false;
		if(isFilePathValid(pathName)){
			if(isKeyUpdated(pathName)){
				saveUnit.saveProperties(storageProperties);
				setStatus = copyToNewLocation();
			}
		}
		return setStatus;
	}
	
	private boolean isFilePathValid(String filePath){
		if(filePath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			return true;
		}
        try {
            Paths.get(filePath);
            File fileDir = new File(filePath);
            return fileDir.isDirectory();
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
	
	private boolean isKeyUpdated(String pathName){
		String previousPath = this.storageProperties.getProperty(PROPERTIES_SAVEPATH_KEY_NAME);
		if(previousPath.equals(pathName)){
			return false;
		}
		this.storageProperties.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, previousPath);
		this.storageProperties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, pathName);
		return true;
	}
	
	private boolean copyToNewLocation(){
		String newDir = getCurrentFileDirectory();
		String oldDir = getOldFileDirectory();
		
		String newPath = getFilePath(newDir);
		String oldPath = getFilePath(oldDir);
		
		ArrayList<ArrayList<Task>> consolidatedTasks = getConsolidatedTasks(newPath, oldPath);
		boolean saveStatus = saveUnit.isSavedSelect(consolidatedTasks, newPath, oldPath);
		return saveStatus;
	}

	private ArrayList<ArrayList<Task>> getConsolidatedTasks(String newPath, String oldPath){
		ArrayList<ArrayList<Task>> oldPathTasks = loadUnit.getTaskSelect(oldPath);
		ArrayList<ArrayList<Task>> newPathTasks = loadUnit.getTaskSelect(newPath);
		ArrayList<ArrayList<Task>> consolidatedTasks = removeDuplicateTasks(oldPathTasks, newPathTasks);
		return consolidatedTasks;
	}

	private ArrayList<ArrayList<Task>> removeDuplicateTasks(ArrayList<ArrayList<Task>> oldPathTasks,
			ArrayList<ArrayList<Task>> newPathTasks) {
		ArrayList<ArrayList<Task>> consolidatedTasks = new ArrayList<ArrayList<Task>>();
		for(int i = 0; i < ALL_ARRAY_SIZE; i++){
			ArrayList<Task> newTasksByType = newPathTasks.get(i);
			ArrayList<Task> oldTasksByType = oldPathTasks.get(i);
			oldTasksByType.addAll(newTasksByType);
			
			HashSet<Task> removeDuplicatedTasksByType = new HashSet<Task>(oldTasksByType);
			ArrayList<Task> consolidatedTasksByType = new ArrayList<Task>(removeDuplicatedTasksByType);
			consolidatedTasks.add(consolidatedTasksByType);
		}
		return consolidatedTasks;
	}
	
	private String getFileDirectory(String filePath){
		String fileDir = this.storageProperties.getProperty(filePath);
		if(fileDir.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			fileDir = EMPTY_STRING;
		}
		return fileDir;
	}
	
	
	
	private String getCurrentFileDirectory(){
		return getFileDirectory(PROPERTIES_SAVEPATH_KEY_NAME);
	}
	
	private String getOldFileDirectory(){
		return getFileDirectory(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME);
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
}
