//@@author A0135775W

package org.jimple.planner.parser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jimple.planner.constants.Constants;

import org.jimple.planner.exceptions.DuplicateDateTimeFieldException;
import org.jimple.planner.exceptions.InvalidDateTimeFieldException;
import org.jimple.planner.exceptions.MissingDateTimeFieldException;

public class TimeParser {
	
	/* -----------------------------------|
	 * TIMEPARSER MISCELLANEOUS CONSTANTS |
	 * -----------------------------------|
	 */
	// AMPM constants
	private final int AMPM_HOURS_MIN = 0;
	private final int AMPM_HOURS_MAX = 12;
	private final String AMPM_FLOATING_NUMBER_MINUTE = "0";
	private final int TIME_FIELDS_SIZE = 2;
	private final int AMPM_INPUT_MIN_LENGTH = 3;
	private final int OFFSET_PM_HOURS = 12;
	private final int OFFSET_LAST_2_CHARACTERS = 2;
	private final String STRING_PM = "pm";
	private final String STRING_AM = "am";
	private final String STRING_DOT = ".";
	
	// Natural language days constants
	private final String STRING_TOMORROW = "tomorrow";
	private final String STRING_TODAY = "today";
	
	// Forward slash date format constants
	private final int DAY_MONTH_YEAR_FORMAT_LENGTH = 3;
	private final int DAY_MONTH_FORMAT_LENGTH = 2;
	private final int INDEX_DAY = 0;
	private final int INDEX_MONTH = 1;
	private final int INDEX_YEAR = 2;
	
	// 4-digit time format constants
	private final int OFFSET_NOT_4_DIGIT_YEAR = 2000;
	private final int MIN_4_DIGIT_YEAR = 1000;
	private final int INDEX_SEPARATE_4_DIGIT_HOUR_MINUTE = 2;
	private final int FORMAT_LENGTH_4_DIGIT = 4;
	
	// Date constants
	private final String STRING_FORWARD_SLASH = "/";
	private final int OFFSET_CALENDAR_MONTH = 1;
	private final int MONTH_MAX = 12;
	private final int MONTH_MIN = 1;
	
	// Time constants
	private final String STRING_COLON = ":";
	private final String STRING_DOT_FOR_SPLIT = "\\.";
	private final int INDEX_HOUR = 0;
	private final int INDEX_MINUTE = 1;
	
	private final int HOUR_MAX = 23;
	private final int HOUR_MIN = 0;
	private final int MINUTE_MAX = 59;
	private final int MINUTE_MIN = 0;
	
	// General constants
	private final int START_INDEX = 0;
	private final int INCREMENT_BY_1 = 1;
	private final String EMPTY_STRING = "";
	
	/* ----------------------------|
	 * TIMEPARSER PARSER CONSTANTS |
	 * ----------------------------|
	 * Constants which are only used in TimeParser.
	 */
	
	// Array of accepted non-number month inputs.
	private String[] STRINGS_MONTH = {"january", "jan",
			"february", "feb",
			"march", "mar",
			"april", "apr",
			"may",
			"june", "jun",
			"july", "jul",
			"august", "aug",
			"september", "sep",
			"october", "oct",
			"november", "nov",
			"december", "dec"};
	
	// Array of month numbers associated with the same index in the STRINGS_MONTH array.
	private int[] VALUES_MONTH = {
			1, 1,
			2, 2,
			3, 3,
			4, 4,
			5,
			6, 6,
			7, 7,
			8, 8,
			9, 9,
			10, 10,
			11, 11,
			12, 12};
	
	// Array of accepted day-of-week inputs.
	private String[] STRINGS_DAY = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
	
	// Array of day numbers associated with the same index in the STRINGS_DAY array.
	private int[] VALUES_DAY = {1, 2, 3, 4, 5, 6, 7};
	
