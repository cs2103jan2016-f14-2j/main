package org.jimple.planner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

class Event {
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;

	// Constructors
	public Event(String aTitle) {
		this.title = aTitle;
		this.description = new String("");
		this.category = new String("");
		this.fromDateTime = null;
		this.toDateTime = null;
	}

	public LocalDateTime getLocalFromTime() {
		return fromDateTime;
	}

	public LocalDateTime getLocalToTime() {
		return toDateTime;
	}

	public String getFromTime() {
		if (fromDateTime == null) {
			return "";
		}
		return fromDateTime.toString();
	}

	public String getToTime() {
		if (toDateTime == null) {
			return "";
		}
		return toDateTime.toString();
	}

	public void setFromDate(String dateTime) {
		if (dateTime == null) {
			fromDateTime = null;
		} else {
			this.fromDateTime = LocalDateTime.parse(dateTime);
		}
	}

	public void setToDate(String dateTime) {
		if (dateTime == null) {
			fromDateTime = null;
		} else {
			this.toDateTime = LocalDateTime.parse(dateTime);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
		} else {
			this.title = title;
		}
	}

	public String getDescription() {
		if (description == null) {
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
		} else {
			this.description = description;
		}
	}

	public String getCategory() {
		if (category == null) {
			return "";
		}
		return category;
	}

	public void setCategory(String category) {
		if (category == null) {
		} else {
			this.category = category;
		}
	}

}

class ListOfMonths	{
	private HashMap<String, String> listOfMonths;
	
	public ListOfMonths()	{
		listOfMonths = new HashMap<String , String>();
		listOfMonths.put("january", "01");
		listOfMonths.put("february", "02");
		listOfMonths.put("march", "03");
		listOfMonths.put("april", "04");
		listOfMonths.put("may", "05");
		listOfMonths.put("june", "06");
		listOfMonths.put("july", "07");
		listOfMonths.put("august", "08");
		listOfMonths.put("september", "09");
		listOfMonths.put("october", "10");
		listOfMonths.put("november", "11");
		listOfMonths.put("december", "12");
	}
	
	public boolean contain(String month)	{
		return listOfMonths.containsKey(month.toLowerCase());
	}
	
	public String monthDigit(String month)	{
		return listOfMonths.get(month.toLowerCase());
	}
}

class timeComparator implements Comparator<Event> {
	public int compare(Event i, Event j) {
		if (i.getLocalFromTime() != null && j.getLocalFromTime() != null) {
			if (i.getLocalFromTime().compareTo(j.getLocalFromTime()) < 0) {
				return 1;
			} else if (i.getLocalFromTime().compareTo(j.getLocalFromTime()) > 0) {
				return -1;
			}
			return 0;
		}
		else if (i.getLocalToTime() != null && j.getLocalToTime() != null)	{
			if (i.getLocalToTime().compareTo(j.getLocalToTime()) < 0) {
				return 1;
			} else if (i.getLocalToTime().compareTo(j.getLocalToTime()) > 0) {
				return -1;
			}
			return 0;
		}
		else if (i.getLocalToTime() != null){
			if (i.getLocalToTime().compareTo(j.getLocalFromTime()) < 0) {
				return 1;
			} else if (i.getLocalToTime().compareTo(j.getLocalFromTime()) > 0) {
				return -1;
			}
			return 0;
		}
		else if (i.getLocalFromTime() != null){
			if (i.getLocalFromTime().compareTo(j.getLocalToTime()) < 0) {
				return 1;
			} else if (i.getLocalFromTime().compareTo(j.getLocalToTime()) > 0) {
				return -1;
			}
			return 0;
		}
		return -2;
	}
}

public class Logic {

	private String ADD_HELP_HEADER = "Add a new task:\n";
	private String EDIT_HELP_HEADER = "Edit a current task:\n";
	private String DISPLAY_HELP_HEADER = "Display all tasks:\n";
	private String DELETE_HELP_HEADER = "Delete a task:\n";

	private String ADD_COMMAND_BY = "type \"add\" + <your event> by <time>\n";
	private String ADD_COMMAND_AT = "type \"add\" + <your event> at <time>\n";
	private String ADD_COMMAND_FROMTO = "type \"add\" + <your event> from <time> to <time>\n";
	private String EDIT_COMMAND_ONE_TIMING = "type \"edit\" + <your event> to <time>\n";
	private String EDIT_COMMAND_TWO_TIMINGS = "type \"edit\" + <your event> from <time> to <time>\n";
	private String DISPLAY_COMMAND = "type \"display\"";
	private String DELETE_COMMAND = "type \"delete\" <event name>";

	private String ADDED_FEEDBACK = "task added to planner";
	private String EDITED_FEEDBACK = "task edited in planner";
	private String DELETED_FEEDBACK = "task deleted";

	private String ERROR_EDIT_FEEDBACK = "task not found";
	private String ERROR_ADDED_FEEDBACK = "could not add to task list";
	private String ERROR_FILE_NOT_FOUND = "could not find file";
	private String ERROR_DELETED_FEEDBACK = "task not found";

