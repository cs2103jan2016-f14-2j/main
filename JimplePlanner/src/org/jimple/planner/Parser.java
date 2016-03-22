/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import parserExceptions.DuplicateDateTimeFieldException;
import parserExceptions.InvalidCommandException;
import parserExceptions.MissingDateTimeFieldException;

public class Parser {
			
	/* ----------------------------|
	 * EXTENDED COMMANDS CONSTANTS |
	 * ----------------------------| 
	 * String[]: Stores the possible extended command strings for each command.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = {"description", "at", "from", "on", "by", "category"};
	private final String[] EXTENDED_COMMANDS_EDIT = {"name", "description", "time", "category"};
	private final String[] EXTENDED_COMMANDS_NIL = {};
	
	/* ----------------------------|
	 * INPUTSTRUCT INDEX CONSTANTS |
	 * ----------------------------|
	 * The constants here store the index they reference in the InputStruct class.
	 */
	
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
	private final int INDEX_DESCRIPTION = 1;
	private final int INDEX_FROM = 2;
	private final int INDEX_TO = 3;
	private final int INDEX_CATEGORY = 4;
	
	

	/* ----------------|
	 * CONSTANTS |
	 * ----------------|
	 */

	// Main commands.
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_CHANGEDIR = "changedir";
	
	// Extended commands.
	private static final String EXTENDED_COMMAND_NAME = "name";
	private static final String EXTENDED_COMMAND_DESCRIPTION = "description";
	private static final String EXTENDED_COMMAND_AT = "at";
	private static final String EXTENDED_COMMAND_ON = "on";
	private static final String EXTENDED_COMMAND_FROM = "from";
	private static final String EXTENDED_COMMAND_BY = "by";
	private static final String EXTENDED_COMMAND_CATEGORY = "category";
	
