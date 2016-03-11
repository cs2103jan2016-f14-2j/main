package org.jimple.planner;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Formatter {
	public ListOfMonths listOfMonths;

	public Formatter() {
		listOfMonths = new ListOfMonths();
	}

	public String formatPrettyDate(LocalDateTime dateTime) {
		String prettyDateTime = new String("");
		prettyDateTime += dateTime.getDayOfMonth();
		prettyDateTime += "/";
		prettyDateTime += dateTime.getMonthValue();
		prettyDateTime += "/";
		prettyDateTime += dateTime.getYear();
		prettyDateTime += "\n";
		prettyDateTime += dateTime.getHour();
		prettyDateTime += ":";
		prettyDateTime += dateTime.getMinute();

		return prettyDateTime;
	}

	public String newFormatDateTime(Date date) {
		String formattedDateTime = new String("");
		formattedDateTime += (date.getYear() + 1900);
		formattedDateTime += newFormatMonth(date.getMonth());
		formattedDateTime += newFormatDay(date.getDay() + 1);
		formattedDateTime += newFormatTime(date);
		return formattedDateTime;
	}

	private String newFormatTime(Date time) {
		String formattedHoursMinutes = new String("");
		formattedHoursMinutes += formatTime(time.getHours());
		formattedHoursMinutes += ":";
		formattedHoursMinutes += formatTime(time.getMinutes());
		return formattedHoursMinutes;
	}

	private String formatTime(int time) {
		String formattedTime = new String("");
		if (isLessThanTen(time)) {
			formattedTime += "0";
		}
		formattedTime += time;
		return formattedTime;
	}

	private String newFormatDay(int day) {
		String formattedDay = new String("");
		if (isLessThanTen(day)) {
			formattedDay += "0";
		}
		formattedDay += day;
		formattedDay += "T";

		return formattedDay;
	}

	private String newFormatMonth(int month) {
		String formattedMonth = new String("");
		formattedMonth += "-";
		formattedMonth += listOfMonths.newMonthDigit(month);
		formattedMonth += "-";
		return formattedMonth;
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

	public String formatDateTime(String unformattedDate) {
		if (unformattedDate != null) {
			String[] dividedDates = unformattedDate.split(" ");
			String formattedDateTime = Integer.toString(LocalDateTime.now().getYear());
			for (String dateTime : dividedDates) {
				if (checkYear(dateTime) != null) {
					formattedDateTime = checkYear(dateTime);
					break;
				}
			}
			formattedDateTime += checkMonth(
					LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
			for (String dateTime : dividedDates) {
				if (!checkMonth(dateTime).equals("")) {
					formattedDateTime = formattedDateTime.substring(0, formattedDateTime.length() - 4)
							.concat(checkMonth(dateTime));
					break;
				}
			}
			formattedDateTime += checkDay(Integer.toString(LocalDateTime.now().getDayOfMonth()));
			for (String dateTime : dividedDates) {
				if (!checkDay(dateTime).equals("")) {
					formattedDateTime = formattedDateTime.substring(0, formattedDateTime.length() - 3)
							.concat(checkDay(dateTime));
					break;
				}
			}
			for (String dateTime : dividedDates) {
				if (!checkTime(dateTime).equals("")) {
					formattedDateTime += checkTime(dateTime);
					break;
				}
			}
			return formattedDateTime;
		}
		return unformattedDate;
	}

	private String checkYear(String dateTime) {
		if (isYear(dateTime)) {
			return dateTime;
		}
		return null;
	}

	private boolean isYear(String dateTime) {
		try {
			if (Integer.parseInt(dateTime) - 1000 > 0) {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return false;
	}

	public String checkMonth(String dateTime) {
		String formattedMonth = new String("");
		if (listOfMonths.contain(dateTime)) {
			formattedMonth += "-";
			formattedMonth += listOfMonths.monthDigit(dateTime);
			formattedMonth += "-";
		}
		return formattedMonth;
	}

	private String checkDay(String dateTime) {
		String formattedDay = new String("");
		if (isDay(dateTime)) {
			if (isLessThanTen(Integer.parseInt(dateTime))) {
				formattedDay += "0";
			}
			formattedDay += dateTime;
			formattedDay += "T";
		}
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

	public String checkTime(String dateTime) {
		String formattedHoursMinutes = new String("");
		if (dateTime.contains("am")) {
			dateTime = dateTime.substring(0, dateTime.length() - 2);
			String[] time = dateTime.split("\\.");
			formattedHoursMinutes += formatHourAM(time);
			formattedHoursMinutes += ":";
			formattedHoursMinutes += formatMinutes(time);
		} else if (dateTime.contains("pm")) {
			dateTime = dateTime.substring(0, dateTime.length() - 2);
			String[] time = dateTime.split("\\.");
			formattedHoursMinutes += formatHourPM(time);
			formattedHoursMinutes += ":";
			formattedHoursMinutes += formatMinutes(time);
		}
		return formattedHoursMinutes;
	}

	private String formatHourPM(String[] time) {
		String formattedHour = new String("");
		if (time[0].equals("12")) {
			formattedHour = time[0];
		} else {
			formattedHour += Integer.parseInt(time[0]) + 12;
		}
		return formattedHour;
	}

	private String formatHourAM(String[] time) {
		String formattedHour = new String("");
		if (isLessThanTen(Integer.parseInt(time[0]))) {
			formattedHour += "0";
		}
		if (time[0].equals("12")) {
			formattedHour += "00";
		} else {
			formattedHour += time[0];
		}
		return formattedHour;
	}

	private String formatMinutes(String[] time) {
		String formattedMinutes = new String("");
		if (time.length > 1) {
			if (isLessThanTen(Integer.parseInt(time[1]))) {
				formattedMinutes += "0";
			}
			formattedMinutes += time[1];
		} else {
			formattedMinutes += "00";
		}
		return formattedMinutes;
	}

	public String testCheckYear(String dateTime) {
		return checkYear(dateTime);
	}

	public String testCheckMonth(String dateTime) {
		return checkMonth(dateTime);
	}

	public String testCheckDay(String dateTime) {
		return checkDay(dateTime);
	}

	public String testCheckTime(String dateTime) {
		return checkTime(dateTime);
	}

	public String testFormatTime(String unformattedDate) {
		return formatDateTime(unformattedDate);
	}

}