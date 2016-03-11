package org.jimple.planner;

import java.time.LocalDateTime;
import java.util.Comparator;

class Task{
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;
	private String type;
	private boolean isOverDue;
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "todo";
	private static final String TYPE_DEADLINE = "deadline";
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
}
