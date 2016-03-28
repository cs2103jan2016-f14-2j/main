package org.jimple.planner.logic;

import org.jimple.planner.Task;

public class LogicPreviousTask {
	private Task previousTask;
	private String previousCommand;

	public LogicPreviousTask(String previousCommand, Task previousTask) {
		this.previousCommand = previousCommand;
		this.previousTask = previousTask;
	}

	public Task getPreviousTask() {
		return previousTask;
	}

	public void setPreviousTask(Task previousTask) {
		this.previousTask = previousTask;
	}

	public String getPreviousCommand() {
		return previousCommand;
	}

	public void setPreviousCommand(String previousCommand) {
		this.previousCommand = previousCommand;
	}
}
