package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.exceptions.*;
import org.jimple.planner.observers.myObserver;

import org.jimple.planner.InputStruct;
import org.jimple.planner.Parser;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.storage.StorageComponent;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Logic {

	private ArrayList<Task> deadlines;
	private ArrayList<Task> todo;
	private ArrayList<Task> events;
	private ArrayList<Task> agenda;
	private ArrayList<Task> tempHistory;
	private ArrayList<Task> searchResults;
	private ArrayList<String> pastUserInputs;
	private ArrayList<myObserver> observers;
	private LinkedList<LogicPreviousTask> undoTasks;
	private Parser parser;
	private Storage store;
	private ArrayList<TaskLabel> taskLabels;
	private LogicAdd adder;
	private LogicEdit editer;
	private LogicDelete deleter;
	private LogicSearch searcher;
	private LogicDirectory directer;
	private LogicUndo undoer;
	private LogicLabel labeler;

	public Logic() {
		agenda = new ArrayList<Task>();
		tempHistory = new ArrayList<Task>();
		searchResults = new ArrayList<Task>();
		undoTasks = new LinkedList<LogicPreviousTask>();
		pastUserInputs = new ArrayList<String>();
		observers = new ArrayList<myObserver>();
		taskLabels = new ArrayList<TaskLabel>();
		parser = new Parser();
		store = new StorageComponent();
		adder = new LogicAdd();
		editer = new LogicEdit();
		deleter = new LogicDelete();
		searcher = new LogicSearch();
		directer = new LogicDirectory();
		undoer = new LogicUndo();
		labeler = new LogicLabel();
		try {
			ArrayList<ArrayList<Task>> allTasks = store.getTasks();
			taskLabels = store.getLabels();
			LogicMasterListModification.assignTaskIds(allTasks);
			LogicLinkLabelsToTasks.linkTasksToLabels(allTasks, taskLabels);
			todo = allTasks.get(0);
			deadlines = allTasks.get(1);
			events = allTasks.get(2);
			LogicMasterListModification.checkOverCurrentTime(deadlines, events);
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
						undoTasks, taskLabels);
				feedback[1] = "";
				break;
			case Constants.STRING_ADD:
				feedback[0] = adder.addToTaskList(store, parsedInput.getVariableArray(), tempHistory, todo, deadlines,
						events, taskLabels, undoTasks);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_EDIT:
				feedback[0] = editer.editTask(store, parsedInput.getVariableArray(), todo, deadlines, events,
						tempHistory, taskLabels, undoTasks);
				feedback[1] = getTaskTypeAndTaskID();
				break;
			case Constants.STRING_SEARCH:
				searchResults.clear();
				searchResults = searcher.searchWord(parsedInput.getVariableArray()[0], todo, deadlines, events);
				feedback[0] = "";
				feedback[1] = Constants.TYPE_SEARCH;
				break;
			case Constants.STRING_CHANGEDIR:
				feedback[0] = directer.changeSaveDirectory(store, parsedInput.getVariableArray(), todo, deadlines,
						events);
				feedback[1] = "";
				break;
			case Constants.STRING_UNDOTASK:
				feedback[0] = undoer.undoPreviousChange(store, undoTasks, todo, deadlines, events, tempHistory, taskLabels);
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
				feedback[0] = labeler.changeLabel(store, parsedInput.getVariableArray(), taskLabels, todo, deadlines, events);
				feedback[1] = "";
				break;
			case Constants.STRING_DELETELABEL:
				feedback[0] = labeler.deleteLabel(store, parsedInput.getVariableArray(), taskLabels, todo, deadlines, events);
				feedback[1]	= "";
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
		} catch (InvalidFromAndToTimeException ift) {
			feedback[0] =  ift.getMessage();
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
		LogicMasterListModification.checkOverCurrentTime(deadlines, events);
		return deadlines;
	}

	public ArrayList<Task> getEventsList() {
		LogicMasterListModification.checkOverCurrentTime(deadlines, events);
		/*ArrayList<Task> dividedTasks = new ArrayList<Task>();
		for (Task anEvent : events)	{
			Task duplicateEvent = new Task(anEvent);
			while (!duplicateEvent.getFromTime().toLocalDate().equals(duplicateEvent.getToTime().toLocalDate()))	{
				Task aNewTask = LogicTaskModification.divideMultipleDays(duplicateEvent);
				dividedTasks.add(aNewTask);
			}
			dividedTasks.add(duplicateEvent);
		}*/
		return events;
	}

	public ArrayList<Task> getSearchList() {
		searchResults.clear();
		searchResults = searcher.searchWord(LogicSearch.mostRecentlySearchedWord, todo, deadlines, events);
		for (int i=0;i<searchResults.size();i++)	{
			System.out.println(searchResults.get(i).getTitle());
		}
		return searchResults;
	}

	public ArrayList<Task> getAgendaList() {
		agenda.clear();
		LogicMasterListModification.checkOverCurrentTime(deadlines, events);
		agenda.addAll(deadlines);
		agenda.addAll(events);
		Task.sortTasksForAgenda(agenda);
		return agenda;
	}

	public ArrayList<TaskLabel> getTaskLabels()	{
		return taskLabels;
	}
	
	public String getPastInputs(int cmdHistoryPointer) {
		if (!pastUserInputs.isEmpty()) {
			return pastUserInputs.get(cmdHistoryPointer);
		}
		return "";
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

	public void notifyAllObservers() {
		for (myObserver observer : observers) {
			observer.update();
		}
	}

	public void notifyAllObservers(String[] displayType) {
		for (myObserver observer : observers) {
			observer.update(displayType);
		}
	}

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

	/**
	 * gets a list of help commands for user to refer to
	 *
	 */
	private String helpCommand() {
		String listOfCommands = new String();
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

}
