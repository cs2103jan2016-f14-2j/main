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
		todo = store.getTasks().get(0);
		deadlines = store.getTasks().get(1);
		events = store.getTasks().get(2);
		return Constants.DIRECTORY_PATH_CHANGED_FEEDBACK + "\"" + variableArray[0] +  "\"";
	}

	private boolean isValidPath(Storage store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

}
