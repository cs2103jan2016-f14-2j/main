package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.storage.Storage;

public class LogicArchive implements LogicMasterListModification {

	public String markTaskAsDone(Storage store, String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<TaskLabel> taskLabels) throws IOException {

		boolean isTodoMarked = false;
		boolean isDeadlinesMarked = false;
		boolean isEventsMarked = false;

		isTodoMarked = findTaskToMark(parsedInput, undoTasks, todo, archivedTasks, true);
		if (!isTodoMarked) {
			isDeadlinesMarked = findTaskToMark(parsedInput, undoTasks, deadlines, archivedTasks, true);
		} else if (!isTodoMarked && !isDeadlinesMarked) {
			isEventsMarked = findTaskToMark(parsedInput, undoTasks, deadlines, archivedTasks, true);
		}

		if (isTodoMarked || isDeadlinesMarked || isEventsMarked) {
			packageForSavingMasterLists(store, todo, deadlines, events);
			packageForSavingArchiveLists(store, archivedTasks);
			return "task " + parsedInput[0] + Constants.DONE_FEEDBACK;
		}

		return "task " + parsedInput[0] + Constants.ERROR_DONE_FEEDBACK;
	}

	private boolean findTaskToMark(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> list,
			ArrayList<Task> archivedTasks, boolean mark) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTaskLabel().getLabelId() == Integer.parseInt(parsedInput[0])) {
				list.get(i).setIsDone(mark);
				archivedTasks.add(list.remove(i));
				return true;
			}
		}
		return false;
	}

	public String markTaskAsUndone(Storage store, String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<TaskLabel> taskLabels) throws IOException {
		if (!archivedTasks.isEmpty()) {
			for (int i = 0; i < archivedTasks.size(); i++) {
				if (archivedTasks.get(i).getTaskId() == Integer.parseInt(parsedInput[0])) {
					Task returnedTask = archivedTasks.remove(i);
					returnedTask.setIsDone(false);
					allocateCorrectTimeArray(returnedTask, todo, deadlines, events);
					return "task " + parsedInput[0] + Constants.UNDONE_FEEDBACK;
				}
			}
		}
		return "task " + parsedInput[0] + Constants.ERROR_UNDONE_FEEDBACK;
	}

}
