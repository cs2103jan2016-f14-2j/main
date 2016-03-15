package org.jimple.planner;

import java.util.ArrayList;

public class StorageStub extends Storage {
	
	public boolean Saved(ArrayList<ArrayList<Task>> allTasksArray)	{
		return true;
	}
	
	public boolean setPath(String filePath)	{
		return false;
	}
}
