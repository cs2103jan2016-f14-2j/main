package org.jimple.planner.storage.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.jimple.planner.Task;
import org.jimple.planner.storage.StorageLoad;
import org.junit.Test;

public class StorageTestGetFromLine {
	private StorageLoad loadUnit = new StorageLoad();
	
	@Test
	public void testGetTaskFromLine1(){
		LinkedList<Task> tasks = new LinkedList<Task>();
		
		String line1 = "/:title:Go exercise, you fatty//:cat:Keep fit/";
		String line2 = "/:title:register for Orbital//:desc:keep my summer occupied//:cat:Self-learning//:from:2016-05-15T16:00/";
		String line3 = "/:title:Attend seminar//:desc:at SOC//:from:2016-08-11T11:00//:to:2016-08-11T17:00/";
		String line4 = "/:title:banana king//:desc:tomahawk//:from:2016-08-11T11:00/";
		String line5 = "/:title:attend wedding//:from:2016-03-15T15:00/";
		
		tasks.add(loadUnit.testGetTaskFromLine(line1));
		tasks.add(loadUnit.testGetTaskFromLine(line2));
		tasks.add(loadUnit.testGetTaskFromLine(line3));
		tasks.add(loadUnit.testGetTaskFromLine(line4));
		tasks.add(loadUnit.testGetTaskFromLine(line5));
		
		String title1 = tasks.get(0).getTitle();
		String desc1 = tasks.get(0).getDescription();
		String cat1 = tasks.get(0).getCategory();
		String from1 = tasks.get(0).getFromTimeString();
		String to1 = tasks.get(0).getToTimeString();
		String type1 = tasks.get(0).getType();
		assertEquals("title", "Go exercise, you fatty", title1);
		assertEquals("desc", "", desc1);
		assertEquals("cat", "Keep fit", cat1);
		assertEquals("from", "", from1);
		assertEquals("to", "", to1);
		assertEquals("type", "floating", type1);
		
		String title2 = tasks.get(1).getTitle();
		String desc2 = tasks.get(1).getDescription();
		String cat2 = tasks.get(1).getCategory();
		String from2 = tasks.get(1).getFromTimeString();
		String to2 = tasks.get(1).getToTimeString();
		String type2 = tasks.get(1).getType();
		assertEquals("title", "register for Orbital", title2);
		assertEquals("desc", "keep my summer occupied", desc2);
		assertEquals("cat", "Self-learning", cat2);
		assertEquals("from", "2016-05-15T16:00", from2);
		assertEquals("to", "", to2);
		assertEquals("type", "deadline", type2);
			
		String title3 = tasks.get(2).getTitle();
		String desc3 = tasks.get(2).getDescription();
		String cat3 = tasks.get(2).getCategory();
		String from3 = tasks.get(2).getFromTimeString();
		String to3 = tasks.get(2).getToTimeString();
		String type3 = tasks.get(2).getType();
		assertEquals("title", "Attend seminar", title3);
		assertEquals("desc", "at SOC", desc3);
		assertEquals("cat", "", cat3);
		assertEquals("from", "2016-08-11T11:00", from3);
		assertEquals("to", "2016-08-11T17:00", to3);
		assertEquals("type", "event", type3);
		
		String title4 = tasks.get(3).getTitle();
		String desc4 = tasks.get(3).getDescription();
		String cat4 = tasks.get(3).getCategory();
		String from4 = tasks.get(3).getFromTimeString();
		String to4 = tasks.get(3).getToTimeString();
		String type4 = tasks.get(3).getType();
		assertEquals("title", "banana king", title4);
		assertEquals("desc", "tomahawk", desc4);
		assertEquals("cat", "", cat4);
		assertEquals("from", "2016-08-11T11:00", from4);
		assertEquals("to", "", to4);
		assertEquals("type", "deadline", type4);
		
		String title5 = tasks.get(4).getTitle();
		String desc5 = tasks.get(4).getDescription();
		String cat5 = tasks.get(4).getCategory();
		String from5 = tasks.get(4).getFromTimeString();
		String to5 = tasks.get(4).getToTimeString();
		String type5 = tasks.get(4).getType();
		assertEquals("title", "attend wedding", title5);
		assertEquals("desc", "", desc5);
		assertEquals("cat", "", cat5);
		assertEquals("from", "2016-03-15T15:00", from5);
		assertEquals("to", "", to5);
		assertEquals("type", "deadline", type5);
	}
}
