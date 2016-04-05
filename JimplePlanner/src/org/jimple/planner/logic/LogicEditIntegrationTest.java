package org.jimple.planner.logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jimple.planner.Constants;
import org.junit.Test;

public class LogicEditIntegrationTest {
	Logic testLogic = new Logic();
	
	@Test
	public void ShouldTestEditFromToTime() throws IOException	{
		testLogic.getEventsDividedList().clear();
		
		testLogic.execute("ADD task 1 FROM 1pm TO 3pm");
		testLogic.execute("EDIT 3 TIME FROM 1pm TO 2pm");
		
		assertEquals("task 1", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals("2016-04-01T13:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals("2016-04-01T14:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		
		testLogic.execute("ADD task 2 FROM 1pm TO 3pm");
		testLogic.execute("EDIT 4 TIME FROM 1400 TO 1500");
		
		assertEquals("task 2", testLogic.getEventsDividedList().get(1).getTitle());
		assertEquals("2016-04-01T14:00", testLogic.getEventsDividedList().get(1).getFromTimeString());
		assertEquals("2016-04-01T15:00", testLogic.getEventsDividedList().get(1).getToTimeString());
		
		testLogic.getEventsDividedList().clear();
	}
	
	@Test
	public void ShouldTestEditByTime() throws IOException	{
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 3 BY 3pm");
		testLogic.execute("EDIT 1 TIME BY 1pm");
		
		assertEquals("task 3", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T13:00", testLogic.getDeadlinesList().get(0).getFromTimeString());
		
		testLogic.execute("ADD task 4 BY 1pm");
		testLogic.execute("EDIT 2 TIME BY 1700 ");
		
		assertEquals("task 4", testLogic.getDeadlinesList().get(1).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T17:00", testLogic.getDeadlinesList().get(1).getFromTimeString());
		
		testLogic.getDeadlinesList().clear();
	}
}
