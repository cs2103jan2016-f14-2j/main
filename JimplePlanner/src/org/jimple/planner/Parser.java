/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.jimple.planner.exceptions.DuplicateDateTimeFieldException;
import org.jimple.planner.exceptions.InvalidCommandException;
import org.jimple.planner.exceptions.MissingDateTimeFieldException;

public class Parser {
			
	/* ----------------------------|
	 * EXTENDED COMMANDS CONSTANTS |
	 * ----------------------------| 
	 * String[]: Stores the possible extended command strings for each command.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = {"DESC", "AT", "FROM", "ON", "BY", "LABEL"};
	private final String[] EXTENDED_COMMANDS_EDIT = {"NAME", "DESC", "TIME", "LABEL"};
	private final String[] EXTENDED_COMMANDS_EDITLABEL = {"NAME", "COLOUR"};
	private final String[] EXTENDED_COMMANDS_NIL = {};
	
	/* --------------|
	 * VALID COLOURS |
	 * --------------|
	 */
	
	private final String[] VALID_COLOURS = {"blue", "green", "yellow", "orange", "red", "purple"};
	
	private final int INDEX_BASE = 0;
	
	// Stores the indexes for task fields. Used by "add" and "edit".
	private final int INDEX_NAME = 1;
	private final int INDEX_DESCRIPTION = 2;
	private final int INDEX_FROM = 3;
	private final int INDEX_TO = 4;
	private final int INDEX_LABEL = 5;
	// Stores the indexes for task fields. Used by "editlabel".
	private final int INDEX_EDITLABEL_NAME = 1;
	private final int INDEX_EDITLABEL_COLOUR = 2;
	
	

	/* ----------------|
	 * CONSTANTS |
	 * ----------------|
	 */

	// Main commands.
	private static final String COMMAND_ADD = "ADD";
	private static final String COMMAND_EDIT = "EDIT";
	private static final String COMMAND_DELETE = "DELETE";
	private static final String COMMAND_SEARCH = "SEARCH";
	private static final String COMMAND_EDITLABEL = "EDITLABEL";
	private static final String COMMAND_DELETELABEL = "DELETELABEL";
	private static final String COMMAND_CHANGEDIR = "CHANGEDIR";
	private static final String COMMAND_CHECKDIR = "CHECKDIR";
	private static final String COMMAND_UNDO = "UNDO";
	private static final String COMMAND_HELP = "DELP";
	
	// Extended commands.
	private static final String EXTENDED_COMMAND_NAME = "NAME";
	private static final String EXTENDED_COMMAND_DESCRIPTION = "DESC";
	private static final String EXTENDED_COMMAND_TIME = "TIME";
	private static final String EXTENDED_COMMAND_AT = "AT";
	private static final String EXTENDED_COMMAND_ON = "ON";
	private static final String EXTENDED_COMMAND_FROM = "FROM";
	private static final String EXTENDED_COMMAND_TO = "TO";
	private static final String EXTENDED_COMMAND_BY = "BY";
	private static final String EXTENDED_COMMAND_LABEL = "LABEL";
	private static final String EXTENDED_COMMAND_COLOUR = "COLOUR";
	
	private final int INDEX_COMMAND = 0;
	private final int INDEX_MAIN_COMMAND_USER_INPUT = 1;
	private final int OFFSET_CALENDAR_MONTH = 1;
	private final String EMPTY_STRING = "";

	/*
	 * ---------------------|
	 * DateTimeParser Class |
	 * ---------------------|
	 * Class that can parse "natural language" inputs for date and time.
	 */
	private TimeParser timeParser = new TimeParser();
	
