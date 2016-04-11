package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;
//@@author A0135808B
public interface StorageInterface{

	public boolean isSavedTasks(ArrayList<ArrayList<Task>> allTaskLists);

	public boolean isSavedLabels(ArrayList<TaskLabel> labelList);

	public ArrayList<ArrayList<Task>> getTasks();

	public ArrayList<TaskLabel> getLabels();

	public boolean setPath(String pathName);

	public String checkPath();
}
