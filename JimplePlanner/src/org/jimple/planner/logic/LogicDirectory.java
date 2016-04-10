package org.jimple.planner.logic;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.storage.*;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import java.util.ArrayList;
import java.util.HashMap;

//@@author A0124952E
public class LogicDirectory implements LogicMasterListModification, LogicTaskModification {
	
	//@@author A0124952E
	protected String changeSaveDirectory(StorageInterface store, LogicConflict conflictChecker, String[] variableArray,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<TaskLabel> taskLabels, HashMap<Integer, Boolean> idHash) {
		if (!isValidPath(store, variableArray)) {
			return Constants.ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		// store = new StorageComponent();
		ArrayList<ArrayList<Task>> allTasks = store.getTasks();
		taskLabels = store.getLabels();
		assignTaskIds(allTasks, idHash);
		LogicLinkLabelsToTasks.linkTasksToLabels(allTasks, taskLabels);
		todo.clear();
		todo.addAll(allTasks.get(0));
		deadlines.clear();
		deadlines.addAll(allTasks.get(1));
		events.clear();
		events.addAll(allTasks.get(2));
		archivedTasks.clear();
		archivedTasks.addAll(allTasks.get(3));
		checkOverCurrentTime(deadlines, events);
		conflictChecker.checkForAllTasksIfConflictWithCurrentTasks(deadlines, events);
		return Constants.DIRECTORY_PATH_CHANGED_FEEDBACK + "\"" + variableArray[0] + "\"";
	}
	
	//@@author A0124952E
	protected String checkPath(StorageInterface store) {
		return store.checkPath();
	}
	
	//@@author A0124952E
	private boolean isValidPath(StorageInterface store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

}
