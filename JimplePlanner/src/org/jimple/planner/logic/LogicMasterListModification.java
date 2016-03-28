package org.jimple.planner.logic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.storage.Storage;

public interface LogicMasterListModification {
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
	
	public static void checkOverCurrentTime(ArrayList<Task> deadlines, ArrayList<Task> events) {
		for (Task aTask : deadlines) {
			if (aTask.getFromTime() != null) {
				if (aTask.getFromTime().compareTo(LocalDateTime.now()) < 0) {
					aTask.setIsOverDue(true);
				}
			}
		}
		for (Task aTask : events) {
			if (aTask.getFromTime() != null) {
				if (aTask.getToTime().compareTo(LocalDateTime.now()) < 0) {
					aTask.setIsOverDue(true);
				}
			}
		}
	}
}
