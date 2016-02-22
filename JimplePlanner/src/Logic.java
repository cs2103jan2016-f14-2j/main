import java.util.Calendar;

class Event {
	private Calendar fromTime;
	private Calendar toTime;
	private String title;
	private String description;
	private String category;
	
	//Constructors
	public Event(String aTitle){
		this.setTitle(aTitle);
		this.setDescription(new String());
		this.setCategory(new String());
		this.setFromTime(null);
		this.setToTime(null);
	}
	
	public String getTitle()	{
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Calendar getToTime() {
		return toTime;
	}

	public void setToTime(Calendar toTime) {
		this.toTime = toTime;
	}

	public Calendar getFromTime() {
		return fromTime;
	}

	public void setFromTime(Calendar fromTime) {
		this.fromTime = fromTime;
	}
	

}

public class Logic {

}
