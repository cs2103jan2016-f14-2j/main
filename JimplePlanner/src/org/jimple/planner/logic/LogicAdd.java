package org.jimple.planner.logic;
import org.jimple.planner.storage.*;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.Constants;

public class LogicAdd implements LogicTaskModification{
	
	protected String addToTaskList(Storage store, String[] parsedInput, ArrayList<Task>tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		assert parsedInput.length == 6;
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask);
		if (!isFromAndToTimeCorrect(newTask)) {
			return Constants.ERROR_ADDED_FEEDBACK;
		}
		allocateCorrectTimeArray(newTask, todo, deadlines, events);
		packageForSavingInFile(store, todo, deadlines, events);
		tempHistory.add(newTask);
		return Constants.ADDED_FEEDBACK;
	}
	
	public String testAddToTaskList(Storage store, String[] parsedInput, ArrayList<Task> tempHistory, 
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		return addToTaskList(store, parsedInput, tempHistory, todo, deadlines, events);
	}
}
