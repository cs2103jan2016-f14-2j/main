/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.util.HashMap;

public class Parser {

	/*
	 * ----------------------------| EXTENDED COMMANDS VARIABLES |
	 * ----------------------------| String[]: Stores the possible extended
	 * command strings for each command. int[]: Stores the index on InputStruct
	 * which each extended command string affects.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = { "desc", "at", "from", "by", "to", "cat" };
	private final int[] EXTENDED_COMMANDS_ADD_INDEX = { 1, 2, 2, 3, 3, 4 };
	private final String[] EXTENDED_COMMANDS_EDIT = { "name", "desc", "from", "to", "cat" };
	private final int[] EXTENDED_COMMANDS_EDIT_INDEX = { 1, 2, 3, 4, 5 };

	/*
	 * ----------------| FINAL VARIABLES | ----------------|
	 */
	private final int INPUTSTRUCT_INDEX_MAIN_COMMAND_USER_INPUT = 0;
	private final int STRING_INDEX_START = 0;
	private final int STRING_INDEX_TRIM_LAST_SPACE_SIZE = 1;
	private final int USER_INPUT_INDEX_COMMAND_STRING = 0;
	private final String EMPTY_STRING = "";

	/*
	 * ---------| HASHMAPS | ---------| Stores the extended command strings with
	 * the index for easy access.
	 */
	private HashMap<String, Integer> extendedCommandsAdd;
	private HashMap<String, Integer> extendedCommandsEdit;
	private HashMap<String, Integer> noExtendedCommands;

	/*
	 * ---------------------| DateTimeParser Class | ---------------------|
	 * Class that can parse "natural language" inputs for date and time.
	 */
	private TimeParser timeParser = new TimeParser();

	/*
	 * Stores the extended commands variables into the respective hashmaps.
	 */
	public Parser() {
		extendedCommandsAdd = new HashMap<String, Integer>();
		extendedCommandsEdit = new HashMap<String, Integer>();
		noExtendedCommands = new HashMap<String, Integer>();

		for (int i = 0; i < EXTENDED_COMMANDS_ADD.length; i++) {
			extendedCommandsAdd.put(EXTENDED_COMMANDS_ADD[i], EXTENDED_COMMANDS_ADD_INDEX[i]);
		}
		for (int i = 0; i < EXTENDED_COMMANDS_EDIT.length; i++) {
			extendedCommandsEdit.put(EXTENDED_COMMANDS_EDIT[i], EXTENDED_COMMANDS_EDIT_INDEX[i]);
		}
	}

	/*
	 * The main method that other components use. Returns an InputStruct
	 * containing the variables of the user input.
	 */
	public InputStruct parseInput(String userInput) {
		String[] splitUserInput = userInput.split(" ");
		switch (getCommandString(splitUserInput)) {
		case "add":
			return getStruct(splitUserInput, extendedCommandsAdd);
		case "edit":
			return getStruct(splitUserInput, extendedCommandsEdit);
		default:
			return getStruct(splitUserInput, noExtendedCommands);
		}
	}

