package org.jimple.planner.test.logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jimple.planner.logic.Logic;
import org.junit.Test;

//@@author A0124952E
/**
 * All test cases in integration tests are time sensitive.
 * Planner.jim file must be empty before starting test cases
 * @author Brandon
 *
 */
public class LogicEditIntegrationTest {
	Logic testLogic = new Logic();
	
	/**
	 * test cases are time sensitive
	 * task 1: works only before 1pm
	 * task 2: works only before 1pm
	 * @throws IOException
	 */
	@Test
	public void ShouldTestEditFromToTime() throws IOException	{
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 1 FROM 1pm TO 3pm");
		testLogic.execute("EDIT 2 TIME FROM 1pm TO 2pm");
		assertEquals("task 1", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T13:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T14:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 2 FROM 1pm TO 3pm");
		testLogic.execute("EDIT 3 TIME FROM 1400 TO 1500");
		assertEquals("task 2", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T14:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T15:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		testLogic.getDeadlinesList().clear();
		
	}
	
	/**
	 * test cases are time sensitive
	 * task 3: pass only if time is before 3pm
	 * task 4: pass only if time is before 1pm
	 * @throws IOException
	 */
	@Test
	public void ShouldTestEditByTime() throws IOException	{
		testLogic.execute("DELETE 1");
		testLogic.execute("DELETE 2");
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 3 BY 3pm");
		System.out.println(testLogic.getDeadlinesList().get(0).getTaskId());
		testLogic.execute("EDIT 1 TIME BY 4pm");
		assertEquals("task 3", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T16:00", testLogic.getDeadlinesList().get(0).getFromTimeString());
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 4 BY 1pm");
		testLogic.execute("EDIT 2 TIME BY 1700 ");
		assertEquals("task 4", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T17:00", testLogic.getDeadlinesList().get(0).getFromTimeString());
		testLogic.getDeadlinesList().clear();
	}
}
