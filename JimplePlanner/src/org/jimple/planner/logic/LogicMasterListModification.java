package org.jimple.planner.logic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.storage.StorageInterface;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0124952E
public interface LogicMasterListModification {
	
	//@@author A0124952E
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

	//@@author A0124952E
	public default void packageForSavingMasterLists(StorageInterface store, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks) throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		allTasksArray.add(todo);
		allTasksArray.add(deadlines);
		allTasksArray.add(events);
		allTasksArray.add(archivedTasks);
		store.isSavedTasks(allTasksArray);
	}

	//@@author A0124952E
	public default void packageForSavingLabelLists(StorageInterface store, ArrayList<TaskLabel> taskLabels) {
		store.isSavedLabels(taskLabels);
	}
	
	//@@author A0124952E
	public default void assignTaskIds(ArrayList<ArrayList<Task>> allTasks, HashMap<Integer, Boolean> idHash)	{
		for (int i=0;i<allTasks.size();i++)	{
			for (Task aTask : allTasks.get(i))	{
				LogicTaskModification.assignOneTaskId(aTask, idHash);
			}
		}
	}

	//@@author A0124952E
	public default void checkOverCurrentTime(ArrayList<Task> deadlines, ArrayList<Task> events) {
		for (Task aTask : deadlines) {
			if (aTask.getFromTime() != null) {
				if (aTask.getFromTime().compareTo(LocalDateTime.now()) < 0) {
					aTask.setIsOverDue(true);
				} else 	{
					aTask.setIsOverDue(false);
				}
			}
		}
		for (Task aTask : events) {
			if (aTask.getFromTime() != null) {
				if (aTask.getToTime().compareTo(LocalDateTime.now()) < 0) {
					aTask.setIsOverDue(true);
				} else	{
					aTask.setIsOverDue(false);
				}
			}
		}
	}

	//@@author A0124952E
	public default ArrayList<Task> getDividedTasks(ArrayList<Task> events) {
		ArrayList<Task> dividedTasks = new ArrayList<Task>();
		for (Task anEvent : events) {
			Task duplicateEvent = new Task(anEvent);
			while (!duplicateEvent.getFromTime().toLocalDate().equals(duplicateEvent.getToTime().toLocalDate())) {
				Task aNewTask = LogicTaskModification.divideMultipleDays(duplicateEvent);
				dividedTasks.add(aNewTask);
			}
			dividedTasks.add(duplicateEvent);
		}
		Task.sortTasksByTime(dividedTasks);
		return dividedTasks;
	}
}
