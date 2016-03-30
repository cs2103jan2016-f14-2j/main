package org.jimple.planner.logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Constants;
import org.jimple.planner.Formatter;
import org.jimple.planner.Task;
import org.jimple.planner.TaskLabel;
import org.jimple.planner.exceptions.InvalidFromAndToTime;
import org.jimple.planner.exceptions.LabelExceedTotalException;
import org.junit.Test;
import org.jimple.planner.storage.*;

public class LogicTest {
	Formatter testformatter = new Formatter();
	Logic testLogic = new Logic();
	Storage testStore = new StorageComponent();
	LogicAdd testAdder = new LogicAdd();
	LogicEdit testEditer = new LogicEdit();
	LogicDelete testDeleter = new LogicDelete();
	LogicSearch testSearcher = new LogicSearch();
	LogicDirectory testDirecter = new LogicDirectory();
	LogicUndo testUndoer = new LogicUndo();
	LogicLabel testLabeler = new LogicLabel();
	ArrayList<Task> floating = new ArrayList<Task>();
	ArrayList<Task> deadlines = new ArrayList<Task>();
	ArrayList<Task> events = new ArrayList<Task>();
	ArrayList<Task> tempHistory = new ArrayList<Task>();
	ArrayList<TaskLabel> taskLabels = new ArrayList<TaskLabel>();
	LinkedList<LogicPreviousTask> undoTasks = new LinkedList<LogicPreviousTask>();

	/*
	 * @Test public void EditShouldReturnFeedback() throws IOException {
	 * String[] parsedInput = {"1", "go school", "that means NUS",
	 * "29 february 12pm", "", ""}; assertEquals("task is edited",
	 * "task edited in planner\n", testLogic.editTask(parsedInput)); }
	 */

	public void initializeThreeArrays() {
		Task todo1 = new Task("a test only one");
		Task deadlines1 = new Task("a test only two");
		Task events1 = new Task("a test only three");
		Task events2 = new Task("a test four");
		todo1.setType("floating");
		todo1.setTaskId(1);
		deadlines1.setType("deadline");
		deadlines1.setTaskId(2);
		deadlines1.setFromDate("2016-03-30T16:00");
		events1.setType("event");
		events1.setTaskId(3);
		events1.setFromDate("2016-03-25T09:00");
		events1.setToDate("2016-03-27T17:00");
		events2.setType("event");
		events2.setTaskId(4);
		events2.setFromDate("2016-03-28T07:00");
		events2.setToDate("2016-03-30T11:00");
		floating.add(todo1);
		deadlines.add(deadlines1);
		events.add(events1);
		events.add(events2);
	}

	@Test
	public void ShouldReturnPrettyDate() {
		LocalDateTime testDate = null;
		testDate = LocalDateTime.parse("2016-01-12T15:30");
		assertEquals("12/1/2016", testformatter.formatPrettyDate(testDate));
	}
	
	/**
	 * EP: 2 test cases
	 * 1. change label color
	 * 2. change label name
	 * 3. no label to change
	 * @throws LabelExceedTotalException 
	 */
	@Test
	public void ShouldReturnChangedLabel() throws LabelExceedTotalException	{
		taskLabels.add(TaskLabel.getNewLabel("food"));
		taskLabels.add(TaskLabel.getNewLabel("work"));
		String[] variableArray1 = {"food", "orange"};
		String[] variableArray2 = {"exercise", "green"};
		String[] variableArray3 = {"stuff", "yellow"};
		assertEquals("food name changed to orange", testLabeler.changeLabel(variableArray1, taskLabels));
		assertEquals("green colour changed to exercise", testLabeler.changeLabel(variableArray2, taskLabels));
		assertEquals("label could not be changed", testLabeler.changeLabel(variableArray3, taskLabels));
	}
	
