package org.jimple.planner.logic;

import org.jimple.planner.Task;
import org.jimple.planner.Constants;

import java.util.LinkedList;

public interface LogicTaskModification {

	public default Task doEdit(String[] variableArray, Task aTask) {
		Task editedTask = new Task(aTask);
		for (int i = 1; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 1:
					editedTask.setTitle(variableArray[i]);
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
				//change to label
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
	
	public default LogicPreviousTask setNewPreviousTask(String command, Task previousTask)	{
		LogicPreviousTask aPreviousTask = new LogicPreviousTask(command, previousTask);
		return aPreviousTask;
	}
	
	public default void checkOverCacheLimit(LinkedList<LogicPreviousTask> undoTasks) {
		while (undoTasks.size() > Constants.DELETE_CACHE_LIMIT) {
			undoTasks.removeFirst();
		}
	}
}
