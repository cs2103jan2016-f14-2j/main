package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
//@@author A0135808B
public interface Storage{
	//@@author A0135808B
	public boolean isSavedTasks(ArrayList<ArrayList<Task>> allTaskLists);
	//@@author A0135808B
	public boolean isSavedLabels(ArrayList<TaskLabel> labelList);
	//@@author A0135808B
	public ArrayList<ArrayList<Task>> getTasks();
	//@@author A0135808B
	public ArrayList<TaskLabel> getLabels();
	//@@author A0135808B
	public boolean setPath(String pathName);
	//@@author A0135808B
	public String checkPath();
}
