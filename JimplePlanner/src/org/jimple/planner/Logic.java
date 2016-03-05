package org.jimple.planner;

import java.io.IOException;
import java.util.ArrayList;

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

	private ArrayList<Task> deadlines;
	private ArrayList<Task> floating;
	private ArrayList<Task> events;
	private ArrayList<Task> tempHistory;
	ListOfMonths listOfMonths;
	Parser parser = new Parser();
	StorageStub store = new StorageStub();
	Formatter formatter = new Formatter();

	public Logic() {
		listOfMonths = new ListOfMonths();
		tempHistory = new ArrayList<Task>();
		try {
			floating = store.getTasks().get(0);
			deadlines = store.getTasks().get(1);
			events = store.getTasks().get(2);
		} catch (IOException e) {
			System.out.print(ERROR_FILE_NOT_FOUND);
		} catch (IndexOutOfBoundsException d) {
			floating = new ArrayList<Task>();
			deadlines = new ArrayList<Task>();
			events = new ArrayList<Task>();
		}
	}

	/**
	 * function is for the UI to call when a user inputs a string
	 *
	 */
	public String[] execute(String inputString) throws IOException {
		String[] feedback = new String[2];
		InputStruct parsedInput = parser.parseInput(inputString);
		switch (parsedInput.getCommand()) {
		case "delete":
			feedback[0] = deleteTask(parsedInput.getVariableArray());
			break;
		case "add":
			feedback[0] = addToTaskList(parsedInput.getVariableArray());
			break;
		case "edit":
			feedback[0] = editTask(parsedInput.getVariableArray(), floating, deadlines, events);
			break;
		case "search":
			ArrayList<String> searchResults = searchWord(parsedInput.getVariableArray(), floating, deadlines, events);
			feedback[1] = formatter.formatSearchString(searchResults);
			feedback[0] = "";
			break;
		}
		return feedback;
	}
	
	public ArrayList<Task> display(String type)	{
		if (type.equals("floating"))	{
			return floating;
		}
		else if (type.equals("events"))	{
			return events;
		}
		else if (type.equals("deadlines"))	{
			return deadlines;
		}
		return null;
	}

	private String editTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two, ArrayList<Task> three) throws IOException {
		boolean isToDoEditted = findTaskToEdit(one, variableArray, 0);
		boolean isWholeDayEditted = findTaskToEdit(two, variableArray, one.size());
		boolean isEventsEditted = findTaskToEdit(three, variableArray, one.size() + two.size());
		if (isToDoEditted || isWholeDayEditted || isEventsEditted) {
			packageForSavingInFile();
			return EDITED_FEEDBACK;
		}
		return ERROR_EDIT_FEEDBACK;
	}

	private boolean findTaskToEdit(ArrayList<Task> list, String[] variableArray, int previousSizes) {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) - previousSizes == i) {
				doEdit(arrayWithoutEditIndex(variableArray), list.get(i));
				return true;
			}
		}
		return false;
	}

	private String[] arrayWithoutEditIndex(String[] variableArray) {
		String[] parsedInput = new String[5];
		for (int i=1;i<variableArray.length;i++)	{
			parsedInput[i-1] = variableArray[i];
		}
		return parsedInput;
	}

	private Task doEdit(String[] variableArray, Task aTask) {
		for (int i = 0; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 0:
					aTask.setTitle(variableArray[0]);
				case 1:
					aTask.setDescription(variableArray[i]);
					break;
				case 2:
					String formattedFromDate = formatter.formatDateTime(variableArray[i]);
					aTask.setFromDate(formattedFromDate);
					break;
				case 3:
					String formattedToDate = formatter.formatDateTime(variableArray[i]);
					if (isContainsValidTime(formattedToDate)) {
						aTask.setToDate(formattedToDate);
					} else {
						aTask.setToDate(formattedToDate.concat("23:59"));
					}
					break;
				case 4:
					aTask.setCategory(variableArray[i]);
					break;
				default:
					break;
				}
			}
		}
		return aTask;
	}

	private String deleteTask(String[] variableArray) {
		// TODO Auto-generated method stub
		return null;
	}

	// adds task into the Event object
	private String addToTaskList(String[] parsedInput) throws IOException {
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask);
		allocateCorrectTimeArray(newTask);
		tempHistory.add(newTask);
		return ADDED_FEEDBACK;
	}

	private boolean isContainsValidTime(String formattedDateTime) {
		if (formattedDateTime.endsWith("T")) {
			return false;
		}
		return true;
	}

	private void allocateCorrectTimeArray(Task newTask) throws IOException {
		// check if null
		if (newTask.getFromTime() == null && newTask.getToTime() == null) {
			floating.add(newTask);
		}
		// check if whole day task
		else if (newTask.getFromTimeString().equals(null) && newTask.getToTime() != null) {
			deadlines.add(newTask);
		} else {
			events.add(newTask);
		}
		packageForSavingInFile();
	}

	private void packageForSavingInFile() throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		allTasksArray.add(floating);
		allTasksArray.add(deadlines);
		allTasksArray.add(events);
		store.isSaved(allTasksArray);
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
				if (task.getFromTimeString().contains(parsedInput[i])) {
					count++;
				}
				break;
			case 4:
				if (task.getToTimeString().contains(parsedInput[i])) {
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
	public ArrayList<String> searchWord(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two,
			ArrayList<Task> three) {
		String wordToBeSearched = variableArray[0];
		ArrayList<String> searchWordResults = new ArrayList<String>();
		if (one.isEmpty() && two.isEmpty() && three.isEmpty()) {
			searchWordResults.add(SEARCH_PLANNER_EMPTY_FEEDBACK);
		} else {
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, one, 0));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, two, one.size()));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, three, one.size() + two.size()));
		}
		return searchWordResults;
	}

	private ArrayList<String> searchFromOneTaskList(String wordToBeSearched, ArrayList<Task> list, int size) {
		ArrayList<String> searchWordResults;
		searchWordResults = getSearchedWordLineIndexes(wordToBeSearched, list, size);
		if (searchWordResults.isEmpty()) {
			// searchWordResults.add(SEARCH_WORD_NOT_FOUND_FEEDBACK);
		} else {
			searchWordResults = getSearchedWordLineIndexes(wordToBeSearched, list, size);
		}
		return searchWordResults;
	}

	private ArrayList<String> getSearchedWordLineIndexes(String wordToBeSearched, ArrayList<Task> list, int size) {
		ArrayList<String> indexesOfWordInstanceFound = new ArrayList<String>();
		int eventListSize = list.size();
		for (int i = 0; i < eventListSize; i++) {
			Task currentEvent = list.get(i);
			if (isContainKeyword(currentEvent, wordToBeSearched)) {
				indexesOfWordInstanceFound.add(String.valueOf(i) + size);
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

	public boolean testIsContainKeyword(Task event, String keyword) {
		return isContainKeyword(event, keyword);
	}

	public ArrayList<String> testSearchWord(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two,
			ArrayList<Task> three) {
		return searchWord(variableArray, one, two, three);
	}
	public boolean testFindTaskToEdit(ArrayList<Task> list, String[] variableArray, int previousSizes)	{
		return findTaskToEdit(list, variableArray, previousSizes);
	}
	public String testEditTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two, ArrayList<Task> three) throws IOException	{
		return editTask(variableArray, one, two, three);
	}
}