	/* ---------|
	 * HASHMAPS |
	 * ---------|
	 * HashMaps to easily access the month/day numbers from their respective strings.
	 */
	private HashMap<String, Integer> calendarMonths = new HashMap<String, Integer>();
	private HashMap<String, Integer> calendarDays = new HashMap<String, Integer>();
	
	/* ------------------------|
	 * TIMEPARSER ERROR VALUES |
	 * ------------------------|
	 * Constants used to depict an error. Used by TimeParser functions to return and detect errors.
	 */
	private final int FIELD_NOT_SET_VALUE = -1;
	private final int FIELD_INVALID_VALUE = -2;
	
	private final String ERROR_MESSAGE_AMPM_DOT_FORMAT = "Invalid input: \"%s\". Time should be formated to hh.mm before am/pm.";
	private final String ERROR_MESSAGE_AMPM_COLON_FORMAT = "Invalid input: \"%s\". Time should be formated to hh:mm before am/pm.";
	private final String ERROR_MESSAGE_PARSE_AMPM_HOUR = "Invalid input: Hour \"%s\" of \"%s\". Please input a valid AM/PM time for hour.";
	private final String ERROR_MESSAGE_DOT_FORMAT = "Invalid input: \"%s\". Time should be written in \"hh.mm\" format.";
	private final String ERROR_MESSAGE_COLON_FORMAT = "Invalid input: \"%s\". Time should be written in \"hh:mm\" format."; 
	private final String ERROR_MESSAGE_FORWARD_SLASH_FORMAT = "Invalid input: \"%s\". Time should be written in \"dd/mm\" or \"dd/mm/yyyy\" format.";
	private final String ERROR_MESSAGE_PARSE_DAY = "Invalid input: Day \"%s\" of \"%s\". Please input a valid time for day.";
	private final String ERROR_MESSAGE_PARSE_MONTH = "Invalid input: Day \"%s\" of \"%s\". Please input a valid time for day.";
	private final String ERROR_MESSAGE_PARSE_YEAR = "Invalid input: Day \"%s\" of \"%s\". Please input a valid time for day.";
	private final String ERROR_MESSAGE_YEAR_PAST = "Invalid input: Year \"%s\" of \"%s\" is before the current year.";
	private final String ERROR_MESSAGE_PARSE_HOUR = "Invalid input: Day \"%s\" of \"%s\". Please input a valid time for day.";
	private final String ERROR_MESSAGE_PARSE_MINUTE = "Invalid input: Day \"%s\" of \"%s\". Please input a valid time for day.";
	private final String ERROR_MESSAGE_DATE_PAST = "Invalid input: Date \"%s\" is before the current date.";
	
	/* ----------------------------|
	 * TIMEPARSER DATE/TIME FIELDS |
	 * ----------------------------|
	 * Temporary variables which store the parsed date/time fields. These variables will be entered into the output Calendar. 
	 */
	private int day = FIELD_NOT_SET_VALUE;
	private int month = FIELD_NOT_SET_VALUE;
	private int year = FIELD_NOT_SET_VALUE;
	private int hour = FIELD_NOT_SET_VALUE;
	private int minute = FIELD_NOT_SET_VALUE;
	
	private final String FIELD_YEAR = "year";
	private final String FIELD_MONTH = "month";
	private final String FIELD_DAY = "day";
	private final String FIELD_HOUR = "hour";
	private final String FIELD_MINUTE = "minute";
	
	/* ---------|
	 * CALENDAR |
	 * ---------|
	 * The main java data structure used to calculate date/time.
	 */
	private Calendar c = null;
	
	//private static Logger LOGGER = Logger.getLogger("TimeParser");
	//FileHandler fh;
	
