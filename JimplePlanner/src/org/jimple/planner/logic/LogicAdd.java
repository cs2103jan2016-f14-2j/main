package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.storage.Storage;
import org.jimple.planner.Task;

public class LogicAdd implements LogicTaskModification{
	private static final String ERROR_ADDED_FEEDBACK = "task could not be added";
	private static final String ADDED_FEEDBACK = "task added to planner";
	
	protected String addToTaskList(Storage store, String[] parsedInput, ArrayList<Task>tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		assert parsedInput.length == 5;
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask);
		if (!isFromAndToTimeCorrect(newTask)) {
			return ERROR_ADDED_FEEDBACK;
		}
		allocateCorrectTimeArray(newTask, todo, deadlines, events);
		packageForSavingInFile(store, todo, deadlines, events);
		tempHistory.add(newTask);
		return ADDED_FEEDBACK;
	}
	
	public String testAddToTaskList(Storage store, String[] parsedInput, ArrayList<Task> tempHistory, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		return addToTaskList(store, parsedInput, tempHistory, todo, deadlines, events);
	}
}
