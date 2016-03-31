/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jimple.planner.exceptions.DuplicateDateTimeFieldException;
import org.jimple.planner.exceptions.InvalidDateTimeFieldException;
import org.jimple.planner.exceptions.MissingDateTimeFieldException;

public class TimeParser {
	
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
	
	private String[] STRINGS_DAY = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
	
	private int[] VALUES_DAY = {1, 2, 3, 4, 5, 6, 7};
	
	private final int FIELD_NOT_SET_VALUE = -1;
	private final int FIELD_INVALID_VALUE = -2;
	
	private HashMap<String, Integer> calendarMonths = new HashMap<String, Integer>();
	private HashMap<String, Integer> calendarDays = new HashMap<String, Integer>();
	
	private int day = FIELD_NOT_SET_VALUE;
	private int month = FIELD_NOT_SET_VALUE;
	private int year = FIELD_NOT_SET_VALUE;
	private int hour = FIELD_NOT_SET_VALUE;
	private int minute = FIELD_NOT_SET_VALUE;
	
	private Calendar c = null;
	
	//private static Logger LOGGER = Logger.getLogger("TimeParser");
	//FileHandler fh;
	
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
	 */
	public Calendar parseTime(String extendedCommand, String input) throws Exception {
		resetTimeAndDate();
		String[] splitInput = input.split(" ");
		for (String i: splitInput) {
			if (parseIfIsDay(i)) {
			} else if (parseIfIsMonth(i)) {
			} else if (parseIfIsAMPMFormat(i)) {
			} else if (parseIfIsTodayOrTomorrow(i)) {
			} else if (parseIfIsColonTimeFormat(i)) {
			} else if (parseIfIsDotTimeFormat(i)) {
			} else if (parseIfIsForwardSlashDateFormat(i)) {
			} else if (parseIfIs4DigitFloatingNumber(i)) {
			} else if (parseIfIs2OrLessDigitFloatingNumber(i)) {
			} else {
				throw new InvalidDateTimeFieldException("Date/Time: \"" + i + "\" not recognised.");
			}
		}
		if (!formatCalendarIfValid(extendedCommand)) {
			return null;
		}
		return c;
	}