	/* -----------------------|
	 * TIMEPARSER CONSTRUCTOR |
	 * -----------------------|
	 * Sets up the hashmaps for easy access.
	 */
	public TimeParser() {
		for (int i = 0; i < STRINGS_MONTH.length; i++) {
			calendarMonths.put(STRINGS_MONTH[i], VALUES_MONTH[i]);
		}
		for (int i = 0; i < STRINGS_DAY.length; i++) {
			calendarDays.put(STRINGS_DAY[i], VALUES_DAY[i]);
		}
		/*try {
			//fh = new FileHandler("C:/Users/user/git/main/JimplePlanner/LogFile.log");
			//LOGGER.addHandler(fh);
			//SimpleFormatter formatter = new SimpleFormatter();
			//fh.setFormatter(formatter);
		} catch (SecurityException e) {
			//LOGGER.log(Level.WARNING, "No permission to edit or create the log file.", e);
		} catch (IOException e) {
			//LOGGER.log(Level.WARNING, "Log file cannot be found.", e);
		}*/
	}
	
	/* -------------------|
	 * MAIN PARSER METHOD |
	 * -------------------|
	 * Takes in a string and returns the Calendar with the specified date/time. If there is an error, returns null.
	 */
	public Calendar parseTime(String extendedCommand, String input) throws Exception {
		resetTimeAndDate();
		String[] splitInput = input.split(" ");
		for (String i: splitInput) { // For each word being read, goes through a list of parsers which is then converted to the appropriate date/time to be stored in calendar.
			if (parseIfIsDay(i)) {
			} else if (parseIfIsMonth(i)) {
			} else if (parseIfIsAMPMFormat(i)) {
			} else if (parseIfIsTodayOrTomorrow(i)) {
			} else if (parseIfIsColonTimeFormat(i)) {
			} else if (parseIfIsDotTimeFormat(i)) {
			} else if (parseIfIsForwardSlashDateFormat(i)) {
			} else if (parseIfIs4DigitFloatingNumber(i)) {
			} else if (parseIfIs2OrLessDigitFloatingNumber(i)) {
			} else {  // All parsers failed, word being read not supported.
				throw new InvalidDateTimeFieldException("Date/Time: \"" + i + "\" not recognised.");
			}
		}
		if (!formatCalendarIfValid(extendedCommand, input)) {
			return null;
		}
		return c;
	}

	/* -------------------------|
	 * CALENDAR VERIFIER METHOD |
	 * -------------------------|
	 * After parsing through the entire input string, checks if all the required fields are specified.
	 */
	private boolean formatCalendarIfValid(String extendedCommand, String userInput) throws DuplicateDateTimeFieldException, MissingDateTimeFieldException, InvalidDateTimeFieldException {
		c = Calendar.getInstance(); // Initialises the Calendar.
		initSecondaryAndPresetFields(extendedCommand);
		// Set up fields in calendar according to parsed inputs.
		/*if (!isAfterCurrentDateAndTime()) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_DATE_PAST, userInput));
		}*/
		if (setCalendarField(FIELD_HOUR, getField(FIELD_HOUR))) {
			if (setCalendarField(FIELD_MINUTE, getField(FIELD_MINUTE))) {
				if (setCalendarField(FIELD_YEAR, getField(FIELD_YEAR))) {
					if (setCalendarField(FIELD_MONTH, getField(FIELD_MONTH))) {
						if (setCalendarField(FIELD_DAY, getField(FIELD_DAY))) {
							return true;
						}
					}
				}
			}
		}
		return false; // If any fields are missing, parsing has failed, return false.
	}

	private boolean isAfterCurrentDateAndTime() throws InvalidDateTimeFieldException {
		if (!isToday(getField(FIELD_DAY), getField(FIELD_MONTH)) && !isAfterCurrentDate(getField(FIELD_DAY), getField(FIELD_MONTH))) {
			//System.out.println("A");
			return false;
		} else if (isToday(getField(FIELD_DAY), getField(FIELD_MONTH)) && !isAfterCurrentTime(getField(FIELD_HOUR), getField(FIELD_MINUTE))) {
			//System.out.println("B");
			return false;
		}
		return true;
	}
	
