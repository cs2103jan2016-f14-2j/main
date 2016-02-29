package org.jimple.planner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

	public String getFromTime() {
		if (fromDateTime == null) {
			return "";
		}
		return fromDateTime.toString();
	}

	public void setFromDate(String dateTime) {
		if (dateTime==null) {
		} else {
			this.fromDateTime = LocalDateTime.parse(dateTime);
		}
	}

	public String getToTime() {
		if (toDateTime == null) {
			return "";
		}
		return toDateTime.toString();
	}

	public void setToDate(String dateTime) {
		if (dateTime==null) {
		} else {
			this.toDateTime = LocalDateTime.parse(dateTime);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		if (description == null)	{
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		if (category == null)	{
			return "";
		}
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	private String ADDED_FEEDBACK = "task added to planner\n";
	private String EDITED_FEEDBACK = "task edited in planner\n";

	private String ERROR_EDIT_FEEDBACK = "task not found\n";
	private String ERROR_ADDED_FEEDBACK = "could not add to task list\n";
	private String ERROR_FILE_NOT_FOUND = "could not find file\n";

	private ArrayList<Event> temporaryHistory;
	private ArrayList<Event> currentListOfTasksInFile;
	Parser parser = new Parser();
	Storage store = new Storage();

	public Logic() {
		temporaryHistory = new ArrayList<Event>();
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
			break;
		case "add":
			feedback += addToTaskList(parsedInput.variableArray, inputString);
			break;
		case "edit":
			feedback += editTask(parsedInput.variableArray);
			break;
		}
		return feedback;
	}

	// adds task into the Event object
	public String addToTaskList(String[] parsedInput, String originalInput) throws IOException {
		Event newTask = new Event(parsedInput[0]);
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != "") {
				switch (i) {
				case 1:
					newTask.setDescription(parsedInput[i]);
					break;
				case 2:
					newTask.setFromDate(parsedInput[i]);
					break;
				case 3:
					newTask.setToDate(parsedInput[i]);
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
			for (int i = 0; i < parsedInput.length; i++) {
				switch (i) {
				case 1:
					currentListOfTasksInFile.get(taskNumber).setTitle(parsedInput[i]);
					break;
				case 2:
					currentListOfTasksInFile.get(taskNumber).setDescription(parsedInput[i]);
					break;
				case 3:
					String formattedDate = formatDate(parsedInput[i]);
					currentListOfTasksInFile.get(taskNumber).setFromDate(parsedInput[i]);
					break;
				case 4:
					currentListOfTasksInFile.get(taskNumber).setToDate(parsedInput[i]);
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

	private String formatDate(String unformattedDate) {
		String[] dividedDates = unformattedDate.split(" ");
		String formattedDateTime = new String("");
		for (String dateTime : dividedDates) {
			formattedDateTime += checkYear(dateTime);
		}
		for (String dateTime : dividedDates) {
			formattedDateTime += checkMonth(dateTime);
		}
		for (String dateTime : dividedDates) {
			formattedDateTime += checkDay(dateTime);
		}
		for (String dateTime : dividedDates) {
			formattedDateTime += checkTime(dateTime);
		}
		return formattedDateTime;
	}

	private String checkYear(String dateTime) {
		String formattedYear = new String("");
		if (dateTime.equals(" ")) {

		} 
		else if (dateTime.equals("today"))	{
			formattedYear += LocalDateTime.now().getYear();
			formattedYear += "-";
			formattedYear += LocalDateTime.now().getMonthValue();
			formattedYear += "-";
			formattedYear += LocalDateTime.now().getDayOfMonth();
		}
		else {
			formattedYear = Integer.toString(LocalDateTime.now().getYear());
		}
		return formattedYear;
	}

	private String checkMonth(String dateTime) {
		String formattedDate = new String("");
		switch (dateTime) {
		case "january":
			formattedDate = "1";
			break;
		case "february":
			formattedDate = "2";
			break;
		case "march":
			formattedDate = "3";
			break;
		case "april":
			formattedDate = "4";
			break;
		case "may":
			formattedDate = "5";
			break;
		case "june":
			formattedDate = "6";
			break;
		case "july":
			formattedDate = "7";
			break;
		case "august":
			formattedDate = "8";
			break;
		case "september":
			formattedDate = "9";
			break;
		case "october":
			formattedDate = "10";
			break;
		case "november":
			formattedDate = "11";
			break;
		case "december":
			formattedDate = "12";
			break;
		default:
			break;
		}
		return formattedDate;
	}

	private String checkDay(String dateTime) {
		return null;
	}

	private String checkTime(String dateTime) {
		return null;
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
