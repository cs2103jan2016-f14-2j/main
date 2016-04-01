package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;

public interface Storage{
	public boolean isSavedTasks(ArrayList<ArrayList<Task>> allTaskLists);
	
	public boolean isSavedArchivedTasks(ArrayList<Task> archiveTasks);
	
	public boolean isSavedLabels(ArrayList<TaskLabel> labelList);
	
	public ArrayList<ArrayList<Task>> getTasks();
	
	public ArrayList<Task> getArchivedTasks();
	
	public ArrayList<TaskLabel> getLabels();
	
	public boolean setPath(String pathName);
	
	public String checkPath();
}
