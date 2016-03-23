package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.jimple.planner.Task;
import org.jimple.planner.observers.myObserver;

import org.jimple.planner.InputStruct;
import org.jimple.planner.Parser;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.storage.StorageComponent;
import parserExceptions.*;

import org.jimple.planner.Constants;

import java.util.Collections;

public class Logic {

	private ArrayList<Task> deadlines;
	private ArrayList<Task> todo;
	private ArrayList<Task> events;
	private ArrayList<Task> agenda;
	private ArrayList<Task> tempHistory;
	private ArrayList<Task> searchResults;
	private ArrayList<Task> deletedTasks;
	private ArrayList<String> pastUserInputs;
	private ArrayList<myObserver> observers;
	Parser parser;
	Storage store;
	LogicAdd adder;
	LogicEdit editer;
	LogicDelete deleter;
	LogicSearch searcher;
	LogicDirectory directer;
	LogicUndo undoer;

	public Logic() {
		agenda = new ArrayList<Task>();
		tempHistory = new ArrayList<Task>();
		searchResults = new ArrayList<Task>();
		deletedTasks = new ArrayList<Task>();
		pastUserInputs = new ArrayList<String>();
		observers = new ArrayList<myObserver>();
		parser = new Parser();
		store = new StorageComponent();
		adder = new LogicAdd();
		editer = new LogicEdit();
		deleter = new LogicDelete();
		searcher = new LogicSearch();
		directer = new LogicDirectory();
		undoer = new LogicUndo();
		try {
			ArrayList<ArrayList<Task>> allTasks = store.getTasks();
			LogicTaskModification.assignTaskIds(allTasks);
			todo = allTasks.get(0);
			deadlines = allTasks.get(1);
			events = allTasks.get(2);
		} catch (IndexOutOfBoundsException e) {
			todo = new ArrayList<Task>();
			deadlines = new ArrayList<Task>();
			events = new ArrayList<Task>();
		}
	}

	/**
	 * function is for the UI to call when a user inputs a string
	 *
	 */
	public void execute(String inputString) throws IOException {
		String[] feedback = new String[2];
		pastUserInputs.add(inputString);
		try {
			InputStruct parsedInput = parser.parseInput(inputString);
			switch (parsedInput.getCommand()) {
			case Constants.STRING_DELETE:
				feedback[0] = deleter.deleteTask(store, parsedInput.getVariableArray(), todo, deadlines, events,
						deletedTasks);
				feedback[1] = Constants.STRING_DELETE;
				break;
			case Constants.STRING_ADD:
				feedback[0] = adder.addToTaskList(store, parsedInput.getVariableArray(), tempHistory, todo, deadlines,
						events);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_EDIT:
				feedback[0] = editer.editTask(store, parsedInput.getVariableArray(), todo, deadlines, events);
				feedback[1] = Constants.STRING_EDIT;
				break;
			case Constants.STRING_SEARCH:
				searchResults = searcher.searchWord(parsedInput.getVariableArray()[0], todo, deadlines, events);
				feedback[0] = "";
				feedback[1] = Constants.STRING_SEARCH;
				break;
			case Constants.STRING_CHANGEDIR:
				feedback[0] = directer.changeSaveDirectory(store, parsedInput.getVariableArray(), todo, deadlines, events);
				feedback[1] = Constants.STRING_CHANGEDIR;
				break;
			case Constants.STRING_UNDO:
				feedback[0] = undoer.undoPreviousChange(store, deletedTasks, todo, deadlines, events);
				feedback[1] = Constants.STRING_UNDO;
				break;
			case Constants.STRING_HELP:
				feedback[0] = helpCommand();
				feedback[1] = Constants.STRING_HELP;
				break;
			default:
				feedback[0] = Constants.ERROR_WRONG_COMMAND_FEEDBACK;
				feedback[1] = "";
				break;
			}
		} catch (DuplicateDateTimeFieldException dfe) {
			feedback[0] = dfe.getMessage();
			feedback[1] = "";
		} catch (InvalidCommandException ice) {
			feedback[0] = ice.getMessage();
			feedback[1] = "";
		} catch (InvalidDateTimeFieldException ife) {
			feedback[0] = ife.getMessage();
			feedback[1] = "";
		} catch (MissingDateTimeFieldException mfe) {
			feedback[0] = mfe.getMessage();
			feedback[1] = "";
		} catch (Exception e) {
			feedback[0] = Constants.ERROR_WRONG_INPUT_FEEDBACK;
			feedback[1] = "";
		}
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
		searchResults = searcher.searchWord(LogicSearch.mostRecentlySearchedWord, todo, deadlines, events);
		return searchResults;
	}

	public ArrayList<Task> getAgendaList() {
		agenda.clear();
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

	private String getTaskTypeAndTaskID() {
		if (!tempHistory.isEmpty()) {
			return Integer.toString(tempHistory.get(tempHistory.size() - 1).getTaskId())
					+ tempHistory.get(tempHistory.size() - 1).getType();
		} else {
			return "";
		}
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
	private String helpCommand() {
		String listOfCommands = new String();
		listOfCommands += Constants.ADD_HELP_HEADER;
		listOfCommands += Constants.ADD_COMMAND_BY;
		listOfCommands += Constants.ADD_COMMAND_AT;
		listOfCommands += Constants.ADD_COMMAND_FROMTO;

		listOfCommands += Constants.EDIT_HELP_HEADER;
		listOfCommands += Constants.EDIT_COMMAND_ONE_TIMING;
		listOfCommands += Constants.EDIT_COMMAND_TWO_TIMINGS;
		
		listOfCommands += Constants.DELETE_HELP_HEADER;
		listOfCommands += Constants.DELETE_COMMAND;
		
		listOfCommands += Constants.SEARCH_HELP_HEADER;
		listOfCommands += Constants.SEARCH_COMMAND;
		
		listOfCommands += Constants.UNDO_HELP_HEADER;
		listOfCommands += Constants.UNDO_COMMAND;
		
		listOfCommands += Constants.CHANGEDIR_HELP_HEADER;
		listOfCommands += Constants.CHANGEDIR_COMMAND;
		return listOfCommands;
	}

	/**
	 * 
	 * unimplemented methods. may be used in the future
	 */
	private boolean isConflictWithCurrentTasks(Task newTask, ArrayList<Task> deadlines, ArrayList<Task> events) {
		boolean isConflict = false;
		switch (newTask.getType()) {
		case Constants.TYPE_DEADLINE:
			for (int i = 0; i < deadlines.size(); i++) {
				if (newTask.getToTime().equals(deadlines.get(i).getToTime())) {
					isConflict = true;
				}
			}
			break;
		case Constants.TYPE_EVENT:
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
