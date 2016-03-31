package org.jimple.planner.storage.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import static org.jimple.planner.Constants.TYPE_TODO;
import static org.jimple.planner.Constants.TYPE_DEADLINE;
import static org.jimple.planner.Constants.TYPE_EVENT;
import org.jimple.planner.Task;
import org.jimple.planner.storage.StorageLoad;
import org.junit.Test;

public class StorageTestGetFromLine {
	private StorageLoad loadUnit = new StorageLoad();
	/*
	 * These tests are for testing the parsing of each String as written as a line inside the file itself and making sure
	 * it returns the correct Task instance that goes along with this
	 */
	
	@Test
	//random sample of tasks in a normal situation
	public void testGetTaskFromLine1(){
		LinkedList<Task> tasks = new LinkedList<Task>();
		
		String line1 = "/s/:title:Go exercise, you fatty/s//s/:desc:Keep fit/s//s/:label:/tl/Exercise Time/tl//tl/3/tl//s/";
		String line2 = "/s/:title:register for Orbital/s//s/:desc:keep my summer occupied/s//s/:from:2016-05-15T16:00/s/";
		String line3 = "/s/:title:Attend seminar/s//s/:desc:at SOC/s//s/:from:2016-08-11T11:00/s//s/:to:2016-08-11T17:00/s/";
		String line4 = "/s/:title:banana king/s//s/:desc:tomahawk/s//s/:from:2016-08-11T11:00/s/";
		String line5 = "/s/:title:attend wedding/s//s/:from:2016-03-15T15:00/s/";
		
		tasks.add(loadUnit.testGetTaskFromLine(line1));
		tasks.add(loadUnit.testGetTaskFromLine(line2));
		tasks.add(loadUnit.testGetTaskFromLine(line3));
		tasks.add(loadUnit.testGetTaskFromLine(line4));
		tasks.add(loadUnit.testGetTaskFromLine(line5));
		
		String title1 = tasks.get(0).getTitle();
		String desc1 = tasks.get(0).getDescription();
		String from1 = tasks.get(0).getFromTimeString();
		String to1 = tasks.get(0).getToTimeString();
		String type1 = tasks.get(0).getType();
		String labelName1 = tasks.get(0).getTaskLabel().getLabelName();
		int labelColourId1 = tasks.get(0).getTaskLabel().getColourId();
		assertEquals("title", "Go exercise, you fatty", title1);
		assertEquals("desc", "Keep fit", desc1);
		assertEquals("from", "", from1);
		assertEquals("to", "", to1);
		assertEquals("type", "floating", type1);
		assertEquals("labelName", "Exercise Time", labelName1);
		assertEquals("labelColourId", 3, labelColourId1);
		
		String title2 = tasks.get(1).getTitle();
		String desc2 = tasks.get(1).getDescription();
		String from2 = tasks.get(1).getFromTimeString();
		String to2 = tasks.get(1).getToTimeString();
		String type2 = tasks.get(1).getType();
		assertEquals("title", "register for Orbital", title2);
		assertEquals("desc", "keep my summer occupied", desc2);
		assertEquals("from", "2016-05-15T16:00", from2);
		assertEquals("to", "", to2);
		assertEquals("type", "deadline", type2);
			
		String title3 = tasks.get(2).getTitle();
		String desc3 = tasks.get(2).getDescription();
		String from3 = tasks.get(2).getFromTimeString();
		String to3 = tasks.get(2).getToTimeString();
		String type3 = tasks.get(2).getType();
		assertEquals("title", "Attend seminar", title3);
		assertEquals("desc", "at SOC", desc3);
		assertEquals("from", "2016-08-11T11:00", from3);
		assertEquals("to", "2016-08-11T17:00", to3);
		assertEquals("type", "event", type3);
		
		String title4 = tasks.get(3).getTitle();
		String desc4 = tasks.get(3).getDescription();
		String from4 = tasks.get(3).getFromTimeString();
		String to4 = tasks.get(3).getToTimeString();
		String type4 = tasks.get(3).getType();
		assertEquals("title", "banana king", title4);
		assertEquals("desc", "tomahawk", desc4);
		assertEquals("from", "2016-08-11T11:00", from4);
		assertEquals("to", "", to4);
		assertEquals("type", "deadline", type4);
		
		String title5 = tasks.get(4).getTitle();
		String desc5 = tasks.get(4).getDescription();
		String from5 = tasks.get(4).getFromTimeString();
		String to5 = tasks.get(4).getToTimeString();
		String type5 = tasks.get(4).getType();
		assertEquals("title", "attend wedding", title5);
		assertEquals("desc", "", desc5);
		assertEquals("from", "2016-03-15T15:00", from5);
		assertEquals("to", "", to5);
		assertEquals("type", "deadline", type5);
	}
	
	@Test
	public void testGetTaskFromLine2(){
		String line1 = "/s/:title:task1/s/";
		String line2 = "/s/:title:task1/s//s/:to:2016-08-11T17:00/s/";
		String line3 = "/s/:title:task1/s//s/:from:2016-08-11T11:00/s//s/:to:2016-08-11T17:00/s/";
		String line4 = "/s/:title:task1/s//s/:from:2016-08-11T11:00/s/";
		
		Task task1 = loadUnit.testGetTaskFromLine(line1);
		Task task2 = loadUnit.testGetTaskFromLine(line2);
		Task task3 = loadUnit.testGetTaskFromLine(line3);
		Task task4 = loadUnit.testGetTaskFromLine(line4);
		
		assertTrue("valid task", task1.isValidType());
		assertFalse("invalid task", task2.isValidType());
		assertTrue("valid task", task3.isValidType());
		assertTrue("valid task", task4.isValidType());
		
		assertEquals("is todo", task1.getType(), TYPE_TODO);
		assertEquals("is event", task3.getType(), TYPE_EVENT);
		assertEquals("is deadline", task4.getType(), TYPE_DEADLINE);
	}
}
