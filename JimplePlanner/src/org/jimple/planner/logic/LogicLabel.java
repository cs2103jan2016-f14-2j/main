package org.jimple.planner.logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.Constants;
import org.jimple.planner.TaskLabel;

import org.jimple.planner.Task;

public class LogicLabel {
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

	protected String changeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (isContainColour(variableArray, taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "colour");
			String labelColour = changeLabelName(variableArray[0], taskLabels, labelPosition);
			return labelColour + Constants.LABEL_COLOR_FEEDBACK + variableArray[0];
		} else if (isContainName(variableArray, taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "name");
			String labelName = changeLabelColour(variableArray[1], taskLabels, labelPosition);
			return labelName + Constants.LABEL_NAME_FEEDBACK + variableArray[1];
		}
		return Constants.ERROR_LABEL_FEEDBACK;
	}

	protected String deleteLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (variableArray[0].equals(taskLabels.get(i).getLabelName())
					|| colourToIDMap.get(variableArray[1]).equals(taskLabels.get(i).getColourId())) {
				taskLabels.get(i).removeLabelId(taskLabels.get(i));
				TaskLabel removedTask = taskLabels.remove(i);
				removeLabelsFromMasterList(todo, deadlines, events, removedTask);
				return Constants.LABEL_DELETED_FEEDBACK;
			}
		}
		return Constants.ERROR_LABEL_DELETED_FEEDBACK;
	}

	private void removeLabelsFromMasterList(ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			TaskLabel removedTask) {
		for (int j = 0; j < todo.size(); j++) {
			if (todo.get(j).getTaskLabel().getLabelId() == removedTask.getLabelId()) {
				todo.get(j).setTaskLabel(TaskLabel.getDefaultLabel());
			}
		}
		for (int j = 0; j < deadlines.size(); j++) {
			if (deadlines.get(j).getTaskLabel().getLabelId() == removedTask.getLabelId()) {
				deadlines.get(j).setTaskLabel(TaskLabel.getDefaultLabel());
			}
		}
		for (int j = 0; j < events.size(); j++) {
			if (events.get(j).getTaskLabel().getLabelId() == removedTask.getLabelId()) {
				events.get(j).setTaskLabel(TaskLabel.getDefaultLabel());
			}
		}
	}

	private int getLabelPosition(String[] variableArray, ArrayList<TaskLabel> taskLabels, String type) {
		for (int i = 0; i < taskLabels.size(); i++) {
			if (type.equals("colour")) {
				if (colourToIDMap.get(variableArray[1]).equals(taskLabels.get(i).getColourId())) {
					return taskLabels.get(i).getLabelId();
				}
			} else if (type.equals("name")) {
				if (variableArray[0].equals(taskLabels.get(i).getLabelName())) {
					return taskLabels.get(i).getLabelId();
				}
			}
		}
		return 0;
	}

	private boolean isContainColour(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (!taskLabels.isEmpty()) {
			for (int i = 0; i < taskLabels.size(); i++) {
				if (colourToIDMap.get(variableArray[1]).equals(taskLabels.get(i).getColourId())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isContainName(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (!taskLabels.isEmpty()) {
			for (TaskLabel aLabel : taskLabels) {
				if (variableArray[0].equals(aLabel.getLabelName())) {
					return true;
				}
			}
		}
		return false;
	}

	private String changeLabelName(String newName, ArrayList<TaskLabel> taskLabels, int labelPosition) {
		taskLabels.get(labelPosition - 1).setLabelName(newName);
		return iDToColourMap.get(taskLabels.get(labelPosition - 1).getColourId());

	}

	private String changeLabelColour(String newColour, ArrayList<TaskLabel> taskLabels, int labelPosition) {
		taskLabels.get(labelPosition - 1).setColourId(colourToIDMap.get(newColour));
		return taskLabels.get(labelPosition - 1).getLabelName();
	}
}
