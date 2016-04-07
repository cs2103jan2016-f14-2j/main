//@@author A0135775W

package org.jimple.planner.parser;

import org.jimple.planner.constants.Constants;

/* ------------|
 * INPUTSTRUCT |
 * ------------|
 * This class is the output of the Parser. It contains the command string and all possible variables for all
 * commands in the Jimple Planner. Stores the variables detected in the user
 * input.
 */

public class InputStruct {
	
	/* ---------------|
	 * SIZE VARIABLES |
	 * ---------------|
	 * Variables containing the intended size of the variable array for each command, used only in InputStruct.
	 */
	private final int ARRAY_SIZE_ADD = 6;
	private final int ARRAY_SIZE_EDIT = 6;
	private final int ARRAY_SIZE_DELETE = 1;
	private final int ARRAY_SIZE_SEARCH = 1;
	private final int ARRAY_SIZE_DONE = 1;
	private final int ARRAY_SIZE_RETURN = 1;
	private final int ARRAY_SIZE_EDITLABEL = 3;
	private final int ARRAY_SIZE_DELETELABEL = 1;
	private final int ARRAY_SIZE_CHECKCONFLICT = 1;
	private final int ARRAY_SIZE_CHANGEDIR = 1;

	private String commandString;

	// The string array being used, according to commandString.
	private String[] variableArray;

	public void setCommand(String inputCommand) {
		commandString = inputCommand;
	}

	public String getCommand() {
		return commandString;
	}

	public void setVariableArraySize(int inputSize) {
		variableArray = new String[inputSize];
	}

	public String[] getVariableArray(){
		if (variableArray != null) {
			return variableArray;
		} else {
			throw new NullPointerException("No variable array found.");
		}
	}

	/* ------------|
	 * CONSTRUCTOR |
	 * ------------|
	 * */
	public InputStruct(String inputCommandString) {
		commandString = inputCommandString;
		// Initializes the size of the variable array according to the commandString.
		switch (commandString) {
		case Constants.STRING_ADD :
			setVariableArraySize(ARRAY_SIZE_ADD);
			break;
		case Constants.STRING_EDIT :
			setVariableArraySize(ARRAY_SIZE_EDIT);
			break;
		case Constants.STRING_DELETE :
			setVariableArraySize(ARRAY_SIZE_DELETE);
			break;
		case Constants.STRING_SEARCH :
			setVariableArraySize(ARRAY_SIZE_SEARCH);
			break;
		case Constants.STRING_DONE :
			setVariableArraySize(ARRAY_SIZE_DONE);
			break;
		case Constants.STRING_RETURN :
			setVariableArraySize(ARRAY_SIZE_RETURN);
			break;
		case Constants.STRING_EDITLABEL :
			setVariableArraySize(ARRAY_SIZE_EDITLABEL);
			break;
		case Constants.STRING_DELETELABEL :
			setVariableArraySize(ARRAY_SIZE_DELETELABEL);
			break;
		case Constants.STRING_CHECKCONFLICT :
			setVariableArraySize(ARRAY_SIZE_CHECKCONFLICT);
			break;
		case Constants.STRING_CHANGEDIR :
			setVariableArraySize(ARRAY_SIZE_CHANGEDIR);
			break;
		default:
			break;
		}
	}
	
	/* Set the task type based on which date/time fields ("from" and "to") are available.
	 * EVENT: "from" & "to" set.
	 * DEADLINE: Only "from" set.
	 * TODO: None set.
	 */
	public void checkAndSetTaskType() {
		if (isTodo()) {
			setAtIndex(Constants.INDEX_BASE, Constants.TASK_TYPE_TODO);
		} else if (isDeadline()) {
			setAtIndex(Constants.INDEX_BASE, Constants.TASK_TYPE_DEADLINE);
		} else if (isEvent()) {
			setAtIndex(Constants.INDEX_BASE, Constants.TASK_TYPE_EVENT);
		}
	}
	
	private boolean isTodo() {
		return variableArray[Constants.INDEX_FROM] == null && variableArray[Constants.INDEX_TO] == null;
	}
	
	private boolean isDeadline() {
		return variableArray[Constants.INDEX_FROM] != null && variableArray[Constants.INDEX_TO] == null;
	}
	
	private boolean isEvent() {
		return variableArray[Constants.INDEX_FROM] != null && variableArray[Constants.INDEX_TO] != null;
	}

	/* --------------|
	 * ADD VARIABLES |
	 * --------------|
	 * Index 0: Task Type
	 *  - "todo": To-do
	 *  - "deadline": Deadline
	 *  - "event": Event
	 * Index 1: Task Name
	 * Index 2: Task Description
	 * Index 3: Task Time (From)
	 * Index 4: Task Time (To)
	 * Index 5: Task Label
	 */

	/* --------------|
	 * EDIT VARIABLES|
	 * --------------|
	 * Index 0: Task ID
	 * Index 1: Task Name
	 * Index 2: Task Description
	 * Index 3: Task Time (From)
	 * Index 4: Task Time (To)
	 * Index 5: Label Name
	 */

	/* ----------------|
	 * DELETE VARIABLE |
	 * ----------------|
	 * Index 0: Task ID
	 */

	/* ----------------|
	 * SEARCH VARIABLE |
	 * ----------------|
	 * Index 0: String to Search
	 */
	
	/* --------------|
	 * DONE VARIABLE |
	 * --------------|
	 * Index 0: Task ID
	 */
	
	/* ----------------|
	 * RETURN VARIABLE |
	 * ----------------|
	 * Index 0: Task ID
	 */
	
	/* --------------------|
	 * EDITLABEL VARIABLES |
	 * --------------------|
	 * Index 0: Label Name
	 * Index 1: New Label Name
	 * Index 2: New Label Colour
	 */
	
	/* ---------------------|
	 * DELETELABEL VARIABLE |
	 * ---------------------|
	 * Index 0: Label Name
	 */
	
	/* -----------------------|
	 * CHECKCONFLICT VARIABLE |
	 * -----------------------|
	 * Index 0: Task ID
	 */
	
	/* -------------------|
	 * CHANGEDIR VARIABLE |
	 * -------------------|
	 * Index 0: Directory String
	 */

	/* -----------------------------|
	 * HELP/CHECKDIR/UNDO VARIABLES |
	 * -----------------------------|
	 * N/A
	 */

	public void setAtIndex(int inputIndex, String inputString) {
		variableArray[inputIndex] = inputString;
	}

	public String getAtIndex(int inputIndex) {
		return variableArray[inputIndex];
	}
	
}

