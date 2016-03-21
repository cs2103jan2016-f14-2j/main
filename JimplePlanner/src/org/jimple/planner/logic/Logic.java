package org.jimple.planner.logic;

import org.jimple.planner.storage.*;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.observers.myObserver;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.InputStruct;
import org.jimple.planner.Parser;
import org.jimple.planner.Task;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.storage.StorageComponent;

import java.util.Collections;
import java.util.Comparator;

public class Logic {

	private static final String STRING_SEARCH = "search";
	private static final String STRING_ADD = "add";
	private static final String STRING_DELETE = "delete";
	private static final String STRING_EDIT = "edit";
	private static final String STRING_CHANGEDIR = "changedir";

	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "floating";
	private static final String STRING_UNDO = "undo";

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

	private String ERROR_FILE_NOT_FOUND = "could not find file";
	private String ERROR_CONFLICT_FEEDBACK = "conflict with current events";
	private String ERROR_SEARCH_WORD_NOT_FOUND_FEEDBACK = "searchword not found in planner";
	private String ERROR_SEARCH_PLANNER_EMPTY_FEEDBACK = "planner is empty";
	private String ERROR_WRONG_INPUT_FEEDBACK = "wrong input format";
	private String ERROR_WRONG_COMMAND_FEEDBACK = "unknown command";

	private ArrayList<Task> deadlines;
	private ArrayList<Task> todo;
	private ArrayList<Task> events;
	private ArrayList<Task> agenda;
	private ArrayList<Task> tempHistory;
	private ArrayList<Task> searchResults;
	private ArrayList<Task> deletedTasks;
	private ArrayList<String> pastUserInputs;
	private ArrayList<myObserver> observers;
	Parser parser = new Parser();
	Storage store = new StorageComponent();
	LogicAdd adder = new LogicAdd();
	LogicEdit editer = new LogicEdit();
	LogicDelete deleter = new LogicDelete();
	LogicSearch searcher = new LogicSearch();
	LogicDirectory directer = new LogicDirectory();
	LogicUndo undoer = new LogicUndo();
	Logger logger;

	public Logic() {
		tempHistory = new ArrayList<Task>();
		deletedTasks = new ArrayList<Task>();
		agenda = new ArrayList<Task>();
		pastUserInputs = new ArrayList<String>();
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		observers = new ArrayList<myObserver>();
		try {
			ArrayList<ArrayList<Task>> allTasks = store.getTasks();
			LogicTaskModification.assignTaskIdsAndTaskType(allTasks);
			todo = allTasks.get(0);
			deadlines = allTasks.get(1);
			events = allTasks.get(2);
		} catch (IndexOutOfBoundsException e) {
			todo = new ArrayList<Task>();
			deadlines = new ArrayList<Task>();
			events = new ArrayList<Task>();
			logger.log(Level.WARNING, "when planner.jim does not exist", e);
		}

	}

	/**
	 * function is for the UI to call when a user inputs a string
	 *
	 */
	public void execute(String inputString) throws IOException {
		String[] feedback = new String[2];
		pastUserInputs.add(inputString);
		logger.log(Level.INFO, "preparing to start processing");
		try {
			InputStruct parsedInput = parser.parseInput(inputString);
			switch (parsedInput.getCommand()) {
			case STRING_DELETE:
				feedback[0] = deleter.deleteTask(store, parsedInput.getVariableArray(), todo, deadlines, events,
						deletedTasks);
				feedback[1] = STRING_DELETE;
				break;
			case STRING_ADD:
				feedback[0] = adder.addToTaskList(store, parsedInput.getVariableArray(), tempHistory, todo, deadlines,
						events);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case STRING_EDIT:
				feedback[0] = editer.editTask(store, parsedInput.getVariableArray(), todo, deadlines, events);
				feedback[1] = STRING_EDIT;
				break;
			case STRING_SEARCH:
				searchResults = searcher.searchWord(parsedInput.getVariableArray()[0], todo, deadlines, events);
				feedback[0] = "";
				feedback[1] = STRING_SEARCH;
				break;
			case STRING_CHANGEDIR:
				feedback[0] = directer.changeSaveDirectory(store, parsedInput.getVariableArray());
				feedback[1] = STRING_CHANGEDIR;
				break;
			case STRING_UNDO:
				feedback[0] = undoer.undoPreviousChange(store, deletedTasks, todo, deadlines, events);
				feedback[1] = STRING_UNDO;
			default:
				feedback[0] = ERROR_WRONG_COMMAND_FEEDBACK;
				feedback[1] = "";
				logger.log(Level.WARNING, "wrong input");
				break;
			}
		} catch (Exception e) {
			feedback[0] = ERROR_WRONG_INPUT_FEEDBACK;
			feedback[1] = "";
			logger.log(Level.WARNING, "input error", e);
		}
		logger.log(Level.INFO, "end of processing");
		notifyAllObservers(feedback);
	}

	public ArrayList<Task> getToDoList() {
		return todo;
	}

	public ArrayList<Task> getDeadlinesList() {
		checkOverCurrentTime();
		return deadlines;
	}

	public ArrayList<Task> getEventsList() {
		checkOverCurrentTime();
		return events;
	}

	public ArrayList<Task> getSearchList() {
		return searchResults;
	}

	public ArrayList<Task> getAgendaList() {
		agenda.addAll(deadlines);
		agenda.addAll(events);
		Collections.sort(agenda, Task.getFromDateComparator());
		return agenda;
	}

	public ArrayList<String> getPastInputs() {
		if (!pastUserInputs.isEmpty()) {
			return pastUserInputs;
		}
		return null;
	}

	public void setSearchList(ArrayList<Task> searchState) {
		searchResults = searchState;
		checkOverCurrentTime();
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

	private String getTaskTypeAndTaskID() {
		if (!tempHistory.isEmpty()) {
			return Integer.toString(tempHistory.get(tempHistory.size() - 1).getTaskId())
					+ tempHistory.get(tempHistory.size() - 1).getType();
		} else {
			return "";
		}
	}

	public void attach(myObserver observer) {
		observers.add(observer);
	}

	public void notifyAllObservers(String[] displayType) {
		for (myObserver observer : observers) {
			observer.update(displayType);
		}
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

	public boolean testConflictWithCurrentTasks(Task newTask, ArrayList<Task> deadlines, ArrayList<Task> events) {
		return isConflictWithCurrentTasks(newTask, deadlines, events);
	}

	/**
	 * temporary methods to be removed after refactor
	 */

	public ArrayList<Task> searchWord(String wordToBeSearched) {
		return searcher.searchWord(wordToBeSearched, todo, deadlines, events);
	}
}
