package org.jimple.planner.task;

import static org.jimple.planner.constants.Constants.TASK_LABEL_COLOUR_DEFAULT_0;
import static org.jimple.planner.constants.Constants.TASK_LABEL_NAME_DEFAULT;

import java.security.SecureRandom;
//@@author A0135808B
public class TaskLabel {
	private String labelName;
	private int colourId;
	private int labelId;

	private TaskLabel(String name, int colourId, int labelId){
		this.labelName = name;
		this.colourId = colourId;
		this.labelId = labelId;
	}

	public static TaskLabel createDefaultLabel(){
		return new TaskLabel(TASK_LABEL_NAME_DEFAULT, TASK_LABEL_COLOUR_DEFAULT_0, 0);
	}

	public static TaskLabel createNewLabel(String name){
		int colourId = getNextLabelColourId();
		TaskLabel newLabel = createNewLabel(name, colourId);
		return newLabel;
	}

	public static TaskLabel createNewLabel(String name, int colourId){
		int id = getNextLabelId();
		TaskLabel newLabel = new TaskLabel(name, colourId, id);
		return newLabel;
	}

	public static TaskLabel createDuplicateTaskLabel(TaskLabel taskLabel){
		String aName = taskLabel.getLabelName();
		int aColourId = taskLabel.getColourId();
		int alabelId = taskLabel.getLabelId();
		TaskLabel duplicatedTaskLabel = new TaskLabel(aName, aColourId, alabelId);
		return duplicatedTaskLabel;
	}

	public static TaskLabel createDummyLabel(String name, int colourId){
		return new TaskLabel(name, colourId, 0);
	}

	private static int getNextLabelId() {
		int id = generateRandomIntegers(Integer.MAX_VALUE);
		return id;
	}

	private static int getNextLabelColourId(){
		int colourId = generateRandomIntegers(7);
		return colourId;
	}
	
	//generates a number from [1, bound), bound exclusive

	private static int generateRandomIntegers(int bound){
		SecureRandom randomGen = new SecureRandom();
		int id = randomGen.nextInt(bound);
		while(id==0){
			id = randomGen.nextInt(bound);
		}
		return id;
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
		assert labelColourId <= 6 && labelColourId >=0;
		this.colourId = labelColourId;
	}

	public int getLabelId() {
		return labelId;
	}

	//@@author generated
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
		if (getClass() != obj.getClass())
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
