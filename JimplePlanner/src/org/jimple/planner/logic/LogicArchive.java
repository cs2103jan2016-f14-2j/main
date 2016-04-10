package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0124952E
public class LogicArchive implements LogicMasterListModification, LogicTaskModification {
	
	//@@author A0124952E
	protected String markTaskAsDone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		boolean isTodoMarked = false;
		boolean isDeadlinesMarked = false;
		boolean isEventsMarked = false;

		isTodoMarked = findTaskToMark(parsedInput, undoTasks, todo, archivedTasks, tempHistory);
		if (!isTodoMarked) {
			isDeadlinesMarked = findTaskToMark(parsedInput, undoTasks, deadlines, archivedTasks, tempHistory);
		}
		if (!isTodoMarked && !isDeadlinesMarked) {
			isEventsMarked = findTaskToMark(parsedInput, undoTasks, events, archivedTasks, tempHistory);
		}

		if (isTodoMarked || isDeadlinesMarked || isEventsMarked) {
			return "task " + parsedInput[0] + Constants.DONE_FEEDBACK;
		}

		return "task " + parsedInput[0] + Constants.ERROR_DONE_FEEDBACK;
	}
	
	//@@author A0124952E
	private boolean findTaskToMark(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> list,
			ArrayList<Task> archivedTasks, ArrayList<Task> tempHistory) {
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getTaskId() == Integer.parseInt(parsedInput[0])) {
					Task anArchivedTask = list.remove(i);
					anArchivedTask.setIsDone(true);
					archivedTasks.add(anArchivedTask);
					undoTasks.add(setNewPreviousTask(Constants.STRING_DONE, anArchivedTask));
					tempHistory.add(anArchivedTask);
					return true;
				}
			}
		}
		return false;
	}
	
	//@@author A0124952E
	protected String markTaskAsUndone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		if (!archivedTasks.isEmpty()) {
			for (int i = 0; i < archivedTasks.size(); i++) {
				if (archivedTasks.get(i).getTaskId() == Integer.parseInt(parsedInput[0])) {
					Task returnedTask = archivedTasks.remove(i);
					returnedTask.setIsDone(false);
					allocateCorrectTimeArray(returnedTask, todo, deadlines, events);
					undoTasks.add(setNewPreviousTask(Constants.STRING_RETURN, returnedTask));
					tempHistory.add(returnedTask);
					return "task " + parsedInput[0] + Constants.UNDONE_FEEDBACK;
				}
			}
		}
		return "task " + parsedInput[0] + Constants.ERROR_UNDONE_FEEDBACK;
	}
	
	//@@author A0124952E
	public String testMarkTaskAsDone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		return markTaskAsDone(parsedInput, undoTasks, tempHistory, todo, deadlines, events, archivedTasks, taskLabels);
	}
	
	//@@author A0124952E
	public String testMarkTaskAsUndone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		return markTaskAsUndone(parsedInput, undoTasks, tempHistory, todo, deadlines, events, archivedTasks,
				taskLabels);
	}

}
