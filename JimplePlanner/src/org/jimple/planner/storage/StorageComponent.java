package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public class StorageComponent implements Storage{
	private static StorageSave storageSave = null;
	private static StorageLoad storageLoad = null;
	private StorageProperties storageProperties = null;
	
	
	public StorageComponent() {
		storageSave = new StorageSave();
		storageLoad = new StorageLoad();
		storageProperties = new StorageProperties();
	}
	@Override
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists) {
		String fileName = storageProperties.getCurrentFilePath();
		String tempFileName = storageProperties.getCurrentTempFilePath();
		return storageSave.isSavedSelect(allTaskLists, fileName, tempFileName);
	}

	@Override
	public ArrayList<ArrayList<Task>> getTasks(){
		String fileName = storageProperties.getCurrentFilePath();
		return storageLoad.getTaskSelect(fileName);
	}

	@Override
	public boolean setPath(String pathName) {
		return storageProperties.setPath(pathName);
	}

}
