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
 * To test, ensure time requirements are met
 * @author Brandon
 *
 */
public class LogicAddIntegrationTest {
	Logic testLogic = new Logic();
	
	/**
	 * Test cases are time sensitive. 
	 * task 1: pass only if before 1pm
	 * task 2: pass only if before 9 december 2016
	 * task 4: pass only if after 12am
	 * task 5: pass only if before 1pm
	 * @throws IOException
	 */
	@Test
	public void ShouldTestFromAndToTime() throws IOException	{
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 1 FROM 1pm TO 3pm");
		assertEquals("task 1", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T13:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T15:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 2 FROM 9 december 1300 TO 1400");
		assertEquals("task 2", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals("2016-12-09T13:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals("2016-12-09T14:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 3 FROM today TO tomorrow");
		assertEquals("task 3", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T00:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T23:59", testLogic.getEventsDividedList().get(1).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 4 FROM 12am TO 7pm");
		assertEquals("task 4", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T00:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T19:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 5 FROM 1300 TO 1800");
		assertEquals("task 5", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T13:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T18:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
	}
	
	/**
	 * test cases are time sensitive
	 * task 9: pass only if date is adjusted to the coming monday of the week
	 * @throws IOException
	 */
	@Test
	public void ShouldTestByTime() throws IOException	{
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 9 BY monday");
		assertEquals("task 9", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals("2016-04-18T23:59", testLogic.getDeadlinesList().get(0).getFromTimeString());
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 10 BY today");
		assertEquals("task 10", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T23:59", testLogic.getDeadlinesList().get(0).getFromTimeString());
		testLogic.getDeadlinesList().clear();
		
		testLogic.execute("ADD task 11 BY tomorrow");
		assertEquals("task 11", testLogic.getDeadlinesList().get(0).getTitle());
		assertEquals(LocalDateTime.now().plusDays(1).toLocalDate() + "T23:59", testLogic.getDeadlinesList().get(0).getFromTimeString());
		testLogic.getDeadlinesList().clear();
	}
	
	/**
	 * test cases are time sensitive
	 * task 6: pass only if time is before 10 dec 2016
	 * task 7: pass only if time is before 11 dec 2016 3pm
	 * @throws IOException
	 */
	@Test
	public void ShouldTestOnTime() throws IOException	{
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 6 ON 10 dec");
		assertEquals("task 6", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals("2016-12-10T00:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals("2016-12-10T23:59", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 7 ON 11 dec 3pm");
		assertEquals("task 7", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals("2016-12-11T15:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals("2016-12-11T23:59", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
	}
	
	/**
	 * test cases are time sensitive
	 * task 12: pass only if time now is before 10pm
	 * task 13: pass only if time now is before 13 dec 2016
	 * @throws IOException
	 */
	@Test
	public void ShouldTestAtTime() throws IOException	{
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
		
		testLogic.execute("ADD task 12 AT 10pm");
		assertEquals("task 12", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T22:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T23:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();

		testLogic.execute("ADD task 13 AT 13 dec 6pm");
		assertEquals("task 13", testLogic.getEventsDividedList().get(0).getTitle());
		assertEquals("2016-12-13T18:00", testLogic.getEventsDividedList().get(0).getFromTimeString());
		assertEquals("2016-12-13T19:00", testLogic.getEventsDividedList().get(0).getToTimeString());
		testLogic.getEventsDividedList().clear();
		testLogic.getEventsList().clear();
	}
}
