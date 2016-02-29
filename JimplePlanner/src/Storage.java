import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Storage {
	private static String DEFAULT_FILE_NAME = "planner.jim";
	private static String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	private static String TAGS_CATEGORY = ":cat:";
	private static String TAGS_DESCRIPTION = ":desc:";
	private static String TAGS_FROM_TIME = ":from:";
	private static String TAGS_TO_TIME = ":to:";
	private static String TAGS_TITLE = ":title:";
	private static String TAGS_LINE_FIELD_SEPARATOR = "/";
	private static String EMPTY_STRING = "";
	
	private File createFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	private BufferedReader createDefaultFileReader() throws FileNotFoundException {
		File file = createFile(DEFAULT_FILE_NAME);
		FileInputStream fileIn = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		return reader;
	}
	
	private BufferedWriter createTempFileWriter() throws FileNotFoundException {
		File file = createFile(DEFAULT_TEMP_FILE_NAME);
		FileOutputStream fileOut = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		return writer;
	}

	//This method extracts all relevant fields from an Event and stores them as a String, each String line is an Event
	public String extractEventToString(Event event){
		String lineString = formatToSaveString(TAGS_TITLE + event.getTitle());
		if(isDescriptionExist(event)){
			String descriptionString = formatToSaveString(TAGS_DESCRIPTION + event.getDescription());
			lineString = lineString + descriptionString;
		} 
		if(isCategoryExist(event)) {
			String categoryString = formatToSaveString(TAGS_CATEGORY + event.getCategory());
			lineString = lineString + categoryString;
		} 
		if (isFromTimeExist(event)){
			String fromTimeString = formatToSaveString(TAGS_FROM_TIME + event.getFromTime());
			lineString = lineString + fromTimeString;
		} 
		if (isToTimeExist(event)){
			String fromToString = formatToSaveString(TAGS_TO_TIME + event.getToTime());
			lineString = lineString + fromToString;
		}
		return lineString;
	}
	
	private boolean isDescriptionExist(Event event){
		return !(event.getDescription().length()==0);
	}
	
	private boolean isCategoryExist(Event event){
		return !(event.getCategory().length()==0);
	}
	
	private boolean isFromTimeExist(Event event){
		return !(event.getFromTime().length()==0);
	}
	
	private boolean isToTimeExist(Event event){
		return !(event.getToTime().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "/"
	private String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	
	public boolean isSaved(ArrayList<Event> events) throws IOException{
		writeToFile(events);
		boolean saveStatus = isSaveToFile();
		return saveStatus;
	}
	
	private void writeToFile(ArrayList<Event> events) throws IOException  {
		BufferedWriter tempWriter = createTempFileWriter();
		for(Event event: events){
			String lineString = extractEventToString(event);
			tempWriter.write(lineString);
			tempWriter.newLine();
		}
		tempWriter.close();
	}
	
	//this handles the deletion of files and the subsequent renaming of temporary file to the default filename
	private boolean isSaveToFile(){
		File file = createFile(DEFAULT_FILE_NAME);
		File tempFile = createFile(DEFAULT_TEMP_FILE_NAME);

		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	public ArrayList<Event> getEvents() throws IOException{
		BufferedReader defaultFileReader = createDefaultFileReader();
		ArrayList<Event> events = new ArrayList<Event>();
		String fileLineContent;
		while ((fileLineContent = defaultFileReader.readLine()) != null) {
			Event event = getEventFromLine(fileLineContent);
			events.add(event);
		}
		defaultFileReader.close();
		return events;
	}
	
	private ArrayList<String> getSeparateFields(String fileLineContent){
		ArrayList<String> separatedContents = new ArrayList<String>(Arrays.asList(fileLineContent.split(TAGS_LINE_FIELD_SEPARATOR)));
		return separatedContents;
	}
	
	private Event getEventFromLine(String fileLineContent){
		ArrayList<String> fileLineContentSeparated = getSeparateFields(fileLineContent);
		Event event = new Event(EMPTY_STRING);
		for(String field: fileLineContentSeparated){
			setFields(event, field);
		}
		return event;
	}
	
	//This method is purely for test purposes only
	public Event testGetEventFromLine(String fileLineContent){
		return getEventFromLine(fileLineContent);
	}
	
	private void setFields(Event event, String field){
		if(isTitle(field)){
			String titleString = getRemovedTitleTagString(field);
			event.setTitle(titleString);
		} else if(isCategory(field)){
			String catField = getRemovedCategoryTagString(field);
			event.setCategory(catField);
		} else if(isDescription(field)){
			String descField = getRemovedDescriptionTagString(field);
			event.setDescription(descField);
		} else if(isFromTime(field)){
			String fromField = getRemovedFromTagString(field);
			event.setFromDate(fromField);
		} else if (isToTime(field)){
			String toField = getRemovedToTagString(field);
			event.setToDate(toField);
		}
	}
	
	private boolean isTitle(String field){
		return field.contains(TAGS_TITLE);
	}
	
	private boolean isCategory(String field){
		return field.contains(TAGS_CATEGORY);
	}
	
	private boolean isDescription(String field){
		return field.contains(TAGS_DESCRIPTION);
	}
	
	private boolean isFromTime(String field){
		return field.contains(TAGS_FROM_TIME);
	}
	
	private boolean isToTime(String field){
		return field.contains(TAGS_TO_TIME);
	}
	
	private String getRemovedTitleTagString(String field){
		String removedTag = field.replace(TAGS_TITLE, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedCategoryTagString(String field){
		String removedTag = field.replace(TAGS_CATEGORY, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedDescriptionTagString(String field){
		String removedTag = field.replace(TAGS_DESCRIPTION, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedFromTagString(String field){
		String removedTag = field.replace(TAGS_FROM_TIME, EMPTY_STRING);
		return removedTag;
	}
	
	private String getRemovedToTagString(String field){
		String removedTag = field.replace(TAGS_TO_TIME, EMPTY_STRING);
		return removedTag;
	}
}
