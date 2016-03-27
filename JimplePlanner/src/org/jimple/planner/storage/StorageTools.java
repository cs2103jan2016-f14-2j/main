package org.jimple.planner.storage;

import static org.jimple.planner.Constants.EMPTY_STRING;
import static org.jimple.planner.Constants.ERROR_INVALID_TASK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Task;

public interface StorageTools {
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
	
	default void assignTaskIds(ArrayList<ArrayList<Task>> allTasksArray) {
		int taskId = 1;
		for (ArrayList<Task> taskList : allTasksArray) {
			for (Task task : taskList) {
				task.setTaskId(taskId);
				taskId++;
			}
		}
	}
	default void checkTaskValidity(Task task) {
		if(!task.isValidType()){
			InvalidTaskException invalidTaskException = new InvalidTaskException(ERROR_INVALID_TASK);
			try {
				throw invalidTaskException;
			} catch (InvalidTaskException e) {
				System.out.println("task title:" + task.getTitle());
				System.out.println("task's fromTime is: "+task.getFromTime());
				System.out.println("task's toTime is: "+task.getToTime());
				e.printStackTrace();
			}
		}
	}
}
