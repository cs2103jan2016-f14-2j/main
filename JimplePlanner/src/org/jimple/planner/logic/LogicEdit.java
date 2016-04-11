package org.jimple.planner.logic;

import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.exceptions.InvalidFromAndToTimeException;

//@@author A0124952E
public class LogicEdit implements LogicTaskModification, LogicMasterListModification {

	// @@author A0124952E
	protected String editTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, ArrayList<Task> tempHistory,
			ArrayList<TaskLabel> taskLabels, LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		boolean isToDoEdited = false;
		boolean isWholeDayEdited = false;
		boolean isEventsEdited = false;
		boolean isArchivedEdited = false;

		isToDoEdited = findTaskToEdit(variableArray, todo, todo, deadlines, events, archivedTasks, tempHistory,
				taskLabels, undoTasks, idHash);
		if (!isToDoEdited) {
			isWholeDayEdited = findTaskToEdit(variableArray, deadlines, todo, deadlines, events, archivedTasks,
					tempHistory, taskLabels, undoTasks, idHash);
		}
		if (!isWholeDayEdited && !isToDoEdited) {
			isEventsEdited = findTaskToEdit(variableArray, events, todo, deadlines, events, archivedTasks, tempHistory,
					taskLabels, undoTasks, idHash);
		}
		if (!isWholeDayEdited && !isToDoEdited && !isEventsEdited) {
			isArchivedEdited = findTaskToEdit(variableArray, archivedTasks, todo, deadlines, events, archivedTasks, tempHistory, taskLabels,
					undoTasks, idHash);
		}
		if (isToDoEdited || isWholeDayEdited || isEventsEdited || isArchivedEdited) {
			return "task " + variableArray[0] + Constants.EDITED_FEEDBACK;
		}
		return "task " + variableArray[0] + Constants.ERROR_EDIT_FEEDBACK;
	}

	// @@author A0124952E
	protected boolean findTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<Task> tempHistory, ArrayList<TaskLabel> taskLabels, LinkedList<LogicPreviousTask> undoTasks,
			HashMap<Integer, Boolean> idHash) throws IOException, InvalidFromAndToTimeException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) == list.get(i).getTaskId()) {
				Task taskToBeEdited = list.remove(i);
				Task editedTask = doEdit(variableArray, taskToBeEdited, taskLabels);
				if (!isFromAndToTimeCorrect(editedTask)) {
					list.add(taskToBeEdited);
					throw new InvalidFromAndToTimeException(Constants.ERROR_WRONG_TIME_FEEDBACK);
				}
				tempHistory.add(taskToBeEdited);
				undoTasks.add(setNewPreviousTask(Constants.STRING_EDIT, editedTask));

				if (!editedTask.getIsDone()) {
					allocateCorrectTimeArray(editedTask, todo, deadlines, events);
				} else if (editedTask.getIsDone()) {
					archivedTasks.add(editedTask);
				}
				return true;
			}
		}
		return false;
	}

	// @@author generated
	public boolean testFindTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks, ArrayList<Task> tempHistory,
			ArrayList<TaskLabel> taskLabels, LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return findTaskToEdit(variableArray, list, todo, deadlines, events, archivedTasks, tempHistory, taskLabels, undoTasks, idHash);
	}

	// @@author generated
	public String testEditTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, ArrayList<Task> tempHistory, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return editTask(variableArray, todo, deadlines, events, archivedTasks, tempHistory, taskLabels, undoTasks, idHash);
	}

}