	public Parser() {}

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
		String mainCommand = getCommandString(splitUserInput);
		try {
			switch (mainCommand) {
				case COMMAND_ADD :
					InputStruct addStruct = getStruct(splitUserInput, EXTENDED_COMMANDS_ADD);
					addStruct.checkAndSetTaskType();
					return addStruct;
				case COMMAND_EDIT :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_EDIT);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"");
				case COMMAND_DELETE :
					if (isNumber(getMainCommandUserInputString(splitUserInput))) {
						return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" requires a TaskID number. \"");
				case COMMAND_SEARCH :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				case COMMAND_EDITLABEL :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_EDITLABEL);
				case COMMAND_CHANGEDIR :
					return getStruct(splitUserInput, EXTENDED_COMMANDS_NIL);
				case COMMAND_CHECKDIR :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(COMMAND_CHECKDIR);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" should not be followed by any parameters.");
				case COMMAND_UNDO :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(COMMAND_UNDO);
					}
					throw new InvalidCommandException("Command: \"" + mainCommand + "\" should not be followed by any parameters.");
				case COMMAND_HELP :
					if (isCommandOnly(splitUserInput)) {
						return new InputStruct(COMMAND_HELP);
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
	
	private String getMainCommandUserInputString(String[] splitUserInput) {
		return splitUserInput[INDEX_MAIN_COMMAND_USER_INPUT];
	}
	
	private boolean isCommandOnly(String[] splitUserInput) {
		return splitUserInput.length == 1;
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
		
		String mainCommand = getCommandString(splitUserInput);
		
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
					parseMainCommand(currInputStruct, currCommand, currInputString.trim());
				} else {
					parseExtendedCommand(mainCommand, currCommand, currInputString.trim(), currInputStruct);
				}
				currCommand = currString;
				// Resets "userInputString".
				currInputString = EMPTY_STRING;
			} else {
				currInputString += currString + " ";
			} 
		}
		if (currCommand == mainCommand) {
			parseMainCommand(currInputStruct, currCommand, currInputString.trim());
		} else {
			parseExtendedCommand(mainCommand, currCommand, currInputString.trim(), currInputStruct);
		}
		return currInputStruct;
	}

	private void parseMainCommand(InputStruct currInputStruct, String currCommand, String currInputString) {
		if (currCommand.equals(COMMAND_ADD)) {
			currInputStruct.setAtIndex(INDEX_NAME, currInputString);
		} else {
			currInputStruct.setAtIndex(INDEX_BASE, currInputString);
		}
	}
		
	private void parseExtendedCommand(String mainCommand, String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (mainCommand) {
			case COMMAND_ADD :
				parseExtendedCommandAdd(extendedCommand, inputString, inputStruct);
				break;
			case COMMAND_EDIT :
				parseExtendedCommandEdit(extendedCommand, inputString, inputStruct);
				break;
			case COMMAND_EDITLABEL :
				parseExtendedCommandEditLabel(extendedCommand, inputString, inputStruct);
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
			case EXTENDED_COMMAND_LABEL :
				setCategory(inputString, inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void parseExtendedCommandEdit(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case EXTENDED_COMMAND_NAME :
				setName(inputString, inputStruct);
			case EXTENDED_COMMAND_DESCRIPTION :
				setDescription(inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_TIME :
				String dateTimeExtendedCommand = getCommandString(inputString.split(" "));
				setTime(dateTimeExtendedCommand, inputString.substring(dateTimeExtendedCommand.length()+1), inputStruct);
				break;
			case EXTENDED_COMMAND_LABEL :
				setCategory(inputString, inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	private void parseExtendedCommandEditLabel(String extendedCommand, String inputString, InputStruct inputStruct) throws Exception {
		switch (extendedCommand) {
			case EXTENDED_COMMAND_NAME :
				setLabelName(inputString, inputStruct);
				break;
			case EXTENDED_COMMAND_COLOUR :
				setLabelColour(inputString.toLowerCase(), inputStruct);
				break;
			default :
				throw new InvalidCommandException("\"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void setLabelName(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(INDEX_EDITLABEL_NAME, userInput);
	}
	
	private void setLabelColour(String userInput, InputStruct inputStruct) throws InvalidCommandException {
		if (isValidColour(userInput)) {
			inputStruct.setAtIndex(INDEX_EDITLABEL_COLOUR, userInput);
		} else {
			throw new InvalidCommandException("Label Colour: \"" + userInput + "\" invalid. Type \"help\" to see the list of valid label colours.");
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
			default :
				throw new InvalidCommandException("Date/Time input: \"" + extendedCommand + "\" not recognised.");
		}
	}
	
	private void parseAt(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(EXTENDED_COMMAND_AT, userInput);
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.add(Calendar.HOUR_OF_DAY, 1);
		inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(parsedCalendar));
	}
	
	private void parseOn(String userInput, InputStruct inputStruct) throws Exception {
		Calendar parsedCalendar = timeParser.parseTime(EXTENDED_COMMAND_ON, userInput);
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(parsedCalendar));
		parsedCalendar.set(Calendar.HOUR_OF_DAY, 23);
		parsedCalendar.set(Calendar.MINUTE, 59);
		inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(parsedCalendar));
	}
	
	private void parseFrom(String userInput, InputStruct inputStruct) throws Exception {
		if (!userInput.contains(" TO ")) {
			throw new MissingDateTimeFieldException("\"FROM\" must be accompanied by \"TO\".");
		} else {
			String[] splitFromTo = userInput.split(" TO ");
			Date from = timeParser.parseTime(EXTENDED_COMMAND_FROM, splitFromTo[0]).getTime();
			inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(timeParser.parseTime(EXTENDED_COMMAND_FROM, splitFromTo[0])));
			Calendar to = timeParser.parseTime(EXTENDED_COMMAND_TO, splitFromTo[1]);
			while (!isAfterFromDate(from, to)) {
				to.add(Calendar.DATE, 1);
			}
			inputStruct.setAtIndex(INDEX_TO, calendarToStringFormat(to));
		}
	}
	
	private void parseBy(String userInput, InputStruct inputStruct) throws Exception {
		inputStruct.setAtIndex(INDEX_FROM, calendarToStringFormat(timeParser.parseTime(EXTENDED_COMMAND_BY, userInput)));
	}
	
	private void setCategory(String userInput, InputStruct inputStruct) {
		inputStruct.setAtIndex(INDEX_LABEL, userInput);
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
	
	private boolean isAfterFromDate(Date inputFrom, Calendar inputTo) {
		boolean case1 = inputFrom.getMonth() < inputTo.get(Calendar.MONTH);
		System.out.println(inputFrom.getMonth() + " " + inputTo.get(Calendar.MONTH));
		boolean case2 = inputFrom.getMonth() == inputTo.get(Calendar.MONTH) && inputFrom.getDate() < inputTo.get(Calendar.DAY_OF_MONTH);
		boolean case3 = inputFrom.getMonth() == inputTo.get(Calendar.MONTH) && inputFrom.getDate() == inputTo.get(Calendar.DAY_OF_MONTH);
		if (case3) {
			boolean case4 = inputFrom.getHours() < inputTo.get(Calendar.HOUR_OF_DAY);
			boolean case5 = inputFrom.getMinutes() < inputTo.get(Calendar.MINUTE);
			return case4 || case5;
		}
		return case1 || case2;
	}
	
	private boolean isExtendedCommand(String input, String[] extendedCommands) {
		for (int i = 0; i < extendedCommands.length; i++) {
			if (input.equals(extendedCommands[i])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidColour(String input) {
		for (int i = 0; i < VALID_COLOURS.length; i++) {
			if (input.equals(VALID_COLOURS[i])) {
				return true;
			}
		}
		return false; 
	}
	
	private String getCommandString(String[] userInputStringArray) {
		return userInputStringArray[INDEX_COMMAND];
	}

}