package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.storage.*;

public class LogicUndo implements LogicTaskModification, LogicMasterListModification {

	protected String undoPreviousChange(Storage store, LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> tempHistory,
			ArrayList<TaskLabel> taskLabels) throws IOException {
		String[] variableArray = new String[1];
		if (undoTasks.size() == 0) {
			return Constants.UNDO_FEEDBACK_ERROR;
		}
		checkOverCacheLimit(undoTasks);
		LogicPreviousTask aTask = undoTasks.removeLast();
		switch (aTask.getPreviousCommand()) {
		case Constants.STRING_ADD:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events);
			break;
		case Constants.STRING_DELETE:
			allocateCorrectTimeArray(aTask.getPreviousTask(), todo, deadlines, events);
			break;
		case Constants.STRING_EDIT:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events);
			allocateCorrectTimeArray(tempHistory.remove(tempHistory.size() - 1), todo, deadlines, events);
			break;
		}
		return "task \"" + aTask.getPreviousTask().getTitle() + "\"" + Constants.UNDO_FEEDBACK;
	}

	private void deletionForUndo(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;

		isFloatDeleted = findTaskToDeleteForUndo(variableArray, todo);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTaskToDeleteForUndo(variableArray, deadlines);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			findTaskToDeleteForUndo(variableArray, events);
		}
	}

	private boolean findTaskToDeleteForUndo(String[] variableArray, ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				list.remove(i);
				return true;
			}
		}
		return false;
	}
}
