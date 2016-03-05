package org.jimple.planner;

import java.time.LocalDateTime;

class Task{
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;

	// Constructors
	public Task(String aTitle) {
		this.title = aTitle;
		this.description = new String("");
		this.category = new String("");
		this.fromDateTime = null;
		this.toDateTime = null;
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

	public void setCategory(String category) {
		this.category = category;
	}
}