	/* -----------------------------------|
	 * DATE/TIME INPUT FLEXIBILITY METHOD |
	 * -----------------------------------|
	 * The TimeParser allows users to input:
	 * - Date & Time
	 * - Date only
	 * - Time only
	 * This method detects which of the above the current user input is, then fills in certain empty fields (if applicable) according to the extended command used.
	 */
	private boolean initSecondaryAndPresetFields(String extendedCommand) throws DuplicateDateTimeFieldException {
		if (isTimeOnly()) {
			setDateToNextInstanceOfSpecifiedTime();
		}
		if (isMissingTime()) {
			setTimeAccordingToExtendedCommand(extendedCommand);
		}
		if (!isFieldSet(FIELD_YEAR)) {
			setYearToNextInstanceOfSpecifiedDateTime();
		}
		c.set(Calendar.SECOND, 0); // Seconds preset to 0 (default).
		return true;
	}

	// Increments year by 1 if specified date is before current date.
	private void setYearToNextInstanceOfSpecifiedDateTime() throws DuplicateDateTimeFieldException {
		c.setTimeInMillis(System.currentTimeMillis());
		if (!isAfterCurrentDate(getField(FIELD_DAY), getField(FIELD_MONTH)) && !isToday(getField(FIELD_DAY), getField(FIELD_MONTH))) {
			c.add(Calendar.YEAR, INCREMENT_BY_1);
			setField(FIELD_YEAR, c.get(Calendar.YEAR));
		} else {
			setField(FIELD_YEAR, c.get(Calendar.YEAR));
		}
	}

	/* Time will be set according to their extended command:
	 * ON/AT/FROM: 00:00
	 * TO/BY: 23:59
	 */
	private void setTimeAccordingToExtendedCommand(String extendedCommand) throws DuplicateDateTimeFieldException {
		switch (extendedCommand) {
			case Constants.STRING_ON :
			case Constants.STRING_AT :
			case Constants.STRING_FROM :
				setField(FIELD_HOUR, HOUR_MIN);
				setField(FIELD_MINUTE, MINUTE_MIN);
				break;
			case Constants.STRING_TO :
			case Constants.STRING_BY :
				setField(FIELD_HOUR, HOUR_MAX);
				setField(FIELD_MINUTE, MINUTE_MAX);
				break;
			default :
				break;
		}
	}

	// Increments date by 1 if specified time is before current time.
	private void setDateToNextInstanceOfSpecifiedTime() throws DuplicateDateTimeFieldException {
		c.setTimeInMillis(System.currentTimeMillis());
		if (!isAfterCurrentTime(getField(FIELD_HOUR), getField(FIELD_MINUTE))) {
			c.add(Calendar.DATE, INCREMENT_BY_1);
		}
		setField(FIELD_DAY, c.get(Calendar.DAY_OF_MONTH));
		setField(FIELD_MONTH, c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH);
		setField(FIELD_YEAR, c.get(Calendar.YEAR));
	}

	private boolean isMissingTime() {
		return !isFieldSet(FIELD_HOUR) && !isFieldSet(FIELD_MINUTE);
	}

	private boolean isTimeOnly() {
		return isFieldSet(FIELD_HOUR) && isFieldSet(FIELD_MINUTE) && !isFieldSet(FIELD_DAY) && !isFieldSet(FIELD_MONTH) && !isFieldSet(FIELD_YEAR);
	}
	
	private boolean isToday(int inputDay, int inputMonth) {
		c.setTimeInMillis(System.currentTimeMillis());
		return inputMonth == c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH && inputDay == c.get(Calendar.DAY_OF_MONTH);
	}
	
	// Returns true if input date is after current date. Vice versa.
	private boolean isAfterCurrentDate(int inputDay, int inputMonth) {
		c.setTimeInMillis(System.currentTimeMillis());
		boolean case1 = inputMonth > c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH;
		boolean case2 = inputMonth == c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH && inputDay > c.get(Calendar.DAY_OF_MONTH);
		return case1 || case2;
	}
	
