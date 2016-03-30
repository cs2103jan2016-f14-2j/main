package org.jimple.planner.logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.Constants;
import org.jimple.planner.TaskLabel;

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

	protected String deleteLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		for (TaskLabel aLabel : taskLabels) {
			if (variableArray[0].equals(aLabel.getLabelName())
					|| colourToIDMap.get(variableArray[1]).equals(aLabel.getColourId())) {
				aLabel.removeLabelId(aLabel);
				return Constants.LABEL_DELETED_FEEDBACK;
			}
		}
		return Constants.ERROR_LABEL_DELETED_FEEDBACK;
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
		for (int i = 0; i < taskLabels.size(); i++) {
			if (colourToIDMap.get(variableArray[1]).equals(taskLabels.get(i).getColourId())) {
				return true;
			}
		}
		return false;
	}

	private boolean isContainName(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		for (TaskLabel aLabel : taskLabels) {
			if (variableArray[0].equals(aLabel.getLabelName())) {
				return true;
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
