package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.storage.*;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0124952E
public class LogicUndo implements LogicTaskModification, LogicMasterListModification {

	protected String undoPreviousChange(Storage store, LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<Task> tempHistory, ArrayList<TaskLabel> taskLabels, HashMap<Integer, Boolean> idHash)
					throws IOException {
		String[] variableArray = new String[1];
		if (undoTasks.size() == 0) {
			return Constants.UNDO_FEEDBACK_ERROR;
		}
		checkOverCacheLimit(undoTasks);
		LogicPreviousTask aTask = undoTasks.removeLast();
		switch (aTask.getPreviousCommand()) {
		case Constants.STRING_ADD:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events, archivedTasks, idHash);
			break;
		case Constants.STRING_DELETE:
			allocateCorrectTimeArray(aTask.getPreviousTask(), todo, deadlines, events);
			break;
		case Constants.STRING_EDIT:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events, archivedTasks, idHash);
			LogicTaskModification.assignOneTaskId(aTask.getPreviousTask(), idHash);
			allocateCorrectTimeArray(tempHistory.remove(tempHistory.size() - 1), todo, deadlines, events);
			break;
		case Constants.STRING_DONE:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events, archivedTasks, idHash);
			LogicTaskModification.assignOneTaskId(aTask.getPreviousTask(), idHash);
			aTask.getPreviousTask().setIsDone(false);
			allocateCorrectTimeArray(aTask.getPreviousTask(), todo, deadlines, events);
			break;
		case Constants.STRING_RETURN:
			variableArray[0] = Integer.toString(aTask.getPreviousTask().getTaskId());
			deletionForUndo(variableArray, todo, deadlines, events, archivedTasks, idHash);
			LogicTaskModification.assignOneTaskId(aTask.getPreviousTask(), idHash);
			aTask.getPreviousTask().setIsDone(true);
			archivedTasks.add(aTask.getPreviousTask());
			break;
		}
		return "task \"" + aTask.getPreviousTask().getTitle() + "\"" + Constants.UNDO_FEEDBACK;
	}

	private void deletionForUndo(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, HashMap<Integer, Boolean> idHash) {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventDeleted = false;

		isFloatDeleted = findTaskToDeleteForUndo(variableArray, todo, idHash);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTaskToDeleteForUndo(variableArray, deadlines, idHash);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventDeleted = findTaskToDeleteForUndo(variableArray, events, idHash);
		}
		if (!isFloatDeleted && !isDeadlineDeleted && !isEventDeleted) {
			findTaskToDeleteForUndo(variableArray, archivedTasks, idHash);
		}
	}

	private boolean findTaskToDeleteForUndo(String[] variableArray, ArrayList<Task> list,
			HashMap<Integer, Boolean> idHash) {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				Task removedTask = list.remove(i);
				removeTaskId(removedTask, idHash);
				return true;
			}
		}
		return false;
	}
}
