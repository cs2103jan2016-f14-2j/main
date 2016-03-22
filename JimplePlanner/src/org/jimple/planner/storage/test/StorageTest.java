package org.jimple.planner.storage.test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jimple.planner.Task;
import org.jimple.planner.storage.StorageLoad;
import org.jimple.planner.storage.StorageSave;
import org.junit.Test;

public class StorageTest {
	private StorageSave saveUnit = new StorageSave();
	private StorageLoad loadUnit = new StorageLoad();
	
	@Test
	public void testGetTasks() throws IOException{
		ArrayList<ArrayList<Task>> tasks = loadUnit.getTestTasks();
		ArrayList<Task> floating = tasks.get(0);
		ArrayList<Task> deadline = tasks.get(1);
		ArrayList<Task> event = tasks.get(2);
		
		String[] floating1Check = {"Go exercise, you fatty", "Keep fit"};
		String[] floating2Check = {"Read Lord of The Rings"};
		String[] floating3Check = {"Learn to play the harmonica"};
		String[] floating4Check = {"go through my anime backlog"};
		String floating1Title = floating.get(0).getTitle();
		String floating1Category = floating.get(0).getCategory();
		String floating2Title = floating.get(1).getTitle();
		String floating3Title = floating.get(2).getTitle();
		String floating4Title = floating.get(3).getTitle();
		assertEquals("true if same", floating1Check[0], floating1Title);
		assertEquals("true if same", floating1Check[1], floating1Category);
		assertEquals("true if same", floating2Check[0], floating2Title);
		assertEquals("true if same", floating3Check[0], floating3Title);
		assertEquals("true if same", floating4Check[0], floating4Title);
		
		String[] deadline3Check = {"submit report before countdown party", "2015-12-31T23:59"};
		String[] deadline4Check = {"deadline for GER1000 quiz", "2016-03-06T23:59"};
		String[] deadline2Check = {"Hand in cs2103 progress report", "Homework", "2016-03-09T23:59"}; //homework is cate
		String[] deadline5Check = {"register for Orbital", "keep my summer occupied", "Self-learning", "2016-05-15T16:00"};
		String[] deadline1Check = {"Do 2100 assignment", "due very soon", "Homework", "2016-07-29T23:59"};
		String deadline1Title = deadline.get(0).getTitle();
		String deadline1ToTime = deadline.get(0).getFromTimeString();
		String deadline2Title = deadline.get(1).getTitle();
		String deadline2ToTime = deadline.get(1).getFromTimeString();
		String deadline3Title = deadline.get(2).getTitle();
		String deadline3ToTime = deadline.get(2).getFromTimeString();
		String deadline3Category = deadline.get(2).getCategory();
		String deadline4Title = deadline.get(3).getTitle();
		String deadline4ToTime = deadline.get(3).getFromTimeString();
		String deadline4Category = deadline.get(3).getCategory();
		String deadline4Description = deadline.get(3).getDescription();
		String deadline5Title = deadline.get(4).getTitle();
		String deadline5ToTime = deadline.get(4).getFromTimeString();
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
		
		String[] event1Actual = loadUnit.testExtractTasksToStringArray(event.get(0));
		String[] event2Actual = loadUnit.testExtractTasksToStringArray(event.get(1));
		String[] event3Actual = loadUnit.testExtractTasksToStringArray(event.get(2));
		String[] event4Actual = loadUnit.testExtractTasksToStringArray(event.get(3));
		String[] event5Actual = loadUnit.testExtractTasksToStringArray(event.get(4));
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
