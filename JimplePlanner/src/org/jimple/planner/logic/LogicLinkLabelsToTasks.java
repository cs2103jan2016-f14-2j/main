package org.jimple.planner.logic;

import java.util.ArrayList;

import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

class LogicLinkLabelsToTasks {
	//@@author A0135808B
	public static void linkTasksToLabels(ArrayList<ArrayList<Task>> allTasks, ArrayList<TaskLabel> labelList){
		for(ArrayList<Task> taskList: allTasks){
			for(Task task: taskList){
				TaskLabel currentTaskLabel = task.getTaskLabel();
				if(currentTaskLabel.equals(TaskLabel.createDefaultLabel())){
					break;
				}
				for(TaskLabel masterLabel: labelList){
					if(isSameLabel(currentTaskLabel, masterLabel)){
						currentTaskLabel = TaskLabel.createDuplicateTaskLabel(masterLabel);
						task.setTaskLabel(currentTaskLabel);
					}
				}
			}
		}
	}
	//@@author A0135808B
	private static boolean isSameLabel(TaskLabel currentTaskLabel, TaskLabel masterLabel) {
		String currentTaskLabelName = currentTaskLabel.getLabelName();
		boolean labelNameEqual = masterLabel.getLabelName().equals(currentTaskLabelName);
		return labelNameEqual;
	}
}