	// Returns true if input time is after current time. Vice versa.
	private boolean isAfterCurrentTime(int inputHour, int inputMinute) {
		c.setTimeInMillis(System.currentTimeMillis());
		boolean case1 = inputHour > c.get(Calendar.HOUR_OF_DAY);
		boolean case2 = inputHour == c.get(Calendar.HOUR_OF_DAY) && inputMinute > c.get(Calendar.MINUTE);
		return case1 || case2;
	}
	
	private boolean resetTimeAndDate() {
		day = FIELD_NOT_SET_VALUE;
		month = FIELD_NOT_SET_VALUE;
		year = FIELD_NOT_SET_VALUE;
		hour = FIELD_NOT_SET_VALUE;
		minute = FIELD_NOT_SET_VALUE;
		return true;
	}
	
	/* ------------------------------|
	 * SETTERS, CHECKERS AND GETTERS |
	 * ------------------------------|
	 */
	// Fields set here are the output Calendar fields.
	private boolean setCalendarField(String inputField, int inputValue) throws MissingDateTimeFieldException {
		if (!isFieldSet(inputField)) {
			throw new MissingDateTimeFieldException(inputField + " not set.");
		} else {
			switch (inputField) {
				case FIELD_DAY :
					c.set(Calendar.DAY_OF_MONTH, inputValue);
					return true;
				case FIELD_MONTH :
					c.set(Calendar.MONTH, inputValue-1);
					return true;
				case FIELD_YEAR :
					c.set(Calendar.YEAR, inputValue);
					return true;
				case FIELD_HOUR :
					c.set(Calendar.HOUR_OF_DAY, inputValue);
					return true;
				case FIELD_MINUTE :
					c.set(Calendar.MINUTE, inputValue);
					return true;
				default :
					break;
			}
		}
		return false;
	}
	
	// Fields being set here are the temporary fields, not the output Calendar fields.
	private boolean setField(String inputField, int inputValue) throws DuplicateDateTimeFieldException {
		if (isFieldSet(inputField)) {
			throw new DuplicateDateTimeFieldException(inputField + " cannot be set to " + inputValue + ". " + inputField + " already set to " + getField(inputField));
		} else {
			switch (inputField) {
				case FIELD_DAY :
					day = inputValue;
					return true;
				case FIELD_MONTH :
					month = inputValue;
					return true;
				case FIELD_YEAR :
					year = inputValue;
					return true;
				case FIELD_HOUR :
					hour = inputValue;
					return true;
				case FIELD_MINUTE :
					minute = inputValue;
					return true;
				default :
					System.out.println("Invalid field for setting Date/Time");
					break;
			}
		}
		return false;
	}
	
	// Fields being checked here are the temporary fields, not the output Calendar fields.
	private boolean isFieldSet(String inputField) {
		switch (inputField) {
			case FIELD_DAY :
				return day != FIELD_NOT_SET_VALUE;
			case FIELD_MONTH :
				return month != FIELD_NOT_SET_VALUE;
			case FIELD_YEAR :
				return year != FIELD_NOT_SET_VALUE;
			case FIELD_HOUR :
				return hour != FIELD_NOT_SET_VALUE;
			case FIELD_MINUTE :
				return minute != FIELD_NOT_SET_VALUE;
			default :
				return true;
		}
	}
	
	// Fields being retrieved here are the temporary fields, not the output Calendar fields.
	private int getField(String inputField) {
		switch (inputField) {
			case FIELD_DAY :
				return day;
			case FIELD_MONTH :
				return month;
			case FIELD_YEAR :
				return year;
			case FIELD_HOUR :
				return hour;
			case FIELD_MINUTE :
				return minute;
			default :
				return FIELD_INVALID_VALUE;
		}
	}
	
	/* ------------------|
	 * DATE/TIME PARSERS |
	 * ------------------|
	 * The parsers detect different cases of date/time inputs which then stores the detected values into the proper date/time variable.
	 */
	
