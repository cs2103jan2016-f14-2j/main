package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.storage.Storage;
import org.jimple.planner.Task;

public class LogicEdit implements LogicTaskModification {
	private static final String EDITED_FEEDBACK = "task edited in planner";
	private static final String ERROR_EDIT_FEEDBACK = "task could not be editted";
	private static final String STRING_EDIT = "edit";
	private static final String STRING_DELETE = "delete";

	protected String editTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException {
		boolean isToDoEditted = false;
		boolean isWholeDayEditted = false;
		boolean isEventsEditted = false;
		isToDoEditted = findTaskToEdit(variableArray, todo, deadlines, events);
		if (!isToDoEditted) {
			isWholeDayEditted = findTaskToEdit(variableArray, deadlines, events, todo);
		}
		if (!isWholeDayEditted && !isToDoEditted) {
			isEventsEditted = findTaskToEdit(variableArray, events, todo, deadlines);
		}
		if (isToDoEditted || isWholeDayEditted || isEventsEditted) {
			packageForSavingInFile(store, todo, deadlines, events);
			return EDITED_FEEDBACK;
		}
		return ERROR_EDIT_FEEDBACK;
	}

	protected boolean findTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> listTwo, ArrayList<Task> listThree) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				Task taskToBeEditted = list.remove(i);
				Task editedTask = doEdit(createArrayWithoutFirstIndex(variableArray), taskToBeEditted);
				if (!isFromAndToTimeCorrect(editedTask)) {
					list.add(taskToBeEditted);
					return false;
				}
				allocateCorrectTimeArray(editedTask, list, listTwo, listThree);
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

	public boolean testFindTaskToEdit(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		return findTaskToEdit(variableArray, todo, deadlines, events);
	}

	public String testEditTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException {
		return editTask(store, variableArray, todo, deadlines, events);
	}

}
