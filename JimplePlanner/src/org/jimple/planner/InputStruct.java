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
	private final int ARRAY_SIZE_ADD = 5;
	private final int ARRAY_SIZE_EDIT = 6;
	private final int ARRAY_SIZE_DELETE = 1;
	private final int ARRAY_SIZE_SEARCH = 1;
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

	public String[] getVariableArray() {
		return variableArray;
	}

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

	/* --------------|
	 * ADD VARIABLES |
	 * --------------|
	 * Index 0: Event Name
	 * Index 1: Event Description
	 * Index 2: Event Time (From)
	 * Index 3: Event Time (To)
	 * Index 4: Event Category
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

