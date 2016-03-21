package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public class StorageComponent implements Storage{
	private static SaveUnit saveUnit = null;
	private static LoadUnit loadUnit = null;
	private PropertiesUnit propertiesUnit = null;
	
	
	public StorageComponent() {
		saveUnit = new SaveUnit();
		loadUnit = new LoadUnit();
		propertiesUnit = new PropertiesUnit();
	}
	@Override
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists) {
		String fileName = propertiesUnit.getCurrentFilePath();
		String tempFileName = propertiesUnit.getCurrentTempFilePath();
		return saveUnit.isSavedSelect(allTaskLists, fileName, tempFileName);
	}

	@Override
	public ArrayList<ArrayList<Task>> getTasks(){
		String fileName = propertiesUnit.getCurrentFilePath();
		return loadUnit.getTaskSelect(fileName);
	}

	@Override
	public boolean setPath(String pathName) {
		return propertiesUnit.setPath(pathName);
	}

}
