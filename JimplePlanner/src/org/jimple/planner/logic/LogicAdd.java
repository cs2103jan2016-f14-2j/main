package org.jimple.planner.logic;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.exceptions.InvalidFromAndToTimeException;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//@@author A0124952E
public class LogicAdd implements LogicTaskModification, LogicMasterListModification {

	// @@author A0124952E
	protected String addToTaskList(String[] parsedInput, ArrayList<Task> tempHistory, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		assert parsedInput.length == 6;
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask, taskLabels);
		if (!isFromAndToTimeCorrect(newTask)) {
			throw new InvalidFromAndToTimeException(Constants.ERROR_WRONG_TIME_FEEDBACK);
		}
		allocateCorrectTimeArray(newTask, todo, deadlines, events);
		LogicTaskModification.assignOneTaskId(newTask, idHash);
		tempHistory.add(newTask);
		undoTasks.add(setNewPreviousTask(Constants.STRING_ADD, newTask));
		return "\"" + parsedInput[1] + "\"" + Constants.ADDED_FEEDBACK;
	}

	//@@author generated
	public String testAddToTaskList(String[] parsedInput, ArrayList<Task> tempHistory, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return addToTaskList(parsedInput, tempHistory, todo, deadlines, events, taskLabels, undoTasks, idHash);
	}
}
