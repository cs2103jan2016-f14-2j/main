package org.jimple.planner.test.storage;

import java.util.ArrayList;

import org.jimple.planner.task.Task;

public class StorageGetExampleTasks {
	public static ArrayList<Task> getExampleTodos(){
		Task floating1 = new Task("Go exercise, you fatty");
		floating1.setDescription("Keep fit");
		Task floating2 = new Task("Read Lord of The Rings");
		Task floating3 = new Task("Learn to play the harmonica");
		Task floating4 = new Task("go through my anime backlog");
		ArrayList<Task> floating = new ArrayList<Task>();
		
		floating.add(floating1);
		floating.add(floating2);
		floating.add(floating3);
		floating.add(floating4);
		return floating;
	}
	
	public static ArrayList<Task> getExampleDeadline(){
		Task deadline1 = new Task("Do 2100 assignment");
		deadline1.setDescription("due very soon");
		deadline1.setFromDate("2016-07-29T23:59");
		Task deadline2 = new Task("Hand in cs2103 progress report");
		deadline2.setDescription("Homework");
		deadline2.setFromDate("2016-03-09T23:59");
		Task deadline3 = new Task("submit report before countdown party");
		deadline3.setFromDate("2015-12-31T23:59");
		Task deadline4 = new Task("deadline for GER1000 quiz");
		deadline4.setFromDate("2016-03-06T23:59");
		Task deadline5 = new Task("register for Orbital");
		deadline5.setDescription("keep my summer occupied");
		deadline5.setFromDate("2016-05-15T16:00");
		
		ArrayList<Task> deadline = new ArrayList<Task>();
		deadline.add(deadline1);
		deadline.add(deadline2);
		deadline.add(deadline3);
		deadline.add(deadline4);
		deadline.add(deadline5);
		return deadline;
	}
	
	public static ArrayList<Task> getExampleEvents(){
		Task event1 = new Task("Makan here for chap goh mei");
		event1.setDescription("at cousin's place");
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
	
	public static ArrayList<ArrayList<Task>> getExampleTasks(){
		ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
		ArrayList<Task> todo = getExampleTodos();
		ArrayList<Task> deadline = getExampleDeadline();
		ArrayList<Task> event = getExampleEvents();
		
		tasks.add(todo);
		tasks.add(deadline);
		tasks.add(event);
		tasks.add(new ArrayList<Task>());
		return tasks;
	}
}