	@Test
	public void ShouldReturnMultipleTaskOfDifferentDays()	{
		Task aTask = new Task("test Task");
		aTask.setFromDate("2016-04-10T10:00");
		aTask.setToDate("2016-04-12T15:00");
		ArrayList<Task>	modifiedTasks = new ArrayList<Task>();
		while (!LogicTaskModification.isFromDateEqualToDate(aTask))	{
			Task dividedTask = LogicTaskModification.divideMultipleDays(aTask);
			modifiedTasks.add(dividedTask);
		}
		modifiedTasks.add(aTask);
		assertEquals("2016-04-10T10:00", modifiedTasks.get(0).getFromTimeString());
		assertEquals("2016-04-10T23:59", modifiedTasks.get(0).getToTimeString());
		assertEquals("2016-04-11T00:00", modifiedTasks.get(1).getFromTimeString());
		assertEquals("2016-04-11T23:59", modifiedTasks.get(1).getToTimeString());
		assertEquals("2016-04-12T00:00", modifiedTasks.get(2).getFromTimeString());
		assertEquals("2016-04-12T15:00", modifiedTasks.get(2).getToTimeString());
	}
	
	/**
	 * EP: 5 test cases 1. Empty 2. when undo is for an "add" command 3. when
	 * undo is for a "delete" command 4. when undo is for a "edit" command 5.
	 * when delete cache is >20
	 * 
	 * @throws InvalidFromAndToTime
	 * @throws LabelExceedTotalException 
	 */
	@Test
	public void ShouldReturnUndoCommand() throws IOException, InvalidFromAndToTime, LabelExceedTotalException {
		String[] variableArray1 = { "1" };
		String[] variableArray2 = { "todo", "task to be undone", null, null, null, null };
		String[] variableArray3 = { "1", "edit task to be undone", null, null, null, null };

		// test for empty
		assertEquals(Constants.UNDO_FEEDBACK_ERROR,
				testUndoer.undoPreviousChange(testStore, undoTasks, floating, deadlines, events, tempHistory));
		initializeThreeArrays();

		testDeleter.deleteTask(testStore, variableArray1, floating, deadlines, events, undoTasks);
		assertEquals("task \"" + "a test only one" + "\"" + Constants.UNDO_FEEDBACK,
				testUndoer.undoPreviousChange(testStore, undoTasks, floating, deadlines, events, tempHistory));
		// test for add
		testAdder.addToTaskList(testStore, variableArray2, tempHistory, floating, deadlines, events, taskLabels, undoTasks);
		assertEquals("task \"" + "task to be undone" + "\"" + Constants.UNDO_FEEDBACK,
				testUndoer.undoPreviousChange(testStore, undoTasks, floating, deadlines, events, tempHistory));
		// test for edit
		testEditer.editTask(testStore, variableArray3, floating, deadlines, events, tempHistory, taskLabels, undoTasks);
		assertEquals("task \"" + "edit task to be undone" + "\"" + Constants.UNDO_FEEDBACK,
				testUndoer.undoPreviousChange(testStore, undoTasks, floating, deadlines, events, tempHistory));

		// test for overshot
		for (int i = 0; i < 22; i++) {
			LogicPreviousTask testTask = new LogicPreviousTask("floating", new Task(Integer.toString(i)));
			undoTasks.add(testTask);
		}
		assertEquals("task \"" + "21" + "\"" + Constants.UNDO_FEEDBACK,
				testUndoer.undoPreviousChange(testStore, undoTasks, floating, deadlines, events, tempHistory));
	}

	/*
	 * EP: 2 cases 1. Task is found and deleted 2. Task cannot be found and no
	 * deletion
	 */
	@Test
	public void ShouldReturnTrueAfterDeleteFromArray() throws IOException {
		String[] variableArray = { "1" };
		initializeThreeArrays();
		assertTrue("delete string", testDeleter.testFindTaskToDelete(variableArray, floating, undoTasks));
		variableArray[0] = "4";
		assertFalse("delete string", testDeleter.testFindTaskToDelete(variableArray, floating, undoTasks));
	}

