package org.jimple.planner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

class TimeParser {
	
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
	
	private HashMap<String, Integer> calendarMonths = new HashMap<String, Integer>();
	private HashMap<String, Integer> calendarDays = new HashMap<String, Integer>();
	
	private static int day = -1;
	private static int month = -1;
	private static int year = -1;
	private static int hour = -1;
	private static int minute = -1;
	
	private Calendar calendar= null;
	
	public TimeParser() {
		for (int i = 0; i < monthStrings.length; i++) {
			calendarMonths.put(monthStrings[i], monthNo[i]);
		}
		for (int i = 0; i < dayStrings.length; i++) {
			calendarDays.put(dayStrings[i], dayNo[i]);
		}
	}
	
	public Calendar timeParser(String input) {
		resetTimeAndDate();
		String[] splitInput = input.split(" ");
		for (String time: splitInput) {
			parseIfIsDay(time);
			parseIfIsAMPMFormat(time);
			parseIfIsMonth(time);
			parseIfIsFloatingNumber(time);
			parseIfIs4DigitTimeFormat(time);
		}
		if (!formatCalendarIfValid()) {
			return null;
		}
		return returnFunction();
	}
	
	private Calendar returnFunction() {
		if (calendar != null) {
			return calendar;
		}
		System.out.println("Anyhow input isit?");
		return null;
	}
	
	private boolean formatCalendarIfValid() {
		calendar = Calendar.getInstance();
		boolean hasDay = false;
		boolean hasMonth = false;
		for (int i = 0; i < 5; i++) {
			switch (i) {
				case 0 :
					if (day != -1) {
						calendar.set(calendar.DAY_OF_MONTH, day);
						hasDay = true;
					} else {
						return false;
					}
					break;
				case 1 :
					if (month != -1) {
						hasMonth = true;
						calendar.set(calendar.MONTH, month);
					} else {
						return false;
					}
					break;
				case 2 :
					if (hour != -1) {
						calendar.set(calendar.HOUR_OF_DAY, hour);
					} else if (hasDay && hasMonth) {
						calendar.set(calendar.HOUR_OF_DAY, 23);
					} else {
						return false;
					}
					break;
				case 3 :
					if (minute != -1) {
						calendar.set(calendar.MINUTE, minute);
					} else if (hasDay && hasMonth) {
						calendar.set(calendar.MINUTE, 59);
					} else {
						return false;
					}
					break;
				case 4 :
					calendar.set(calendar.YEAR, 2016);
					calendar.set(calendar.SECOND, 0);
					break;
				default :
					break;
			}
		}
		return true;
	}
	
	private boolean resetTimeAndDate() {
		day = -1;
		month = -1;
		hour = -1;
		minute = -1;
		return true;
	}
	
	private boolean parseIfIsDay(String input) {
		if (calendarDays.containsKey(input)) {
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			int inputDayNo = calendarDays.get(input);
			while (calendar.getTime().getDay() != inputDayNo - 1) {
				calendar.add(calendar.DATE, 1);
			}
			day = calendar.getTime().getDate();
			month = calendar.getTime().getMonth();
			hour = 23;
			minute = 59;
			return true;
		}
		return false;
	}
	
	private boolean parseIfIsAMPMFormat(String input) {
		if (input.length() == 3 || input.length() == 4) {
			String first2Characters = input.substring(0, input.length()-2);
			String last2Characters = input.substring(input.length()-2, input.length()).toLowerCase();
			if (isANumber(first2Characters)) {
				int inputTime = Integer.parseInt(first2Characters);
				switch (last2Characters) {
					case "am" :
						hour = inputTime;
						minute = 0;
						return true;
					case "pm" :
						hour = 12 + inputTime;
						minute = 0;
						return true;
					default:
						return false;
				}
			}
		}
		return false;
	}
	
	private boolean parseIfIsMonth(String input) {
		if (calendarMonths.containsKey(input)) {
			month = calendarMonths.get(input);
			return true;
		}
		return false;
	}
	
	private boolean parseIfIsFloatingNumber(String input) {
		if (isANumber(input)) {
			if (input.length() <= 2) {
				if (day == -1) {
					day = Integer.parseInt(input);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean parseIfIs4DigitTimeFormat(String input) {
		if (input.length() == 4) {
			if (isANumber(input)) {
				String first2Characters = input.substring(0, 2);
				String last2Characters = input.substring(input.length()-2, input.length());
				int inputHour = Integer.parseInt(first2Characters);
				int inputMinute = Integer.parseInt(last2Characters);
				if (inputHour >= 0 && inputHour < 24) {
					hour = inputHour;
					if (inputMinute >= 0 && inputMinute < 60) {
						minute = inputMinute;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isANumber(String input) {
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