	private ArrayList<Event> temporaryHistory;
	private ArrayList<Event> currentListOfTasksInFile;
	private ListOfMonths listOfMonths;
	Parser parser = new Parser();
	Storage store = new Storage();

	public Logic() {
		temporaryHistory = new ArrayList<Event>();
		listOfMonths = new ListOfMonths();
		
		try {
			currentListOfTasksInFile = store.getEvents();
		} catch (IOException e) {
			System.out.print(ERROR_FILE_NOT_FOUND);
		}
	}

	/**
	 * function is for the UI to call when a user inputs a string
	 *
	 */
	public String execute(String inputString) throws IOException {
		String feedback = new String("");
		InputStruct parsedInput = parser.parseInput(inputString);
		switch (parsedInput.commandString) {
		case "delete":
			feedback += deleteTask(parsedInput.variableArray);
			break;
		case "add":
			feedback += addToTaskList(parsedInput.variableArray);
			break;
		case "edit":
			feedback += editTask(parsedInput.variableArray);
			break;
		}
		return feedback;
	}

	// adds task into the Event object
	public String addToTaskList(String[] parsedInput) throws IOException {
		Event newTask = new Event(parsedInput[0]);
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != "") {
				switch (i) {
				case 1:
					newTask.setDescription(parsedInput[i]);
					break;
				case 2:
					String formattedFromDate = formatTime(parsedInput[i]);
					newTask.setFromDate(formattedFromDate);
					break;
				case 3:
					String formattedToDate = formatTime(parsedInput[i]);
					newTask.setToDate(formattedToDate);
					break;
				case 4:
					newTask.setCategory(parsedInput[i]);
					break;
				default:
					break;
				}
			}
		}
		temporaryHistory.add(newTask);
		currentListOfTasksInFile.add(newTask);
		if (store.isSaved(currentListOfTasksInFile)) {
			return ADDED_FEEDBACK;
		}
		return ERROR_ADDED_FEEDBACK;
	}

	/**
	 * edit a task Condition: can only edit with line number
	 */

	public String editTask(String[] parsedInput) throws IOException {
		int taskNumber = Integer.parseInt(parsedInput[0]) - 1;
		if (currentListOfTasksInFile.get(taskNumber) != null) {
			for (int i = 1; i < parsedInput.length; i++) {
				switch (i) {
				case 1:
					currentListOfTasksInFile.get(taskNumber).setTitle(parsedInput[i]);
					break;
				case 2:
					currentListOfTasksInFile.get(taskNumber).setDescription(parsedInput[i]);
					break;
				case 3:
					String formattedFromDate = formatTime(parsedInput[i]);
					currentListOfTasksInFile.get(taskNumber).setFromDate(formattedFromDate);
					break;
				case 4:
					String formattedToDate = formatTime(parsedInput[i]);
					currentListOfTasksInFile.get(taskNumber).setToDate(formattedToDate);
					break;
				case 5:
					currentListOfTasksInFile.get(taskNumber).setCategory(parsedInput[i]);
					break;
				}
			}
			if (store.isSaved(currentListOfTasksInFile)) {
				return EDITED_FEEDBACK;
			}
		}
		return ERROR_EDIT_FEEDBACK;
	}

	public String deleteTask(String[] line) throws IOException {
		if (Integer.parseInt(line[0]) <= currentListOfTasksInFile.size()) {
			for (int i = 0; i < currentListOfTasksInFile.size(); i++) {
				if (i == Integer.parseInt(line[0]) - 1) {
					currentListOfTasksInFile.remove(i);
				}
			}
			store.isSaved(currentListOfTasksInFile);
			return DELETED_FEEDBACK;
		}
		return ERROR_DELETED_FEEDBACK;
	}

	public Queue<Event> display(String type) {
		Queue<Event> events = new LinkedList<Event>();
		if (type.equals("agenda")) {
			
		} else if (type.equals("todo")) {

		}

		return events;
	}

	public String formatTime(String unformattedDate) {
		if (unformattedDate != null) {
			String[] dividedDates = unformattedDate.split(" ");
			String formattedDateTime = Integer.toString(LocalDateTime.now().getYear());
			for (String dateTime : dividedDates) {
				if (checkYear(dateTime) != null) {
					formattedDateTime = checkYear(dateTime);
					break;
				}
			}
			for (String dateTime : dividedDates) {
				if (!checkMonth(dateTime).equals("")) {
					formattedDateTime += checkMonth(dateTime);
					break;
				}
			}
			for (String dateTime : dividedDates) {
				if (!checkDay(dateTime).equals("")) {
					formattedDateTime += checkDay(dateTime);
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

	public String checkYear(String dateTime) {
		if (isYearInteger(dateTime)) {
			return dateTime;
		} else if (dateTime.equals("today")) {
			return Integer.toString(LocalDateTime.now().getYear());
		}
		return null;
	}

	private boolean isYearInteger(String dateTime) {
		boolean isInt = true;
		try {
			int unknownInt = Integer.parseInt(dateTime);
			if ((unknownInt - 1000) < 0) {
				isInt = false;
			}
		} catch (NumberFormatException e) {
			isInt = false;
		}
		return isInt;
	}

	public String checkMonth(String dateTime) {
		String formattedDate = new String("");
		if (listOfMonths.contain(dateTime.toLowerCase())) {
			formattedDate += "-";
			formattedDate += listOfMonths.monthDigit(dateTime.toLowerCase());
			formattedDate += "-";
		} else if (dateTime.equals("today")) {
			formattedDate += "-";
			if (LocalDateTime.now().getMonthValue() < 10) {
				formattedDate += "0";
			}
			formattedDate += LocalDateTime.now().getMonthValue();
			formattedDate += "-";
		}
		return formattedDate;
	}

	public String checkDay(String dateTime) {
		String formattedDay = new String("");
		if (isDayInteger(dateTime)) {
			if (Integer.parseInt(dateTime) < 10) {
				formattedDay += "0";
			}
			formattedDay += dateTime;
			formattedDay += "T";
		} else if (dateTime.equals("today")) {
			if (LocalDateTime.now().getDayOfMonth() < 10) {
				formattedDay += "0";
			}
			formattedDay += LocalDateTime.now().getDayOfMonth();
			formattedDay += "T";
		}
		return formattedDay;
	}

	private boolean isDayInteger(String dateTime) {
		boolean isInt = true;
		try {
			int unknownInt = Integer.parseInt(dateTime);
			if ((unknownInt - 1000) > 0) {
				isInt = false;
			}
		} catch (NumberFormatException e) {
			isInt = false;
		}
		return isInt;
	}

	public String checkTime(String dateTime) {
		String time = new String("");
		if (dateTime.contains("pm")) {
			time = formatHoursMinutes(dateTime.substring(0, dateTime.length() - 2), "pm");

		} else if (dateTime.contains("am")) {
			time = formatHoursMinutes(dateTime.substring(0, dateTime.length() - 2), "am");
		}

		return time;
	}

	public String formatHoursMinutes(String hours, String timeOfDay) {
		String[] hourMinute = hours.split("\\.");
		String formattedHourMinute = new String("");
		if (timeOfDay.equals("pm")) {
			int twentyFourHourTime = Integer.parseInt(hourMinute[0]) + 12;
			formattedHourMinute += twentyFourHourTime;
			formattedHourMinute += ":";
			if (hourMinute.length == 1) {
				formattedHourMinute += "00";
			} else {
				formattedHourMinute += hourMinute[1];
			}
		} else if (timeOfDay.equals("am")) {
			if (Integer.parseInt(hourMinute[0]) < 10) {
				formattedHourMinute += "0";
			} else if (Integer.parseInt(hourMinute[0]) == 12) {
				hourMinute[0] = "00";
			}
			formattedHourMinute += hourMinute[0];
			formattedHourMinute += ":";
			if (hourMinute.length == 1) {
				formattedHourMinute += "00";
			} else {
				formattedHourMinute += hourMinute[1];
			}
		}
		return formattedHourMinute;
	}

	/**
	 * returns total number of word matches compared to an event
	 * 
	 */
	private int matchingWordCount(String[] parsedInput, Event task) {
		int count = 0;
		for (int i = 0; i < parsedInput.length; i++) {
			switch (i) {
			case 1:
				if (task.getTitle().contains(parsedInput[i])) {
					count++;
				}
				break;
			case 2:
				if (task.getDescription().contains(parsedInput[i])) {
					count++;
				}
				break;
			case 3:
				if (task.getFromTime().contains(parsedInput[i])) {
					count++;
				}
				break;
			case 4:
				if (task.getToTime().contains(parsedInput[i])) {
					count++;
				}
				break;
			case 5:
				if (task.getCategory().contains(parsedInput[i])) {
					count++;
				}
				break;
			default:
				break;
			}
		}
		return count;
	}

	/**
	 * gets a list of help commands for user to refer to
	 *
	 */
	public String helpCommand(String[] parsedInput) {
		String listOfCommands = new String();
		listOfCommands += ADD_HELP_HEADER;
		listOfCommands += ADD_COMMAND_BY;
		listOfCommands += ADD_COMMAND_AT;
		listOfCommands += ADD_COMMAND_FROMTO;

		listOfCommands += EDIT_HELP_HEADER;
		listOfCommands += EDIT_COMMAND_ONE_TIMING;
		listOfCommands += EDIT_COMMAND_TWO_TIMINGS;

		listOfCommands += DISPLAY_HELP_HEADER;
		listOfCommands += DISPLAY_COMMAND;

		listOfCommands += DELETE_HELP_HEADER;
		listOfCommands += DELETE_COMMAND;
		return listOfCommands;
	}
}
