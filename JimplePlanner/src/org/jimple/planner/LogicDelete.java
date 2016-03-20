package org.jimple.planner;

import java.io.IOException;
import java.util.ArrayList;

public class LogicDelete extends LogicEdit{
	private static final String DELETED_FEEDBACK = "task deleted";
	private static final String ERROR_DELETED_FEEDBACK = "could not find task to be deleted";
	private static final String STRING_DELETE = "delete";
	
	protected String deleteTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events)
			throws IOException {
		assert variableArray.length == 1;
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventsDeleted = false;

		isFloatDeleted = findTask(todo, variableArray, 0, STRING_DELETE, todo, deadlines, events);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTask(deadlines, variableArray, todo.size(), STRING_DELETE, todo, deadlines, events);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventsDeleted = findTask(events, variableArray, todo.size() + deadlines.size(), STRING_DELETE, todo, deadlines, events);
		}
		if (isFloatDeleted || isDeadlineDeleted || isEventsDeleted) {
			packageForSavingInFile(store, todo, deadlines, events);
			return DELETED_FEEDBACK;
		}
		return ERROR_DELETED_FEEDBACK;
	}
	
	public String testDeleteTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException {
		return deleteTask(store, variableArray, todo, deadlines, events);
	}

}
