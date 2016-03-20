package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Storage;
import org.jimple.planner.Task;

public interface LogicTaskModification {
	final String TYPE_TODO = "floating";
	final String TYPE_DEADLINE = "deadline";
	final String TYPE_EVENT = "event";
	
	public default Task doEdit(String[] variableArray, Task aTask) {
		Task editedTask = new Task(aTask);
		for (int i = 0; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 0:
					editedTask.setTitle(variableArray[0]);
					break;
				case 1:
					editedTask.setDescription(variableArray[i]);
					break;
				case 2:
					editedTask.setFromDate(variableArray[i]);
					break;
				case 3:
					editedTask.setToDate(variableArray[i]);
					break;
				case 4:
					editedTask.setCategory(variableArray[i]);
					break;
				default:
					break;
				}
			}
		}
		return editedTask;
	}
	
	public default void allocateCorrectTimeArray(Task newTask, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		// check if null
		if (newTask.getType().compareTo(TYPE_TODO) == 0) {
			todo.add(newTask);
		}
		// check if whole day task
		else if (newTask.getType().compareTo(TYPE_DEADLINE) == 0) {
			deadlines.add(newTask);
		} else if (newTask.getType().compareTo(TYPE_EVENT) == 0){
			events.add(newTask);
		}
	}
	
	public default void packageForSavingInFile(Storage store, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		allTasksArray.add(todo);
		allTasksArray.add(deadlines);
		allTasksArray.add(events);
		assignTaskIds(allTasksArray);
		store.isSaved(allTasksArray);
	}
	
	public default void assignTaskIds(ArrayList<ArrayList<Task>> allTasksArray) {
		int taskId = 1;
		for (ArrayList<Task> taskList : allTasksArray) {
			for (Task task : taskList) {
				task.setTaskId(taskId);
				taskId++;
			}
		}
	}
	
	public default boolean isFromAndToTimeCorrect(Task task) {
		if (task.getFromTime() == null || task.getToTime() == null) {
			return true;
		} else if (task.getFromTime().compareTo(task.getToTime()) < 0) {
			return true;
		}
		return false;
	}

}