	/*
	 * EP: 2 cases 1. able to find task and edit 2. unable to find task and no
	 * edit
	 */
	@Test
	public void ShouldReturnTrueAfterEditting() throws IOException, InvalidFromAndToTime, LabelExceedTotalException {
		String[] variableArray = { "1", "task one", null, "2016-03-12T14:00", null, null };
		initializeThreeArrays();
		assertTrue("return true after editting", testEditer.testFindTaskToEdit(variableArray, floating, floating,
				deadlines, events, tempHistory, taskLabels, undoTasks));
		variableArray[0] = "6";
		assertFalse("return true after editting", testEditer.testFindTaskToEdit(variableArray, floating, floating,
				deadlines, events, tempHistory, taskLabels, undoTasks));
	}

	/*
	 * EP: 2 cases 1. able to find task and edit and store inside storage 2.
	 * unable to find task and no edit
	 */
	@Test
	public void ShouldReturnFeedbackAfterCheckThreeArrayToEdit() throws IOException, InvalidFromAndToTime, LabelExceedTotalException {
		String[] variableArray = { "1", "task one", null, "2016-03-30T05:00", null, null };
		initializeThreeArrays();
		assertEquals("return same string", "task 1 edited in planner",
				testEditer.testEditTask(testStore, variableArray, floating, deadlines, events, tempHistory, taskLabels, undoTasks));
		variableArray[0] = "10";
		assertEquals("return same string", "task 10 could not be edited",
				testEditer.testEditTask(testStore, variableArray, floating, deadlines, events, tempHistory, taskLabels, undoTasks));
	}

	/**
	 * EP: 5 cases 
	 * 1. add for deadlines 
	 * 2. add for events 
	 * 3. add for todo
	 * 4. add a label when does not exist
	 * 5. add a label when exist
	 * @throws LabelExceedTotalException 
	 */
	@Test
	public void ShouldReturnFeedbackAfterAdding() throws IOException, LabelExceedTotalException {
		String[] parsedInput1 = { "deadlines", "finish 2103 homework", null, "2016-03-28T13:00", null, null };
		assertEquals("task is added to file", "\"finish 2103 homework\" added to planner", testAdder
				.addToTaskList(testStore, parsedInput1, tempHistory, floating, deadlines, events, taskLabels, undoTasks));
		
		String[] parsedInput2 = { "events", "finish 2010 homework", null, "2016-03-29T12:00", "2016-03-30T13:00",
				null };
		assertEquals("\"finish 2010 homework\" added to planner", testAdder.addToTaskList(testStore, parsedInput2,
				tempHistory, floating, deadlines, events, taskLabels, undoTasks));
		
		String[] parsedInput3 = { "todo", "finish 3241 homework", null, null, null, null };
		assertEquals("\"finish 3241 homework\" added to planner", testAdder.addToTaskList(testStore, parsedInput3,
				tempHistory, floating, deadlines, events, taskLabels, undoTasks));
		
		String[] parsedInput4 = { "todo", "finish 3241 homework", null, null, null, "work" };
		testAdder.addToTaskList(testStore, parsedInput4, tempHistory, floating, deadlines, events, taskLabels, undoTasks);
		assertEquals("work", taskLabels.get(0).getLabelName());
		
		taskLabels.add(TaskLabel.getNewLabel("food"));
		String[] parsedInput5 = { "todo", "buy hotpot ingredients", null, null, null, "food" };
		testAdder.addToTaskList(testStore, parsedInput5, tempHistory, floating, deadlines, events, taskLabels, undoTasks);
		assertEquals("food", floating.get(2).getTaskLabel().getLabelName());
	}

	/**
	 * EP: 2 cases 1. Return all tasks with titles inside name 2. Return empty
	 * list
	 */
	@Test
	public void ShoudReturnListOfTasksFromSearching() {
		String wordToBeSearched = "only";
		ArrayList<Task> list = new ArrayList<Task>();
		list.add(new Task("stuff and stuff"));
		list.add(new Task("some stuff only"));
		list.add(new Task("only stuff"));

		ArrayList<Task> searchResults = testSearcher.testgetSearchedTasks(wordToBeSearched, list);
		assertEquals("some stuff only", searchResults.get(0).getTitle());
		assertEquals("only stuff", searchResults.get(1).getTitle());
		wordToBeSearched = "thing";
		searchResults = testSearcher.testgetSearchedTasks(wordToBeSearched, list);
		assertTrue(searchResults.isEmpty());
	}

