package org.jimple.planner.storage;

import java.util.ArrayList;

import org.jimple.planner.Task;

public interface StorageAssignTaskId {
	default void assignTaskIds(ArrayList<ArrayList<Task>> allTasksArray){
		int taskId = 1;
		for(ArrayList<Task> taskList: allTasksArray){
			for(Task task: taskList){
				task.setTaskId(taskId);
				taskId++;
			}
		}
	}
}
