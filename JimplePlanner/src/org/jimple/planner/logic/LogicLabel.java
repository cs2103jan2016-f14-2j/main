package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.storage.StorageInterface;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

//@@author A0124952E
public class LogicLabel implements LogicMasterListModification, LogicTaskModification {
	HashMap<String, Integer> colourToIDMap = new HashMap<String, Integer>();
	HashMap<Integer, String> iDToColourMap = new HashMap<Integer, String>();

	public LogicLabel() {
		colourToIDMap.put("blue", 1);
		colourToIDMap.put("green", 2);
		colourToIDMap.put("yellow", 3);
		colourToIDMap.put("orange", 4);
		colourToIDMap.put("red", 5);
		colourToIDMap.put("purple", 6);
		iDToColourMap.put(1, "blue");
		iDToColourMap.put(2, "green");
		iDToColourMap.put(3, "yellow");
		iDToColourMap.put(4, "orange");
		iDToColourMap.put(5, "red");
		iDToColourMap.put(6, "purple");
	}

	protected String changeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		if (isName(variableArray[0], taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "name");
			boolean isLabelChanged = isChangeLabel(variableArray, taskLabels, labelPosition, todo, deadlines, events);
			if (isLabelChanged) {
				return variableArray[0] + Constants.LABEL_FEEDBACK;
			}
		} else if (isColour(variableArray[0], taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "colour");
			boolean isLabelChanged = isChangeLabel(variableArray, taskLabels, labelPosition, todo, deadlines, events);
			if (isLabelChanged) {
				return variableArray[0] + Constants.LABEL_FEEDBACK;
			}
		}
		return Constants.ERROR_LABEL_FEEDBACK;
	}

	protected String deleteLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks) throws IOException {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (variableArray[0].equals(taskLabels.get(i).getLabelName())) {
				TaskLabel removedTask = taskLabels.remove(i);
				removeLabelsFromMasterList(todo, deadlines, events, archivedTasks, removedTask);
				return Constants.LABEL_DELETED_FEEDBACK;
			}
		}
		return Constants.ERROR_LABEL_DELETED_FEEDBACK;
	}

	private void removeLabelsFromMasterList(ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, TaskLabel removedTask) {
		for (int j = 0; j < todo.size(); j++) {
			if (todo.get(j).getTaskLabel().getLabelName().equals(removedTask.getLabelName())) {
				todo.get(j).setTaskLabel(TaskLabel.createDefaultLabel());
			}
		}
		for (int j = 0; j < deadlines.size(); j++) {
			if (deadlines.get(j).getTaskLabel().getLabelName().equals(removedTask.getLabelName())) {
				deadlines.get(j).setTaskLabel(TaskLabel.createDefaultLabel());
			}
		}
		for (int j = 0; j < events.size(); j++) {
			if (events.get(j).getTaskLabel().getLabelName().equals(removedTask.getLabelName())) {
				events.get(j).setTaskLabel(TaskLabel.createDefaultLabel());
			}
		}
		for (int j = 0; j < archivedTasks.size(); j++) {
			if (archivedTasks.get(j).getTaskLabel().getLabelName().equals(removedTask.getLabelName())) {
				archivedTasks.get(j).setTaskLabel(TaskLabel.createDefaultLabel());
			}
		}
	}

	private int getLabelPosition(String[] variableArray, ArrayList<TaskLabel> taskLabels, String type) {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (type.equals("colour")) {
				if (colourToIDMap.get(variableArray[0]).equals(taskLabels.get(i).getColourId())) {
					return taskLabels.get(i).getLabelId();
				}
			} else if (type.equals("name")) {
				if (variableArray[0].equals(taskLabels.get(i).getLabelName())) {
					return taskLabels.get(i).getLabelId();
				}
			}
		}
		return -1;
	}

	private boolean isColour(String firstString, ArrayList<TaskLabel> taskLabels) {
		if (!taskLabels.isEmpty()) {
			if (colourToIDMap.containsKey(firstString)) {
				for (int i = 0; i < taskLabels.size(); i++) {
					if (colourToIDMap.get(firstString).equals(taskLabels.get(i).getColourId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isName(String firstString, ArrayList<TaskLabel> taskLabels) {
		if (!taskLabels.isEmpty()) {
			for (TaskLabel aLabel : taskLabels) {
				if (firstString.equals(aLabel.getLabelName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isChangeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		boolean isLabelChanged = false;
		for (int i = 1; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 1:
					isLabelChanged = changeLabelName(variableArray[i], taskLabels, labelPosition, todo, deadlines,
							events);
					break;
				case 2:
					isLabelChanged = changeLabelColour(variableArray[i], taskLabels, labelPosition, todo, deadlines,
							events);
					break;
				}
			}
		}
		return isLabelChanged;
	}

	private boolean isTaskLabelExist(String newName, ArrayList<TaskLabel> taskLabels) {
		for (TaskLabel aLabel : taskLabels) {
			if (aLabel.getLabelName().equals(newName)) {
				return true;
			}
		}
		return false;
	}

	private boolean changeLabelName(String newName, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (taskLabels.get(i).getLabelId() == labelPosition) {
				if (!isTaskLabelExist(newName, taskLabels)) {
					taskLabels.get(i).setLabelName(newName);
				}
			}
		}
		changeMasterListLabels(newName, labelPosition, todo, deadlines, events, taskLabels, "name");
		return true;
	}


	private boolean changeLabelColour(String newColour, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (taskLabels.get(i).getLabelId() == labelPosition) {
				taskLabels.get(i).setColourId(colourToIDMap.get(newColour));
			}
		}
		changeMasterListLabels(newColour, labelPosition, todo, deadlines, events, taskLabels, "colour");
		return true;
	}

	private void changeMasterListLabels(String newString, int labelPosition, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels, String type)
					throws IOException {
		for (int i = 0; i < todo.size(); i++) {
			if (todo.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (!isTaskLabelExist(newString, taskLabels)) {
					if (type.equals("name")) {
						todo.get(i).getTaskLabel().setLabelName(newString);
					} else if (type.equals("colour")) {
						todo.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
					}
				} else {
					todo.get(i).setTaskLabel(checkNewTaskLabel(newString, taskLabels));
				}
			}
		}
		for (int i = 0; i < deadlines.size(); i++) {
			if (deadlines.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (!isTaskLabelExist(newString, taskLabels)) {
					if (type.equals("name")) {
						deadlines.get(i).getTaskLabel().setLabelName(newString);
					} else if (type.equals("colour")) {
						deadlines.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
					}
				} else {
					deadlines.get(i).setTaskLabel(checkNewTaskLabel(newString, taskLabels));
				}
			}
		}
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (!isTaskLabelExist(newString, taskLabels)) {
					if (type.equals("name")) {
						events.get(i).getTaskLabel().setLabelName(newString);
					} else if (type.equals("colour")) {
						events.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
					}
				} else {
					events.get(i).setTaskLabel(checkNewTaskLabel(newString, taskLabels));
				}
			}
		}
	}

	public String testChangeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		return changeLabel(variableArray, taskLabels, todo, deadlines, events);
	}
}