	/**
	 * Searches for strings in master arrays and returns results that are the same
	 */
	@Test
	public void ShouldReturnArrayListOfTasks() throws IOException {
		ArrayList<Task> expected = new ArrayList<Task>();
		String wordToBeSearched = "only";
		assertEquals("should be same", expected,
				testSearcher.testSearchWord(wordToBeSearched, floating, deadlines, events));
		initializeThreeArrays();
		expected.add(new Task("a test only one"));
		expected.add(new Task("a test only two"));
		expected.add(new Task("a test only three"));
		ArrayList<Task> result = testSearcher.testSearchWord(wordToBeSearched, floating, deadlines, events);
		assertEquals("should be the same", expected.get(0).getTitle(), result.get(0).getTitle());
		assertEquals("should be the same", expected.get(1).getTitle(), result.get(1).getTitle());
		assertEquals("should be the same", expected.get(2).getTitle(), result.get(2).getTitle());
	}

	@Test
	public void ShouldReturnTrueIfContainKeyword() {
		Task event = new Task("a test only");
		event.setDescription("good stuff");
		assertTrue("returns true", testSearcher.testIsContainKeyword(event, "good"));
		assertTrue("returns true", testSearcher.testIsContainKeyword(event, "a test only"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "wrong"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "good food"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "a only"));
	}

	@Test
	public void ShouldReturnCorrectFormatMessage() {
		assertEquals("return formated date", "2016-05-12T16:00", testformatter.testFormatTime("12 May 4pm"));
		assertEquals("return formated date", "2016-03-30T14:30", testformatter.testFormatTime("today 2.30pm"));
		assertEquals("return formated date", "2018-12-18T00:00", testformatter.testFormatTime("2018 12am 18 december"));
		assertEquals("return formated date", "2016-03-30T23:00", testformatter.testFormatTime("11pm"));
	}

	@Test
	public void ShouldReturnCorrectYear() {
		assertEquals("return year", null, testformatter.testCheckYear("today"));
		assertEquals("return year", "2018", testformatter.testCheckYear("2018"));
		assertEquals("return year", null, testformatter.testCheckYear("may"));
		assertEquals("return year", null, testformatter.testCheckYear("6"));
		// assertEquals("returns feedback", "taskEd")
	}

	@Test
	public void ShoudReturnCorrectMonth() {
		assertEquals("return month", "-02-", testformatter.testCheckMonth("february"));
		assertEquals("return month", "-05-", testformatter.testCheckMonth("May"));
		assertEquals("return month", "-12-", testformatter.testCheckMonth("december"));
		assertEquals("return month", "", testformatter.testCheckMonth("today"));
		assertEquals("return month", "", testformatter.testCheckMonth("10"));
		assertEquals("return month", "", testformatter.testCheckMonth("2018"));
	}

	@Test
	public void ShouldReturnCorrectDay() {
		assertEquals("return day", "10T", testformatter.testCheckDay("10"));
		assertEquals("return day", "05T", testformatter.testCheckDay("5"));
		assertEquals("return day", "", testformatter.testCheckDay("today"));
		assertEquals("return day", "", testformatter.testCheckDay("march"));
		assertEquals("return day", "", testformatter.testCheckDay("2017"));
	}

	@Test
	public void ShouldReturnCorrectTime() {
		assertEquals("return time", "00:00", testformatter.testCheckTime("12am"));
		assertEquals("return time", "09:00", testformatter.testCheckTime("9am"));
		assertEquals("return time", "12:30", testformatter.testCheckTime("12.30pm"));
		assertEquals("return time", "", testformatter.testCheckTime("today"));
		assertEquals("return time", "", testformatter.testCheckTime("2017"));
		assertEquals("return time", "", testformatter.testCheckTime("12"));
	}

}
