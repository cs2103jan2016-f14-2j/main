/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.util.HashMap;

public class Parser {

	/* ----------------------------|
	 * EXTENDED COMMANDS VARIABLES |
	 * ----------------------------| 
	 * String[]: Stores the possible extended command strings for each command.
	 * int[]: Stores the index on InputStruct which each extended command string affects.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = { "desc", "at", "from", "by", "to", "cat" };
	private final int[] EXTENDED_COMMANDS_ADD_INDEX = { 1, 2, 2, 3, 3, 4 };
	private final String[] EXTENDED_COMMANDS_EDIT = { "name", "desc", "from", "to", "cat" };
	private final int[] EXTENDED_COMMANDS_EDIT_INDEX = { 1, 2, 3, 4, 5 };

	/* ----------------|
	 * FINAL VARIABLES |
	 * ----------------|
	 */
	private final int INPUTSTRUCT_INDEX_MAIN_COMMAND_USER_INPUT = 0;
	private final int STRING_INDEX_START = 0;
	private final int STRING_INDEX_TRIM_LAST_SPACE_SIZE = 1;
	private final int USER_INPUT_INDEX_COMMAND_STRING = 0;
	private final String EMPTY_STRING = "";

	/* ---------|
	 * HASHMAPS |
	 * ---------| 
	 * Stores the extended command strings with the index for easy access.
	 */
	private HashMap<String, Integer> extendedCommandsAdd;
	private HashMap<String, Integer> extendedCommandsEdit;
	private HashMap<String, Integer> noExtendedCommands;

	/*
	 * ---------------------|
	 * DateTimeParser Class |
	 * ---------------------|
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
			if (!inputExtendedCommandsHashMap.containsKey(currString.toLowerCase())) {
				userInputString += currString + " ";
			}
			if (inputExtendedCommandsHashMap.containsKey(currString) || i == userInputStringArray.length - 1) {
				// Word being read is an extended command.

				// When word being read is an extended command, stores the "userInputString" into the index in the InputStruct specified by "currIndex". Removes the whitespace at the end.
				if (isDateTimeInput(currExtendedCommand)) {
					// Parses using theTimeFormat class first, if the extended command is date-time specific.
					outputStruct.setAtIndex(currIndex, timeParser.timeParser(currExtendedCommand, userInputString));
				} else {
					outputStruct.setAtIndex(currIndex, removeLastCharacter(userInputString));
				}

				// Updates the "currIndex" and "currExtendedCommand" to the current extended command.
				if (inputExtendedCommandsHashMap.containsKey(currString)) {
					currExtendedCommand = currString.toLowerCase();
					currIndex = inputExtendedCommandsHashMap.get(currExtendedCommand);
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