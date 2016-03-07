package org.jimple.planner;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

public class StorageTest {
	private ArrayList<Task> getTodoStub(){
		Task todo1 = new Task("Go exercise, you fatty");
		todo1.setCategory("Keep fit");
		Task todo2 = new Task("Read Lord of The Rings");
		Task todo3 = new Task("Learn to play the harmonica");
		Task todo4 = new Task("go through my anime backlog");
		ArrayList<Task> todo = new ArrayList<Task>();
		
		todo.add(todo1);
		todo.add(todo2);
		todo.add(todo3);
		todo.add(todo4);
		return todo;
	}
	
	private ArrayList<Task> getDeadlineStub(){
		Task deadline1 = new Task("Do 2100 assignment");
		deadline1.setDescription("due very soon");
		deadline1.setCategory("Homework");
		deadline1.setToDate("2016-07-29T23:59");
		Task deadline2 = new Task("Hand in cs2103 progress report");
		deadline2.setCategory("Homework");
		deadline2.setToDate("2016-03-09T23:59");
		Task deadline3 = new Task("submit report before countdown party");
		deadline3.setToDate("2015-12-31T23:59");
		Task deadline4 = new Task("deadline for GER1000 quiz");
		deadline4.setToDate("2016-03-06T23:59");
		Task deadline5 = new Task("register for Orbital");
		deadline5.setDescription("keep my summer occupied");
		deadline5.setCategory("Self-learning");
		deadline5.setToDate("2016-05-15T16:00");
		
		ArrayList<Task> deadline = new ArrayList<Task>();
		deadline.add(deadline1);
		deadline.add(deadline2);
		deadline.add(deadline3);
		deadline.add(deadline4);
		deadline.add(deadline5);
		return deadline;
	}
	
	private ArrayList<Task> getEventStub(){
		Task event1 = new Task("Makan here for chap goh mei");
		event1.setDescription("at cousin's place");
		event1.setCategory("family time");
		event1.setFromDate("2016-02-16T19:00");
		event1.setToDate("2016-02-16T22:00");
		Task event2 = new Task("eat with the bros");
		event2.setDescription("the same place");
		event2.setFromDate("2016-01-10T15:00");
		event2.setToDate("2016-01-10T17:00");
		Task event3 = new Task("Attend seminar");
		event3.setDescription("at SOC");
		event3.setFromDate("2016-08-11T11:00");
		event3.setToDate("2016-08-11T17:00");
		Task event4 = new Task("business workshop");
		event4.setFromDate("2016-06-16T12:00");
		event4.setToDate("2016-06-16T14:00");
		Task event5 = new Task("prepare for chap goh mei dinner");
		event5.setCategory("family time");
		event5.setFromDate("2016-02-16T12:00");
		event5.setToDate("2016-02-16T15:00");

		ArrayList<Task> event = new ArrayList<Task>();
		event.add(event1);
		event.add(event2);
		event.add(event3);
		event.add(event4);
		event.add(event5);
		return event;
	}
	
	private ArrayList<ArrayList<Task>> getTasksStub(){
		ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
		ArrayList<Task> todo = getTodoStub();
		ArrayList<Task> deadline = getDeadlineStub();
		ArrayList<Task> event = getEventStub();
		
		tasks.add(todo);
		tasks.add(deadline);
		tasks.add(event);
		return tasks;
	}
	
	@Test
	public void testIsSaved() throws IOException {
		ArrayList<ArrayList<Task>> tasks = getTasksStub();
		Storage storage = new Storage();
		boolean saveState = storage.isSaved(tasks);
		assertTrue("this should return true if saved", saveState);
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

	}
	
