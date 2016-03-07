package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class LogicTest {
	Formatter testformatter = new Formatter();
	Logic testLogic = new Logic();
	ArrayList<Task>	floating = new ArrayList<Task>();
	ArrayList<Task>	deadlines = new ArrayList<Task>();
	ArrayList<Task>	events = new ArrayList<Task>();
	
	/*@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "29 february 12pm", "", ""};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}*/
	
	public void initializeThreeArrays()	{
		Task event1 = new Task("a test only one");
		Task event2 = new Task("a test only two");
		Task event3 = new Task("a test only three");
		Task event4 = new Task("a test four");
		floating.add(event1);
		deadlines.add(event2);
		events.add(event3);
		events.add(event4);
	}
	
	@Test
	public void ShouldReturnFeedbackAfterDeleteFromArray() throws IOException	{
		String[] variableArray = {"2"};
		initializeThreeArrays();
		assertEquals("delete string", "task deleted", testLogic.testDeleteTask(variableArray, floating, deadlines, events));
		variableArray[0] = "4";
		assertEquals("delete string", "could not find task to be deleted", testLogic.testDeleteTask(variableArray, floating, deadlines, events));
	}
	
	@Test
	public void ShouldReturnFeedbackAfterCheckThreeArrayToEdit() throws IOException	{
		String[] variableArray = {"3", "task one", null, null, "today", null};
		initializeThreeArrays();
		assertEquals("return same string", "task edited in planner", testLogic.testEditTask(variableArray, floating, deadlines, events));
		variableArray[0] = "4";
		assertEquals("return same string", "task not found", testLogic.testEditTask(variableArray, floating, deadlines, events));
	}
	
	@Test
	public void AddShouldReturnFeedback() throws IOException {
		String[] parsedInput1 = {"finish 2103 homework", null, null, "3 december 2017 12am", null};
		String[] parsedInput2 = {"finish 2103 homework", null, null, "today", null};
		String[] parsedInput3 = {"finish 2103 homework", null, null, "9 may", null};
		assertEquals("task is added to file", "task added to planner", testLogic.testAddToTaskList(parsedInput1));
		assertEquals("task is added to file", "task added to planner", testLogic.testAddToTaskList(parsedInput2));
		assertEquals("task is added to file", "task added to planner", testLogic.testAddToTaskList(parsedInput3));
	}
	
	@Test
	public void ShouldReturnTrueAfterEditting()	{
		String[] variableArray = {"1", "task one", null, null, "today", null};
		ArrayList<Task> testArray = new ArrayList<Task>();
		Task event1 = new Task("first");
		Task event2 = new Task("second");
		Task event3 = new Task("third");
		testArray.add(event1);
		testArray.add(event2);
		testArray.add(event3);
		assertTrue("return true after editting", testLogic.testFindTaskToEdit(testArray, variableArray, 0));
	}
	
	@Test
	public void ShouldReturnArrayListOfLineIndex()	{
		ArrayList<String> expected = new ArrayList<String>();
		String[] variableArray = {"only"};
		expected.add("planner is empty");
		assertEquals("should be same", expected, testLogic.testSearchWord(variableArray, floating, deadlines, events));
		initializeThreeArrays();
		expected.remove(0);
		expected.add("00");
		expected.add("01");
		expected.add("02");
		assertEquals("should be same", expected, testLogic.testSearchWord(variableArray, floating, deadlines, events));
		assertEquals("String should be same", "00\n01\n02\n", testformatter.formatSearchString(testLogic.testSearchWord(variableArray, floating, deadlines, events)));
	}
	
	@Test
	public void ShouldReturnTrueIfContainKeyword()	{
		Task event = new Task("a test only");
		event.setDescription("good stuff");
		event.setCategory("food");
		assertTrue("returns true", testLogic.testIsContainKeyword(event, "good"));
		assertTrue("returns true", testLogic.testIsContainKeyword(event, "a test only"));
		assertFalse("returns false", testLogic.testIsContainKeyword(event, "wrong"));
		assertFalse("returns false", testLogic.testIsContainKeyword(event, "good food"));
		assertFalse("returns false", testLogic.testIsContainKeyword(event, "a only"));
	}
	
	@Test
	public void ShouldReturnCorrectFormatMessage()	{
		assertEquals("return formated date", "2016-05-12T16:00", testformatter.testFormatTime("12 May 4pm"));
		assertEquals("return formated date", "2016-03-06T14:30", testformatter.testFormatTime("today 2.30pm"));
		assertEquals("return formated date", "2018-12-18T00:00", testformatter.testFormatTime("2018 12am 18 december"));
		assertEquals("return formated date", "2016-03-06T23:00", testformatter.testFormatTime("11pm"));
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
