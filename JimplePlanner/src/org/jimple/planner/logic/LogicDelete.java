package org.jimple.planner.logic;
import org.jimple.planner.storage.*;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

public class LogicDelete extends LogicEdit{
	
	protected String deleteTask(Storage store, String[] variableArray, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> deletedTasks)
			throws IOException {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventsDeleted = false;

		isFloatDeleted = findTaskToDelete(variableArray, todo, deletedTasks);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTaskToDelete(variableArray, deadlines, deletedTasks);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventsDeleted = findTaskToDelete(variableArray, events, deletedTasks);
		}
		if (isFloatDeleted || isDeadlineDeleted || isEventsDeleted) {
			packageForSavingInFile(store, todo, deadlines, events);
			return "task " + variableArray[0] + Constants.DELETED_FEEDBACK;
		}
		return "task " + variableArray[0] + Constants.ERROR_DELETED_FEEDBACK;
	}
	
	private boolean findTaskToDelete(String[] variableArray, ArrayList<Task> list,
			ArrayList<Task> deletedTasks)
			throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				checkOverDeletedCacheLimit(deletedTasks);
				Task removedTask = list.remove(i);
				deletedTasks.add(removedTask);
				return true;
			}
		}
		return false;
	}
	
	private void checkOverDeletedCacheLimit(ArrayList<Task> deletedTasks) {
		while (deletedTasks.size() > Constants.DELETE_CACHE_LIMIT) {
			deletedTasks.remove(0);
		}
	}

	public String testDeleteTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> deletedTasks) throws IOException {
		return deleteTask(store, variableArray, todo, deadlines, events, deletedTasks);
	}
	
	public boolean testFindTaskToDelete(String[] variableArray, ArrayList<Task> list, ArrayList<Task> deletedTasks) throws IOException	{
		return findTaskToDelete(variableArray, list, deletedTasks);
	}

}
