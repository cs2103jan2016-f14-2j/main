package org.jimple.planner.logic;
import org.jimple.planner.storage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import org.jimple.planner.Constants;
import org.jimple.planner.Task;

public class LogicDelete implements LogicTaskModification, LogicMasterListModification {
	
	protected String deleteTask(Storage store, String[] variableArray, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			LinkedList<LogicPreviousTask> undoTasks)
			throws IOException {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventsDeleted = false;

		isFloatDeleted = findTaskToDelete(variableArray, todo, undoTasks);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTaskToDelete(variableArray, deadlines, undoTasks);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventsDeleted = findTaskToDelete(variableArray, events, undoTasks);
		}
		if (isFloatDeleted || isDeadlineDeleted || isEventsDeleted) {
			packageForSavingInFile(store, todo, deadlines, events);
			return "task " + variableArray[0] + Constants.DELETED_FEEDBACK;
		}
		return "task " + variableArray[0] + Constants.ERROR_DELETED_FEEDBACK;
	}
	
	private boolean findTaskToDelete(String[] variableArray, ArrayList<Task> list,
			LinkedList<LogicPreviousTask> undoTasks)
			throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				checkOverCacheLimit(undoTasks);
				Task removedTask = list.remove(i);
				undoTasks.add(setNewPreviousTask(Constants.STRING_DELETE, removedTask));
				return true;
			}
		}
		return false;
	}

	public String testDeleteTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, LinkedList<LogicPreviousTask> undoTasks) throws IOException {
		return deleteTask(store, variableArray, todo, deadlines, events, undoTasks);
	}
	
	public boolean testFindTaskToDelete(String[] variableArray, ArrayList<Task> list, LinkedList<LogicPreviousTask> undoTasks) throws IOException	{
		return findTaskToDelete(variableArray, list, undoTasks);
	}

}
