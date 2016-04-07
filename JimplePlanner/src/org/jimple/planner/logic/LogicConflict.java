package org.jimple.planner.logic;

import java.util.ArrayList;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.exceptions.NoSuchTaskWithConflictException;
import org.jimple.planner.task.Task;

//@@author A0124952E
public class LogicConflict {
	public static String[] mostRecentlyCheckedConflict = new String[1];

	protected void checkForAllTasksIfConflictWithCurrentTasks(ArrayList<Task> deadlines, ArrayList<Task> events) {
		for (Task aDeadline : deadlines) {
			aDeadline.getConflictedTasks().clear();
			checkIsConflictWithCurrentTasks(aDeadline, deadlines, events);
		}
		for (Task anEvent : events) {
			anEvent.getConflictedTasks().clear();
			checkIsConflictWithCurrentTasks(anEvent, deadlines, events);
		}
	}

	protected ArrayList<Task> getConflictedTasks(String[] parsedInput, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> conflictedTasks) {
		if (!deadlines.isEmpty() || !events.isEmpty()) {
			mostRecentlyCheckedConflict = parsedInput;
			for (Task aDeadline : deadlines) {
				if (aDeadline.getTaskId() == Integer.parseInt(parsedInput[0])) {
					conflictedTasks = aDeadline.getConflictedTasks();
				}
			}
			for (Task anEvent : events) {
				if (anEvent.getTaskId() == Integer.parseInt(parsedInput[0])) {
					conflictedTasks = anEvent.getConflictedTasks();
				}
			}
		}
		return conflictedTasks;
	}

	private void checkIsConflictWithCurrentTasks(Task newTask, ArrayList<Task> deadlines, ArrayList<Task> events) {
		switch (newTask.getType()) {
		case Constants.TYPE_DEADLINE:
			if (!deadlines.isEmpty()) {
				for (int i = 0; i < deadlines.size(); i++) {
					if (newTask.getFromTime().equals(deadlines.get(i).getFromTime())
							&& newTask.getTaskId() != deadlines.get(i).getTaskId()) {
						newTask.getConflictedTasks().add(deadlines.get(i));
						deadlines.get(i).getConflictedTasks().add(newTask);
						break;
					}
				}
			}
			break;
		case Constants.TYPE_EVENT:
			if (!events.isEmpty()) {
				for (int i = 0; i < events.size(); i++) {
					if ((isToTimeExceedTimeRange(newTask, events.get(i))
							|| isFromTimeExceedTimeRange(newTask, events.get(i))
							|| isFromAndToTimeEncompassTimeRange(newTask, events.get(i)))
							&& newTask.getTaskId() != events.get(i).getTaskId()) {
						newTask.getConflictedTasks().add(events.get(i));
						events.get(i).getConflictedTasks().add(newTask);
						break;
					}
				}
			}
			System.out.println("working");
			break;
		}
	}

	private boolean isToTimeExceedTimeRange(Task newTask, Task event) {
		if (newTask.getToTime().compareTo(event.getFromTime()) > 0
				&& newTask.getToTime().compareTo(event.getToTime()) < 0) {
			return true;
		}
		return false;
	}

	private boolean isFromTimeExceedTimeRange(Task newTask, Task event) {
		if (newTask.getFromTime().compareTo(event.getFromTime()) > 0
				&& newTask.getFromTime().compareTo(event.getToTime()) < 0) {
			return true;
		}
		return false;
	}

	private boolean isFromAndToTimeEncompassTimeRange(Task newTask, Task event) {
		if (newTask.getFromTime().compareTo(event.getFromTime()) <= 0
				&& newTask.getToTime().compareTo(event.getToTime()) >= 0) {
			return true;
		}
		return false;
	}

}
