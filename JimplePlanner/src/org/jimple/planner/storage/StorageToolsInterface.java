package org.jimple.planner.storage;

import static org.jimple.planner.constants.Constants.EMPTY_STRING;
import static org.jimple.planner.constants.Constants.ERROR_INVALID_TASK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.exceptions.InvalidTaskException;
import org.jimple.planner.task.Task;

//@@author A0135808B
public interface StorageToolsInterface {
	//@@author A0135808B
	default File createFile(String fileName) {
		File file = new File(fileName);
		
		assert !file.isDirectory();
		
		String dirPath = file.getAbsolutePath().replaceAll(file.getName(), EMPTY_STRING);
		File dir = new File(dirPath);
		dir.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	//@@author A0135808B
	default void assignTaskIds(ArrayList<ArrayList<Task>> allTasksArray) {
		int taskId = 1;
		for (ArrayList<Task> taskList : allTasksArray) {
			for (Task task : taskList) {
				task.setTaskId(taskId);
				taskId++;
			}
		}
	}
	//@@author A0135808B
	default void checkTaskValidity(Task task) throws InvalidTaskException {
		if(!task.isValidType()){
			InvalidTaskException invalidTaskException = new InvalidTaskException(ERROR_INVALID_TASK);
				throw invalidTaskException;
		}
	}
}
