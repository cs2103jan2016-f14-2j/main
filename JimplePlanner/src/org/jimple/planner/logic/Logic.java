package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.exceptions.*;
import org.jimple.planner.observers.myObserver;

import org.jimple.planner.parser.InputStruct;
import org.jimple.planner.parser.Parser;
import org.jimple.planner.storage.StorageInterface;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.task.Task;
import org.jimple.planner.task.TaskLabel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

//@@author A0124952E
public class Logic implements LogicMasterListModification, LogicTaskModification {

	private ArrayList<Task> deadlines;
	private ArrayList<Task> todo;
	private ArrayList<Task> events;
	private ArrayList<Task> agenda;
	private ArrayList<Task> tempHistory;
	private ArrayList<Task> searchResults;
	private ArrayList<Task> archivedTasks;
	private ArrayList<Task> conflictedTasks;
	private ArrayList<String> pastUserInputs;
	private ArrayList<myObserver> observers;
	private ArrayList<TaskLabel> taskLabels;
	private LinkedList<LogicPreviousTask> undoTasks;
	private HashMap<Integer, Boolean> idHash;

	private Parser parser;
	private StorageInterface store;
	private LogicAdd adder;
	private LogicEdit editer;
	private LogicDelete deleter;
	private LogicSearch searcher;
	private LogicDirectory directer;
	private LogicUndo undoer;
	private LogicLabel labeler;
	private LogicArchive archiver;
	private LogicConflict conflictChecker;
	static Logger logger = Logger.getLogger(Logic.class.getName());

	// @@author A0124952E
	public Logic() {
		agenda = new ArrayList<Task>();
		tempHistory = new ArrayList<Task>();
		searchResults = new ArrayList<Task>();
		archivedTasks = new ArrayList<Task>();
		conflictedTasks = new ArrayList<Task>();
		undoTasks = new LinkedList<LogicPreviousTask>();
		pastUserInputs = new ArrayList<String>();
		observers = new ArrayList<myObserver>();
		taskLabels = new ArrayList<TaskLabel>();
		idHash = new HashMap<Integer, Boolean>();
		parser = new Parser();
		store = new Storage();
		adder = new LogicAdd();
		editer = new LogicEdit();
		deleter = new LogicDelete();
		searcher = new LogicSearch();
		directer = new LogicDirectory();
		undoer = new LogicUndo();
		labeler = new LogicLabel();
		archiver = new LogicArchive();
		conflictChecker = new LogicConflict();
		try {
			initializeIDMap();
			ArrayList<ArrayList<Task>> allTasks = store.getTasks();
			taskLabels = store.getLabels();
			assignTaskIds(allTasks, idHash);
			LogicLinkLabelsToTasks.linkTasksToLabels(allTasks, taskLabels);
			todo = allTasks.get(0);
			deadlines = allTasks.get(1);
			events = allTasks.get(2);
			archivedTasks = allTasks.get(3);
			checkOverCurrentTime(deadlines, events);
			conflictChecker.checkForAllTasksIfConflictWithCurrentTasks(deadlines, events);
		} catch (IndexOutOfBoundsException e) {
			todo = new ArrayList<Task>();
			deadlines = new ArrayList<Task>();
			events = new ArrayList<Task>();
			logger.log(Level.FINER, e.toString(), e);
			logger.log(Level.FINER, "First initialization of jimple files");
		}
	}