	private final int INDEX_MAIN_COMMAND = 0;
	private final int INDEX_MAIN_COMMAND_USER_INPUT = 1;
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
	}

	/*
	 * The main method that other components use. Returns an InputStruct
	 * containing the variables of the user input.
	 */
	public InputStruct parseInput(String userInput) throws Exception {
		
		// Not null string.
		assert(userInput != null);
		
		// Not whitespace String.
		assert(userInput.trim() != "");
		
		String[] splitUserInput = userInput.split(" ");
		String mainCommand = getMainCommandString(splitUserInput);
		String mainCommandUserInput = getMainCommandUserInputString(splitUserInput);
		try {
			switch (mainCommand) {
				case COMMAND_ADD :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_ADD);
				case COMMAND_EDIT :
					if (isNumber(mainCommandUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_EDIT);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"" + mainCommandUserInput + "\" not valid.");
				case COMMAND_DELETE :
					if (isNumber(mainCommandUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"" + mainCommandUserInput + "\" not valid.");
				case COMMAND_SEARCH :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				case COMMAND_CHANGEDIR :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				default :
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" not recongnised.");
			}
		} catch (InvalidCommandException ice) {
			throw ice;
		} catch (DuplicateDateTimeFieldException dfe) {
			throw dfe;
		} catch (MissingDateTimeFieldException mfe) {
			throw mfe;
		}
	}
	
	private String getMainCommandUserInputString(String[] splitUserInput) {
		return splitUserInput[INDEX_MAIN_COMMAND_USER_INPUT];
	}
	
	private boolean isNumber(String input) {
		if (input == "") {
			return false;
		}
		boolean isNumber = true;
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				isNumber = false;
				break;
			}
		}
		return isNumber;
	}

	/*
	 * Detects and stores the variables in the user input.
	 */
	private InputStruct getStruct(String[] splitUserInput, String[] extendedCommands) throws Exception {
		
		String mainCommand = getMainCommandString(splitUserInput);
		
		// Creates the InputStruct to be returned.
		InputStruct currInputStruct = new InputStruct(mainCommand);

		String currCommand = mainCommand;

		// userInputString is the string currently being read. While an extended command is not found, words are added to this String.
		String currInputString = EMPTY_STRING;

		for (int i = 1; i < splitUserInput.length; i++) {

			// Updates userInputString if word being read is not an extended command.
			String currString = splitUserInput[i];
			if (isExtendedCommand(currString, extendedCommands)) { // Word being read is an extended command.
				if (currCommand == mainCommand) {
					currInputStruct.setAtIndex(INDEX_BASE, currInputString);
				} else {
					parseExtendedCommand(mainCommand, currCommand, currInputString, currInputStruct);
				}
				currCommand = currString.toLowerCase();
				// Resets "userInputString".
				currInputString = EMPTY_STRING;
			} else {
				currInputString += currString + " ";
			} 
		}
		parseExtendedCommand(mainCommand, currCommand, currInputString, currInputStruct);
		return currInputStruct;
	}
		
	private void parseExtendedCommand(String mainCommand, String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (mainCommand) {
			case COMMAND_ADD :
				parseExtendedCommandAdd(extendedCommand, inputString, inputStruct);
				break;
			case COMMAND_EDIT :
				parseExtendedCommandEdit(extendedCommand, inputString, inputStruct);
				break;
		}
	}
	
	private void parseExtendedCommandAdd(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case EXTENDED_COMMAND_DESCRIPTION :
				setDescription(inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_AT :
			case EXTENDED_COMMAND_ON :
			case EXTENDED_COMMAND_FROM :
			case EXTENDED_COMMAND_BY :
				setTime(extendedCommand, inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_CATEGORY :
				setCategory(inputString, inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void parseExtendedCommandEdit(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case EXTENDED_COMMAND_NAME:
				setName(inputString, inputStruct);
			case EXTENDED_COMMAND_DESCRIPTION :
				setDescription(inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_AT :
			case EXTENDED_COMMAND_ON :
			case EXTENDED_COMMAND_FROM :
			case EXTENDED_COMMAND_BY :
				setTime(extendedCommand, inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_CATEGORY :
				setCategory(inputString, inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void setName(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(INDEX_NAME, userInput);
	}
	
	private void setDescription(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(INDEX_DESCRIPTION, userInput);
	}
	
	private void setTime(String extendedCommand, String userInput, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case EXTENDED_COMMAND_AT :
				parseAt(userInput, inputStruct);
				break;
			case EXTENDED_COMMAND_ON :
				parseOn(userInput, inputStruct);
				break;
			case EXTENDED_COMMAND_FROM :
				parseFrom(userInput, inputStruct);
				break;
			case EXTENDED_COMMAND_BY :
				parseBy(userInput, inputStruct);
				break;
		}
	}
	
	private void parseAt(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(userInput);
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.add(Calendar.HOUR_OF_DAY, 1);
		inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(parsedCalendar));
	}
	
	private void parseOn(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(userInput);
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.set(Calendar.HOUR_OF_DAY, 23);
		parsedCalendar.set(Calendar.MINUTE, 59);
		inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(parsedCalendar));
		System.out.println(inputStruct.getAtIndex(INDEX_FROM) + " " + inputStruct.getAtIndex(INDEX_TO));
	}
	
	private void parseFrom(String userInput, InputStruct inputStruct) throws Exception {
		if (!userInput.contains(" to ")) {
			throw new MissingDateTimeFieldException("\"from\" must be accompanied by \"to\".");
		} else {
			String[] splitFromTo = userInput.split(" to ");
			inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(timeParser.parseTime(splitFromTo[0])));
			inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(timeParser.parseTime(splitFromTo[1])));
		}
		System.out.println(inputStruct.getAtIndex(INDEX_FROM) + " " + inputStruct.getAtIndex(INDEX_TO));
	}
	
	private void parseBy(String userInput, InputStruct inputStruct) throws Exception {
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(timeParser.parseTime(userInput)));
	}
	
	private void setCategory(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(INDEX_CATEGORY, userInput);
	}
	
	private String calendarToStringFormat(Calendar parsedCalendar) {
		Date parsedDate = parsedCalendar.getTime();
		String date = String.format("%02d", parsedDate.getDate());
		String month = String.format("%02d", parsedDate.getMonth()+1);
		String hours = String.format("%02d", parsedDate.getHours());
		String minutes = String.format("%02d", parsedDate.getMinutes());
		String outputDate = (parsedDate.getYear()+1900) + "-"+  month + "-" + date + "T" + hours + ":" + minutes;
		return outputDate;
	}
	
	private boolean isExtendedCommand(String input, String[] extendedCommands) {
		for (int i = 0; i < extendedCommands.length; i++) {
			if (input.equals(extendedCommands[i])) {
				return true;
			}
		}
		return false;
	}

	private String getMainCommandString(String[] userInputStringArray) {
		return userInputStringArray[INDEX_MAIN_COMMAND].toLowerCase();
	}

}