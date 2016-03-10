package org.jimple.planner;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Formatter {
	public ListOfMonths listOfMonths;

	public Formatter() {
		listOfMonths = new ListOfMonths();
	}

	public String formatTaskForDisplay(ArrayList<Task> list) {
		String formattedTasks = new String("");
		for (Task aTask : list) {
			formattedTasks += aTask.getTitle().concat("\n");
		}
		return null;
	}

	public String formatSearchString(ArrayList<String> searchResults) {
		String formattedResult = new String("");
		for (String result : searchResults) {
			formattedResult += result;
			formattedResult += "\n";
		}
		return formattedResult;
	}

	public String formatDateTime(Calendar unformattedCalendarDate) {
		String formattedDateTime = new String("");
		if (unformattedCalendarDate != null) {
			//formattedDateTime = Integer.toString(LocalDateTime.now().getYear());
			formattedDateTime = Integer.toString(unformattedCalendarDate.YEAR);
			formattedDateTime += checkMonth(unformattedCalendarDate.MONTH);
			formattedDateTime += checkDay(unformattedCalendarDate.DAY_OF_MONTH);
			formattedDateTime += checkTime(unformattedCalendarDate);
			return formattedDateTime;
		}
		return null;
	}

	public String checkMonth(Integer dateTime) {
		String formattedMonth = new String("");
		formattedMonth += "-";
		formattedMonth += listOfMonths.monthDigit(dateTime);
		formattedMonth += "-";
		return formattedMonth;
	}

	private String checkDay(Integer dateTime) {
		String formattedDay = new String("");
		if (isLessThanTen(dateTime)) {
			formattedDay += "0";
		}
		formattedDay += dateTime;
		formattedDay += "T";
		return formattedDay;
	}

	private boolean isLessThanTen(int dateTime) {
		if (dateTime < 10) {
			return true;
		}
		return false;
	}

	private boolean isDay(String dateTime) {
		try {
			if (Integer.parseInt(dateTime) - 1000 < 0) {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return false;
	}

	public String checkTime(Calendar dateTime) {
		String formattedHoursMinutes = new String("");
		formattedHoursMinutes += formatHour(dateTime.HOUR);
		formattedHoursMinutes += ":";
		formattedHoursMinutes += formatMinutes(dateTime.MINUTE);
		return formattedHoursMinutes;
	}

	private String formatHour(int time) {
		String formattedHour = new String("");
		if (isLessThanTen(time)) {
			formattedHour += "0";
		}
		formattedHour += time;
		return formattedHour;
	}

	private String formatMinutes(int time) {
		String formattedMinutes = new String("");
		if (isLessThanTen(time)) {
			formattedMinutes += "0";
		}
		formattedMinutes += time;
		return formattedMinutes;
	}


	public String testCheckMonth(int dateTime) {
		return checkMonth(dateTime);
	}

	public String testCheckDay(int dateTime) {
		return checkDay(dateTime);
	}

	public String testCheckTime(Calendar dateTime) {
		return checkTime(dateTime);
	}

	public String testFormatTime(Calendar unformattedDate) {
		return formatDateTime(unformattedDate);
	}

}