	/*
	 * DAY PARSER
	 * Parses days. E.g Monday, Wednesday, Sunday
	 */
	private boolean parseIfIsDay(String input) throws DuplicateDateTimeFieldException{
		if (calendarDays.containsKey(input)) {
			c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			int inputDayNo = calendarDays.get(input);
			while (c.get(Calendar.DAY_OF_WEEK) != inputDayNo) {
				c.add(Calendar.DATE, INCREMENT_BY_1);
			}
			setField(FIELD_DAY, c.get(Calendar.DATE));
			setField(FIELD_MONTH, c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH);
			return true;
		}
		return false;
	}
	
	/*
	 * AMPM PARSER
	 * Parses am and pm. E.g 12am, 5.30pm, 10:25pm.
	 */
	private boolean parseIfIsAMPMFormat(String input) throws DuplicateDateTimeFieldException, InvalidDateTimeFieldException {
		if (input.length() < AMPM_INPUT_MIN_LENGTH) {
			return false;
		}
		String last2Characters = input.substring(input.length() - OFFSET_LAST_2_CHARACTERS, input.length()).toLowerCase();
		if (last2Characters.equals(STRING_AM) || last2Characters.equals(STRING_PM)) {
			String beforeLast2Characters = input.substring(START_INDEX, input.length() - OFFSET_LAST_2_CHARACTERS);
			if (beforeLast2Characters.contains(STRING_DOT)) {
				return parseDotAMPM(beforeLast2Characters, last2Characters, input);
			} else if (beforeLast2Characters.contains(STRING_COLON)) {
				return parseColonAMPM(beforeLast2Characters, last2Characters, input);
			} else if (isANumber(beforeLast2Characters)){
				return parseFloatingNumberAMPM(beforeLast2Characters, last2Characters, input);
			} else {
				throw new InvalidDateTimeFieldException("Invalid time AM/PM format: \"" + input + "\".");
			}
		}
		return false;
	}
	
