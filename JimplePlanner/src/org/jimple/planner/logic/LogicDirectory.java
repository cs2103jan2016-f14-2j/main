package org.jimple.planner.logic;

import org.jimple.planner.storage.*;

import java.util.ArrayList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

public class LogicDirectory {
	
	protected String changeSaveDirectory(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) {
		if (!isValidPath(store, variableArray)) {
			return Constants.ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		//store = new StorageComponent();
		ArrayList<ArrayList<Task>> allTasks = store.getTasks();
		LogicMasterListModification.assignTaskIds(allTasks);
		todo.clear();
		todo.addAll(allTasks.get(0));
		deadlines.clear();
		deadlines.addAll(allTasks.get(1));
		events.clear();
		events.addAll(allTasks.get(2));
		return Constants.DIRECTORY_PATH_CHANGED_FEEDBACK + "\"" + variableArray[0] +  "\"";
	}
	
	protected String checkPath(Storage store)	{
		return store.checkPath();
	}
	
	private boolean isValidPath(Storage store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}
	
}
