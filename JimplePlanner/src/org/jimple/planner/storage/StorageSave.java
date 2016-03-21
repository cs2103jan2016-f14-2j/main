package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public interface StorageSave extends StorageCreateFile, StorageAssignTaskId{
	public boolean isSavedSelect(ArrayList<ArrayList<Task>> allTasksList, String filePath, String tempFilePath);
}
