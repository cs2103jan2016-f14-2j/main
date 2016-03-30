package org.jimple.planner.logic;

import org.jimple.planner.storage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.exceptions.LabelExceedTotalException;
import org.jimple.planner.Constants;

public class LogicAdd implements LogicTaskModification, LogicMasterListModification {

	protected String addToTaskList(Storage store, String[] parsedInput, ArrayList<Task> tempHistory,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks) throws IOException, LabelExceedTotalException {
		assert parsedInput.length == 6;
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask, taskLabels);
		if (!isFromAndToTimeCorrect(newTask)) {
			return Constants.ERROR_WRONG_TIME_FEEDBACK;
		}
		allocateCorrectTimeArray(newTask, todo, deadlines, events);
		packageForSavingInFile(store, todo, deadlines, events);
		tempHistory.add(newTask);
		undoTasks.add(setNewPreviousTask(Constants.STRING_ADD, newTask));
		return "\"" + parsedInput[1] + "\"" + Constants.ADDED_FEEDBACK;
	}

	public String testAddToTaskList(Storage store, String[] parsedInput, ArrayList<Task> tempHistory,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks) throws IOException, LabelExceedTotalException {
		return addToTaskList(store, parsedInput, tempHistory, todo, deadlines, events, taskLabels, undoTasks);
	}
}