	// @@author A0124952E
	/**
	 * function is for the UI to call when a user inputs a string
	 */
	public void execute(String inputString) throws IOException {
		String[] feedback = new String[2];
		pastUserInputs.add(inputString);
		try {
			InputStruct parsedInput = parser.parseInput(inputString);
			switch (parsedInput.getCommand()) {
			case Constants.STRING_DELETE:
				feedback[0] = deleter.deleteTask(parsedInput.getVariableArray(), todo, deadlines, events, archivedTasks,
						undoTasks, idHash);
				feedback[1] = "";
				break;
			case Constants.STRING_ADD:
				feedback[0] = adder.addToTaskList(parsedInput.getVariableArray(), tempHistory, todo, deadlines, events,
						taskLabels, undoTasks, idHash);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_EDIT:
				feedback[0] = editer.editTask(parsedInput.getVariableArray(), todo, deadlines, events, tempHistory,
						taskLabels, undoTasks, idHash);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_SEARCH:
				searchResults.clear();
				searchResults = searcher.searchWord(parsedInput.getVariableArray()[0], todo, deadlines, events,
						archivedTasks);
				feedback[0] = "";
				feedback[1] = Constants.TYPE_SEARCH;
				break;
			case Constants.STRING_CHANGEDIR:
				feedback[0] = directer.changeSaveDirectory(store, conflictChecker, parsedInput.getVariableArray(), todo,
						deadlines, events, archivedTasks, taskLabels, idHash);
				feedback[1] = "";
				break;
			case Constants.STRING_UNDOTASK:
				feedback[0] = undoer.undoPreviousChange(undoTasks, todo, deadlines, events, archivedTasks, tempHistory,
						taskLabels, idHash);
				feedback[1] = "";
				break;
			case Constants.STRING_HELP:
				feedback[0] = helpCommand();
				feedback[1] = Constants.TYPE_HELP;
				break;
			case Constants.STRING_CHECKDIR:
				feedback[0] = directer.checkPath(store);
				feedback[1] = "";
				break;
			case Constants.STRING_EDITLABEL:
				feedback[0] = labeler.changeLabel(parsedInput.getVariableArray(), taskLabels, todo, deadlines, events);
				feedback[1] = "";
				break;
			case Constants.STRING_DELETELABEL:
				feedback[0] = labeler.deleteLabel(parsedInput.getVariableArray(), taskLabels, todo, deadlines, events,
						archivedTasks);
				feedback[1] = "";
				break;
			case Constants.STRING_DONE:
				feedback[0] = archiver.markTaskAsDone(parsedInput.getVariableArray(), undoTasks, tempHistory, todo,
						deadlines, events, archivedTasks, taskLabels);
				feedback[1] = getTaskID() + Constants.TYPE_ARCHIVE;
				break;
			case Constants.STRING_RETURN:
				feedback[0] = archiver.markTaskAsUndone(parsedInput.getVariableArray(), undoTasks, tempHistory, todo,
						deadlines, events, archivedTasks, taskLabels);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_CHECKCONFLICT:
				conflictedTasks = conflictChecker.getConflictedTasks(parsedInput.getVariableArray(), deadlines, events);
				feedback[0] = "";
				feedback[1] = Constants.TYPE_CONFLICTED;
				break;
			default:
				feedback[0] = Constants.ERROR_WRONG_COMMAND_FEEDBACK;
				feedback[1] = "";
				break;
			}
		} catch (DuplicateDateTimeFieldException dfe) {
			feedback[0] = dfe.getMessage();
			feedback[1] = "";
			logger.log(Level.WARNING, dfe.toString(), dfe);
			logger.log(Level.WARNING, dfe.getMessage());
			logger.log(Level.WARNING, "User input: " + inputString + "\n");
		} catch (InvalidCommandException ice) {
			feedback[0] = ice.getMessage();
			feedback[1] = "";
			logger.log(Level.WARNING, ice.toString(), ice);
			logger.log(Level.WARNING, ice.getMessage());
			logger.log(Level.WARNING, "User input: "+ inputString + "\n");
		} catch (InvalidDateTimeFieldException ife) {
			feedback[0] = ife.getMessage();
			feedback[1] = "";
			logger.log(Level.WARNING, ife.toString(), ife);
			logger.log(Level.WARNING, ife.getMessage());
			logger.log(Level.WARNING, "User input: "+ inputString + "\n");
		} catch (InvalidFromAndToTimeException ift) {
			feedback[0] = ift.getMessage();
			feedback[1] = "";
			logger.log(Level.WARNING, ift.toString(), ift);
			logger.log(Level.WARNING, ift.getMessage());
			logger.log(Level.WARNING, inputString + "\n");
		} catch (MissingDateTimeFieldException mfe) {
			feedback[0] = mfe.getMessage();
			feedback[1] = "";
			logger.log(Level.WARNING, mfe.toString(), mfe);
			logger.log(Level.WARNING, mfe.getMessage());
			logger.log(Level.WARNING, "User input: " + inputString + "\n");
		} catch (IOException io) {
			logger.log(Level.SEVERE, io.toString(), io);
			logger.log(Level.SEVERE, "Jimple files does not exist\n");
		} catch (Exception e) {
			feedback[0] = Constants.ERROR_WRONG_INPUT_FEEDBACK;
			feedback[1] = "";
			logger.log(Level.WARNING, e.toString(), e);
			logger.log(Level.WARNING, "Unhandled exception\n");
			logger.log(Level.WARNING, "User input: " + inputString + "\n");
		}
		conflictChecker.checkForAllTasksIfConflictWithCurrentTasks(deadlines, events);
		packageForSavingMasterLists(store, todo, deadlines, events, archivedTasks);
		packageForSavingLabelLists(store, taskLabels);
		notifyAllObservers(feedback);
	}

	//@@author generated
	public ArrayList<Task> getToDoList() {
		return todo;
	}

	//@@author generated
	public ArrayList<Task> getDeadlinesList() {
		checkOverCurrentTime(deadlines, events);
		return deadlines;
	}

	//@@author generated
	public ArrayList<Task> getEventsDividedList() {
		checkOverCurrentTime(deadlines, events);
		ArrayList<Task> dividedTasks = getDividedTasks(events);
		return dividedTasks;
	}

	//@@author generated
	public ArrayList<Task> getEventsList() {
		checkOverCurrentTime(deadlines, events);
		return events;
	}

