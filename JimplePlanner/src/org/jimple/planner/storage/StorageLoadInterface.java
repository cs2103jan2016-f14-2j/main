package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public interface StorageLoadInterface extends StorageTools{
	public ArrayList<ArrayList<Task>> getTaskSelect(String filePath);
}
