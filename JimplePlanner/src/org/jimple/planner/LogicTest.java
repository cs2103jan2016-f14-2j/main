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
	public void ShouldReturnCorrectYear()	{
		assertEquals("correct year format", "2016", testLogic.checkYear("today"));
		assertEquals("correct year format", "2018", testLogic.checkYear("2018"));
		assertEquals("correct year format", null, testLogic.checkYear("may"));
		assertEquals("correct year format", null, testLogic.checkYear("31"));
		assertEquals("correct year format", null, testLogic.checkYear("5.30pm"));
	}
	
	@Test
	public void ShouldReturnCorrectMonth()	{
		assertEquals("correct month format", "-06-", testLogic.checkMonth("june"));
		assertEquals("correct month format", "-02-", testLogic.checkMonth("February"));
		assertEquals("correct month format", "-03-", testLogic.checkMonth("today"));
		assertEquals("correct month format", "-12-", testLogic.checkMonth("December"));
		assertEquals("correct month format", "", testLogic.checkMonth("24"));
		assertEquals("correct month format", "", testLogic.checkMonth("2016"));
		assertEquals("correct year format", "", testLogic.checkMonth("2.30pm"));
	}
	
	@Test
	public void ShouldReturnCorrectDay()	{
		assertEquals("correct day format", "05T", testLogic.checkDay("5"));
		assertEquals("correct day format", "27T", testLogic.checkDay("27"));
		assertEquals("correct day format", "03T", testLogic.checkDay("today"));
		assertEquals("correct day format", "", testLogic.checkDay("may"));
		assertEquals("correct day format", "", testLogic.checkDay("2016"));
		assertEquals("correct year format", "", testLogic.checkDay("1.30am"));
	}
	
	@Test
	public void ShouldReturnCorrectHourMinuteFormat()	{
		assertEquals("right time format", "15:30", testLogic.checkTime("3.30pm"));
		assertEquals("right time format", "03:30", testLogic.checkTime("3.30am"));
		assertEquals("right time format", "", testLogic.checkTime("may"));
		assertEquals("right time format", "", testLogic.checkTime("2016"));
		assertEquals("right time format", "", testLogic.checkTime("today"));
		assertEquals("right time format", "", testLogic.checkTime("7"));
	}
	
	@Test
	public void ShouldReturnFormattedHoursMinutes()	{
		assertEquals("returns correct hours and minutes", "15:30", testLogic.formatHoursMinutes("3.30", "pm"));
		assertEquals("returns correct hours and minutes", "15:00", testLogic.formatHoursMinutes("3", "pm"));
		assertEquals("returns correct hours and minutes", "00:00", testLogic.formatHoursMinutes("12", "am"));
		assertEquals("returns correct hours and minutes", "11:30", testLogic.formatHoursMinutes("11.30", "am"));
		assertEquals("returns correct hours and minutes", "09:05", testLogic.formatHoursMinutes("9.05", "am"));
	}
	
	@Test
	public void ShouldReturnCorrectTimeFormat()	{
		assertEquals("returns correct time", "2016-03-03T15:30", testLogic.formatTime("today 3.30pm"));
		assertEquals("returns correct time", "2017-05-10T00:00", testLogic.formatTime("10 May 2017 12am"));
		assertEquals("returns correct time", "2016-03-15T16:30", testLogic.formatTime("15 march 4.30pm"));
	}
	

}
