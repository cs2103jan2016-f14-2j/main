package org.jimple.planner;

import java.util.stream.IntStream;

import org.jimple.planner.exceptions.LabelExceedTotalException;

import static org.jimple.planner.Constants.TASK_LABEL_NAME_DEFAULT;
import static org.jimple.planner.Constants.ERROR_EXCEEDED_TOTAL_NUM_OF_LABELS;
import static org.jimple.planner.Constants.TASK_LABEL_COLOUR_DEFAULT;

public class TaskLabel {
	//These are to check if the given IDs have been taken
	private static final int[] idArray = IntStream.range(1, 6).toArray();
	private static boolean[] idBoolArray = new boolean[7];
	//Colour counter determines the colour of the label next, must be in range [1, 6]
	private static int colourCounter = 1;
	
	private String labelName;
	private int colourId;
	private int labelId;
	
	private TaskLabel(String name, int colourId, int labelId){
		this.labelName = name;
		this.colourId = colourId;
		this.labelId = labelId;
	}
	
	public static TaskLabel getDefaultLabel(){
		TaskLabel.idBoolArray[0] = true;
		return new TaskLabel(TASK_LABEL_NAME_DEFAULT, TASK_LABEL_COLOUR_DEFAULT, 0);
	}
	
	public static TaskLabel getNewLabel(String name) throws LabelExceedTotalException{
		int id = getNextLabelId();
		if(isValidId(id)){
			throw new LabelExceedTotalException(ERROR_EXCEEDED_TOTAL_NUM_OF_LABELS);
		}
		TaskLabel.idBoolArray[id] = true;
		TaskLabel newLabel = new TaskLabel(name, colourCounter, id);
		updateColourCounter();
		return newLabel;
	}

	private static boolean isValidId(int id) {
		return id<0;
	}
	
	public static TaskLabel duplicateTaskLabel(TaskLabel taskLabel){
		String aName = taskLabel.getLabelName();
		int aColourId = taskLabel.getColourId();
		int alabelId = taskLabel.getLabelId();
		TaskLabel duplicatedTaskLabel = new TaskLabel(aName, aColourId, alabelId);
		return duplicatedTaskLabel;
	}
	
	public void removeLabelId(TaskLabel taskLabel){
		int aLabelId = taskLabel.getLabelId();
		assert TaskLabel.idBoolArray[aLabelId] == true;
		TaskLabel.idBoolArray[aLabelId] = false;
	}
	
	private static int getNextLabelId() {
		int id = -1;
		for(int i: idArray){
			if(!idBoolArray[i]){
				id = i;
				break;
			}
		}
		return id;
	}
	
	private static void updateColourCounter(){
		TaskLabel.colourCounter++;
		if(TaskLabel.colourCounter>6){
			TaskLabel.colourCounter = 1;
		}
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public int getColourId() {
		return colourId;
	}

	public void setColourId(int labelColourId) {
		this.colourId = labelColourId;
	}

	public int getLabelId() {
		return labelId;
	}

	@Override
	public int hashCode() {
		final int prime = 101;
		int result = 1;
		result = prime * result + colourId;
		result = prime * result + labelId;
		result = prime * result + ((labelName == null) ? 0 : labelName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TaskLabel))
			return false;
		TaskLabel other = (TaskLabel) obj;
		if (colourId != other.colourId)
			return false;
		if (labelId != other.labelId)
			return false;
		if (labelName == null) {
			if (other.labelName != null)
				return false;
		} else if (!labelName.equals(other.labelName))
			return false;
		return true;
	}
	
	
}
