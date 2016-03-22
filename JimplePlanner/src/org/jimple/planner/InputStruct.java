package org.jimple.planner;

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
	 * Variables containing the intended size of the variable array for each command.
	 */
	private final int ARRAY_SIZE_ADD = 6;
	private final int ARRAY_SIZE_EDIT = 6;
	private final int ARRAY_SIZE_DELETE = 1;
	private final int ARRAY_SIZE_SEARCH = 1;
	private final int ARRAY_SIZE_CHANGEDIR = 1;
	
	/* Stores the index for the user input after the main command.
	 *  - "add": Index of the task name.
	 *  - "edit": Index of the task ID.
	 *  - "delete": Index of the task ID.
	 *  - "search": Index of the query String.
	 *  - "changedir": Index of the directory path.
	 */
	private final int INDEX_BASE = 0;
	
	// Stores the indexes for task fields. Used by "add" and "edit".
	private final int INDEX_NAME = 1;
	private final int INDEX_DESCRIPTION = 2;
	private final int INDEX_FROM = 3;
	private final int INDEX_TO = 4;
	private final int INDEX_CATEGORY = 5;
	
	private final String TASK_TYPE_TODO = "todo";
	private final String TASK_TYPE_DEADLINE = "deadline";
	private final String TASK_TYPE_EVENT = "event";

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
		return variableArray;
	}

	/* ------------|
	 * CONSTRUCTOR |
	 * ------------|
	 * */
	public InputStruct(String inputCommandString) {
		commandString = inputCommandString;
		// Initializes the size of the variable array according to the commandString.
		switch (commandString) {
		case "add" :
			setVariableArraySize(ARRAY_SIZE_ADD);
			break;
		case "edit" :
			setVariableArraySize(ARRAY_SIZE_EDIT);
			break;
		case "delete" :
			setVariableArraySize(ARRAY_SIZE_DELETE);
			break;
		case "search" :
			setVariableArraySize(ARRAY_SIZE_SEARCH);
			break;
		case "changedir" :
			setVariableArraySize(ARRAY_SIZE_CHANGEDIR);
		default:
			break;
		}
	}
	
	public void checkAndSetTaskType() {
		if (isTodo()) {
			setAtIndex(INDEX_BASE, TASK_TYPE_TODO);
		} else if (isDeadline()) {
			setAtIndex(INDEX_BASE, TASK_TYPE_DEADLINE);
		} else if (isEvent()) {
			setAtIndex(INDEX_BASE, TASK_TYPE_EVENT);
		}
	}
	
	private boolean isTodo() {
		return variableArray[INDEX_FROM] == null && variableArray[INDEX_TO] == null;
	}
	
	private boolean isDeadline() {
		return variableArray[INDEX_FROM] != null && variableArray[INDEX_TO] == null;
	}
	
	private boolean isEvent() {
		return variableArray[INDEX_FROM] != null && variableArray[INDEX_TO] != null;
	}

	/* --------------|
	 * ADD VARIABLES |
	 * --------------|
	 * Index 0: Task Type
	 *  - "todo": To-do
	 *  - "deadline": Deadline
	 *  - "event": Event
	 * Index 1: Event Name
	 * Index 2: Event Description
	 * Index 3: Event Time (From)
	 * Index 4: Event Time (To)
	 * Index 5: Event Category
	 */

	/* --------------|
	 * EDIT VARIABLES|
	 * --------------|
	 * Index 0: Event Index
	 * Index 1: Event Name
	 * Index 2: Event Description
	 * Index 3: Event Time (From)
	 * Index 4: Event Time (To)
	 * Index 5: Event Category
	 */

	/* ----------------|
	 * DELETE VARIABLE |
	 * ----------------|
	 * Index 0: Event Index
	 */

	/* ----------------|
	 * SEARCH VARIABLE |
	 * ----------------|
	 * Index 0: String to Search
	 */
	
	/* -------------------|
	 * CHANGEDIR VARIABLE |
	 * -------------------|
	 * Index 0: Directory String
	 */

	/* ---------------|
	 * HELP VARIABLES |
	 * ---------------|
	 * N/A
	 */

	public void setAtIndex(int inputIndex, String inputString) {
		variableArray[inputIndex] = inputString;
	}

	public String getAtIndex(int inputIndex) {
		return variableArray[inputIndex];
	}
	
}

