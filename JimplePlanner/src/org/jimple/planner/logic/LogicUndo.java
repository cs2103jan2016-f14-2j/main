package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import org.jimple.planner.Task;
import org.jimple.planner.storage.Storage;

public class LogicUndo implements LogicTaskModification {

	private static final String UNDO_FEEDBACK = "task undone";
	private static final String UNDO_FEEDBACK_ERROR = "no task to undo";
	private static final int DELETE_CACHE_LIMIT = 20;

	public String undoPreviousChange(Storage store, ArrayList<Task> deletedTask, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		if (deletedTask.isEmpty())	{
			return UNDO_FEEDBACK_ERROR;
		}
		checkOverDeletedCacheLimit(deletedTask);
		Task aTask = deletedTask.remove(deletedTask.size() - 1);
		allocateCorrectTimeArray(aTask, todo, deadlines, events);
		packageForSavingInFile(store, todo, deadlines, events);
		return UNDO_FEEDBACK;
	}

	private void checkOverDeletedCacheLimit(ArrayList<Task> deletedTask) {
		if (deletedTask.size() > DELETE_CACHE_LIMIT) {
			deletedTask.remove(0);
		}
	}
}
