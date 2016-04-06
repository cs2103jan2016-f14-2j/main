package org.jimple.planner.logic;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.storage.*;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import java.util.ArrayList;

//@@author A0124952E
public class LogicDirectory implements LogicMasterListModification, LogicTaskModification{

	protected String changeSaveDirectory(Storage store, String[] variableArray, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) {
		if (!isValidPath(store, variableArray)) {
			return Constants.ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		// store = new StorageComponent();
		ArrayList<ArrayList<Task>> allTasks = store.getTasks();
		taskLabels = store.getLabels();
		assignTaskIds(allTasks);
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
		checkForAllTasksIfConflictWithCurrentTasks(deadlines, events);
		return Constants.DIRECTORY_PATH_CHANGED_FEEDBACK + "\"" + variableArray[0] + "\"";
	}

	protected String checkPath(Storage store) {
		return store.checkPath();
	}

	private boolean isValidPath(Storage store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

}
