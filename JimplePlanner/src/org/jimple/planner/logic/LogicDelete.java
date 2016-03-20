package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Storage;
import org.jimple.planner.Task;

public class LogicDelete extends LogicEdit{
	private static final String DELETED_FEEDBACK = "task deleted";
	private static final String ERROR_DELETED_FEEDBACK = "could not find task to be deleted";
	private static final String STRING_DELETE = "delete";
	
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
			return DELETED_FEEDBACK;
		}
		return ERROR_DELETED_FEEDBACK;
	}
	
	private boolean findTaskToDelete(String[] variableArray, ArrayList<Task> list,
			ArrayList<Task> deletedTasks)
			throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				Task removedTask = list.remove(i);
				deletedTasks.add(removedTask);
				return true;
			}
		}
		return false;
	}
	
	public String testDeleteTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> deletedTasks) throws IOException {
		return deleteTask(store, variableArray, todo, deadlines, events, deletedTasks);
	}

}
