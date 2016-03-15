/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

package org.jimple.planner;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class TimeParser {
	
	private String[] monthStrings = {"january", "jan",
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
	
	private int[] monthNo = {0, 0,
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
	
	private String[] dayStrings = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
	
	private int[] dayNo = {1, 2, 3, 4, 5, 6, 7};
	
	private final int FIELD_NOT_SET_VALUE = -1;
	private final int FIELD_INVALID_VALUE = -2;
	
	private HashMap<String, Integer> calendarMonths = new HashMap<String, Integer>();
	private HashMap<String, Integer> calendarDays = new HashMap<String, Integer>();
	
	private int day = FIELD_NOT_SET_VALUE;
	private int month = FIELD_NOT_SET_VALUE;
	private int year = FIELD_NOT_SET_VALUE;
	private int hour = FIELD_NOT_SET_VALUE;
	private int minute = FIELD_NOT_SET_VALUE;
	
	private int daySecondary = FIELD_NOT_SET_VALUE;
	private int monthSecondary = FIELD_NOT_SET_VALUE;
	private int yearSecondary = FIELD_NOT_SET_VALUE;
	private int hourSecondary = FIELD_NOT_SET_VALUE;
	private int minuteSecondary = FIELD_NOT_SET_VALUE;
	
	private Calendar c = null;
	
	public TimeParser() {
		for (int i = 0; i < monthStrings.length; i++) {
			calendarMonths.put(monthStrings[i], monthNo[i]);
		}
		for (int i = 0; i < dayStrings.length; i++) {
			calendarDays.put(dayStrings[i], dayNo[i]);
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
			//} else if (parseIfIsTodayOrTomorrow(i)) {
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
			String date = parseIfLessThanTen(parsedDate.getDate());
			String month = parseIfLessThanTen(parsedDate.getMonth()+1);
			String hours = parseIfLessThanTen(parsedDate.getHours());
			String minutes = parseIfLessThanTen(parsedDate.getMinutes());
			String correctDate = (parsedDate.getYear()+1900) + "-"+  month + "-" + date + "T" + hours
			+ ":" + minutes;
			return correctDate;
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
		// Seconds preset to 0 (default).
		c.set(Calendar.SECOND, 0);
		if (isFieldSet("hour") && isFieldSet("minute")) {
			if (isAfterCurrentTime(getField("hour"), getField("minute"))) {
				setSecondaryField("day", c.get(Calendar.DAY_OF_MONTH));
				setSecondaryField("month", c.get(Calendar.MONTH) + 1);
				setSecondaryField("year", c.get(Calendar.YEAR));
			}
		} 
		switch (extendedCommand) {
			case "on" :
				setSecondaryField("hour", 00);
				setSecondaryField("minute", 00);
				break;
			case "by" :
				setSecondaryField("hour", 23);
				setSecondaryField("minute", 59);
				break;
				default :
					break;
		}
		return true;
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
	
	private boolean setSecondaryField(String inputField, int inputValue) {
		switch (inputField) {
			case "day" :
				daySecondary = inputValue;
				return true;
			case "month" :
				monthSecondary = inputValue;
				return true;
			case "year" :
				yearSecondary = inputValue;
				return true;
			case "hour" :
				hourSecondary = inputValue;
				return true;
			case "minute" :
				minuteSecondary = inputValue;
				return true;
			default :
				System.out.println("Invalid field for setting Date/Time");
				break;
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
	
	private String parseIfLessThanTen(int date) {
		if (isLessThanTen(date))	{
			return "0" + date;
		}
		return Integer.toString(date);
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
	
	private boolean isLessThanTen(int dateTime) {
		if (dateTime < 10) {
			return true;
		}
		return false;
	}
}
