package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

public class StorageTest {
	@Test
	public void testIsSaved() throws IOException {
		ArrayList<Task> events = new ArrayList<Task>();
		Storage storage = new Storage();
		Task event1 = new Task("Makan here for chap goh mei");
		event1.setDescription("at cousin's place");
		event1.setCategory("family time");
		
		Task event2 = new Task("Go exercise, you fatty");
		event2.setCategory("Keep fit");
		
		Task event3 = new Task("Do 2100 assignment");
		event3.setDescription("due very soon");
		event3.setCategory("Homework");
		
		events.add(event1);
		events.add(event2);
		events.add(event3);
		
		assertTrue("this should return true if saved", storage.isSaved(events));
	}
	
	@Test
	public void testGetEventFromLine() throws IOException{
		LinkedList<Task> events = new LinkedList<Task>();
		
		String line1 = "/:title:Makan here for chap goh mei//:desc:at cousin's place//:cat:family time/";
		String line2 = "/:title:Go exercise, you fatty//:cat:Keep fit/";
		String line3 = "/:title:Do 2100 assignment//:desc:due very soon//:cat:Homework/";
		
		Storage storage = new Storage();
		events.add(storage.testGetEventFromLine(line1));
		events.add(storage.testGetEventFromLine(line2));
		events.add(storage.testGetEventFromLine(line3));
		
		if(events.size()!=0){
			String desc1 = events.get(0).getDescription();
			assertEquals("true if same", "at cousin's place", desc1);
			String cat1 = events.get(0).getCategory();
			assertEquals("true if same", "family time", cat1);
			String title1 = events.get(0).getTitle();
			assertEquals("true if same", "Makan here for chap goh mei", title1);
			
			String title2 = events.get(1).getTitle();
			assertEquals("true if same", "Go exercise, you fatty", title2);
			String cat2 = events.get(1).getCategory();
			assertEquals("true if same", "Keep fit", cat2);
			
			String title3 = events.get(2).getTitle();
			assertEquals("true if same", "Do 2100 assignment", title3);
			String desc3 = events.get(2).getDescription();
			assertEquals("true if same", "due very soon", desc3);
			String cat3 = events.get(2).getCategory();
			assertEquals("true if same", "Homework", cat3);
			
		} else{
			assertTrue("just a short confirmation that it is truly empty", events.isEmpty());
		}
	}
	
	@Test
	public void testGetEvents() throws IOException{
		Storage storage = new Storage();
		ArrayList<Task> events = storage.getEvents();
		if(events.size()!=0){
			String desc1 = events.get(0).getDescription();
			assertEquals("true if same", "at cousin's place", desc1);
			String cat1 = events.get(0).getCategory();
			assertEquals("true if same", "family time", cat1);
			String title1 = events.get(0).getTitle();
			assertEquals("true if same", "Makan here for chap goh mei", title1);
			
			String title2 = events.get(1).getTitle();
			assertEquals("true if same", "Go exercise, you fatty", title2);
			String cat2 = events.get(1).getCategory();
			assertEquals("true if same", "Keep fit", cat2);
			
			String title3 = events.get(2).getTitle();
			assertEquals("true if same", "Do 2100 assignment", title3);
			String desc3 = events.get(2).getDescription();
			assertEquals("true if same", "due very soon", desc3);
			String cat3 = events.get(2).getCategory();
			assertEquals("true if same", "Homework", cat3);
			
		} else{
			assertTrue("just a short confirmation that it is truly empty", events.isEmpty());
		}
	}
}