	/*
	 * Detects and stores the variables in the user input.
	 */
	private InputStruct getStruct(String[] userInputStringArray,
			HashMap<String, Integer> inputExtendedCommandsHashMap) {

		// Creates the InputStruct to be returned.
		InputStruct outputStruct = new InputStruct(getCommandString(userInputStringArray));

		// currIndex is the index on the InputStruct that the strings currently
		// being read affects.
		int currIndex = INPUTSTRUCT_INDEX_MAIN_COMMAND_USER_INPUT;

		String currExtendedCommand = EMPTY_STRING;

		// userInputString is the string currently being read. Updates while the
		// next extended command is not found.
		String userInputString = EMPTY_STRING;

		for (int i = 1; i < userInputStringArray.length; i++) {

			// Updates userInputString if word being read is not an extended
			// command.
			String currString = userInputStringArray[i];
			if (!inputExtendedCommandsHashMap.containsKey(currString)) {
				userInputString += currString + " ";
			}
			if (inputExtendedCommandsHashMap.containsKey(currString) || i == userInputStringArray.length - 1) {
				// Word being read is an extended command.

				// When word being read is an extended command, stores the
				// "userInputString" into the index in the InputStruct specified
				// by "currIndex". Removes the whitespace at the end.
				if (isDateTimeInput(currExtendedCommand)) {
					// Parses using theTimeFormat class first, if the extended
					// command is date-time specific.
					// System.out.println(timeParser.timeParser(currExtendedCommand,
					// userInputString));
					outputStruct.setAtIndex(currIndex, timeParser.timeParser(currExtendedCommand, userInputString));
				} else {
					outputStruct.setAtIndex(currIndex, removeLastCharacter(userInputString));
				}

				// Updates the "currIndex" and "currExtendedCommand" to the
				// current extended command.
				currExtendedCommand = currString;
				if (inputExtendedCommandsHashMap.containsKey(currString)) {
					currIndex = inputExtendedCommandsHashMap.get(currString);
				}

				// Resets "userInputString".
				userInputString = EMPTY_STRING;
			}
		}
		if (!userInputString.equals("")) {
			outputStruct.setAtIndex(currIndex, removeLastCharacter(userInputString));
		}
		return outputStruct;
	}

	private boolean isDateTimeInput(String extendedCommand) {
		if (extendedCommandsAdd.containsKey(extendedCommand)) {
			return ((extendedCommandsAdd.get(extendedCommand) == 2) || (extendedCommandsAdd.get(extendedCommand) == 3));
		}
		return false;
	}

	private String getCommandString(String[] userInputStringArray) {
		return userInputStringArray[USER_INPUT_INDEX_COMMAND_STRING];
	}

	private String removeLastCharacter(String inputString) {
		return inputString.substring(STRING_INDEX_START, inputString.length() - STRING_INDEX_TRIM_LAST_SPACE_SIZE);
	}

}

/*
 * ------------| INPUTSTRUCT | ------------| This class is the output of the
 * Parser. It contains the command string and all possible variables for all
 * commands in the Jimple Planner. Stores the variables detected in the user
 * input.
 */
class InputStruct {

	/*
	 * ----------------| SIZE VARIABLES | ----------------| Variables containing
	 * the intended size of the variable array for each command.
	 */
	private final int ARRAY_SIZE_ADD = 5;
	private final int ARRAY_SIZE_EDIT = 6;
	private final int ARRAY_SIZE_DELETE = 1;
	private final int ARRAY_SIZE_SEARCH = 1;

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

		// Initializes the size of the variable array according to the
		// commandString.
		switch (commandString) {
		case "add":
			setVariableArraySize(ARRAY_SIZE_ADD);
			break;
		case "edit":
			setVariableArraySize(ARRAY_SIZE_EDIT);
			break;
		case "delete":
			setVariableArraySize(ARRAY_SIZE_DELETE);
			break;
		case "search":
			setVariableArraySize(ARRAY_SIZE_SEARCH);
			break;
		default:
			break;
		}
	}

	/*
	 * --------------| ADD VARIABLES | --------------| Index 0: Event Name Index
	 * 1: Event Description Index 2: Event Time (From) Index 3: Event Time (To)
	 * Index 4: Event Category
	 */

	/*
	 * --------------| EDIT VARIABLES| --------------| Index 0: Event Index
	 * Index 1: Event Name Index 2: Event Description Index 3: Event Time (From)
	 * Index 4: Event Time (To) Index 5: Event Category
	 */

	/*
	 * ----------------| DELETE VARIABLE | ----------------| Index 0: Event
	 * Index
	 */

	/*
	 * ----------------| SEARCH VARIABLE | ----------------| Index 0: String to
	 * Search
	 */

	/*
	 * ---------------| HELP VARIABLES | ---------------| N/A
	 */

	public void setAtIndex(int inputIndex, String inputString) {
		variableArray[inputIndex] = inputString;
	}

	public String getAtIndex(int inputIndex) {
		return variableArray[inputIndex];
	}

}
