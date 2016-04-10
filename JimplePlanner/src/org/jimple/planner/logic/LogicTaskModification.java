package org.jimple.planner.logic;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//@@author A0124952E
public interface LogicTaskModification {

	public default Task doEdit(String[] variableArray, Task aTask, ArrayList<TaskLabel> taskLabels) {
		Task editedTask = new Task(aTask);
		for (int i = 1; i < variableArray.length; i++) {
			switch (i) {
			case 1:
				if (variableArray[i] != null) {
					editedTask.setTitle(variableArray[i]);
				}
				break;
			case 2:
				if (variableArray[i] != null) {
					editedTask.setDescription(variableArray[i]);
				}
				break;
			case 3:
				if (variableArray[i] != null) {
					editedTask.setFromDate(variableArray[i]);
				}
				break;
			case 4:
				if (variableArray[i] != null) {
					editedTask.setToDate(variableArray[i]);
				} else if (variableArray[i] == null && variableArray[i - 1] != null) {
					editedTask.setToDate(null);
				}
				break;
			case 5:
				if (variableArray[i] != null) {
					TaskLabel label = checkNewTaskLabel(variableArray[i], taskLabels);
					editedTask.setTaskLabel(label);
				}
				break;
			default:
				break;
			}
		}
		return editedTask;
	}

	public default void checkIfConflictedTaskExistInList(ArrayList<Task> list, Task removedTask) {
		for (int j = 0; j < list.size(); j++) {
			for (int k = 0; k < list.get(j).getConflictedTasks().size(); k++) {
				if (list.get(j).getConflictedTasks().get(k).getTaskId() == removedTask.getTaskId()) {
					list.get(j).getConflictedTasks().remove(k);
				}
			}
		}
	}

	public default TaskLabel checkNewTaskLabel(String name, ArrayList<TaskLabel> taskLabels) {
		if (!taskLabels.isEmpty()) {
			for (TaskLabel aLabel : taskLabels) {
				if (aLabel.getLabelName().equals(name)) {
					return TaskLabel.createDuplicateTaskLabel(aLabel);
				} else if (name.equals("DEFAULT")) {
					return TaskLabel.createDefaultLabel();
				}
			}
		}
		TaskLabel newLabel = TaskLabel.createNewLabel(name);
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
	
	public static void assignOneTaskId(Task newTask, HashMap<Integer, Boolean> idHash) {
		for (int i = 0; i < Constants.MAX_ID; i++) {
			if (idHash.get(i+1).booleanValue() == false) {
				newTask.setTaskId(i+1);
				idHash.put(i+1, true);
				break;
			}
		}
	}
	
	public default void removeTaskId(Task removedTask, HashMap<Integer, Boolean> idHash)	{
		idHash.put(removedTask.getTaskId(), false);
	}

}
