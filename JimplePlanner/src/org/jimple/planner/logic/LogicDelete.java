package org.jimple.planner.logic;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//@@author A0124952E
public class LogicDelete implements LogicTaskModification, LogicMasterListModification {
	
	//@@author A0124952E
	protected String deleteTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, LinkedList<LogicPreviousTask> undoTasks,
			HashMap<Integer, Boolean> idHash) throws IOException {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventsDeleted = false;
		boolean isArchivedDeleted = false;

		isFloatDeleted = findTaskToDelete(variableArray, todo, undoTasks, idHash);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTaskToDelete(variableArray, deadlines, undoTasks, idHash);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventsDeleted = findTaskToDelete(variableArray, events, undoTasks, idHash);
		}
		if (!isFloatDeleted && !isDeadlineDeleted && !isEventsDeleted) {
			isArchivedDeleted = findTaskToDelete(variableArray, archivedTasks, undoTasks, idHash);
		}
		if (isFloatDeleted || isDeadlineDeleted || isEventsDeleted || isArchivedDeleted) {
			return "task " + variableArray[0] + Constants.DELETED_FEEDBACK;
		}
		return "task " + variableArray[0] + Constants.ERROR_DELETED_FEEDBACK;
	}
	
	//@@author A0124952E
	private boolean findTaskToDelete(String[] variableArray, ArrayList<Task> list,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				checkOverCacheLimit(undoTasks);
				Task removedTask = list.remove(i);
				removeTaskId(removedTask, idHash);
				// checkIfConflictedTaskExistInList(list, removedTask);
				undoTasks.add(setNewPreviousTask(Constants.STRING_DELETE, removedTask));
				return true;
			}
		}
		return false;
	}
	
	//@@author generated
	public String testDeleteTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, LinkedList<LogicPreviousTask> undoTasks,
			HashMap<Integer, Boolean> idHash) throws IOException {
		return deleteTask(variableArray, todo, deadlines, events, archivedTasks, undoTasks, idHash);
	}

	//@@author generated
	public boolean testFindTaskToDelete(String[] variableArray, ArrayList<Task> list,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash) throws IOException {
		return findTaskToDelete(variableArray, list, undoTasks, idHash);
	}

}
