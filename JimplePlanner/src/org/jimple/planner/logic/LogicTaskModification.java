package org.jimple.planner.logic;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.Constants;

import java.util.ArrayList;
import java.util.LinkedList;

public interface LogicTaskModification {

	public default Task doEdit(String[] variableArray, Task aTask, ArrayList<TaskLabel> taskLabels){
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
				case 5:
					TaskLabel label = checkNewTaskLabel(variableArray[i], taskLabels);
					editedTask.setTaskLabel(label);
					break;
				default:
					break;
				}
			}
		}
		return editedTask;
	}

	public default TaskLabel checkNewTaskLabel(String name, ArrayList<TaskLabel> taskLabels){
		if (!taskLabels.isEmpty()) {
			for (TaskLabel aLabel : taskLabels) {
				if (aLabel.getLabelName().equals(name)) {
					return TaskLabel.duplicateTaskLabel(aLabel);
				} else if (name == null) {
					return TaskLabel.getDefaultLabel();
				}
			}
		}
		TaskLabel newLabel = TaskLabel.getNewLabel(name);
		taskLabels.add(newLabel);
		return newLabel;
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

	public default LogicPreviousTask setNewPreviousTask(String command, Task previousTask) {
		LogicPreviousTask aPreviousTask = new LogicPreviousTask(command, previousTask);
		return aPreviousTask;
	}

	public default void checkOverCacheLimit(LinkedList<LogicPreviousTask> undoTasks) {
		while (undoTasks.size() > Constants.DELETE_CACHE_LIMIT) {
			undoTasks.removeFirst();
		}
	}

	public static Task divideMultipleDays(Task aTask) {
		Task newTask = new Task(aTask);
		newTask.setToDate(newTask.getFromTime().toLocalDate().toString() + "T23:59");
		aTask.setFromDate(aTask.getFromTime().toLocalDate().plusDays(1).toString() + "T00:00");
		return newTask;
	}

	public static boolean isFromDateEqualToDate(Task aTask) {
		return aTask.getFromTime().toLocalDate().equals(aTask.getToTime().toLocalDate());
	}

}
