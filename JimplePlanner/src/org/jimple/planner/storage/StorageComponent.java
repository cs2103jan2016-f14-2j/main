package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
//@@author A0135808B
public class StorageComponent implements Storage{
	private StorageSave storageSave;
	private StorageLoad storageLoad;
	private StorageProperties storageProperties;
	//@@author A0135808B
	public StorageComponent() {
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		storageProperties = new StorageProperties();
	}
	//@@author A0135808B
	@Override
	public boolean isSavedTasks(ArrayList<ArrayList<Task>> allTaskLists) {
		String fileName = storageProperties.getCurrentSaveFilePath();
		String tempFileName = storageProperties.getCurrentTempSaveFilePath();
		return storageSave.isSavedTasksSelect(allTaskLists, fileName, tempFileName);
	}
	//@@author A0135808B
	@Override
	public boolean isSavedLabels(ArrayList<TaskLabel> labelList) {
		return storageProperties.isSavedLabels(labelList);
	}
	//@@author A0135808B
	@Override	
	public ArrayList<ArrayList<Task>> getTasks(){
		String fileName = storageProperties.getCurrentSaveFilePath();
		return storageLoad.getTaskSelect(fileName);
	}
	//@@author A0135808B
	@Override
	public ArrayList<TaskLabel> getLabels() {
		ArrayList<TaskLabel> labelList = null;
		labelList = storageProperties.getLabels();
		return labelList;
	}
	//@@author A0135808B
	@Override
	public boolean setPath(String pathName) {
		return storageProperties.setPath(pathName);
	}
	//@@author A0135808B
	@Override
	public String checkPath(){
		return storageProperties.checkPath();
	}
}
