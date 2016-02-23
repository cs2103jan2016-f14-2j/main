import java.time.LocalDateTime;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

class Event {
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private String title;
	private String description;
	private String category;

	// Constructors
	public Event(String aTitle) {
		this.setTitle(aTitle);
		this.setDescription(new String());
		this.setCategory(new String());
		this.fromDateTime = null;
		this.toDateTime = null;
	}

	public String getFromTime() {
		if(fromDateTime == null){
			return "";
		}
		return fromDateTime.toString();
	}
	
	public void setFromDate(String dateTime) {
		this.fromDateTime =  LocalDateTime.parse(dateTime);
	}
	
	public String getToTime() {
		if(toDateTime == null){
			return "";
		}
		return toDateTime.toString();
	}
	
	public void setToDate(String dateTime)	{
		this.toDateTime = LocalDateTime.parse(dateTime);
	}
	
	public String getTitle() {
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
	
}

public class Logic {

	private String ADD_COMMAND = "add";
	private String EDIT_COMMAND = "edit";
	private String DISPLAY_COMMAND = "display";
	private String DELETE_COMMAND = "delete";
	private String HELP_COMMAND = "help";

	private Stack<String> temporaryHistory;
	Parser parser = new Parser();
	Storage store = new Storage();

	// Constructor
	public Logic() {
		temporaryHistory = new Stack<String>();
	}

	public void execute(String inputString) {
		InputStruct parsedInput = parser.parseInput(inputString);

	}
	
	//puts task into the Event object
	private void addToTaskList(String[] parsedInput, String originalInput) {
		Event newTask = new Event(parsedInput[0]);
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != "") {
				switch (i) {
				case 1:
					newTask.setDescription(parsedInput[i]);
					break;
				case 2:
					newTask.setFromDate(parsedInput[i]);
					break;
				case 3:
					newTask.setToDate(parsedInput[i]);
					break;
				case 4:
					newTask.setCategory(parsedInput[i]);
					break;
				default:
					break;
				}
			}
		}
		store.extractEventToString(newTask);
		temporaryHistory.push(originalInput);
	}
}
