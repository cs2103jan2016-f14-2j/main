package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Storage;
import org.jimple.planner.Task;

public class LogicEdit implements LogicTaskModification{
	private static final String EDITED_FEEDBACK = "task edited in planner";
	private static final String ERROR_EDIT_FEEDBACK = "task could not be editted";
	private static final String STRING_EDIT = "edit";
	private static final String STRING_DELETE = "delete";

	protected String editTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events)
			throws IOException {
		assert variableArray.length == 6;
		boolean isToDoEditted = false;
		boolean isWholeDayEditted = false;
		boolean isEventsEditted = false;
		isToDoEditted = findTask(todo, variableArray, 0, STRING_EDIT, todo, deadlines, events);
		if (!isToDoEditted) {
			isWholeDayEditted = findTask(deadlines, variableArray, todo.size(), STRING_EDIT, todo, deadlines, events);
		}
		if (!isWholeDayEditted && !isToDoEditted) {
			isEventsEditted = findTask(events, variableArray, todo.size() + deadlines.size(), STRING_EDIT, todo, deadlines, events);
		}
		if (isToDoEditted || isWholeDayEditted || isEventsEditted) {
			packageForSavingInFile(store, todo, deadlines, events);
			return EDITED_FEEDBACK;
		}
		return ERROR_EDIT_FEEDBACK;
	}
	
	protected boolean findTask(ArrayList<Task> list, String[] variableArray, int previousSizes, String type, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events)
			throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) - previousSizes == i) {
				if (type.equals(STRING_EDIT)) {
					Task taskToBeEditted = list.remove(i);
					Task editedTask = doEdit(createArrayWithoutFirstIndex(variableArray), taskToBeEditted);
					if (!isFromAndToTimeCorrect(editedTask)) {
						list.add(taskToBeEditted);
						return false;
					}
					allocateCorrectTimeArray(editedTask, todo, deadlines, events);
				} else if (type.equals(STRING_DELETE)) {
					list.remove(i);
				}
				return true;
			}
		}
		return false;
	}
	
	private String[] createArrayWithoutFirstIndex(String[] variableArray) {
		String[] parsedInput = new String[5];
		for (int i = 1; i < variableArray.length; i++) {
			parsedInput[i - 1] = variableArray[i];
		}
		return parsedInput;
	}
	
	public boolean testFindTaskToEdit(ArrayList<Task> list, String[] variableArray, int previousSizes, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events)
			throws IOException {
		return findTask(list, variableArray, previousSizes, STRING_EDIT, todo, deadlines, events);
	}
	
	public String testEditTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events)
			throws IOException {
		return editTask(store, variableArray, todo, deadlines, events);
	}

}