	private boolean formatCalendarIfValid(String extendedCommand) throws DuplicateDateTimeFieldException, MissingDateTimeFieldException {
		c = Calendar.getInstance();
		//System.out.println(isFieldSet("hour") + " " +  isFieldSet("minute") + isFieldSet("day") + isFieldSet("month") + isFieldSet("year"));
		initSecondaryAndPresetFields(extendedCommand);
		if (setCalendarField("hour", getField("hour"))) {
			if (setCalendarField("minute", getField("minute"))) {
				if (setCalendarField("year", getField("year"))) {
					if (setCalendarField("month", getField("month"))) {
						System.out.println("A" + c.getTime().getMonth());
						if (setCalendarField("day", getField("day"))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
		
	private boolean initSecondaryAndPresetFields(String extendedCommand) throws DuplicateDateTimeFieldException {
		if (isFieldSet("hour") && isFieldSet("minute") && !isFieldSet("day") && !isFieldSet("month") && !isFieldSet("year")) {
			c.setTimeInMillis(System.currentTimeMillis());
			if (!isAfterCurrentTime(getField("hour"), getField("minute"))) {
				c.add(Calendar.DATE, 1);
			}
			setField("day", c.get(Calendar.DAY_OF_MONTH));
			setField("month", c.get(Calendar.MONTH) + 1);
			setField("year", c.get(Calendar.YEAR));
		}
		if (!isFieldSet("hour") && !isFieldSet("minute")) {
			switch (extendedCommand) {
				case "ON" :
				case "AT" :
				case "FROM" :
					setField("hour", 00);
					setField("minute", 00);
					break;
				case "TO" :
				case "BY" :
					setField("hour", 23);
					setField("minute", 59);
					break;
				default :
					break;
			}
		}
		if (!isFieldSet("year")) {
			c.setTimeInMillis(System.currentTimeMillis());
			if (!isAfterCurrentDate(day, month)) {
				c.add(Calendar.YEAR, 1);
				setField("year", c.get(Calendar.YEAR));
			} else {
				setField("year", c.get(Calendar.YEAR));
			}
		}
		// Seconds preset to 0 (default).
		c.set(Calendar.SECOND, 0);
		return true;
	}
	
	private boolean isAfterCurrentDate(int inputDay, int inputMonth) {
		c.setTimeInMillis(System.currentTimeMillis());
		boolean case1 = inputMonth > c.get(Calendar.MONTH);
		boolean case2 = inputMonth == c.get(Calendar.MONTH) && inputDay >= c.get(Calendar.DAY_OF_MONTH);
		return case1 || case2;
	}
	
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
	private boolean setCalendarField(String inputField, int inputValue) throws MissingDateTimeFieldException {
		if (!isFieldSet(inputField)) {
			throw new MissingDateTimeFieldException(inputField + " not set.");
		} else {
			switch (inputField) {
				case "day" :
					c.set(Calendar.DAY_OF_MONTH, inputValue);
					return true;
				case "month" :
					c.set(Calendar.MONTH, inputValue-1);
					return true;
				case "year" :
					c.set(Calendar.YEAR, inputValue);
					return true;
				case "hour" :
					c.set(Calendar.HOUR_OF_DAY, inputValue);
					return true;
				case "minute" :
					c.set(Calendar.MINUTE, inputValue);
					return true;
				default :
					System.out.println("Invalid field for setting Date/Time");
					break;
			}
		}
		return false;
	}
	
	private boolean setField(String inputField, int inputValue) throws DuplicateDateTimeFieldException {
		if (isFieldSet(inputField)) {
			throw new DuplicateDateTimeFieldException(inputField + " cannot be set to " + inputValue + ". " + inputField + " already set to " + getField(inputField));
		} else {
			switch (inputField) {
				case "day" :
					day = inputValue;
					return true;
				case "month" :
					month = inputValue;
					return true;
				case "year" :
					year = inputValue;
					return true;
				case "hour" :
					hour = inputValue;
					return true;
				case "minute" :
					minute = inputValue;
					return true;
				default :
					System.out.println("Invalid field for setting Date/Time");
					break;
			}
		}
		return false;
	}
	
	private boolean isFieldSet(String inputField) {
		switch (inputField) {
			case "day" :
				return day != FIELD_NOT_SET_VALUE;
			case "month" :
				return month != FIELD_NOT_SET_VALUE;
			case "year" :
				return year != FIELD_NOT_SET_VALUE;
			case "hour" :
				return hour != FIELD_NOT_SET_VALUE;
			case "minute" :
				return minute != FIELD_NOT_SET_VALUE;
			default :
				System.out.println("Invalid field for setting Date/Time");
				return true;
		}
	}
	
	private int getField(String inputField) {
		switch (inputField) {
			case "day" :
				return day;
			case "month" :
				return month;
			case "year" :
				return year;
			case "hour" :
				return hour;
			case "minute" :
				return minute;
			default :
				System.out.println("Invalid field for setting Date/Time");
				return FIELD_INVALID_VALUE;
		}
	}
	
	/* --------|
	 * PARSERS |
	 * --------|
	 * The parsers detect different cases of date/time inputs which then stores the detected values into the proper date/time variable.
	 */
	
	// Parses days. E.g Monday, Wednesday, Sunday
	private boolean parseIfIsDay(String input) throws DuplicateDateTimeFieldException{
		if (calendarDays.containsKey(input)) {
			c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			int inputDayNo = calendarDays.get(input);
			while (c.get(Calendar.DAY_OF_WEEK) != inputDayNo) {
				c.add(Calendar.DATE, 1);
			}
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH) + 1);
			return true;
		}
		return false;
	}
	
	// Parses am and pm. E.g 12am, 5.30pm, 6:25pm.
	private boolean parseIfIsAMPMFormat(String input) throws DuplicateDateTimeFieldException, InvalidDateTimeFieldException {
		if (input.length() < 3) {
			return false;
		}
		String last2Characters = input.substring(input.length()-2, input.length()).toLowerCase();
		if (last2Characters.equals("am") || last2Characters.equals("pm")) {
			String beforeLast2Characters = input.substring(0, input.length()-2);
			if (beforeLast2Characters.contains(".")) {
				return parseDotAMPM(beforeLast2Characters, last2Characters);
			} else if (beforeLast2Characters.contains(":")) {
				return parseColonAMPM(beforeLast2Characters, last2Characters);
			} else if (isANumber(beforeLast2Characters)){
				return parseFloatingNumberAMPM(beforeLast2Characters, last2Characters);
			} else {
				throw new InvalidDateTimeFieldException("Invalid time AM/PM format: \"" + input + "\". Type \"help\" to see the accepted formats.");
			}
		}
		return false;
	}
	
	private boolean parseDotAMPM(String input, String inputAMPM) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitTime = input.split("\\.");
		if (splitTime.length == 2) {
			String inputHour = splitTime[0];
			String inputMinute = splitTime[1];
			if (!isANumber(inputHour) || !isValidAMPMHour(inputHour)) {
				throw new InvalidDateTimeFieldException("Invalid input: Hour \"" + inputHour + "\" of \"" + input + "\" Please input a valid AM/PM time for hour.");
			} else {
				switch (inputAMPM) {
					case "am" :
						setField("hour", Integer.parseInt(inputHour) % 12);
						break;
					case "pm" :
						setField("hour", (Integer.parseInt(inputHour) % 12) + 12);
						break;
				}
			}
			if (!isANumber(inputMinute) || !isValidMinute(inputMinute)) {
				throw new InvalidDateTimeFieldException("Invalid input: Minute \"" + inputMinute + "\" of \"" + input + "\" Please input a valid time for minute.");
			} else {
				setField("minute", Integer.parseInt(inputMinute));
			}
			return true;
		}
		throw new InvalidDateTimeFieldException("Invalid input: \"" + input + "\". Time should be formated to hh.mm before am/pm.");
	}
	
	private boolean parseColonAMPM(String input, String inputAMPM) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitTime = input.split(":");
		if (splitTime.length == 2) {
			String inputHour = splitTime[0];
			String inputMinute = splitTime[1];
			if (!isANumber(inputHour) || !isValidAMPMHour(inputHour)) {
				throw new InvalidDateTimeFieldException("Invalid input: Hour \"" + inputHour + "\" of \"" + input + "\" Please input a valid AM/PM time for hour.");
			} else {
				switch (inputAMPM) {
					case "am" :
						setField("hour", Integer.parseInt(inputHour) % 12);
						break;
					case "pm" :
						setField("hour", (Integer.parseInt(inputHour) % 12) + 12);
						break;
				}
			}
			if (!isANumber(inputMinute) || !isValidMinute(inputMinute)) {
				throw new InvalidDateTimeFieldException("Invalid input: Minute \"" + inputMinute + "\" of \"" + input + "\" Please input a valid time for minute.");
			} else {
				setField("minute", Integer.parseInt(inputMinute));
			}
			return true;
		}
		throw new InvalidDateTimeFieldException("Invalid input: \"" + input + "\". Time should be formated to hh.mm before am/pm.");
	}
	
	private boolean parseFloatingNumberAMPM(String input, String inputAMPM) throws DuplicateDateTimeFieldException, InvalidDateTimeFieldException {
		if (isANumber(input) && isValidAMPMHour(input)) {
			int inputTime = Integer.parseInt(input);
			switch (inputAMPM) {
				case "am" :
					setField("hour", inputTime % 12);
					setField("minute", 0);
					return true;
				case "pm" :
					setField("hour", 12 + (inputTime % 12));
					setField("minute", 0);
					return true;
				default:
					break;
			}
			return true;
		} else {
			throw new InvalidDateTimeFieldException("Invalid input: Hour \"" + input + "\" of \"" + input + inputAMPM + "\" Please input a valid time for AM/PM.");
		}
	}
	
	private boolean isValidAMPMHour(String input) {
		int inputHour = Integer.parseInt(input);
		return inputHour > 0 && inputHour <= 12;
	}
	
	// Parses the words "today" and "tomorrow".
	private boolean parseIfIsTodayOrTomorrow(String input) throws DuplicateDateTimeFieldException{
		c = Calendar.getInstance();
		switch (input.toLowerCase()) {
		case "today" :
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH) + 1);
			setField("hour", c.get(Calendar.HOUR_OF_DAY));
			setField("minute", c.get(Calendar.MINUTE));
			return true;
		case "tomorrow" :
			c.add(Calendar.DATE, 1);
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH) + 1);
			return true;
		default :
			break;
		}
		return false;
	}

	// Parses months. E.g February, May, December.
	private boolean parseIfIsMonth(String input) throws DuplicateDateTimeFieldException {
		if (calendarMonths.containsKey(input)) {
			setField("month", calendarMonths.get(input));
			return true;
		}
		return false;
	}

	// Parses time in hh:mm format. E.g 00:00, 12:34, 23:59.
	private boolean parseIfIsColonTimeFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains(":")) {
			String[] splitTime = input.split(":");
			if (splitTime.length == 2) {
				String inputHour = splitTime[0];
				String inputMinute = splitTime[1];
				if (!isANumber(inputHour) || !isValidHour(inputHour)) {
					throw new InvalidDateTimeFieldException("Invalid input: Hour \"" + inputHour + "\" of \"" + input + "\" Please input a valid time for hour.");
				} else {
					setField("hour", Integer.parseInt(inputHour));
				}
				if (!isANumber(inputMinute) || !isValidMinute(inputMinute)) {
					throw new InvalidDateTimeFieldException("Invalid input: Minute \"" + inputMinute + "\" of \"" + input + "\" Please input a valid time for minute.");
				} else {
					setField("minute", Integer.parseInt(inputMinute));
				}
				return true;
			} else {
				throw new InvalidDateTimeFieldException("Invalid input: " + input + ".Time should be written in \"hh:mm\" format.");
			}
		}
		return false;
	}
	
	private boolean parseIfIsDotTimeFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains("\\.")) {
			String[] splitTime = input.split("\\.");
			System.out.println(splitTime.length);
			if (splitTime.length == 2) {
				String inputHour = splitTime[0];
				String inputMinute = splitTime[1];
				if (!isANumber(inputHour) || !isValidHour(inputHour)) {
					throw new InvalidDateTimeFieldException("Invalid input: Hour \"" + inputHour + "\" of \"" + input + "\" Please input a valid time for hour.");
				} else {
					setField("hour", Integer.parseInt(inputHour));
				}
				if (!isANumber(inputMinute) || !isValidMinute(inputMinute)) {
					throw new InvalidDateTimeFieldException("Invalid input: Minute \"" + inputMinute + "\" of \"" + input + "\" Please input a valid time for minute.");
				} else {
					setField("minute", Integer.parseInt(inputMinute));
				}
				return true;
			} else {
				throw new InvalidDateTimeFieldException("Invalid input: " + input + ". Time should be written in \"hh:mm\" format.");
			}
		}
		return false;
	}
	
	// Parses dates in dd/mm, dd/mm/yy, dd/mm/yyy, dd/mm/yyyy format. E.g 12/5/16, 12/05/2016, 12/05.
	private boolean parseIfIsForwardSlashDateFormat(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		if (input.contains("/")) {
			String[] splitDate = input.split("/");
			if (splitDate.length == 2) {
				return parseDayAndMonth(input);
			} else if (splitDate.length == 3) {
				return parseDayMonthAndYear(input);
			} else {
				throw new InvalidDateTimeFieldException("Invalid input: " + input + ".Time should be written in \"dd/mm\" or \"dd/mm/yyyy\" format.");
			}
		}
		return false;
	}
	
	private boolean parseDayAndMonth(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitDate = input.split("/");
		String inputDay = splitDate[0];
		String inputMonth = splitDate[1];
		if (!isANumber(inputDay)) {
			throw new InvalidDateTimeFieldException("Invalid input: Day \"" + inputDay + "\" of \"" + input + "\" Please input a valid time for day.");
		} else {
			setField("day", Integer.parseInt(inputDay));
		}
		if (!isANumber(inputMonth) || !isValidMonth(inputMonth)) {
			throw new InvalidDateTimeFieldException("Invalid input: Month \"" + inputMonth + "\" of \"" + input + "\" Please input a valid time for month.");
		} else {
			setField("month", Integer.parseInt(inputMonth));
		}
		return true;
	}
	
	private boolean parseDayMonthAndYear(String input) throws InvalidDateTimeFieldException, DuplicateDateTimeFieldException {
		String[] splitDate = input.split("/");
		String inputDay = splitDate[0];
		String inputMonth = splitDate[1];
		String inputYear = splitDate[2];
		if (!isANumber(inputDay)) {
			throw new InvalidDateTimeFieldException("Invalid input: Day \"" + inputDay + "\" of \"" + input + "\" Please input a valid time for day.");
		} else {
			setField("day", Integer.parseInt(inputDay));
		}
		if (!isANumber(inputMonth) || !isValidMonth(inputMonth)) {
			throw new InvalidDateTimeFieldException("Invalid input: Month \"" + inputMonth + "\" of \"" + input + "\" Please input a valid time for month.");
		} else {
			setField("month", Integer.parseInt(inputMonth));
		}
		if (!isANumber(inputYear)) {
			throw new InvalidDateTimeFieldException("Invalid input: Year \"" + inputYear + "\" of \"" + input + "\" Please input a valid time for year.");
		} else {
			int intYear = Integer.parseInt(inputYear);
			if (isAfterCurrentYear(intYear)) {
				setField("year", intYear);
			} else {
				if (year > 999) {
					setField("year", 2000 + intYear);
				} else {
					throw new InvalidDateTimeFieldException("Invalid input: Year \"" + inputYear + "\" of \"" + input + "\" is before the current year.");
				}
			}
			month = Integer.parseInt(inputMonth);
		}
		return true;
	}
	
	private boolean isValidMonth(String input) {
		int inputMonth = Integer.parseInt(input);
		return inputMonth > 0 && inputMonth <= 12;
	}
	
	private boolean isValidMinute(String input) {
		int inputInt = Integer.parseInt(input);
		return inputInt >= 0 && inputInt < 60;
	}
	
	private boolean isValidHour(String input) {
		int inputInt = Integer.parseInt(input);
		return inputInt >= 0 && inputInt < 24;
	}
	
	private boolean isAfterCurrentYear(int inputYear) {
		Calendar c = Calendar.getInstance();
		return inputYear > c.get(Calendar.YEAR);
	}
	
	// Parses numbers with 2 or less digits. E.g "12" may, "7" april.
	private boolean parseIfIs2OrLessDigitFloatingNumber(String input) throws DuplicateDateTimeFieldException {
		if (isANumber(input) && input.length() <= 2) {
			setField("day", Integer.parseInt(input));
			return true;
		}
		return false;
	}
	
	// Parses numbers with 4 digits. E.g (time) 1200, (time) 2359, (year) 2500. 
	private boolean parseIfIs4DigitFloatingNumber(String input) throws DuplicateDateTimeFieldException{
		if (input.length() == 4 && isANumber(input)) {
			int inputNumber = Integer.parseInt(input);
			if (!isValid24HourInput(inputNumber)) {
				setField("year", inputNumber);
			} else {
				int inputHour = inputNumber / 100;
				int inputMinute = inputNumber % 100;
				setField("hour", inputHour);
				setField("minute", inputMinute);
				return true;
			}
		}
		return false;
	}
	
	private boolean isValid24HourInput(int inputNo) {
		return inputNo >= 0 && inputNo < 2400;
	}
	
	private boolean isANumber(String input) {
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
}
