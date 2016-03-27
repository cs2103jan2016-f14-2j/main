package org.jimple.planner.storage.test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.storage.StorageSave;
import org.junit.Test;

public class TestWriteTask {
	private StorageSave storageSave = new StorageSave();
	private ExampleTasks exampleTasksGenerator = new ExampleTasks();
	
	//This is a normal use case for writing to file
	@Test
	public void writeTasksTest1() {
		String[] linesToCheck = {
				"/:title:Go exercise, you fatty//:cat:Keep fit/",
				"/:title:Read Lord of The Rings/",
				"/:title:Learn to play the harmonica/",
				"/:title:go through my anime backlog/",
				"/:title:submit report before countdown party//:from:2015-12-31T23:59/",
				"/:title:deadline for GER1000 quiz//:from:2016-03-06T23:59/",
				"/:title:Hand in cs2103 progress report//:cat:Homework//:from:2016-03-09T23:59/",
				"/:title:register for Orbital//:desc:keep my summer occupied//:cat:Self-learning//:from:2016-05-15T16:00/",
				"/:title:Do 2100 assignment//:desc:due very soon//:cat:Homework//:from:2016-07-29T23:59/",
				"/:title:eat with the bros//:desc:the same place//:from:2016-01-10T15:00//:to:2016-01-10T17:00/",
				"/:title:prepare for chap goh mei dinner//:cat:family time//:from:2016-02-16T12:00//:to:2016-02-16T15:00/",
				"/:title:Makan here for chap goh mei//:desc:at cousin's place//:cat:family time//:from:2016-02-16T19:00//:to:2016-02-16T22:00/",
				"/:title:business workshop//:from:2016-06-16T12:00//:to:2016-06-16T14:00/",
				"/:title:Attend seminar//:desc:at SOC//:from:2016-08-11T11:00//:to:2016-08-11T17:00/"
		};
		StringWriter sw = new StringWriter();
		BufferedWriter output = new BufferedWriter(sw);
		ArrayList<ArrayList<Task>> tasks = ExampleTasks.getExampleTasks();
		storageSave.testWriteTasks(tasks, output);
		String totalOutput = sw.toString();
		String[] totalOutputArray = totalOutput.split(System.getProperty("line.separator"));
		String message1 = "String[] should be equal, "+String.valueOf(totalOutputArray.length)+" "+String.valueOf(linesToCheck.length);
		assertEquals(message1, totalOutputArray.length, linesToCheck.length);
		for(int i=0; i<totalOutputArray.length; i++){
			String message2 = "line " + String.valueOf(i);
			assertEquals(message2, totalOutputArray[i], linesToCheck[i]);
		}
	}
	
	//Boundary case, when all lists are empty
	@Test
	public final void writeTasksTest2(){
		StringWriter sw = new StringWriter();
		BufferedWriter output = new BufferedWriter(sw);
		ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
		tasks.add(new ArrayList<Task>());
		tasks.add(new ArrayList<Task>());
		tasks.add(new ArrayList<Task>());
		storageSave.testWriteTasks(tasks, output);
		String totalOutput = sw.toString();
		assertEquals("Should be empty", totalOutput, "");
	}

}
