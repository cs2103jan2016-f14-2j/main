package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.task.Task;

//@@author A0135808B
public interface StorageSaveInterface extends StorageToolsInterface{

	public boolean isSavedTasksSelect(ArrayList<ArrayList<Task>> allTasksList, String filePath, String tempFilePath);
}
