package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LogicTest {
	Logic testLogic = new Logic();
	
	/*@Test
	public void AddShouldReturnFeedback() throws IOException {
		String originalInput = "add finish 2103 homework by tomorrow";
		String[] parsedInput = {"finish 2103 homework", "", "", "3 december 2017 12am", ""};
		assertEquals("task is added to file", "task added to planner\n", testLogic.addToTaskList(parsedInput));
	}*/
	
	/*@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "29 february 12pm", "", ""};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}*/
	
	@Test
	public void ShouldReturnCorrectFormatMessage()	{
		
	}
	
	@Test
	public void ShouldReturnCorrectYear()	{
		assertEquals("return year", "2016", testLogic.testCheckYear("today"));
		assertEquals("return year", "2018", testLogic.testCheckYear("2018"));
		assertEquals("return year", null, testLogic.testCheckYear("may"));
		assertEquals("return year", null, testLogic.testCheckYear("6"));
		//assertEquals("returns feedback", "taskEd")
	}
	
	@Test
	public void ShoudReturnCorrectMonth()	{
		assertEquals("return month", "-02-", testLogic.testCheckMonth("february"));
		assertEquals("return month", "-05-", testLogic.testCheckMonth("May"));
		assertEquals("return month", "-12-", testLogic.testCheckMonth("december"));
		assertEquals("return month", "-03-", testLogic.testCheckMonth("today"));
		assertEquals("return month", "", testLogic.testCheckMonth("10"));
		assertEquals("return month", "", testLogic.testCheckMonth("2018"));
	}
	
	@Test
	public void ShouldReturnCorrectDay()	{
		assertEquals("return day", "10T", testLogic.testCheckDay("10"));
		assertEquals("return day", "05T", testLogic.testCheckDay("5"));
		assertEquals("return day", "04T", testLogic.testCheckDay("today"));
		assertEquals("return day", "", testLogic.testCheckDay("march"));
		assertEquals("return day", "", testLogic.testCheckDay("2017"));
	}
	
	@Test
	public void ShouldReturnCorrectTim()	{
		assertEquals("return time", "00:00", testLogic.testCheckTime("12am"));
		assertEquals("return time", "09:00", testLogic.testCheckTime("9am"));
		assertEquals("return time", "12:30", testLogic.testCheckTime("12.30pm"));
		assertEquals("return time", "", testLogic.testCheckTime("today"));
		assertEquals("return time", "", testLogic.testCheckTime("2017"));
		assertEquals("return time", "", testLogic.testCheckTime("12"));
	}

}
