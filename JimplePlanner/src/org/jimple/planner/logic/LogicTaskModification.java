package org.jimple.planner.logic;

import org.jimple.planner.storage.*;
import org.jimple.planner.Task;
import org.jimple.planner.Constants;

import java.io.IOException;
import java.util.ArrayList;

public interface LogicTaskModification {

	public default Task doEdit(String[] variableArray, Task aTask) {
		Task editedTask = new Task(aTask);
		for (int i = 1; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 1:
					editedTask.setTitle(variableArray[1]);
					break;
				case 2:
					editedTask.setDescription(variableArray[i]);
					break;
				case 3:
					editedTask.setFromDate(variableArray[i]);
					break;
				case 4:
					editedTask.setToDate(variableArray[i]);
					break;
				case 5:
					editedTask.setCategory(variableArray[i]);
					break;
				default:
					break;
				}
			}
		}
		return editedTask;
	}

	public default void allocateCorrectTimeArray(Task newTask, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException {
		// check if null
		if (newTask.getType().compareTo(Constants.TYPE_TODO) == 0) {
			todo.add(newTask);
		}
		// check if whole day task
		else if (newTask.getType().compareTo(Constants.TYPE_DEADLINE) == 0) {
			deadlines.add(newTask);
		} else if (newTask.getType().compareTo(Constants.TYPE_EVENT) == 0) {
			events.add(newTask);
		}
	}

	public default void packageForSavingInFile(Storage store, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events) throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		allTasksArray.add(todo);
		allTasksArray.add(deadlines);
		allTasksArray.add(events);
		assignTaskIds(allTasksArray);
		store.isSaved(allTasksArray);
	}

	public static void assignTaskIds(ArrayList<ArrayList<Task>> allTasksArray) {
		int taskId = 1;
		for (ArrayList<Task> taskList : allTasksArray) {
			for (Task task : taskList) {
				task.setTaskId(taskId);
				taskId++;
			}
		}
	}

	public default boolean isFromAndToTimeCorrect(Task task) {
		if (task.getFromTime() == null && task.getToTime() == null) {
			return true;
		} else if (task.getFromTime() != null && task.getToTime() == null) {
			return true;
		} else if (task.getFromTime().compareTo(task.getToTime()) < 0) {
			return true;
		}
		return false;
	}

}
