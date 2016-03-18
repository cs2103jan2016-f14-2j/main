package org.jimple.planner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

class Task{
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;
	private String type;
	private boolean isOverDue;
	private int taskId;
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "floating";
	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_STATIC = "static";
	private Formatter formatter;
	
	// Constructors
	public Task(String aTitle) {
		this.formatter = new Formatter();
		this.title = aTitle;
		this.description = new String("");
		this.category = new String("");
		this.fromDateTime = null;
		this.toDateTime = null;
		this.type = TYPE_TODO;
		this.isOverDue = false;
		this.taskId = 1000;
	}
	
	public String getPrettyFromDate()	{
		String prettyFromDate = new String("");
		prettyFromDate = formatter.formatPrettyDate(fromDateTime);
		return prettyFromDate;
	}
	
	public String getPrettyToDate()	{
		String prettyToDate = new String("");
		prettyToDate = formatter.formatPrettyDate(toDateTime);
		return prettyToDate;
	}
	
	public String getPrettyFromTime()	{
		String prettyFromTime = new String("");
		prettyFromTime = formatter.formatPrettyTime(fromDateTime);
		return prettyFromTime;
	}
	
	public String getPrettyToTime()	{
		String prettyToTime = new String("");
		prettyToTime = formatter.formatPrettyTime(toDateTime);
		return prettyToTime;
	}

	public String getFromTimeString() {
		if (fromDateTime == null) {
			return "";
		}
		return fromDateTime.toString();
	}

	public LocalDateTime getFromTime(){
		return fromDateTime;
	}
	
	public void setFromDate(String dateTime) {
		if (dateTime == null) {
		} else {
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

	public LocalDateTime getToTime(){
		return toDateTime;
	}
	
	public void setToDate(String dateTime) {
		if (dateTime == null) {
		} else {
			this.toDateTime = LocalDateTime.parse(dateTime);
		}
		checkAndSetType();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	private void checkAndSetType(){
		if(isTodo()){
			this.type = TYPE_TODO;
		} else if(isDeadline()){
			this.type = TYPE_DEADLINE;
		} else if (isEvent()){
			this.type = TYPE_EVENT;
		}
	}
	
	private boolean isTodo(){
		return (getToTime() == null && getFromTime() == null);
	}
	
	private boolean isDeadline(){
		return (getToTime() != null && getFromTime() == null);
	}
	
	private boolean isEvent(){
		return (getFromTime() != null);
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

	public String getCategory() {
		if (category == null) {
			return "";
		}
		return category;
	}
	
	public void setIsOverDue(boolean overDueStatus)	{
		isOverDue = overDueStatus;
	}
	
	public boolean getIsOverDue()	{
		return isOverDue;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setTaskId(int taskId){
		this.taskId = taskId;
	}
	
	public int getTaskId(){
		return taskId;
	}
	
	public static Comparator<Task> getFromDateComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getFromTime().compareTo(task2.getFromTime());
			}
		};
	}
	
	public static Comparator<Task> getToDateComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getToTime().compareTo(task2.getToTime());
			}
		};
	}
	
	public static Comparator<Task> getTaskIdComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				int task1id = task1.getTaskId();
				int task2id = task2.getTaskId();
				int result;
				if(task1id<task2id){
					result = -1;
				} else if (task1id==task2id){
					result = 0;
				} else {
					result = 1;
				}
				return result;
			}
		};
	}
	
	/*
	 * the following methods are to be used only for non hashing purposes, if a hashset is to be used,
	 * DO NOT EDIT any of the tasks inside this hashset for it will cause a memory leak
	 */
	@Override
	public int hashCode() {
		final int prime = 97;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fromDateTime == null) ? 0 : fromDateTime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((toDateTime == null) ? 0 : toDateTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
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
