package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jimple.planner.Constants;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.Task;

public class LogicLabel implements LogicMasterListModification{
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

	protected String changeLabel(Storage store, String[] variableArray, ArrayList<TaskLabel> taskLabels,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		if (isName(variableArray[0], taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "name");
			boolean isLabelChanged = isChangeLabel(store, variableArray, taskLabels, labelPosition, todo, deadlines, events);
			if (isLabelChanged) {
				return Constants.LABEL_COLOR_FEEDBACK;
			}
		} else if (isColour(variableArray[0], taskLabels)) {
			int labelPosition = getLabelPosition(variableArray, taskLabels, "colour");
			boolean isLabelChanged = isChangeLabel(store, variableArray, taskLabels, labelPosition, todo, deadlines, events);
			if (isLabelChanged) {
				return Constants.LABEL_NAME_FEEDBACK;
			}
		}
		return Constants.ERROR_LABEL_FEEDBACK;
	}

	protected String deleteLabel(Storage store, String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
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
				if (colourToIDMap.get(variableArray[0]).equals(taskLabels.get(i).getColourId())) {
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

	private boolean isChangeLabel(Storage store, String[] variableArray, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		boolean isLabelChanged = false;
		for (int i = 1; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 1:
					isLabelChanged = changeLabelName(store, variableArray[i], taskLabels, labelPosition, todo, deadlines,
							events);
					break;
				case 2:
					isLabelChanged = changeLabelColour(store, variableArray[i], taskLabels, labelPosition, todo, deadlines,
							events);
					break;
				}
			}
		}
		return isLabelChanged;
	}

	private boolean changeLabelName(Storage store, String newName, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		taskLabels.get(labelPosition).setLabelName(newName);
		changeMasterListLabels(store, newName, labelPosition, todo, deadlines, events, taskLabels, "name");
		return true;
	}

	private boolean changeLabelColour(Storage store, String newColour, ArrayList<TaskLabel> taskLabels, int labelPosition,
			ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		taskLabels.get(labelPosition).setColourId(colourToIDMap.get(newColour));
		changeMasterListLabels(store, newColour, labelPosition, todo, deadlines, events, taskLabels, "colour");
		return true;
	}

	private void changeMasterListLabels(Storage store, String newString, int labelPosition, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels, String type) throws IOException {
		for (int i = 0; i < todo.size(); i++) {
			if (todo.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (type.equals("name")) {
					todo.get(i).getTaskLabel().setLabelName(newString);
				} else if (type.equals("colour")) {
					todo.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
				}
			}
		}
		for (int i = 0; i < deadlines.size(); i++) {
			if (deadlines.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (type.equals("name")) {
					deadlines.get(i).getTaskLabel().setLabelName(newString);
				} else if (type.equals("colour")) {
					deadlines.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
				}
			}
		}
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getTaskLabel().getLabelId() == labelPosition) {
				if (type.equals("name")) {
					events.get(i).getTaskLabel().setLabelName(newString);
				} else if (type.equals("colour")) {
					events.get(i).getTaskLabel().setColourId(colourToIDMap.get(newString));
				}
			}
		}
		packageForSavingInFile(store, todo, deadlines, events, taskLabels);
	}
}
