package org.jimple.planner.logic;

import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;

class LogicLinkLabelsToTasks {
	public static void linkTasksToLabels(ArrayList<ArrayList<Task>> allTasks, ArrayList<TaskLabel> labelList){
		for(ArrayList<Task> taskList: allTasks){
			for(Task task: taskList){
				TaskLabel currentTaskLabel = task.getTaskLabel();
				if(currentTaskLabel.equals(TaskLabel.getDefaultLabel())){
					break;
				}
				for(TaskLabel masterLabel: labelList){
					if(isSameLabel(currentTaskLabel, masterLabel)){
						currentTaskLabel = TaskLabel.duplicateTaskLabel(masterLabel);
						task.setTaskLabel(currentTaskLabel);
					}
				}
			}
		}
	}

	private static boolean isSameLabel(TaskLabel currentTaskLabel, TaskLabel masterLabel) {
		String currentTaskLabelName = currentTaskLabel.getLabelName();
		int currentTaskLabelColourId = currentTaskLabel.getColourId();
		boolean labelNameEqual = masterLabel.getLabelName().equals(currentTaskLabelName);
		boolean labelColourIdEqual = masterLabel.getColourId() == currentTaskLabelColourId;
		return labelNameEqual && labelColourIdEqual;
	}
}
