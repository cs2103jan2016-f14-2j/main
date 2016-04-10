package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.task.Task;
//@@author A0135808B
public interface StorageLoadInterface extends StorageToolsInterface{

	public ArrayList<ArrayList<Task>> getTaskSelect(String filePath);
}
