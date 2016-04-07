package org.jimple.planner.storage;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.jimple.planner.task.Task;
import org.junit.Test;

public class StorageTestWriteTask {
	private StorageSave storageSave = new StorageSave();
	private StorageGetExampleTasks exampleTasksGenerator = new StorageGetExampleTasks();
	
	/*
	 * The purpose of this test is to see how tasks are written in an environment that does not save to a file directly
	 * each String in linesToCheck represent 1 task from the example of tasks given from getExampleTasks
	 */
	@Test
	public void writeTasksTest1() {
		String[] linesToCheck = {
				"/s/:isDone:false/s//s/:title:Go exercise, you fatty/s//s/:desc:Keep fit/s/",
				"/s/:isDone:false/s//s/:title:Read Lord of The Rings/s/",
				"/s/:isDone:false/s//s/:title:Learn to play the harmonica/s/",
				"/s/:isDone:false/s//s/:title:go through my anime backlog/s/",
				"/s/:isDone:false/s//s/:title:submit report before countdown party/s//s/:from:2015-12-31T23:59/s/",
				"/s/:isDone:false/s//s/:title:deadline for GER1000 quiz/s//s/:from:2016-03-06T23:59/s/",
				"/s/:isDone:false/s//s/:title:Hand in cs2103 progress report/s//s/:desc:Homework/s//s/:from:2016-03-09T23:59/s/",
				"/s/:isDone:false/s//s/:title:register for Orbital/s//s/:desc:keep my summer occupied/s//s/:from:2016-05-15T16:00/s/",
				"/s/:isDone:false/s//s/:title:Do 2100 assignment/s//s/:desc:due very soon/s//s/:from:2016-07-29T23:59/s/",
				"/s/:isDone:false/s//s/:title:eat with the bros/s//s/:desc:the same place/s//s/:from:2016-01-10T15:00/s//s/:to:2016-01-10T17:00/s/",
				"/s/:isDone:false/s//s/:title:prepare for chap goh mei dinner/s//s/:from:2016-02-16T12:00/s//s/:to:2016-02-16T15:00/s/",
				"/s/:isDone:false/s//s/:title:Makan here for chap goh mei/s//s/:desc:at cousin's place/s//s/:from:2016-02-16T19:00/s//s/:to:2016-02-16T22:00/s/",
				"/s/:isDone:false/s//s/:title:business workshop/s//s/:from:2016-06-16T12:00/s//s/:to:2016-06-16T14:00/s/",
				"/s/:isDone:false/s//s/:title:Attend seminar/s//s/:desc:at SOC/s//s/:from:2016-08-11T11:00/s//s/:to:2016-08-11T17:00/s/"

		};
		StringWriter sw = new StringWriter();
		BufferedWriter output = new BufferedWriter(sw);
		ArrayList<ArrayList<Task>> tasks = StorageGetExampleTasks.getExampleTasks();
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
		tasks.add(new ArrayList<Task>());
		storageSave.testWriteTasks(tasks, output);
		String totalOutput = sw.toString();
		assertEquals("Should be empty", totalOutput, "");
	}

}
