package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public interface StorageLoad extends StorageCreateFile, StorageAssignTaskId{
	public ArrayList<ArrayList<Task>> getTaskSelect(String filePath);
}
