package org.jimple.planner.logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jimple.planner.Constants;
import org.junit.Test;

public class LogicIntegrationTest {
	Logic testLogic = new Logic();

	@Test
	public void ShouldTestFromAndToTime() throws IOException	{
		testLogic.execute("ADD task 1 FROM 1pm TO 3pm");
		testLogic.execute("ADD task 2 FROM 12 mar 1300 TO 12 mar 1400");
		testLogic.execute("ADD task 3 FROM today TO tomorrow");
		testLogic.execute("ADD task 4 FROM 12am TO 7pm");
		testLogic.execute("ADD task 5 FROM 1100 TO 1800");

		assertEquals("task 2", testLogic.getEventsList().get(0).getTitle());
		assertEquals("2016-03-12T13:00", testLogic.getEventsList().get(0).getFromTimeString());
		assertEquals("2016-03-12T14:00", testLogic.getEventsList().get(0).getToTimeString());
		
		assertEquals("task 3", testLogic.getEventsList().get(1).getTitle());
		assertEquals(LocalDateTime.now(), testLogic.getEventsList().get(1).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T23:59", testLogic.getEventsList().get(1).getToTimeString());
		
		assertEquals("task 5", testLogic.getEventsList().get(2).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T11:00", testLogic.getEventsList().get(2).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T18:00", testLogic.getEventsList().get(2).getToTimeString());
		
		assertEquals("task 1", testLogic.getEventsList().get(3).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate() + "T13:00", testLogic.getEventsList().get(3).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate() + "T15:00", testLogic.getEventsList().get(3).getToTimeString());
		
		assertEquals("task 4", testLogic.getEventsList().get(4).getTitle());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T00:00", testLogic.getEventsList().get(4).getFromTimeString());
		assertEquals(LocalDateTime.now().toLocalDate().plusDays(1) + "T19:00", testLogic.getEventsList().get(4).getToTimeString());
	}
	
	/*@Test
	public void ShouldTestByTime() throws IOException	{
		testLogic.execute("ADD task 6 ON 10 dec");
		testLogic.execute("ADD task 7 ON 11 dec 3pm");
		testLogic.execute("ADD task 9 BY monday");
		testLogic.execute("ADD task 10 BY today");
		testLogic.execute("ADD task 11 BY tomorrow");
		testLogic.execute("ADD task 12 AT 10pm");
		testLogic.execute("ADD task 13 AT 13 dec 6pm");
	}*/
}
