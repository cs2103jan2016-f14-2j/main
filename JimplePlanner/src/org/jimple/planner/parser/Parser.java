//@@author A0135775W

package org.jimple.planner.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.constants.Constants;

import org.jimple.planner.exceptions.DuplicateDateTimeFieldException;
import org.jimple.planner.exceptions.InvalidCommandException;
import org.jimple.planner.exceptions.MissingDateTimeFieldException;

public class Parser {
			
	/* ----------------------------|
	 * EXTENDED COMMANDS CONSTANTS |
	 * ----------------------------| 
	 * String[]: Stores the possible extended command strings for each command.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = {Constants.STRING_DESC, Constants.STRING_AT, Constants.STRING_FROM, Constants.STRING_ON, Constants.STRING_BY, Constants.STRING_LABEL};
	private final String[] EXTENDED_COMMANDS_EDIT = {Constants.STRING_NAME, Constants.STRING_DESC, Constants.STRING_TIME, Constants.STRING_LABEL};
	private final String[] EXTENDED_COMMANDS_EDITLABEL = {Constants.STRING_NAME, Constants.STRING_COLOUR};
	private final String[] EXTENDED_COMMANDS_NIL = {};
	
	/* --------------|
	 * VALID COLOURS |
	 * --------------|
	 * Supported colours for the labels, red to purple.
	 */ 
	private final String[] VALID_COLOURS = {Constants.STRING_RED, Constants.STRING_ORANGE, Constants.STRING_YELLOW, Constants.STRING_GREEN, Constants.STRING_BLUE, Constants.STRING_PURPLE};
	
	/* ------------------------|
	 * MISCELLANEOUS CONSTANTS |
	 * ------------------------|
	 * Constants which are only used in Parser.
	 */
	
	// Index of main command.
	private final int INDEX_MAIN_COMMAND = 0;
	// Index of user input following the main command
	private final int INDEX_MAIN_COMMAND_USER_INPUT = 1;
	private final int LENGTH_OF_COMMAND_WITH_USER_INPUT = 2;
	private final int LENGTH_OF_COMMAND_ONLY_MAIN_COMMAND = 1;
	private final int OFFSET_CALENDAR_MONTH = 1;
	private final int OFFSET_CALENDAR_YEAR = 1900;
	private final String EMPTY_STRING = "";
	private final String SPACE_STRING = " ";
	
	private final int INCREMENT_BY_1 = 1;
	
	private final String DATE_TIME_STRING_FORMAT = "%02d-%02d-%02dT%02d:%02d";
	
	private final int HOUR_MAX = 23;
	private final int MINUTE_MAX = 59;
	
	/* -------------------|
	 * EXCEPTION MESSAGES |
	 * -------------------|
	 * Messages which are feedbacked to the user when exceptions are thrown, personalized to the exception it is referencing.
	 */
	private final String ERROR_MESSAGE_NO_TASK_NAME = "Command: \"%s\" requires a Task Name.";
	private final String ERROR_MESSAGE_NO_TASK_ID = "Command: \"%s\" requires a TaskID number.";
	private final String ERROR_MESSAGE_NO_SEARCH_STRING = "Command: \"%s\" requires a search string.";
	private final String ERROR_MESSAGE_NO_LABEL_NAME_OR_COLOUR = "Command: \"%s\" requires a label name or colour.";
	private final String ERROR_MESSAGE_NO_LABEL_NAME = "Command: \"%s\" requires a label name.";
	private final String ERROR_MESSAGE_NO_DIRECTORY_PATH = "Command: \"%s\" requires a directory path.";
	private final String ERROR_MESSAGE_NEEDS_ONLY_MAIN_COMMAND = "Command: \"%s\" should not be followed by any parameters.";
	private final String ERROR_MESSAGE_COMMAND_NOT_RECOGNISED = "Command: \"%s\" not recognised.";
	private final String ERROR_MESSAGE_EXTENDED_COMMAND_NOT_RECOGNISED = "Extended Command: \"%s\" not recognised.";
	private final String ERROR_MESSAGE_FROM_WITHOUT_TO = "\"FROM\" must be accompanied with \"TO\".";
	private final String ERROR_MESSAGE_DATE_TIME_NOT_RECOGNISED = "Date/Time input: \"%s\" not recognised.";
	private final String ERROR_MESSAGE_COLOUR_NOT_RECOGNISED = "Label Colour: \"%s\" invalid.";
	