	@Test
	public void testGetTasks() throws IOException{
		Storage storage = new Storage();
		ArrayList<ArrayList<Task>> tasks = storage.getTasks();
		ArrayList<Task> todo = tasks.get(0);
		ArrayList<Task> deadline = tasks.get(1);
		ArrayList<Task> event = tasks.get(2);
		
		String[] todo1Check = {"Go exercise, you fatty", "Keep fit"};
		String[] todo2Check = {"Read Lord of The Rings"};
		String[] todo3Check = {"Learn to play the harmonica"};
		String[] todo4Check = {"go through my anime backlog"};
		String todo1Title = todo.get(0).getTitle();
		String todo1Category = todo.get(0).getCategory();
		String todo2Title = todo.get(1).getTitle();
		String todo3Title = todo.get(2).getTitle();
		String todo4Title = todo.get(3).getTitle();
		assertEquals("true if same", todo1Check[0], todo1Title);
		assertEquals("true if same", todo1Check[1], todo1Category);
		assertEquals("true if same", todo2Check[0], todo2Title);
		assertEquals("true if same", todo3Check[0], todo3Title);
		assertEquals("true if same", todo4Check[0], todo4Title);
		
		String[] deadline3Check = {"submit report before countdown party", "2015-12-31T23:59"};
		String[] deadline4Check = {"deadline for GER1000 quiz", "2016-03-06T23:59"};
		String[] deadline2Check = {"Hand in cs2103 progress report", "Homework", "2016-03-09T23:59"}; //homework is cate
		String[] deadline5Check = {"register for Orbital", "keep my summer occupied", "Self-learning", "2016-05-15T16:00"};
		String[] deadline1Check = {"Do 2100 assignment", "due very soon", "Homework", "2016-07-29T23:59"};
		String deadline1Title = deadline.get(0).getTitle();
		String deadline1ToTime = deadline.get(0).getToTimeString();
		String deadline2Title = deadline.get(1).getTitle();
		String deadline2ToTime = deadline.get(1).getToTimeString();
		String deadline3Title = deadline.get(2).getTitle();
		String deadline3ToTime = deadline.get(2).getToTimeString();
		String deadline3Category = deadline.get(2).getCategory();
		String deadline4Title = deadline.get(3).getTitle();
		String deadline4ToTime = deadline.get(3).getToTimeString();
		String deadline4Category = deadline.get(3).getCategory();
		String deadline4Description = deadline.get(3).getDescription();
		String deadline5Title = deadline.get(4).getTitle();
		String deadline5ToTime = deadline.get(4).getToTimeString();
		String deadline5Category = deadline.get(4).getCategory();
		String deadline5Description = deadline.get(4).getDescription();

		assertEquals("true if same", deadline3Check[0], deadline1Title);
		assertEquals("true if same", deadline3Check[1], deadline1ToTime);
		assertEquals("true if same", deadline4Check[0], deadline2Title);
		assertEquals("true if same", deadline4Check[1], deadline2ToTime);
		assertEquals("true if same", deadline2Check[0], deadline3Title);
		assertEquals("true if same", deadline2Check[2], deadline3ToTime);
		assertEquals("true if same", deadline2Check[1], deadline3Category);
		assertEquals("true if same", deadline5Check[0], deadline4Title);
		assertEquals("true if same", deadline5Check[3], deadline4ToTime);
		assertEquals("true if same", deadline5Check[2], deadline4Category);
		assertEquals("true if same", deadline5Check[1], deadline4Description);
		assertEquals("true if same", deadline1Check[0], deadline5Title);
		assertEquals("true if same", deadline1Check[3], deadline5ToTime);
		assertEquals("true if same", deadline1Check[2], deadline5Category);
		assertEquals("true if same", deadline1Check[1], deadline5Description);
		
		String[] event2Check = {"eat with the bros", "the same place", "", "2016-01-10T15:00", "2016-01-10T17:00"};
		String[] event5Check = {"prepare for chap goh mei dinner", "", "family time", "2016-02-16T12:00", "2016-02-16T15:00"};
		String[] event1Check = {"Makan here for chap goh mei", "at cousin's place", "family time", "2016-02-16T19:00", "2016-02-16T22:00"};
		String[] event4Check = {"business workshop", "", "", "2016-06-16T12:00", "2016-06-16T14:00"};
		String[] event3Check = {"Attend seminar", "at SOC", "", "2016-08-11T11:00", "2016-08-11T17:00"};
		
		String[] event1Actual = storage.testExtractTasksToStringArray(event.get(0));
		String[] event2Actual = storage.testExtractTasksToStringArray(event.get(1));
		String[] event3Actual = storage.testExtractTasksToStringArray(event.get(2));
		String[] event4Actual = storage.testExtractTasksToStringArray(event.get(3));
		String[] event5Actual = storage.testExtractTasksToStringArray(event.get(4));
		for(int i=0; i<5; i++){
			assertEquals("true if same", event2Check[i], event1Actual[i]);
		}
		for(int i=0; i<5; i++){
			assertEquals("true if same", event5Check[i], event2Actual[i]);
		}
		for(int i=0; i<5; i++){
			assertEquals("true if same", event1Check[i], event3Actual[i]);
		}
		for(int i=0; i<5; i++){
			assertEquals("true if same", event4Check[i], event4Actual[i]);
		}
		for(int i=0; i<5; i++){
			assertEquals("true if same", event3Check[i], event5Actual[i]);
		}
	}
}
