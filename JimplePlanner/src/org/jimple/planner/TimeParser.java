/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
	
	private int[] VALUES_MONTH = {0, 0,
			1, 1,
			2, 2,
			3, 3,
			4,
			5, 5,
			6, 6,
			7, 7,
			8, 8,
			9, 9,
			10, 10,
			11, 11};
	
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
	
	public TimeParser() {
		for (int i = 0; i < STRINGS_MONTH.length; i++) {
			calendarMonths.put(STRINGS_MONTH[i], VALUES_MONTH[i]);
		}
		for (int i = 0; i < STRINGS_DAY.length; i++) {
			calendarDays.put(STRINGS_DAY[i], VALUES_DAY[i]);
		}
	}
	
	/* -------------------|
	 * MAIN PARSER METHOD |
	 * -------------------|
	 */
	public String timeParser(String extendedCommand, String input) {
		resetTimeAndDate();
		String[] splitInput = input.split(" ");
		for (String i: splitInput) {
			if (parseIfIsDay(i)) {
			} else if (parseIfIsMonth(i)) {
			} else if (parseIfIsAMPMFormat(i)) {
			} else if (parseIfIsTodayOrTomorrow(i)) {
			} else if (parseIfIs4DigitFloatingNumber(i)) {
			} else if (parseIfIs2OrLessDigitFloatingNumber(i)) {
			} else {
				System.out.println("Date/Time: " + i + " not recognised.");
				return null;
			}
		}
		if (!formatCalendarIfValid(extendedCommand)) {
			System.out.println("Invalid time.");
			return null;
		}
		return calendarToStringFormat();
	}
	
	private String calendarToStringFormat() {
		if (c != null) {
			Date parsedDate = c.getTime();
			String date = String.format("%02d", parsedDate.getDate());
			String month = String.format("%02d", parsedDate.getMonth()+1);
			String hours = String.format("%02d", parsedDate.getHours());
			String minutes = String.format("%02d", parsedDate.getMinutes());
			String outputDate = (parsedDate.getYear()+1900) + "-"+  month + "-" + date + "T" + hours
			+ ":" + minutes;
			return outputDate;
		}
		return null;
	}

	private boolean formatCalendarIfValid(String extendedCommand) {
		c = Calendar.getInstance();
		initSecondaryAndPresetFields(extendedCommand);
		if (setCalendarField("hour", getField("hour"))) {
			if (setCalendarField("minute", getField("minute"))) {
				if (setCalendarField("year", getField("year"))) {
					if (setCalendarField("month", getField("month"))) {
						if (setCalendarField("day", getField("day"))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
		
	private boolean initSecondaryAndPresetFields(String extendedCommand) {
		if (isFieldSet("hour") && isFieldSet("minute") && !isFieldSet("day") && !isFieldSet("month") && !isFieldSet("year")) {
			c.setTimeInMillis(System.currentTimeMillis());
			if (isAfterCurrentTime(getField("hour"), getField("minute"))) {
				c.add(Calendar.DATE, 1);
			}
			setField("day", c.get(Calendar.DAY_OF_MONTH));
			setField("month", c.get(Calendar.MONTH));
			setField("year", c.get(Calendar.YEAR));
		} else if (!isFieldSet("hour") && !isFieldSet("minute") && isFieldSet("day") && isFieldSet("month")) {
			System.out.println("A");
			switch (extendedCommand) {
				case "from" :
				case "at" :
					setField("hour", 00);
					setField("minute", 00);
					break;
				case "on" :
				case "by" :
				case "to" :
					setField("hour", 23);
					setField("minute", 59);
					break;
				default :
					break;
			}
			if (!isFieldSet("year")) {
				c.setTimeInMillis(System.currentTimeMillis());
				if (!isAfterCurrentDate(day, month)) {
					c.add(Calendar.YEAR, 1);
				}
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
		boolean case2 = inputMonth == c.get(Calendar.MONTH) && inputDay > c.get(Calendar.DAY_OF_MONTH);
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
	private boolean setCalendarField(String inputField, int inputValue) {
		if (!isFieldSet(inputField)) {
			System.out.println(inputField + " not set.");
		} else {
			switch (inputField) {
				case "day" :
					c.set(Calendar.DAY_OF_MONTH, inputValue);
					return true;
				case "month" :
					c.set(Calendar.MONTH, inputValue);
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
	
	private boolean setField(String inputField, int inputValue) {
		if (isFieldSet(inputField)) {
			System.out.println(inputField + " cannot be set to " + inputValue + ". " + inputField + " already set to " + getField(inputField));
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
				return false;
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
	 */
	private boolean parseIfIsDay(String input) {
		if (calendarDays.containsKey(input)) {
			c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			int inputDayNo = calendarDays.get(input);
			while (c.get(Calendar.DAY_OF_WEEK) != inputDayNo) {
				c.add(Calendar.DATE, 1);
			}
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH));
			setField("hour", 23);
			setField("minute", 59);
			return true;
		}
		return false;
	}
	
	private boolean parseIfIsAMPMFormat(String input) {
		if (input.length() > 2) {
			String last2Characters = input.substring(input.length()-2, input.length()).toLowerCase();
			String beforeLast2Characters = input.substring(0, input.length()-2);
			if (isANumber(beforeLast2Characters)) {
				int inputTime = Integer.parseInt(beforeLast2Characters);
				if (isValidAMPMNumberInput(inputTime)) {
					switch (last2Characters) {
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
				}
			}
		}
		return false;
	}
	
	private boolean parseIfIsTodayOrTomorrow(String input) {
		c = Calendar.getInstance();
		switch (input.toLowerCase()) {
		case "today" :
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH));
			return true;
		case "tomorrow" :
			c.add(Calendar.DATE, 1);
			setField("day", c.get(Calendar.DATE));
			setField("month", c.get(Calendar.MONTH));
			return true;
		default :
			break;
		}
		return false;
	}
	
	private boolean isValidAMPMNumberInput(int inputNo) {
		return inputNo > 0 && inputNo <= 12;
	}
	
	private boolean parseIfIsMonth(String input) {
		if (calendarMonths.containsKey(input)) {
			setField("month", calendarMonths.get(input));
			return true;
		}
		return false;
	}
	
	private boolean parseIfIs2OrLessDigitFloatingNumber(String input) {
		if (isANumber(input) && input.length() <= 2) {
				setField("day", Integer.parseInt(input));
				return true;
		}
		return false;
	}
	
	private boolean parseIfIs4DigitFloatingNumber(String input) {
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
