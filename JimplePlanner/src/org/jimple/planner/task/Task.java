package org.jimple.planner.task;

import static org.jimple.planner.constants.Constants.TYPE_DEADLINE;
import static org.jimple.planner.constants.Constants.TYPE_EVENT;
import static org.jimple.planner.constants.Constants.TYPE_TODO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String type;
	private boolean isOverDue;
	private boolean isDone;
	private int taskId;
	private ArrayList<Task> conflictedTasks;
	private TaskLabel taskLabel;

	// Default Constructor
	public Task(String aTitle) {
		this.title = aTitle;
		this.description = new String("");
		this.fromDateTime = null;
		this.toDateTime = null;
		this.type = TYPE_TODO;
		this.isOverDue = false;
		this.isDone = false;
		this.taskId = 1000;
		this.taskLabel = TaskLabel.getDefaultLabel();
		this.conflictedTasks = new ArrayList<Task>();
	}

	// Constructor for creating a duplicate
	public Task(Task taskToBeDuplicated) {
		this.title = taskToBeDuplicated.getTitle();
		this.description = taskToBeDuplicated.getDescription();
		this.fromDateTime = taskToBeDuplicated.getFromTime();
		this.toDateTime = taskToBeDuplicated.getToTime();
		this.type = taskToBeDuplicated.getType();
		this.isOverDue = taskToBeDuplicated.getIsOverDue();
		this.isDone = taskToBeDuplicated.getIsDone();
		this.taskId = taskToBeDuplicated.getTaskId();
		this.taskLabel = taskToBeDuplicated.getTaskLabel();
		this.conflictedTasks = taskToBeDuplicated.getConflictedTasks();
	}

	public String getPrettierFromDate(){
		String prettierFromDate = new String("");
		if(fromDateTime.getDayOfYear() == LocalDateTime.now().getDayOfYear()){
			prettierFromDate += "TODAY, ";
		}
		else{
			prettierFromDate += fromDateTime.getDayOfWeek() + ", ";
		}
		prettierFromDate += fromDateTime.getDayOfMonth() + " " + fromDateTime.getMonth() + " " + fromDateTime.getYear();
		return prettierFromDate;
	}

	public String getPrettyFromDate() {
		String prettyFromDate = new String("");
		prettyFromDate = Formatter.formatPrettyDate(fromDateTime);
		return prettyFromDate;
	}

	public String getPrettyToDate() {
		String prettyToDate = new String("");
		prettyToDate = Formatter.formatPrettyDate(toDateTime);
		return prettyToDate;
	}

	public String getPrettyFromTime() {
		String prettyFromTime = new String("");
		prettyFromTime = Formatter.formatPrettyTime(fromDateTime);
		return prettyFromTime;
	}

	public String getPrettyToTime() {
		String prettyToTime = new String("");
		prettyToTime = Formatter.formatPrettyTime(toDateTime);
		return prettyToTime;
	}

	public String getFromTimeString() {
		if (fromDateTime == null) {
			return "";
		}
		return fromDateTime.toString();
	}

	public LocalDateTime getFromTime() {
		return fromDateTime;
	}
	//@@author A0135808B
	public void setFromDate(String dateTime) {
		if (dateTime != null) {
			this.fromDateTime = LocalDateTime.parse(dateTime);
		}
		checkAndSetType();
	}

	public String getToTimeString() {
		if (toDateTime == null) {
			return "";
		}
		return toDateTime.toString();
	}

	public LocalDateTime getToTime() {
		return toDateTime;
	}
	//@@author A0135808B
	public void setToDate(String dateTime) {
		if (dateTime != null) {
			this.toDateTime = LocalDateTime.parse(dateTime);
		} else	{
			this.toDateTime = null;
		}
		checkAndSetType();
	}
	//@@author A0135808B
	public String getType() {
		return type;
	}

	// THIS METHOD IS USED ONLY FOR TEST PURPOSES
	public void setType(String type) {
		this.type = type;
	}
	//@@author A0135808B
	private void checkAndSetType() {
		if (isTodo()) {
			this.type = TYPE_TODO;
		} else if (isDeadline()) {
			this.type = TYPE_DEADLINE;
		} else if (isEvent()) {
			this.type = TYPE_EVENT;
		}
	}
	//@@author A0135808B
	private boolean isTodo() {
		return (getToTime() == null && getFromTime() == null);
	}
	//@@author A0135808B
	private boolean isDeadline() {
		return (getFromTime() != null && getToTime() == null);
	}
	//@@author A0135808B
	private boolean isEvent() {
		return (getFromTime() != null && getToTime() != null);
	}
	//@@author A0135808B
	public boolean isValidType() {
		if (isTodo()) {
			return true;
		} else if (isDeadline()) {
			return true;
		} else if (isEvent()) {
			return true;
		} else {
			return false;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		if (description == null) {
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	//@@author A0135808B
	public TaskLabel getTaskLabel() {
		return taskLabel;
	}

	public void setIsOverDue(boolean overDueStatus) {
		isOverDue = overDueStatus;
	}

	public boolean getIsOverDue() {
		return isOverDue;
	}

	/**
	 * labels: 0 - default 1 - blue 2 - green 3 - yellow 4 - orange 5 - red 6 -
	 * dark red
	 */
	//@@author A0135808B
	public void setTaskLabel(TaskLabel label) {
		this.taskLabel = label;
	}
	//@@author A0135808B
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	//@@author A0135808B
	public int getTaskId() {
		return taskId;
	}
	//@@author A0135808B
	public boolean getIsDone(){
		return isDone;
	}
	//@@author A0135808B
	public void setIsDone(boolean isDone){
		this.isDone = isDone;
	}
	
	public ArrayList<Task> getConflictedTasks()	{
		return conflictedTasks;
	}
	//@@author A0135808B
	public static void sortTasks(ArrayList<ArrayList<Task>> allTaskLists) {
		TaskSorter.sortTasks(allTaskLists);
	}
	//@@author A0135808B
	public static void sortTasksByTime(ArrayList<Task> list) {
		TaskSorter.sortTasksByTime(list);
	}
	//@@author generated
	@Override
	public int hashCode() {
		final int prime = 97;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fromDateTime == null) ? 0 : fromDateTime.hashCode());
		result = prime * result + (isDone ? 1231 : 1237);
		result = prime * result + taskId;
		result = prime * result + ((taskLabel == null) ? 0 : taskLabel.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((toDateTime == null) ? 0 : toDateTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	//@@author generated
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Task))
			return false;
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fromDateTime == null) {
			if (other.fromDateTime != null)
				return false;
		} else if (!fromDateTime.equals(other.fromDateTime))
			return false;
		if (isDone != other.isDone)
			return false;
		if (taskId != other.taskId)
			return false;
		if (taskLabel == null) {
			if (other.taskLabel != null)
				return false;
		} else if (!taskLabel.equals(other.taskLabel))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (toDateTime == null) {
			if (other.toDateTime != null)
				return false;
		} else if (!toDateTime.equals(other.toDateTime))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
