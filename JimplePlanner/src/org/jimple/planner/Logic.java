package org.jimple.planner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Logic {

	private static final String STRING_WHOLEDAY = "23:59";
	private static final String STRING_SEARCH = "search";
	private static final String STRING_ADD = "add";
	private static final String STRING_DELETE = "delete";
	private static final String STRING_EDIT = "edit";
	private static final String STRING_CHANGEDIR = "changedir";

	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "floating";

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
	private String WINDOW_CLOSED_FEEDBACK = "search window closed";
	private String DIRECTORY_PATH_CHANGED_FEEDBACK = "save directory path changed";

	private String ERROR_ADDED_FEEDBACK = "task could not be added";
	private String ERROR_EDIT_FEEDBACK = "task could not be editted";
	private String ERROR_FILE_NOT_FOUND = "could not find file";
	private String ERROR_CONFLICT_FEEDBACK = "conflict with current events";
	private String ERROR_DELETED_FEEDBACK = "could not find task to be deleted";
	private String ERROR_SEARCH_WORD_NOT_FOUND_FEEDBACK = "keyword not found in planner";
	private String ERROR_SEARCH_PLANNER_EMPTY_FEEDBACK = "planner is empty";
	private String ERROR_DIRECTORY_PATH_FEEDBACK = "invalid directory path";
	private String ERROR_WRONG_INPUT_FEEDBACK = "wrong input format";

	private ArrayList<Task> deadlines;
	private ArrayList<Task> todo;
	private ArrayList<Task> events;
	private ArrayList<Task> tempHistory;
	Parser parser = new Parser();
	StorageStub store = new StorageStub();

	public Logic() {
		tempHistory = new ArrayList<Task>();
		try {
			todo = store.getTasks().get(0);
			deadlines = store.getTasks().get(1);
			events = store.getTasks().get(2);
		} catch (IOException e) {
			System.out.print(ERROR_FILE_NOT_FOUND);
		} catch (IndexOutOfBoundsException d) {
			todo = new ArrayList<Task>();
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
		try {
			InputStruct parsedInput = parser.parseInput(inputString);
			switch (parsedInput.getCommand()) {
			case STRING_DELETE:
				feedback[0] = deleteTask(parsedInput.getVariableArray(), todo, deadlines, events);
				feedback[1] = STRING_DELETE;
				break;
			case STRING_ADD:
				feedback[0] = addToTaskList(parsedInput.getVariableArray());
				feedback[1] = tempHistory.get(tempHistory.size() - 1).getType();
				break;
			case STRING_EDIT:
				feedback[0] = editTask(parsedInput.getVariableArray(), todo, deadlines, events);
				feedback[1] = STRING_EDIT;
				break;
			case STRING_SEARCH:
				feedback[0] = parsedInput.getVariableArray()[0];
				feedback[1] = STRING_SEARCH;
				break;
			case STRING_CHANGEDIR:
				feedback[0] = changeSaveDirectory(parsedInput.getVariableArray());
				feedback[1] = STRING_CHANGEDIR;
				break;
			}
		} catch (Exception e) {
			feedback[0] = ERROR_WRONG_INPUT_FEEDBACK;
			feedback[1] = "";
		}
		return feedback;
	}

	private String changeSaveDirectory(String[] variableArray) {
		if (!isValidPath(variableArray)) {
			return ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		return DIRECTORY_PATH_CHANGED_FEEDBACK;
	}

	private boolean isValidPath(String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

	public ArrayList<Task> display(String type) {
		checkOverCurrentTime();
		if (type.equals(TYPE_TODO)) {
			return todo;
		} else if (type.equals(TYPE_EVENT)) {
			return events;
		} else if (type.equals(TYPE_DEADLINE)) {
			return deadlines;
		}
		return null;
	}

	private void checkOverCurrentTime() {
		for (Task aTask : deadlines) {
			if (aTask.getToTime() != null) {
				if (aTask.getToTime().compareTo(LocalDateTime.now()) > 0) {
					aTask.setIsOverDue(true);
				}
			}
		}
		for (Task aTask : events) {
			if (aTask.getFromTime() != null) {
				if (aTask.getFromTime().compareTo(LocalDateTime.now()) > 0) {
					aTask.setIsOverDue(true);
				}
			}
		}
	}

	private String editTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two, ArrayList<Task> three)
			throws IOException {
		boolean isToDoEditted = false;
		boolean isWholeDayEditted = false;
		boolean isEventsEditted = false;
		isToDoEditted = findTask(one, variableArray, 0, STRING_EDIT);
		if (!isToDoEditted) {
			isWholeDayEditted = findTask(two, variableArray, one.size(), STRING_EDIT);
		}
		if (!isWholeDayEditted && !isToDoEditted) {
			isEventsEditted = findTask(three, variableArray, one.size() + two.size(), STRING_EDIT);
		}
		if (isToDoEditted || isWholeDayEditted || isEventsEditted) {
			packageForSavingInFile();
			return EDITED_FEEDBACK;
		}
		return ERROR_EDIT_FEEDBACK;
	}

	private boolean findTask(ArrayList<Task> list, String[] variableArray, int previousSizes, String type) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt(variableArray[0]) - previousSizes == i) {
				if (type.equals(STRING_EDIT)) {
					Task taskToBeEditted = list.remove(i);
					Task editedTask = doEdit(createArrayWithoutFirstIndex(variableArray), taskToBeEditted);
					if (!isFromAndToTimeCorrect(editedTask))	{
						list.add(taskToBeEditted);
						return false;
					}
					allocateCorrectTimeArray(editedTask);
				} else if (type.equals(STRING_DELETE)) {
					list.remove(i);
				}
				return true;
			}
		}
		return false;
	}

	private boolean isFromAndToTimeCorrect(Task task) {
		if (task.getFromTime() == null || task.getToTime() == null) {
			return true;
		} else if (task.getFromTime().compareTo(task.getToTime()) < 0) {
			return true;
		}
		return false;
	}

	private String[] createArrayWithoutFirstIndex(String[] variableArray) {
		String[] parsedInput = new String[5];
		for (int i = 1; i < variableArray.length; i++) {
			parsedInput[i - 1] = variableArray[i];
		}
		return parsedInput;
	}

	public Task doEdit(String[] variableArray, Task aTask) {
		Task editedTask = new Task(aTask);
		for (int i = 0; i < variableArray.length; i++) {
			if (variableArray[i] != null) {
				switch (i) {
				case 0:
					editedTask.setTitle(variableArray[0]);
					break;
				case 1:
					editedTask.setDescription(variableArray[i]);
					break;
				case 2:
					editedTask.setFromDate(variableArray[i]);
					break;
				case 3:
					editedTask.setToDate(variableArray[i]);
					break;
				case 4:
					editedTask.setCategory(variableArray[i]);
					break;
				default:
					break;
				}
			}
		}
		return editedTask;
	}

	private String deleteTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two, ArrayList<Task> three)
			throws IOException {
		boolean isFloatDeleted = false;
		boolean isDeadlineDeleted = false;
		boolean isEventsDeleted = false;

		isFloatDeleted = findTask(one, variableArray, 0, STRING_DELETE);
		if (!isFloatDeleted) {
			isDeadlineDeleted = findTask(two, variableArray, one.size(), STRING_DELETE);
		}
		if (!isFloatDeleted && !isDeadlineDeleted) {
			isEventsDeleted = findTask(three, variableArray, one.size() + two.size(), STRING_DELETE);
		}
		if (isFloatDeleted || isDeadlineDeleted || isEventsDeleted) {
			packageForSavingInFile();
			return DELETED_FEEDBACK;
		}
		return ERROR_DELETED_FEEDBACK;
	}

	// adds task into the Event object
	private String addToTaskList(String[] parsedInput) throws IOException {
		Task newTask = new Task("");
		newTask = doEdit(parsedInput, newTask);
		if (!isFromAndToTimeCorrect(newTask))	{
			return ERROR_ADDED_FEEDBACK;
		}
		allocateCorrectTimeArray(newTask);
		tempHistory.add(newTask);
		return ADDED_FEEDBACK;
	}

	private boolean isConflictWithCurrentTasks(Task newTask, ArrayList<Task> deadlines, ArrayList<Task> events) {
		boolean isConflict = false;
		switch (newTask.getType()) {
		case TYPE_DEADLINE:
			for (int i = 0; i < deadlines.size(); i++) {
				if (newTask.getToTime().equals(deadlines.get(i).getToTime())) {
					isConflict = true;
				}
			}
			break;
		case TYPE_EVENT:
			for (int i = 0; i < events.size(); i++) {
				if (isToTimeExceedTimeRange(newTask, events.get(i))
						|| isFromTimeExceedTimeRange(newTask, events.get(i))) {
					isConflict = true;
				}
			}
			break;
		}
		return isConflict;
	}

	private boolean isToTimeExceedTimeRange(Task newTask, Task event) {
		if (newTask.getToTime().compareTo(event.getFromTime()) > 0
				&& newTask.getToTime().compareTo(event.getToTime()) < 0) {
			return true;
		}
		return false;
	}

	private boolean isFromTimeExceedTimeRange(Task newTask, Task event) {
		if (newTask.getFromTime().compareTo(event.getFromTime()) > 0
				&& newTask.getFromTime().compareTo(event.getToTime()) < 0) {
			return true;
		}
		return false;
	}

	private void allocateCorrectTimeArray(Task newTask) throws IOException {
		// check if null
		if (newTask.getFromTime() == null && newTask.getToTime() == null) {
			todo.add(newTask);
		}
		// check if whole day task
		else if (newTask.getFromTime() == null && newTask.getToTime() != null) {
			deadlines.add(newTask);
		} else {
			events.add(newTask);
		}
		packageForSavingInFile();
	}

	private void packageForSavingInFile() throws IOException {
		ArrayList<ArrayList<Task>> allTasksArray = new ArrayList<ArrayList<Task>>();
		allTasksArray.add(todo);
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

	public String reInsertNewTasks(ArrayList<Task> newList) throws IOException {
		for (Task aTask : newList) {
			if (aTask.getType().equals(TYPE_EVENT)) {
				events.add(aTask);
			} else if (aTask.getType().equals(TYPE_TODO)) {
				todo.add(aTask);
			} else if (aTask.getType().equals(TYPE_DEADLINE)) {
				deadlines.add(aTask);
			}
		}
		packageForSavingInFile();
		return WINDOW_CLOSED_FEEDBACK;
	}

	// Index 0 will always yield a feedback, indexes 1 onwards will give the
	// Indexes of Events that has the keyword
	public ArrayList<Task> searchWord(String wordToBeSearched) {
		ArrayList<Task> searchWordResults = new ArrayList<Task>();
		if (todo.isEmpty() && deadlines.isEmpty() && events.isEmpty()) {
		} else {
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, todo));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, deadlines));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, events));
		}
		return searchWordResults;
	}

	private ArrayList<Task> searchFromOneTaskList(String wordToBeSearched, ArrayList<Task> list) {
		ArrayList<Task> searchWordResults;
		searchWordResults = getSearchedTasks(wordToBeSearched, list);
		if (searchWordResults.isEmpty()) {
			// searchWordResults.add(SEARCH_WORD_NOT_FOUND_FEEDBACK);
		}
		return searchWordResults;
	}

	private ArrayList<Task> getSearchedTasks(String wordToBeSearched, ArrayList<Task> list) {
		ArrayList<Task> objectOfTaskInstanceFound = new ArrayList<Task>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Task currentTask = list.get(i);
				if (isContainKeyword(currentTask, wordToBeSearched)) {
					objectOfTaskInstanceFound.add(list.remove(i));
					i--;
				}
			}
		}
		return objectOfTaskInstanceFound;
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

	public ArrayList<Task> testSearchWord(String wordToBeSearched, ArrayList<Task> one, ArrayList<Task> two,
			ArrayList<Task> three) {
		return searchWord(wordToBeSearched);
	}

	public boolean testFindTaskToEdit(ArrayList<Task> list, String[] variableArray, int previousSizes)
			throws IOException {
		return findTask(list, variableArray, previousSizes, STRING_EDIT);
	}

	public String testEditTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two, ArrayList<Task> three)
			throws IOException {
		return editTask(variableArray, one, two, three);
	}

	public String testDeleteTask(String[] variableArray, ArrayList<Task> one, ArrayList<Task> two,
			ArrayList<Task> three) throws IOException {
		return deleteTask(variableArray, one, two, three);
	}

	public boolean testConflictWithCurrentTasks(Task newTask, ArrayList<Task> deadlines, ArrayList<Task> events) {
		return isConflictWithCurrentTasks(newTask, deadlines, events);
	}
}
