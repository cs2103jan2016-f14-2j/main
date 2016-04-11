package org.jimple.planner.logic;

import org.jimple.planner.task.Task;

//@@author A0124952E
public class LogicPreviousTask {
	private Task previousTask;
	private String previousCommand;
	
	//@@author A0124952E
	public LogicPreviousTask(String previousCommand, Task previousTask) {
		this.previousCommand = previousCommand;
		this.previousTask = previousTask;
	}

	//@@author A0124952E
	public Task getPreviousTask() {
		return previousTask;
	}

	//@@author A0124952E
	public void setPreviousTask(Task previousTask) {
		this.previousTask = previousTask;
	}

	//@@author A0124952E
	public String getPreviousCommand() {
		return previousCommand;
	}

	//@@author A0124952E
	public void setPreviousCommand(String previousCommand) {
		this.previousCommand = previousCommand;
	}
}
