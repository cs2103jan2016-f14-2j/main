package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LogicTest {
	Formatter testformatter = new Formatter();
	Logic testLogic = new Logic();
	
	@Test
	public void AddShouldReturnFeedback() throws IOException {
		String originalInput = "add finish 2103 homework by tomorrow";
		String[] parsedInput = {"finish 2103 homework", null, null, "3 december 2017 12am", null};
		assertEquals("task is added to file", "task added to planner\n", testLogic.testAddToTaskList(parsedInput));
	}
	
	/*@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "29 february 12pm", "", ""};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}*/
	
	@Test
	public void ShouldReturnCorrectFormatMessage()	{
		assertEquals("return formated date", "2016-05-12T16:00", testformatter.testFormatTime("12 May 4pm"));
		assertEquals("return formated date", "2016-03-04T14:30", testformatter.testFormatTime("today 2.30pm"));
		assertEquals("return formated date", "2018-12-18T00:00", testformatter.testFormatTime("2018 12am 18 december"));
	}
	
	@Test
	public void ShouldReturnCorrectYear()	{
		assertEquals("return year", "2016", testformatter.testCheckYear("today"));
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
		assertEquals("return month", "-03-", testformatter.testCheckMonth("today"));
		assertEquals("return month", "", testformatter.testCheckMonth("10"));
		assertEquals("return month", "", testformatter.testCheckMonth("2018"));
	}
	
	@Test
	public void ShouldReturnCorrectDay()	{
		assertEquals("return day", "10T", testformatter.testCheckDay("10"));
		assertEquals("return day", "05T", testformatter.testCheckDay("5"));
		assertEquals("return day", "04T", testformatter.testCheckDay("today"));
		assertEquals("return day", "", testformatter.testCheckDay("march"));
		assertEquals("return day", "", testformatter.testCheckDay("2017"));
	}
	
	@Test
	public void ShouldReturnCorrectTim()	{
		assertEquals("return time", "00:00", testformatter.testCheckTime("12am"));
		assertEquals("return time", "09:00", testformatter.testCheckTime("9am"));
		assertEquals("return time", "12:30", testformatter.testCheckTime("12.30pm"));
		assertEquals("return time", "", testformatter.testCheckTime("today"));
		assertEquals("return time", "", testformatter.testCheckTime("2017"));
		assertEquals("return time", "", testformatter.testCheckTime("12"));
	}

}
