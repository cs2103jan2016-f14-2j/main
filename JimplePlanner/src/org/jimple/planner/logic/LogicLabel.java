package org.jimple.planner.logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.TaskLabel;

public class LogicLabel {
	HashMap<String, Integer> colourMaps = new HashMap<String, Integer>();

	public LogicLabel() {
		colourMaps.put("blue", 1);
		colourMaps.put("green", 2);
		colourMaps.put("yellow", 3);
		colourMaps.put("orange", 4);
		colourMaps.put("red", 5);
		colourMaps.put("purple", 6);
	}

	protected String changeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (isContainColour(variableArray, taskLabels)) {
			changeLabelName(variableArray[0], taskLabels);
		} else if (isContainName(variableArray, taskLabels)) {
			changeLabelColour(variableArray[1], taskLabels);
		}
		return null;
	}

	private boolean isContainColour(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (!variableArray[1].equals("")) {
			for (TaskLabel aLabel : taskLabels) {
				if (variableArray[1].equals(aLabel.getLabelName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isContainName(String[] variableArray, ArrayList<TaskLabel> taskLabels) {
		if (!variableArray[0].equals("")) {
			for (TaskLabel aLabel : taskLabels)	{
				if (colourMaps.get(variableArray[0]).equals(aLabel.getColourId()))	{
					return true;
				}
			}
		}
		return false;
	}

	private void changeLabelName(String newName, ArrayList<TaskLabel> taskLabels) {
		for (int i = 0; i < taskLabels.size(); i++) {
			taskLabels.get(i).setLabelName(newName);
		}
	}

	private void changeLabelColour(String newColour, ArrayList<TaskLabel> taskLabels) {
		for (int i = 0; i < taskLabels.size(); i++) {
			taskLabels.get(i).setColourId(colourMaps.get(newColour));
		}
	}
}
