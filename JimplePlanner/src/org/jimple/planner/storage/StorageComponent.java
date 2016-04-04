package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
//@@author A0135808B
public class StorageComponent implements Storage{
	private StorageSave storageSave;
	private StorageLoad storageLoad;
	private StorageProperties storageProperties;
	
	public StorageComponent() {
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		storageProperties = new StorageProperties();
	}
	
	@Override
	public boolean isSavedTasks(ArrayList<ArrayList<Task>> allTaskLists) {
		String fileName = storageProperties.getCurrentSaveFilePath();
		String tempFileName = storageProperties.getCurrentTempSaveFilePath();
		return storageSave.isSavedTasksSelect(allTaskLists, fileName, tempFileName);
	}
	
	@Override
	public boolean isSavedLabels(ArrayList<TaskLabel> labelList) {
		return storageProperties.isSavedLabels(labelList);
	}
	
	@Override
	public ArrayList<ArrayList<Task>> getTasks(){
		String fileName = storageProperties.getCurrentSaveFilePath();
		return storageLoad.getTaskSelect(fileName);
	}
	
	@Override
	public ArrayList<TaskLabel> getLabels() {
		ArrayList<TaskLabel> labelList = null;
		labelList = storageProperties.getLabels();
		return labelList;
	}

	@Override
	public boolean setPath(String pathName) {
		return storageProperties.setPath(pathName);
	}
	
	@Override
	public String checkPath(){
		return storageProperties.checkPath();
	}
}
