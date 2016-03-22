package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.storage.*;

public class LogicUndo implements LogicTaskModification {

	public String undoPreviousChange(Storage store, ArrayList<Task> deletedTask, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		if (deletedTask.isEmpty())	{
			return Constants.UNDO_FEEDBACK_ERROR;
		}
		Task aTask = deletedTask.remove(deletedTask.size() - 1);
		allocateCorrectTimeArray(aTask, todo, deadlines, events);
		packageForSavingInFile(store, todo, deadlines, events);
		return "task \"" + aTask.getTitle() +  "\"" +Constants.UNDO_FEEDBACK;
	}

}