	// E.g 1.30pm, E.g 6.10am, 11.03pm
	private boolean parseDotAMPM(String inputTime, String inputAMPM, String userInput) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitTime = inputTime.split(STRING_DOT_FOR_SPLIT);
		if (splitTime.length == TIME_FIELDS_SIZE) {
			String inputHour = splitTime[INDEX_HOUR];
			String inputMinute = splitTime[INDEX_MINUTE];
			setAMPMHour(inputHour, inputAMPM, userInput);
			parseMinute(inputMinute, userInput);
			return true;
		}
		throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_AMPM_DOT_FORMAT, inputTime));
	}
	
	// E.g 1:30pm, E.g 6:10am, 6:03pm
	private boolean parseColonAMPM(String inputTime, String inputAMPM, String userInput) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitTime = inputTime.split(STRING_COLON);
		if (splitTime.length == TIME_FIELDS_SIZE) {
			String inputHour = splitTime[INDEX_HOUR];
			String inputMinute = splitTime[INDEX_MINUTE];
			setAMPMHour(inputHour, inputAMPM, userInput);
			parseMinute(inputMinute, userInput);
			return true;
		}
		throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_AMPM_COLON_FORMAT, inputTime));
	}
	
	private boolean parseFloatingNumberAMPM(String inputTime, String inputAMPM, String userInput) throws DuplicateDateTimeFieldException, InvalidDateTimeFieldException {
		setAMPMHour(inputTime, inputAMPM, userInput);
		parseMinute(AMPM_FLOATING_NUMBER_MINUTE, userInput);
		return true;
	}

	private void setAMPMHour(String inputHour, String inputAMPM, String userInput) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputHour) || !isValidAMPMHour(inputHour)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_AMPM_HOUR, inputHour, userInput));
		} else {
			switch (inputAMPM) {
				case STRING_AM :
					setField(FIELD_HOUR, Integer.parseInt(inputHour) % AMPM_HOURS_MAX);
					break;
				case STRING_PM :
					setField(FIELD_HOUR, (Integer.parseInt(inputHour) % AMPM_HOURS_MAX) + OFFSET_PM_HOURS);
					break;
			}
		}
	}
	
	private boolean isValidAMPMHour(String input) {
		int inputHour = Integer.parseInt(input);
		return inputHour > AMPM_HOURS_MIN && inputHour <= AMPM_HOURS_MAX;
	}
	
	// Parses the words "today" and "tomorrow".
	private boolean parseIfIsTodayOrTomorrow(String input) throws DuplicateDateTimeFieldException{
		c = Calendar.getInstance();
		switch (input.toLowerCase()) {
		case STRING_TODAY :
			setField(FIELD_DAY, c.get(Calendar.DATE));
			setField(FIELD_MONTH, c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH);
			return true;
		case STRING_TOMORROW :
			c.add(Calendar.DATE, INCREMENT_BY_1);
			setField(FIELD_DAY, c.get(Calendar.DATE));
			setField(FIELD_MONTH, c.get(Calendar.MONTH) + OFFSET_CALENDAR_MONTH);
			return true;
		default :
			break;
		}
		return false;
	}

	// Parses months. E.g February, May, December.
	private boolean parseIfIsMonth(String input) throws DuplicateDateTimeFieldException {
		String inputLowerCase = input.toLowerCase();
		if (calendarMonths.containsKey(inputLowerCase)) {
			setField(FIELD_MONTH, calendarMonths.get(inputLowerCase));
			return true;
		}
		return false;
	}

	// Parses time in hh:mm format. E.g 00:00, 12:34, 23:59.
	private boolean parseIfIsColonTimeFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains(STRING_COLON)) {
			String[] splitTime = input.split(STRING_COLON);
			if (splitTime.length == TIME_FIELDS_SIZE) {
				String inputHour = splitTime[INDEX_HOUR];
				String inputMinute = splitTime[INDEX_MINUTE];
				parseHour(inputHour, input);
				parseMinute(inputMinute, input);
				return true;
			} else {
				throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_COLON_FORMAT, input));
			}
		}
		return false;
	}
	
	private boolean parseIfIsDotTimeFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains(STRING_DOT_FOR_SPLIT)) {
			String[] splitTime = input.split(STRING_DOT_FOR_SPLIT);
			if (splitTime.length == TIME_FIELDS_SIZE) {
				String inputHour = splitTime[INDEX_HOUR];
				String inputMinute = splitTime[INDEX_MINUTE];
				parseHour(inputHour, input);
				parseMinute(inputMinute, input);
				return true;
			} else {
				throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_DOT_FORMAT, input));
			}
		}
		return false;
	}

	private void parseMinute(String inputMinute, String userInput)
			throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputMinute) || !isValidMinute(inputMinute)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_MINUTE, inputMinute, userInput));
		} else {
			setField(FIELD_MINUTE, Integer.parseInt(inputMinute));
		}
	}

	private void parseHour(String inputHour, String userInput)
			throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputHour) || !isValidHour(inputHour)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_HOUR, inputHour, userInput));
		} else {
			setField(FIELD_HOUR, Integer.parseInt(inputHour));
		}
	}
	
	// Parses dates in dd/mm, dd/mm/yy, dd/mm/yyy, dd/mm/yyyy format. E.g 12/5/16, 12/05/2016, 12/05.
	private boolean parseIfIsForwardSlashDateFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains(STRING_FORWARD_SLASH)) {
			String[] splitDate = input.split(STRING_FORWARD_SLASH);
			if (splitDate.length == DAY_MONTH_FORMAT_LENGTH) {
				return parseDayAndMonth(input);
			} else if (splitDate.length == DAY_MONTH_YEAR_FORMAT_LENGTH) {
				return parseDayMonthAndYear(input);
			} else {
				throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_FORWARD_SLASH_FORMAT, input));
			}
		}
		return false;
	}
	
	// E.g 12/5, 31/10
	private boolean parseDayAndMonth(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitDate = input.split(STRING_FORWARD_SLASH);
		String inputDay = splitDate[INDEX_DAY];
		String inputMonth = splitDate[INDEX_MONTH];
		parseDay(inputDay, input);
		parseMonth(inputMonth, input);
		return true;
	}
	
	// E.g 12/5/16, 31/10/2017
	private boolean parseDayMonthAndYear(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitDate = input.split(STRING_FORWARD_SLASH);
		String inputDay = splitDate[INDEX_DAY];
		String inputMonth = splitDate[INDEX_MONTH];
		String inputYear = splitDate[INDEX_YEAR];
		parseDay(inputDay, input);
		parseMonth(inputMonth, input);
		parseYear(inputYear, input);
		return true;
	}

	private void parseYear(String inputYear, String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputYear)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_YEAR, inputYear, input));
		} else {
			int intYear = Integer.parseInt(inputYear);
			if (intYear < MIN_4_DIGIT_YEAR) {
				intYear += OFFSET_NOT_4_DIGIT_YEAR;
			}
			if (isAfterCurrentYear(intYear)) {
				setField(FIELD_YEAR, intYear);
			} else {
				throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_YEAR_PAST, inputYear, input));
			}
		}
	}
	
	private boolean isAfterCurrentYear(int inputYear) {
		Calendar c = Calendar.getInstance();
		return inputYear >= c.get(Calendar.YEAR);
	}

	private void parseMonth(String inputMonth, String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputMonth) || !isValidMonth(inputMonth)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_MONTH, inputMonth, input));
		} else {
			setField(FIELD_MONTH, Integer.parseInt(inputMonth));
		}
	}

	private void parseDay(String inputDay, String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (!isANumber(inputDay)) {
			throw new InvalidDateTimeFieldException(String.format(ERROR_MESSAGE_PARSE_DAY, inputDay, input));
		} else {
			setField(FIELD_DAY, Integer.parseInt(inputDay));
		}
	}
	
	// Parses numbers with 2 or less digits. E.g "12" may, "7" april.
	private boolean parseIfIs2OrLessDigitFloatingNumber(String input) throws DuplicateDateTimeFieldException {
		if (isANumber(input) && input.length() <= 2) {
			setField(FIELD_DAY, Integer.parseInt(input));
			return true;
		}
		return false;
	}
	
	// Parses numbers with 4 digits. E.g (time) 1200, (time) 2359, (year) 2500. 
	private boolean parseIfIs4DigitFloatingNumber(String input) throws DuplicateDateTimeFieldException{
		if (input.length() == FORMAT_LENGTH_4_DIGIT && isANumber(input)) {
			String inputHour = input.substring(START_INDEX, INDEX_SEPARATE_4_DIGIT_HOUR_MINUTE); // Gets first 2 digits.
			String inputMinute = input.substring(INDEX_SEPARATE_4_DIGIT_HOUR_MINUTE, FORMAT_LENGTH_4_DIGIT); // Gets last 2 digits.
			if (isValidHour(inputHour) && isValidMinute(inputMinute)) {
				setField(FIELD_HOUR, Integer.parseInt(inputHour));
				setField(FIELD_MINUTE, Integer.parseInt(inputMinute));
				return true;
			} else { // If hour or minute in 4 digit input is invalid, then treat input as year.
				setField(FIELD_YEAR, Integer.parseInt(input));
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidMonth(String input) {
		int inputMonth = Integer.parseInt(input);
		return inputMonth >= MONTH_MIN && inputMonth <= MONTH_MAX;
	}
	
	private boolean isValidMinute(String input) {
		int inputInt = Integer.parseInt(input);
		return inputInt >= MINUTE_MIN && inputInt <= MINUTE_MAX;
	}
	
	private boolean isValidHour(String input) {
		int inputInt = Integer.parseInt(input);
		return inputInt >= HOUR_MIN && inputInt <= HOUR_MAX;
	}
	
	private boolean isANumber(String input) {
		if (input == EMPTY_STRING) {
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
}
