package org.jimple.planner;

import static org.jimple.planner.Constants.TYPE_TODO;
import static org.jimple.planner.Constants.TYPE_EVENT;
import static org.jimple.planner.Constants.TYPE_DEADLINE;
import static org.jimple.planner.Constants.TASK_LABEL_BLUE;
import static org.jimple.planner.Constants.TASK_LABEL_DARK_RED;
import static org.jimple.planner.Constants.TASK_LABEL_DEFAULT;
import static org.jimple.planner.Constants.TASK_LABEL_GREEN;
import static org.jimple.planner.Constants.TASK_LABEL_ORANGE;
import static org.jimple.planner.Constants.TASK_LABEL_RED;
import static org.jimple.planner.Constants.TASK_LABEL_YELLOW;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Task{
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String type;
	private boolean isOverDue;
	private int taskId;
	private int label;
	private static Formatter formatter = new Formatter();
	
	// Default Constructor
	public Task(String aTitle) {
		this.title = aTitle;
		this.description = new String("");
		this.fromDateTime = null;
		this.toDateTime = null;
		this.type = TYPE_TODO;
		this.isOverDue = false;
		this.taskId = 1000;
		this.label = TASK_LABEL_DEFAULT;
	}
	
	//Constructor for creating a duplicate
	public Task (Task taskToBeDuplicated)	{
		this.title = taskToBeDuplicated.getTitle();
		this.description = taskToBeDuplicated.getDescription();
		this.fromDateTime = taskToBeDuplicated.getFromTime();
		this.toDateTime = taskToBeDuplicated.getToTime();
		this.type = taskToBeDuplicated.getType();
		this.isOverDue = taskToBeDuplicated.getIsOverDue();
		this.label = taskToBeDuplicated.getLabel();
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

	public LocalDateTime getToTime(){
		return toDateTime;
	}
	
	public void setToDate(String dateTime) {
		if (dateTime != null) {
			this.toDateTime = LocalDateTime.parse(dateTime);
		}
		checkAndSetType();
	}
	
	public String getType() {
		return type;
	}
	
	//THIS METHOD IS USED ONLY FOR TEST PURPOSES
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
		return (getFromTime() != null && getToTime() == null);
	}
	
	private boolean isEvent(){
		return (getFromTime() != null && getToTime() != null);
	}
	
	public boolean isValidType(){
		if(isTodo()){
			return true;
		} else if (isDeadline()){
			return true;
		} else if (isEvent()){
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

	public int getLabel() {
		return label;
	}
	
	public void setIsOverDue(boolean overDueStatus)	{
		isOverDue = overDueStatus;
	}
	
	public boolean getIsOverDue()	{
		return isOverDue;
	}

	public void setLabel(int label) {
		this.label = label;
	}
	
	public void setTaskId(int taskId){
		this.taskId = taskId;
	}
	
	public int getTaskId(){
		return taskId;
	}
	
	private static Comparator<Task> getFromDateTimeComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getFromTime().compareTo(task2.getFromTime());
			}
		};
	}
	
	private static Comparator<Task> getFromDateComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getFromTime().toLocalDate().compareTo(task2.getFromTime().toLocalDate());
			}
		};
	}
	
	private static Comparator<Task> getTaskIdComparator(){
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
	
	private static void sortDeadlines(ArrayList<Task> deadlineList){
		Comparator<Task> fromDateComparator = Task.getFromDateTimeComparator();
		Collections.sort(deadlineList, fromDateComparator);
	}
	
	private static void sortEvents(ArrayList<Task> eventList){
		Comparator<Task> fromDateComparator = Task.getFromDateTimeComparator();
		Collections.sort(eventList, fromDateComparator);
	}
	
	private static void sortById(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> taskIdComparator = Task.getTaskIdComparator();
		for(ArrayList<Task> taskList: allTaskLists){
			Collections.sort(taskList, taskIdComparator);
		}
	}
	
	public static void sortTasks(ArrayList<ArrayList<Task>> allTaskLists){
		assert allTaskLists.size() == 3;
		sortById(allTaskLists);
		sortDeadlines(allTaskLists.get(1));
		sortEvents(allTaskLists.get(2));
	}
	
	/*
	 * the following methods are to be used only for non hashing purposes, if a hashset is to be used,
	 * DO NOT EDIT any of the tasks inside this hashset for it will cause a memory leak
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fromDateTime == null) ? 0 : fromDateTime.hashCode());
		result = prime * result + label;
		result = prime * result + taskId;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((toDateTime == null) ? 0 : toDateTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (!(obj instanceof Task)){
			return false;
		}
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null){
				return false;
			}
		} else if (!description.equals(other.description)){
			return false;
		}
		if (fromDateTime == null) {
			if (other.fromDateTime != null){
				return false;
			}
		} else if (!fromDateTime.equals(other.fromDateTime)){
			return false;
		}
		if (label != other.label){
			return false;
		}
		if (taskId != other.taskId){
			return false;
		}
		if (title == null) {
			if (other.title != null){
				return false;
			}
		} else if (!title.equals(other.title)){
			return false;
		}
		if (toDateTime == null) {
			if (other.toDateTime != null){
				return false;
			}
		} else if (!toDateTime.equals(other.toDateTime)){
			return false;
		}
		if (type == null) {
			if (other.type != null){
				return false;
			}
		} else if (!type.equals(other.type)){
			return false;
		}
		return true;
	}
}
