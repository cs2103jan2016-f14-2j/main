/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner.parser;

import java.util.Calendar;
import java.util.Date;
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
	private final String DATE_TIME_STRING_FORMAT = "%02d-%02d-%02dT%02d:%02d";
	
	/* -------------------|
	 * EXCEPTION MESSAGES |
	 * -------------------|
	 * Messages which are feedbacked to the user when exceptions are thrown, personalized to the exception it is referencing.
	 */
	
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
		
		// Not null string.
		assert(userInput != null);
		
		// Not whitespace String.
		assert(userInput.trim() != "");
		
		String[] splitUserInput = userInput.split(" ");
		String mainCommand = getCommandString(splitUserInput);
		try {
			switch (mainCommand) {
				case Constants.STRING_ADD :
					if (!isCommandOnly(splitUserInput)) {
						InputStruct addStruct = getStruct(splitUserInput, EXTENDED_COMMANDS_ADD);
						// Initialises the field in InputStruct which specifies which task type the added task is.
						addStruct.checkAndSetTaskType();
						return addStruct;
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a Task Name. \"");
				case Constants.STRING_EDIT :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_EDIT);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"");
				case Constants.STRING_DELETE :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"");
				case Constants.STRING_SEARCH :
					if (!isCommandOnly(splitUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a search string.");
				case Constants.STRING_DONE :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID.");
				case Constants.STRING_RETURN :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID.");
				case Constants.STRING_EDITLABEL :
					if (!isCommandOnly(splitUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_EDITLABEL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a label name or colour.");
				case Constants.STRING_DELETELABEL :
					if (!isCommandOnly(splitUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a label name or colour.");
				case Constants.STRING_CHECKCONFLICT :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID.");
				case Constants.STRING_CHANGEDIR :
					if (!isCommandOnly(splitUserInput)) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a directory path.");
				case Constants.STRING_CHECKDIR :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(Constants.STRING_CHECKDIR);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" should not be followed by any parameters.");
				case Constants.STRING_UNDOTASK :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(Constants.STRING_UNDOTASK);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" should not be followed by any parameters.");
				case Constants.STRING_HELP :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(Constants.STRING_HELP);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" should not be followed by any parameters.");
				default :
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" not recognised.");
			}
		} catch (InvalidCommandException ice) {
			throw ice;
		} catch (DuplicateDateTimeFieldException dfe) {
			throw dfe;
		} catch (MissingDateTimeFieldException mfe) {
			throw mfe;
		}
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
				currInputString += currString + " ";
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
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
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
				String dateTimeExtendedCommand = getCommandString(inputString.split(" "));
				setTime(dateTimeExtendedCommand, inputString.substring(dateTimeExtendedCommand.length()+1), inputStruct);
				break;
			case Constants.STRING_LABEL :
				setCategory(inputString, inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
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
				throw new InvalidCommandException("Date/Time input: \"" + extendedCommand + "\" not recognised.");
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
		parsedCalendar.set(Calendar.HOUR_OF_DAY, 23);
		parsedCalendar.set(Calendar.MINUTE, 59);
		inputStruct.setAtIndex(Constants.INDEX_TO, calendarToStringFormat(parsedCalendar));
	}
	
	/* FROM (needs to be followed by TO)
	 * Date and time set: Specified start date and time to specified end time.
	 * No time set: Start of day of specified start date to end of day of specified end date.
	 * No date set: Next instance of specified start time to next instance of specified end time after that.
	 */
	private void parseFrom(String userInput, InputStruct inputStruct) throws Exception {
		if (!userInput.contains(" TO ")) {
			throw new MissingDateTimeFieldException("\"FROM\" must be accompanied by \"TO\".");
		} else {
			String[] splitFromTo = userInput.split(" TO ");
			Date from = timeParser.parseTime(Constants.STRING_FROM, splitFromTo[0]).getTime();
			inputStruct.setAtIndex(Constants.INDEX_FROM, calendarToStringFormat(timeParser.parseTime(Constants.STRING_FROM, splitFromTo[0])));
			Calendar to = timeParser.parseTime(Constants.STRING_TO, splitFromTo[1]);
			while (!isAfterFromDate(from, to)) {
				to.add(Calendar.DATE, 1);
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
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void setLabelName(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(Constants.INDEX_EDITLABEL_NAME, userInput);
	}
	
	private void setLabelColour(String userInput, InputStruct inputStruct) throws InvalidCommandException {
		if (isValidColour(userInput)) {
			inputStruct.setAtIndex(Constants.INDEX_EDITLABEL_COLOUR, userInput);
		} else {
			throw new InvalidCommandException("Label Colour: \"" + userInput + "\" invalid.");
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

}