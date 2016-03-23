package org.jimple.planner.logic;

import org.jimple.planner.storage.*;
import org.jimple.planner.Constants;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Task;

public class LogicEdit implements LogicTaskModification {

	protected String editTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException, InvalidFromAndToTime {
		boolean isToDoEditted = false;
		boolean isWholeDayEditted = false;
		boolean isEventsEditted = false;
		try {
			isToDoEditted = findTaskToEdit(variableArray, todo, todo, deadlines, events);
			if (!isToDoEditted) {
				isWholeDayEditted = findTaskToEdit(variableArray, deadlines, todo, deadlines, events);
			}
			if (!isWholeDayEditted && !isToDoEditted) {
				isEventsEditted = findTaskToEdit(variableArray, events, todo, deadlines, events);
			}
			if (isToDoEditted || isWholeDayEditted || isEventsEditted) {
				packageForSavingInFile(store, todo, deadlines, events);
				return "task " + variableArray[0] + Constants.EDITED_FEEDBACK;
			}
			return "task " + variableArray[0] + Constants.ERROR_EDIT_FEEDBACK;
		} catch (InvalidFromAndToTime ift) {
			return ift.getMessage();
		}
	}

	protected boolean findTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException, InvalidFromAndToTime {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				Task taskToBeEditted = list.remove(i);
				Task editedTask = doEdit(variableArray, taskToBeEditted);
				if (!isFromAndToTimeCorrect(editedTask)) {
					list.add(taskToBeEditted);
					throw new InvalidFromAndToTime(Constants.ERROR_WRONG_TIME_FEEDBACK);
				}
				allocateCorrectTimeArray(editedTask, todo, deadlines, events);
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

	public boolean testFindTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException, InvalidFromAndToTime {
		return findTaskToEdit(variableArray, list, todo, deadlines, events);
	}

	public String testEditTask(Storage store, String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException, InvalidFromAndToTime {
		return editTask(store, variableArray, todo, deadlines, events);
	}

}