	// @@author A0124952E
	public ArrayList<Task> getSearchList() {
		searchResults.clear();
		searchResults = searcher.searchWord(LogicSearch.mostRecentlySearchedWord, todo, deadlines, events,
				archivedTasks);
		return searchResults;
	}

	// @@author A0124952E
	public ArrayList<Task> getAgendaList() {
		agenda.clear();
		checkOverCurrentTime(deadlines, events);
		ArrayList<Task> dividedTasks = getDividedTasks(events);
		agenda.addAll(deadlines);
		agenda.addAll(dividedTasks);
		Task.sortTasksByTime(agenda);
		return agenda;
	}

	//@@author generated
	public ArrayList<Task> getArchivedList() {
		return archivedTasks;
	}
	
	//@@author generated
	public ArrayList<Task> getConflictedTasks() {
		conflictedTasks = conflictChecker.getConflictedTasks(LogicConflict.mostRecentlyCheckedConflict, deadlines,
				events);
		return conflictedTasks;
	}

	//@@author generated
	public ArrayList<TaskLabel> getTaskLabels() {
		return taskLabels;
	}

	// @@author A0124952E
	public String getPastInputs(int cmdHistoryPointer) {
		if (!pastUserInputs.isEmpty()) {
			return pastUserInputs.get(cmdHistoryPointer);
		}
		return "";
	}

	// @@author A0124952E
	private String getTaskTypeAndTaskID() {
		if (!tempHistory.isEmpty()) {
			return Integer.toString(tempHistory.get(tempHistory.size() - 1).getTaskId())
					+ tempHistory.get(tempHistory.size() - 1).getType();
		} else {
			return "";
		}
	}

	// @@author A0124952E
	private String getTaskID() {
		if (!tempHistory.isEmpty()) {
			return Integer.toString(tempHistory.get(tempHistory.size() - 1).getTaskId());
		} else {
			return "";
		}
	}

	// @@author A0124952E
	private void initializeIDMap() {
		for (int i = 0; i < Constants.MAX_ID; i++) {
			idHash.put(i + 1, false);
		}
	}

	// @@author A0124952E
	public void attach(myObserver observer) {
		observers.add(observer);
	}

	// @@author A0124952E
	public void notifyAllObservers() {
		for (myObserver observer : observers) {
			observer.update();
		}
	}

	// @@author A0124952E
	public void notifyAllObservers(String[] displayType) {
		for (myObserver observer : observers) {
			observer.update(displayType);
		}
	}

	// @@author A0124952E
	public void refreshLists() {
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				notifyAllObservers();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	// @@author A0124952E
	/**
	 * gets a list of help commands for user to refer to
	 *
	 */
	private String helpCommand() {
		String listOfCommands = new String();
		listOfCommands += Constants.NEWLINE;
		listOfCommands += Constants.TIME_FORMAT_HELP_HEADER;
		listOfCommands += Constants.TIME_FORMAT;

		listOfCommands += Constants.ADD_HELP_HEADER;
		listOfCommands += Constants.ADD_COMMAND_BY;
		listOfCommands += Constants.ADD_COMMAND_AT;
		listOfCommands += Constants.ADD_COMMAND_ON;
		listOfCommands += Constants.ADD_COMMAND_FROMTO;

		listOfCommands += Constants.EDIT_HELP_HEADER;
		listOfCommands += Constants.EDIT_COMMAND_NAME;
		listOfCommands += Constants.EDIT_COMMAND_BY_TIME;
		listOfCommands += Constants.EDIT_COMMAND_FROMTO_TIME;
		listOfCommands += Constants.EDIT_COMMAND_LABEL;

		listOfCommands += Constants.DELETE_HELP_HEADER;
		listOfCommands += Constants.DELETE_COMMAND;

		listOfCommands += Constants.SEARCH_HELP_HEADER;
		listOfCommands += Constants.SEARCH_COMMAND;

		listOfCommands += Constants.UNDOTASK_HELP_HEADER;
		listOfCommands += Constants.UNDOTASK_COMMAND;

		listOfCommands += Constants.CHANGEDIR_HELP_HEADER;
		listOfCommands += Constants.CHANGEDIR_COMMAND;

		listOfCommands += Constants.CHECKDIR_HELP_HEADER;
		listOfCommands += Constants.CHECKDIR_COMMAND;

		listOfCommands += Constants.EDITLABEL_HELP_HEADER;
		listOfCommands += Constants.EDITLABEL_COMMAND;

		listOfCommands += Constants.DELETELABEL_HELP_HEADER;
		listOfCommands += Constants.DELETELABEL_COMMAND;

		listOfCommands += Constants.ARCHIVE_HELP_HEADER;
		listOfCommands += Constants.ARCHIVE_COMMAND;

		listOfCommands += Constants.UNARCHIVE_HELP_HEADER;
		listOfCommands += Constants.UNARCHIVE_COMMAND;
		return listOfCommands;
	}

}
