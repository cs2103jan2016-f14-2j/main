package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LogicTest {
	Logic testLogic = new Logic();
	
	@Test
	public void AddShouldReturnFeedback() throws IOException {
		String originalInput = "add finish 2103 homework by tomorrow";
		String[] parsedInput = {"finish 2103 homework", "", "", "2007-12-03T00:00", ""};
		assertEquals("task is added to file", "task added to planner\n", testLogic.addToTaskList(parsedInput, originalInput));
	}
	
	@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "2016-02-25T12:00", null, null};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}
	
	@Test
	public void ShouldReturnCorrectMonth()	{
		
	}
	
	@Test
	public void ShouldReturnCorrectDay()	{
		assertEquals("correct day format", "05T", testLogic.checkDay("5"));
		assertEquals("correct day format", "27T", testLogic.checkDay("27"));
		assertEquals("correct day format", "03T", testLogic.checkDay("today"));
		assertEquals("correct day format", "", testLogic.checkDay("may"));
		assertEquals("correct day format", "", testLogic.checkDay("2016"));
	}
	
	@Test
	public void ShouldReturnCorrectTimeFormat()	{
		assertEquals("right time format", "15:00", testLogic.checkTime("3pm"));
		assertEquals("right time format", "03:30", testLogic.checkTime("3.30am"));
	}
	
	@Test
	public void ShouldReturnFormattedHoursMinutes()	{
		assertEquals("returns correct hours and minutes", "15:30", testLogic.formatHoursMinutes("3.30", "pm"));
		assertEquals("returns correct hours and minutes", "15:00", testLogic.formatHoursMinutes("3", "pm"));
		assertEquals("returns correct hours and minutes", "00:00", testLogic.formatHoursMinutes("12", "am"));
		assertEquals("returns correct hours and minutes", "11:30", testLogic.formatHoursMinutes("11.30", "am"));
		assertEquals("returns correct hours and minutes", "09:05", testLogic.formatHoursMinutes("9.05", "am"));
	}
	
	

}
