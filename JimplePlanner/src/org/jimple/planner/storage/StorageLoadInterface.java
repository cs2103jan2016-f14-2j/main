package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;
//@@author A0135808B
public interface StorageLoadInterface extends StorageTools{
	public ArrayList<ArrayList<Task>> getTaskSelect(String filePath);
}
