package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

public class StorageTest {
	@Test
	public void testIsSaved() throws IOException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		Storage storage = new Storage();
		Task task1 = new Task("Makan here for chap goh mei");
		task1.setDescription("at cousin's place");
		task1.setCategory("family time");
		task1.setFromDate("2016-02-16T19:00");
		task1.setFromDate("2016-02-16T22:00");
		
		Task task2 = new Task("Go exercise, you fatty");
		task2.setCategory("Keep fit");
		
		Task task3 = new Task("Do 2100 assignment");
		task3.setDescription("due very soon");
		task3.setCategory("Homework");
		task3.setToDate("2016-02-29T00:00");
		task3.setToDate("2016-02-29T23:59");
		
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		
		assertTrue("this should return true if saved", storage.isSaved(tasks));
	}
	
	@Test
	public void testGetTaskFromLine() throws IOException{
		LinkedList<Task> tasks = new LinkedList<Task>();
		
		String line1 = "/:title:Makan here for chap goh mei//:desc:at cousin's place//:cat:family time/";
		String line2 = "/:title:Go exercise, you fatty//:cat:Keep fit/";
		String line3 = "/:title:Do 2100 assignment//:desc:due very soon//:cat:Homework/";
		
		Storage storage = new Storage();
		tasks.add(storage.testGetTaskFromLine(line1));
		tasks.add(storage.testGetTaskFromLine(line2));
		tasks.add(storage.testGetTaskFromLine(line3));
		
		if(tasks.size()!=0){
			String desc1 = tasks.get(0).getDescription();
			assertEquals("true if same", "at cousin's place", desc1);
			String cat1 = tasks.get(0).getCategory();
			assertEquals("true if same", "family time", cat1);
			String title1 = tasks.get(0).getTitle();
			assertEquals("true if same", "Makan here for chap goh mei", title1);
			
			String title2 = tasks.get(1).getTitle();
			assertEquals("true if same", "Go exercise, you fatty", title2);
			String cat2 = tasks.get(1).getCategory();
			assertEquals("true if same", "Keep fit", cat2);
			
			String title3 = tasks.get(2).getTitle();
			assertEquals("true if same", "Do 2100 assignment", title3);
			String desc3 = tasks.get(2).getDescription();
			assertEquals("true if same", "due very soon", desc3);
			String cat3 = tasks.get(2).getCategory();
			assertEquals("true if same", "Homework", cat3);
			
		} else{
			assertTrue("just a short confirmation that it is truly empty", tasks.isEmpty());
		}
	}
	
	@Test
	public void testGetTasks() throws IOException{
		Storage storage = new Storage();
		ArrayList<Task> tasks = storage.getTasks();
		if(tasks.size()!=0){
			String desc1 = tasks.get(0).getDescription();
			assertEquals("true if same", "at cousin's place", desc1);
			String cat1 = tasks.get(0).getCategory();
			assertEquals("true if same", "family time", cat1);
			String title1 = tasks.get(0).getTitle();
			assertEquals("true if same", "Makan here for chap goh mei", title1);
			
			String title2 = tasks.get(1).getTitle();
			assertEquals("true if same", "Go exercise, you fatty", title2);
			String cat2 = tasks.get(1).getCategory();
			assertEquals("true if same", "Keep fit", cat2);
			
			String title3 = tasks.get(2).getTitle();
			assertEquals("true if same", "Do 2100 assignment", title3);
			String desc3 = tasks.get(2).getDescription();
			assertEquals("true if same", "due very soon", desc3);
			String cat3 = tasks.get(2).getCategory();
			assertEquals("true if same", "Homework", cat3);
			
		} else{
			assertTrue("just a short confirmation that it is truly empty", tasks.isEmpty());
		}
	}
}
