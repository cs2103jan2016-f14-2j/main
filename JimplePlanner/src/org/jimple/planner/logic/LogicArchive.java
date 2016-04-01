package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.storage.Storage;

public class LogicArchive implements LogicMasterListModification{

	public String markTaskAsDone(Storage store, String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<TaskLabel> taskLabels) throws IOException {
		
		boolean isTodoMarked = false;
		boolean isDeadlinesMarked = false;
		boolean isEventsMarked = false;
		
		isTodoMarked = findTaskToMark(parsedInput, undoTasks, todo, archivedTasks);
		if (!isTodoMarked)	{
			isDeadlinesMarked = findTaskToMark(parsedInput, undoTasks, deadlines, archivedTasks);
		} else if (!isTodoMarked && !isDeadlinesMarked)	{
			isEventsMarked = findTaskToMark(parsedInput, undoTasks, deadlines, archivedTasks);
		}
		
		if (isTodoMarked || isDeadlinesMarked || isEventsMarked)	{
			packageForSavingMasterLists(store, todo, deadlines, events);
			packageForSavingArchiveLists(store, archivedTasks);
			return "task " + parsedInput[0] + Constants.DONE_FEEDBACK;
		}
		
		return "task " + parsedInput[0] + Constants.ERROR_DONE_FEEDBACK;
	}

	private boolean findTaskToMark(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> list,
			ArrayList<Task> archivedTasks) {
		for (int i=0;i<list.size();i++)	{
			if (list.get(i).getTaskLabel().getLabelId() == Integer.parseInt(parsedInput[0]))	{
				list.get(i).setIsDone(true);
				archivedTasks.add(list.remove(i));
			}
		}
		return false;
	}

}
