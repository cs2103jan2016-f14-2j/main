package org.jimple.planner.storage;

import static org.jimple.planner.Constants.ALL_ARRAY_SIZE;
import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT;
import static org.jimple.planner.Constants.FILEPATH_DEFAULT_TEMP;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME;
import static org.jimple.planner.Constants.PROPERTIES_SAVEPATH_TO_CWD;
import static org.jimple.planner.Constants.DEFAULT_FILE_DIRECTORY;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import org.jimple.planner.Task;

public class StorageProperties {
	private static StorageSave storageSave = null;
	private static StorageLoad storageLoad = null;
	private Properties storageProperties = null;
	
	public StorageProperties(){
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		storageProperties = storageLoad.loadProperties();
	}
	
	public boolean setPath(String pathName){
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
		}
        try {
            Paths.get(filePath);
            File fileDir = new File(filePath);
            return fileDir.isDirectory();
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }
	
	private boolean isKeyChanged(String pathName){
		String previousPath = this.storageProperties.getProperty(PROPERTIES_SAVEPATH_KEY_NAME);
		if(previousPath.equals(pathName)){
			return false;
		} else if (isValueSame(pathName, previousPath)){ 
			setIfOrigin(pathName, previousPath);
			return false;
		} else{
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, previousPath);
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, pathName);
			return true;
		}
	}

	private void setIfOrigin(String pathName, String previousPath) {
		if(pathName.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, pathName);
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, pathName);
		} else if(previousPath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_KEY_NAME, previousPath);
			this.storageProperties.setProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME, previousPath);
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
		boolean saveStatus = storageSave.isSavedSelect(consolidatedTasks, newPath, oldPath);
		return saveStatus;
	}

	private ArrayList<ArrayList<Task>> getConsolidatedTasks(String newPath, String oldPath){
		ArrayList<ArrayList<Task>> oldPathTasks = storageLoad.getTaskSelect(oldPath);
		ArrayList<ArrayList<Task>> newPathTasks = storageLoad.getTaskSelect(newPath);
		ArrayList<ArrayList<Task>> consolidatedTasks = removeDuplicateTasks(oldPathTasks, newPathTasks);
		storageSave.sortBeforeWritngToFile(consolidatedTasks);
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
	
	private void deleteResidualDirectory(){
		String oldFileDirPath = this.storageProperties.getProperty(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME);
		oldFileDirPath = getFullFilePath(oldFileDirPath, DEFAULT_FILE_DIRECTORY);
		if(!oldFileDirPath.equals(PROPERTIES_SAVEPATH_TO_CWD)){
			File oldFileDir = new File(oldFileDirPath);
			if(oldFileDir.isDirectory()){
				oldFileDir.delete();
			}
		}
	}
	
	private String getFileDirectoryFromProperties(String key){
		String fileDir = this.storageProperties.getProperty(key);
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
		return getFileDirectoryFromProperties(PROPERTIES_SAVEPATH_KEY_NAME);
	}
	
	private String getOldFileDirectory(){
		return getFileDirectoryFromProperties(PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME);
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
