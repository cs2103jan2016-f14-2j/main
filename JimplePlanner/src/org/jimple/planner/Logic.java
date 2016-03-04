package org.jimple.planner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

class Task {
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;

	// Constructors
	public Task(String aTitle) {
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
		if (dateTime == null) {
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
		if (dateTime == null) {
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
		if (description == null) {
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		if (category == null) {
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

	private String ADDED_FEEDBACK = "task added to planner";
	private String EDITED_FEEDBACK = "task edited in planner";
	private String SEARCH_PLANNER_EMPTY_FEEDBACK = "planner is empty";
	private String SEARCH_WORD_NOT_FOUND_FEEDBACK = "keyword not found in planner";

	private String ERROR_EDIT_FEEDBACK = "task not found";
	private String ERROR_FILE_NOT_FOUND = "could not find file";
	private String ERROR_DELETED_FEEDBACK = "could not find deleted file";

	private ArrayList<Task> temporaryHistory;
	private ArrayList<Task> wholeDay;
	private ArrayList<Task> toDo;
	private ArrayList<Task> events;
	private ArrayList<Task> currentListOfTasksInFile;
	private Formatter data = new Formatter();
	private int editMode;
	Parser parser = new Parser();
	StorageStub store = new StorageStub();
	Formatter formatter = new Formatter();

	public Logic() {
		temporaryHistory = new ArrayList<Task>();
		data.listOfMonths = new ListOfMonths();
		editMode = 0;
		try {
			wholeDay = new ArrayList<Task>();
			toDo = new ArrayList<Task>();
			events = new ArrayList<Task>();
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
		switch (parsedInput.getCommand()) {
		case "delete":
			feedback += deleteTask(parsedInput.getVariableArray());
			break;
		case "add":
			feedback += addToTaskList(parsedInput.getVariableArray());
			break;
		case "edit":
			feedback += editTask(parsedInput.getVariableArray());
			break;
		case "search":
			ArrayList<String> searchResults = searchWord(parsedInput.getVariableArray());
			feedback += "";
			break;
		}
		return feedback;
	}

	private String deleteTask(String[] variableArray) {
		// TODO Auto-generated method stub
		return null;
	}

	// adds task into the Event object
	private String addToTaskList(String[] parsedInput) throws IOException {
		Task newTask = new Task(parsedInput[0]);
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != null) {
				switch (i) {
				case 1:
					newTask.setDescription(parsedInput[i]);
					break;
				case 2:
					String formattedFromDate = formatter.formatDateTime(parsedInput[i]);
					newTask.setFromDate(formattedFromDate);
					break;
				case 3:
					String formattedToDate = formatter.formatDateTime(parsedInput[i]);
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
		allocateCorrectTime(newTask);
		temporaryHistory.add(newTask);
		return ADDED_FEEDBACK;
	}

	private void allocateCorrectTime(Task newTask) throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		// check if null
		if (newTask.getFromTime() == null && newTask.getToTime() == null) {
			toDo.add(newTask);
		}
		// check if whole day task
		else if (newTask.getFromTime().equals("00:00") && newTask.getToTime().equals("23:59")) {
			wholeDay.add(newTask);
		} else {
			events.add(newTask);
		}
		allTasksArray.add(toDo);
		allTasksArray.add(wholeDay);
		allTasksArray.add(events);
		store.Saved(allTasksArray);
	}

	/**
	 * edit a task Condition: can only edit with line number
	 */

	private String editTask(String[] parsedInput) throws IOException {
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
					String formattedFromDate = formatter.formatDateTime(parsedInput[i]);
					currentListOfTasksInFile.get(taskNumber).setFromDate(formattedFromDate);
					break;
				case 4:
					String formattedToDate = formatter.formatDateTime(parsedInput[i]);
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

	/**
	 * returns total number of word matches compared to an event
	 * 
	 */
	private int matchingWordCount(String[] parsedInput, Task task) {
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
	private String helpCommand(String[] parsedInput) {
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

	// Index 0 will always yield a feedback, indexes 1 onwards will give the
	// Indexes of Events that has the keyword
	public ArrayList<String> searchWord(String[] variableArray) {
		String wordToBeSearched = variableArray[0];
		ArrayList<String> searchWordResults;
		if (currentListOfTasksInFile.isEmpty()) {
			searchWordResults = new ArrayList<String>();
			searchWordResults.add(SEARCH_PLANNER_EMPTY_FEEDBACK);
		} else {
			searchWordResults = getSearchedWordLineIndexes(wordToBeSearched);
			if (searchWordResults.isEmpty()) {
				searchWordResults.add(SEARCH_WORD_NOT_FOUND_FEEDBACK);
			} else {
				String searchResultFeedback = "search result for \"" + wordToBeSearched + "\"";
				searchWordResults = getSearchedWordLineIndexes(wordToBeSearched);
				searchWordResults.add(0, searchResultFeedback);
			}
		}
		return searchWordResults;
	}

	private ArrayList<String> getSearchedWordLineIndexes(String wordToBeSearched) {
		ArrayList<String> indexesOfWordInstanceFound = new ArrayList<String>();
		int eventListSize = currentListOfTasksInFile.size();
		for (int i = 0; i < eventListSize; i++) {
			Task currentEvent = currentListOfTasksInFile.get(i);
			if (isContainKeyword(currentEvent, wordToBeSearched)) {
				indexesOfWordInstanceFound.add(String.valueOf(i));
			}
		}
		return indexesOfWordInstanceFound;
	}

	private boolean isContainSubstring(String sourceString, String substring) {
		int substringLength = substring.length();
		if (substringLength == 0) {
			return true;
		}
		char subStringFirstLowerCaseChar = Character.toLowerCase(substring.charAt(0));
		char subStringFirstUpperCaseChar = Character.toUpperCase(substring.charAt(0));
		for (int i = sourceString.length() - substringLength; i >= 0; i--) {
			char sourceCharacterAt = sourceString.charAt(i);
			if (sourceCharacterAt != subStringFirstLowerCaseChar && sourceCharacterAt != subStringFirstUpperCaseChar) {
				continue;
			}
			if (sourceString.regionMatches(true, i, substring, 0, substringLength)) {
				return true;
			}
		}
		return false;
	}

	private boolean isContainKeyword(Task event, String keyword) {
		boolean isTitleSearched = isContainSubstring(event.getTitle(), keyword);
		boolean isDescSearched = isContainSubstring(event.getDescription(), keyword);
		boolean isCategorySearched = isContainSubstring(event.getCategory(), keyword);
		return (isTitleSearched || isDescSearched || isCategorySearched);
	}

	public String testAddToTaskList(String[] parsedInput) throws IOException {
		return addToTaskList(parsedInput);
	}
}