	/* -------|
	 * LOGGER |
	 * -------|
	 */
	private static final Logger LOGGER = Logger.getLogger(Parser.class.getName());
	
	/*
	 * -----------------|
	 * TIMEPARSER CLASS |
	 * -----------------|
	 * Class that can parse "natural language" inputs for date and time.
	 */
	private TimeParser timeParser = new TimeParser();
	
	public Parser() {}

	/* ------------|
	 * MAIN METHOD |
	 * ------------|
	 * The main method that other components use. Returns an InputStruct containing the variables of the user input.
	 */
	public InputStruct parseInput(String userInput) throws Exception {
		
		assert(userInput != null); // Not null string.
		assert(userInput.trim() != ""); // Not whitespace String.
		
		String[] splitUserInput = userInput.split(SPACE_STRING);
		String mainCommand = getCommandString(splitUserInput);
		switch (mainCommand) {
			case Constants.STRING_ADD :
				if (!isCommandOnly(splitUserInput)) {
					InputStruct addStruct = getStruct(splitUserInput, EXTENDED_COMMANDS_ADD);
					// Initialises the field in InputStruct which specifies which task type the added task is.
					addStruct.checkAndSetTaskType();
					return addStruct;
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_NAME, mainCommand)));
				break;
			case Constants.STRING_EDIT :
				if (isNumber(getMainCommandUserInputString(splitUserInput))) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_EDIT);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_ID, mainCommand)));
				break;
			case Constants.STRING_DELETE :
				if (isNumber(getMainCommandUserInputString(splitUserInput))) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_ID, mainCommand)));
				break;
			case Constants.STRING_SEARCH :
				if (!isCommandOnly(splitUserInput)) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_SEARCH_STRING, mainCommand)));
				break;
			case Constants.STRING_DONE :
				if (isNumber(getMainCommandUserInputString(splitUserInput))) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_ID, mainCommand)));
				break;
			case Constants.STRING_RETURN :
				if (isNumber(getMainCommandUserInputString(splitUserInput))) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_ID, mainCommand)));
				break;
			case Constants.STRING_EDITLABEL :
				if (!isCommandOnly(splitUserInput)) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_EDITLABEL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_LABEL_NAME_OR_COLOUR, mainCommand)));
				break;
			case Constants.STRING_DELETELABEL :
				if (!isCommandOnly(splitUserInput)) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_LABEL_NAME, mainCommand)));
				break;
			case Constants.STRING_CHECKCONFLICT :
				if (isNumber(getMainCommandUserInputString(splitUserInput))) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_TASK_ID, mainCommand)));
				break;
			case Constants.STRING_CHANGEDIR :
				if (!isCommandOnly(splitUserInput)) {
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NO_DIRECTORY_PATH, mainCommand)));
				break;
			case Constants.STRING_CHECKDIR :
				if (isCommandOnly(splitUserInput)) {
					return new InputStruct(Constants.STRING_CHECKDIR);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NEEDS_ONLY_MAIN_COMMAND, mainCommand)));
				break;
			case Constants.STRING_UNDOTASK :
				if (isCommandOnly(splitUserInput)) {
					return new InputStruct(Constants.STRING_UNDOTASK);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NEEDS_ONLY_MAIN_COMMAND, mainCommand)));
				break;
			case Constants.STRING_HELP :
				if (isCommandOnly(splitUserInput)) {
					return new InputStruct(Constants.STRING_HELP);
				}
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_NEEDS_ONLY_MAIN_COMMAND, mainCommand)));
				break;
			default :
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_COMMAND_NOT_RECOGNISED, mainCommand)));
				break;
		}
		return null;
	}
	
	/* ---------------------------|
	 * MAIN METHOD HELPER METHODS |
	 * ---------------------------|
	 */
	
	private String getCommandString(String[] userInputStringArray) {
		return userInputStringArray[INDEX_MAIN_COMMAND];
	}
	
	private String getMainCommandUserInputString(String[] splitUserInput) {
		if (splitUserInput.length < LENGTH_OF_COMMAND_WITH_USER_INPUT) {
			return null;
		}
		return splitUserInput[INDEX_MAIN_COMMAND_USER_INPUT];
	}
	
	private boolean isCommandOnly(String[] splitUserInput) {
		return splitUserInput.length == LENGTH_OF_COMMAND_ONLY_MAIN_COMMAND;
	}
	
	private boolean isNumber(String input) {
		if (input == EMPTY_STRING || input == null) {
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

	/* -----------------|
	 * GETSTRUCT METHOD |
	 * -----------------|
	 * Parses the user input, detects and stores the variables in the user input and puts it in the appropriate InputStruct field.
	 */
	private InputStruct getStruct(String[] splitUserInput, String[] extendedCommands) throws Exception {
		
		String mainCommand = getCommandString(splitUserInput);
		
		// Creates the InputStruct to be returned.
		InputStruct currInputStruct = new InputStruct(mainCommand);

		String currCommand = mainCommand;

		// userInputString is the string currently being read. While an extended command is not found, words are added to this String.
		String currInputString = EMPTY_STRING;

		for (int i = 1; i < splitUserInput.length; i++) {
			String currString = splitUserInput[i];
			if (isExtendedCommand(currString, extendedCommands)) { // Word being read is an extended command.
				if (currCommand == mainCommand) {
					parseMainCommand(currInputStruct, currCommand, currInputString.trim());
				} else {
					parseExtendedCommand(mainCommand, currCommand, currInputString.trim(), currInputStruct);
				}
				currCommand = currString; // Updates the current command.
				currInputString = EMPTY_STRING; // Resets "userInputString" as it is already parsed.
			} else { // Updates the current input string if word being read is not an extended command.
				currInputString += currString + SPACE_STRING;
			} 
		}
		//Parses the last user input string which is not covered in the while loop above.
		if (currCommand == mainCommand) {
			parseMainCommand(currInputStruct, currCommand, currInputString.trim());
		} else {
			parseExtendedCommand(mainCommand, currCommand, currInputString.trim(), currInputStruct);
		}
		return currInputStruct;
	}
	
	// Checks if input string is an extended command of the main command.
	private boolean isExtendedCommand(String input, String[] extendedCommands) {
		for (int i = 0; i < extendedCommands.length; i++) {
			if (input.equals(extendedCommands[i])) {
				return true;
			}
		}
		return false;
	}
	
	
	/* -------------------------|
	 * EXTENDED COMMAND PARSERS |
	 * -------------------------|
	 */
	
	//Used due to the "ADD" command requiring a different way of parsing the first user input.
	private void parseMainCommand(InputStruct currInputStruct, String currCommand, String currInputString) {
		if (currCommand.equals(Constants.STRING_ADD)) {
			currInputStruct.setAtIndex(Constants.INDEX_NAME, currInputString);
		} else {
			currInputStruct.setAtIndex(Constants.INDEX_BASE, currInputString);
		}
	}
	
	// Uses the extended command method which is appropriate to the main command. (Different main commands have different extended commands.) 
	private void parseExtendedCommand(String mainCommand, String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (mainCommand) {
			case Constants.STRING_ADD :
				parseExtendedCommandAdd(extendedCommand, inputString, inputStruct);
				break;
			case Constants.STRING_EDIT :
				parseExtendedCommandEdit(extendedCommand, inputString, inputStruct);
				break;
			case Constants.STRING_EDITLABEL :
				parseExtendedCommandEditLabel(extendedCommand, inputString, inputStruct);
				break;
			default:
				break;
		}
	}
	
	// Parses extended commands for "ADD".
	private void parseExtendedCommandAdd(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case Constants.STRING_DESC :
				setDescription(inputString, inputStruct);
				break;
			case Constants.STRING_AT :
			case Constants.STRING_ON :
			case Constants.STRING_FROM :
			case Constants.STRING_BY :
				setTime(extendedCommand, inputString, inputStruct);
				break;
			case Constants.STRING_LABEL :
				setCategory(inputString, inputStruct);
				break;
			default :
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_EXTENDED_COMMAND_NOT_RECOGNISED, extendedCommand)));
		}
	}
	
	// Parses extended commands for "EDIT".
	private void parseExtendedCommandEdit(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case Constants.STRING_NAME :
				setName(inputString, inputStruct);
				break;
			case Constants.STRING_DESC :
				setDescription(inputString, inputStruct);
				break;
			case Constants.STRING_TIME :
				String dateTimeExtendedCommand = getCommandString(inputString.split(SPACE_STRING));
				setTime(dateTimeExtendedCommand, inputString.substring(dateTimeExtendedCommand.length() + INCREMENT_BY_1), inputStruct);
				break;
			case Constants.STRING_LABEL :
				setCategory(inputString, inputStruct);
				break;
			default :
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_EXTENDED_COMMAND_NOT_RECOGNISED, extendedCommand)));
		}
	}
	
	private void setName(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(Constants.INDEX_NAME, userInput);
	}
	
	private void setDescription(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(Constants.INDEX_DESCRIPTION, userInput);
	}
	
	// Parses the extended commands for time which each functions differently.
	private void setTime(String extendedCommand, String userInput, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case Constants.STRING_AT :
				parseAt(userInput, inputStruct);
				break;
			case Constants.STRING_ON :
				parseOn(userInput, inputStruct);
				break;
			case Constants.STRING_FROM :
				parseFrom(userInput, inputStruct);
				break;
			case Constants.STRING_BY :
				parseBy(userInput, inputStruct);
				break;
			default :
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_DATE_TIME_NOT_RECOGNISED, extendedCommand)));
		}
	}
	
	/* AT
	 * Date and time set: 1hr from specified date and time.
	 * No time set: 1 hr from start of specified date.
	 * No date set: 1 hr from next instance of specified time.
	 */
	private void parseAt(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(Constants.STRING_AT, userInput);
		inputStruct.setAtIndex(Constants.INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.add(Calendar.HOUR_OF_DAY, 1);
		inputStruct.setAtIndex(Constants.INDEX_TO, calendarToStringFormat(parsedCalendar));
	}

	/* ON
	 * Date and time set: Specified date and time to end of that day.
	 * No time set: Start of specified date to end of that day.
	 * No date set: Next instance of specified time to end of that day.
	 */ 
	void parseOn(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(Constants.STRING_ON, userInput);
		inputStruct.setAtIndex(Constants.INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.set(Calendar.HOUR_OF_DAY, HOUR_MAX);
		parsedCalendar.set(Calendar.MINUTE, MINUTE_MAX);
		inputStruct.setAtIndex(Constants.INDEX_TO, calendarToStringFormat(parsedCalendar));
	}
	
	/* FROM (needs to be followed by TO)
	 * Date and time set: Specified start date and time to specified end time.
	 * No time set: Start of day of specified start date to end of day of specified end date.
	 * No date set: Next instance of specified start time to next instance of specified end time after that.
	 */
	private void parseFrom(String userInput, InputStruct inputStruct) throws Exception {
		if (!userInput.contains(" TO ")) {
			logAndThrow(new MissingDateTimeFieldException(ERROR_MESSAGE_FROM_WITHOUT_TO));
		} else {
			String[] splitFromTo = userInput.split(" TO ");
			Date from = timeParser.parseTime(Constants.STRING_FROM, splitFromTo[0]).getTime();
			inputStruct.setAtIndex(Constants.INDEX_FROM, calendarToStringFormat(timeParser.parseTime(Constants.STRING_FROM, splitFromTo[0])));
			Calendar to = timeParser.parseTime(Constants.STRING_TO, splitFromTo[1]);
			while (!isAfterFromDate(from, to)) {
				to.add(Calendar.DATE, INCREMENT_BY_1);
			}
			inputStruct.setAtIndex(Constants.INDEX_TO, calendarToStringFormat(to));
		}
	}
	
	/* BY
	 * Date and time set: Deadline by specified date and time.
	 * No time set: Deadline by end of day of specified date.
	 * No date set: Deadline by next instance of specified time.
	 */
	private void parseBy(String userInput, InputStruct inputStruct) throws Exception {
		inputStruct.setAtIndex(Constants.INDEX_FROM, calendarToStringFormat(timeParser.parseTime(Constants.STRING_BY, userInput)));
	}
	
	private void setCategory(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(Constants.INDEX_LABEL, userInput);
	}
	
	// Parses extended commands for "EDITLABEL".
	private void parseExtendedCommandEditLabel(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case Constants.STRING_NAME :
				setLabelName(inputString, inputStruct);
				break;
			case Constants.STRING_COLOUR :
				setLabelColour(inputString.toLowerCase(), inputStruct);
				break;
			default :
				logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_EXTENDED_COMMAND_NOT_RECOGNISED, extendedCommand)));
		}
	}
	
	private void setLabelName(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(Constants.INDEX_EDITLABEL_NAME, userInput);
	}
	
	private void setLabelColour(String userInput, InputStruct inputStruct) throws Exception {
		if (isValidColour(userInput)) {
			inputStruct.setAtIndex(Constants.INDEX_EDITLABEL_COLOUR, userInput);
		} else {
			logAndThrow(new InvalidCommandException(String.format(ERROR_MESSAGE_COLOUR_NOT_RECOGNISED, userInput)));
		}
	}
	
	private boolean isValidColour(String input) {
		for (int i = 0; i < VALID_COLOURS.length; i++) {
			if (input.equals(VALID_COLOURS[i])) {
				return true;
			}
		}
		return false; 
	}
	
	private String calendarToStringFormat(Calendar parsedCalendar) {
		Date parsedDate = parsedCalendar.getTime();
		int year = parsedDate.getYear() + OFFSET_CALENDAR_YEAR;
		int date = parsedDate.getDate();
		int month = parsedDate.getMonth() + OFFSET_CALENDAR_MONTH;
		int hours = parsedDate.getHours();
		int minutes = parsedDate.getMinutes();
		String outputDate = String.format(DATE_TIME_STRING_FORMAT, year, month, date, hours, minutes);
		return outputDate;
	}
	
	// Checks if time of input Date is after time of input Calendar.
	private boolean isAfterFromDate(Date inputFrom, Calendar inputTo) {
		boolean case1 = inputFrom.getMonth() < inputTo.get(Calendar.MONTH);
		boolean case2 = inputFrom.getMonth() == inputTo.get(Calendar.MONTH) && inputFrom.getDate() < inputTo.get(Calendar.DAY_OF_MONTH);
		boolean case3 = inputFrom.getMonth() == inputTo.get(Calendar.MONTH) && inputFrom.getDate() == inputTo.get(Calendar.DAY_OF_MONTH);
		if (case3) {
			boolean case4 = inputFrom.getHours() < inputTo.get(Calendar.HOUR_OF_DAY);
			boolean case5 = inputFrom.getHours() == inputTo.get(Calendar.HOUR_OF_DAY) && inputFrom.getMinutes() < inputTo.get(Calendar.MINUTE);
			return case4 || case5;
		}
		return case1 || case2;
	}
	
	public void logAndThrow(Exception e) throws Exception {
        LOGGER.log(Level.WARNING, e.getMessage());
        throw e;
    }

}