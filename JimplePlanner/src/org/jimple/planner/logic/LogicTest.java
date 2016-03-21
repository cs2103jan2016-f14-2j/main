package org.jimple.planner.logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.jimple.planner.Formatter;
import org.jimple.planner.storage.Storage;
import org.jimple.planner.storage.StorageComponent;
import org.jimple.planner.Task;
import org.junit.Test;

public class LogicTest {
	Formatter testformatter = new Formatter();
	Logic testLogic = new Logic();
	Storage testStore = new StorageComponent();
	LogicAdd testAdder = new LogicAdd();
	LogicEdit testEditer = new LogicEdit();
	LogicDelete testDeleter = new LogicDelete();
	LogicSearch testSearcher = new LogicSearch();
	LogicDirectory testDirecter = new LogicDirectory();
	ArrayList<Task>	floating = new ArrayList<Task>();
	ArrayList<Task>	deadlines = new ArrayList<Task>();
	ArrayList<Task>	events = new ArrayList<Task>();
	ArrayList<Task> deletedTasks = new ArrayList<Task>();
	ArrayList<Task>	tempHistory = new ArrayList<Task>();
	
	/*@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "29 february 12pm", "", ""};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}*/
	
	public void initializeThreeArrays()	{
		Task todo1 = new Task("a test only one");
		Task deadlines1 = new Task("a test only two");
		Task events1 = new Task("a test only three");
		Task events2 = new Task("a test four");
		todo1.setType("floating");
		todo1.setTaskId(1);
		deadlines1.setType("deadline");
		deadlines1.setTaskId(2);
		events1.setType("event");
		events1.setTaskId(3);
		events2.setType("event");
		events2.setTaskId(4);
		floating.add(todo1);
		deadlines.add(deadlines1);
		events.add(events1);
		events.add(events2);
	}
	@Test
	public void ShouldReturnPrettyDate()	{
		LocalDateTime testDate = null;
		testDate = LocalDateTime.parse("2016-01-12T15:30");
		assertEquals("12/1/2016", testformatter.formatPrettyDate(testDate));
	}
	
	@Test
	public void ShouldReturnTrueIfNoTimeConflict()	{
		Task event1 = new Task("a test only one");
		event1.setToDate("2016-03-10T16:00");
		Task event2 = new Task("a test only two");
		event2.setFromDate("2016-03-11T09:00");
		event2.setToDate("2016-03-11T17:00");
		Task event3 = new Task("a test only three");
		event3.setToDate("2016-03-10T16:00");
		Task event4 = new Task("a test only three");
		event4 = new Task("a test only four");
		deadlines.add(event1);
		events.add(event2);
		assertTrue("returns true", testLogic.testConflictWithCurrentTasks(event3, deadlines, events));
		event4.setFromDate("2016-03-11T07:00");
		event4.setToDate("2016-03-11T16:00");
		assertTrue("returns true", testLogic.testConflictWithCurrentTasks(event4, deadlines, events));
		event4.setFromDate("2016-03-11T10:00");
		event4.setToDate("2016-03-11T18:00");
		assertTrue("returns true", testLogic.testConflictWithCurrentTasks(event4, deadlines, events));
		event4.setFromDate("2016-03-11T12:00");
		event4.setToDate("2016-03-11T13:00");
		assertTrue("returns true", testLogic.testConflictWithCurrentTasks(event4, deadlines, events));
		event4.setFromDate("2016-03-11T18:00");
		event4.setToDate("2016-03-11T19:00");
		assertFalse("returns false", testLogic.testConflictWithCurrentTasks(event4, deadlines, events));
	}
	
	@Test
	public void ShouldReturnTrueAfterDeleteFromArray() throws IOException	{
		String[] variableArray = {"1"};
		initializeThreeArrays();
		assertTrue("delete string", testDeleter.testFindTaskToDelete(variableArray, floating, deletedTasks));
		variableArray[0] = "4";
		assertFalse("delete string", testDeleter.testFindTaskToDelete(variableArray, floating, deletedTasks));
	}
	
	@Test
	public void ShouldReturnTrueAfterEditting()	throws IOException{
		String[] variableArray = {"1", "task one", null, null, "2016-03-12T14:00", null};
		initializeThreeArrays();
		assertTrue("return true after editting", testEditer.testFindTaskToEdit(variableArray, floating, deadlines, events));
	}
	
