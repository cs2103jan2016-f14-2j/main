package org.jimple.planner.test.task;

import static org.junit.Assert.*;

import org.jimple.planner.task.Task;
import org.junit.Test;

public class TaskTest {
	private static final String FIELD_TO_EVENT = "2016-05-10T23:59";
	private static final String FIELD_FROM_EVENT = "2016-08-10T00:00";
	private static final String FIELD_FROM_DEADLINE = "2016-03-24T00:00";
	private static final String FIELD_TITLE_TODO = "buy bananas";
	private static final String FIELD_TITLE_EVENT = "summer internship";
	private static final String FIELD_TITLE_DEADLINE = "complete pre-tutorial activities";
	private static final String FIELD_DESCRIPTION_TODO = "get cavendish only";
	private static final String FIELD_DESCRIPTION_EVENT = "location: Google HQ";
	private static final String FIELD_DESCRIPTION_DEADLINE = "Each team member should ensure the existence of at least some automated tests for his/her part of the code.";
	private static final String TYPE_FLOATING = "floating";
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_DEADLINE = "deadline";
	private Task todoTask;
	private Task eventTask;
	private Task deadlineTask;
	private Task genericTask;
	
	private String title;
	private String description;
	private String category;
	private String type;
	private boolean isOverDue;
	private int taskId;
	
	private void createAllTaskTypes(){
		createTodoTask();
		createEventTask();
		createDeadlineTask();
	}
	private void createTodoTask(){
		todoTask = new Task(FIELD_TITLE_TODO);
		todoTask.setDescription(FIELD_DESCRIPTION_TODO);
	}
	
	private void createEventTask(){
		eventTask = new Task(FIELD_TITLE_EVENT);
		eventTask.setDescription(FIELD_DESCRIPTION_EVENT);
		eventTask.setFromDate(FIELD_FROM_EVENT);
		eventTask.setToDate(FIELD_TO_EVENT);
	}
	
	private void createDeadlineTask(){
		deadlineTask = new Task(FIELD_TITLE_DEADLINE);
		deadlineTask.setDescription(FIELD_DESCRIPTION_DEADLINE);
		deadlineTask.setFromDate(FIELD_FROM_DEADLINE);
	}
	
	@Test
	public void validateCheckAndSetType() {
		createAllTaskTypes();
		assertTrue("task type: floating", todoTask.getType()==TYPE_FLOATING);
		assertTrue("task type: event", eventTask.getType()==TYPE_EVENT);
		assertTrue("task type: deadline", deadlineTask.getType()==TYPE_DEADLINE);
	}

}
