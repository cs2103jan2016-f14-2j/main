package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public interface Storage{
	public boolean isSaved(ArrayList<ArrayList<Task>> allTaskLists);
	
	public ArrayList<ArrayList<Task>> getTasks();
	
	public boolean setPath(String pathName);
}