	@Test
	public void ShouldReturnFeedbackAfterCheckThreeArrayToEdit() throws IOException	{
		String[] variableArray = {"0", "task one", null, null, "2016-03-02T05:00", null};
		initializeThreeArrays();
		//assertEquals("return same string", "task edited in planner", testEditer.testEditTask(testStore, variableArray, floating, deadlines, events));
		variableArray[0] = "10";
		assertEquals("return same string", "task could not be editted", testEditer.testEditTask(testStore, variableArray, floating, deadlines, events));
	}
	
	@Test
	public void AddShouldReturnFeedback() throws IOException {
		String[] parsedInput1 = {"finish 2103 homework", null, null, "2016-03-09T13:00", null};
		assertEquals("task is added to file", "task added to planner", testAdder.testAddToTaskList(testStore, parsedInput1, tempHistory, floating, deadlines, events));
	}
	
	@Test
	public void ShoudReturnListOfTasksFromSearching()	{
		String wordToBeSearched = "only";
		ArrayList<Task> list = new ArrayList<Task>();
		list.add(new Task("stuff and stuff"));
		list.add(new Task("some stuff only"));
		list.add(new Task ("only stuff"));
		
		ArrayList<Task> searchResults = testSearcher.testgetSearchedTasks(wordToBeSearched, list);
		assertEquals("some stuff only", searchResults.get(0).getTitle());
		assertEquals("only stuff", searchResults.get(1).getTitle());
	}
	
	@Test
	public void ShouldReturnArrayListOfTasks() throws IOException	{
		ArrayList<Task> expected = new ArrayList<Task>();
		String wordToBeSearched = "only";
		assertEquals("should be same", expected, testSearcher.testSearchWord(wordToBeSearched, floating, deadlines, events));
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
	public void ShouldReturnTrueIfContainKeyword()	{
		Task event = new Task("a test only");
		event.setDescription("good stuff");
		event.setCategory("food");
		assertTrue("returns true", testSearcher.testIsContainKeyword(event, "good"));
		assertTrue("returns true", testSearcher.testIsContainKeyword(event, "a test only"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "wrong"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "good food"));
		assertFalse("returns false", testSearcher.testIsContainKeyword(event, "a only"));
	}
	
	@Test
	public void ShouldReturnCorrectFormatMessage()	{
		assertEquals("return formated date", "2016-05-12T16:00", testformatter.testFormatTime("12 May 4pm"));
		assertEquals("return formated date", "2016-03-21T14:30", testformatter.testFormatTime("today 2.30pm"));
		assertEquals("return formated date", "2018-12-18T00:00", testformatter.testFormatTime("2018 12am 18 december"));
		assertEquals("return formated date", "2016-03-21T23:00", testformatter.testFormatTime("11pm"));
	}
	
	@Test
	public void ShouldReturnCorrectYear()	{
		assertEquals("return year", null, testformatter.testCheckYear("today"));
		assertEquals("return year", "2018", testformatter.testCheckYear("2018"));
		assertEquals("return year", null, testformatter.testCheckYear("may"));
		assertEquals("return year", null, testformatter.testCheckYear("6"));
		//assertEquals("returns feedback", "taskEd")
	}
	
	@Test
	public void ShoudReturnCorrectMonth()	{
		assertEquals("return month", "-02-", testformatter.testCheckMonth("february"));
		assertEquals("return month", "-05-", testformatter.testCheckMonth("May"));
		assertEquals("return month", "-12-", testformatter.testCheckMonth("december"));
		assertEquals("return month", "", testformatter.testCheckMonth("today"));
		assertEquals("return month", "", testformatter.testCheckMonth("10"));
		assertEquals("return month", "", testformatter.testCheckMonth("2018"));
	}
	
	@Test
	public void ShouldReturnCorrectDay()	{
		assertEquals("return day", "10T", testformatter.testCheckDay("10"));
		assertEquals("return day", "05T", testformatter.testCheckDay("5"));
		assertEquals("return day", "", testformatter.testCheckDay("today"));
		assertEquals("return day", "", testformatter.testCheckDay("march"));
		assertEquals("return day", "", testformatter.testCheckDay("2017"));
	}
	
	@Test
	public void ShouldReturnCorrectTime()	{
		assertEquals("return time", "00:00", testformatter.testCheckTime("12am"));
		assertEquals("return time", "09:00", testformatter.testCheckTime("9am"));
		assertEquals("return time", "12:30", testformatter.testCheckTime("12.30pm"));
		assertEquals("return time", "", testformatter.testCheckTime("today"));
		assertEquals("return time", "", testformatter.testCheckTime("2017"));
		assertEquals("return time", "", testformatter.testCheckTime("12"));
	}

}
