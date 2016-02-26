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
import java.util.LinkedList;

public class Storage {
	public static String DEFAULT_FILE_NAME = "planner.jim";
	public static String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	public static String TAGS_CATEGORY = ":cat:";
	public static String TAGS_DESCRIPTION = ":desc:";
	public static String TAGS_FROM_TIME = ":from:";
	public static String TAGS_TO_TIME = ":to:";
	public static String TAGS_TITLE = ":title:";
	public static String TAGS_LINE_FIELD_SEPARATOR = "/";
	public static String EMPTY_STRING = "";
	
	
	private static File createFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	private static void forceCreateDefaultFiles(){
		createFile(DEFAULT_FILE_NAME);
		createFile(DEFAULT_TEMP_FILE_NAME);
	}
	
	private static BufferedReader createDefaultFileReader() throws FileNotFoundException {
		File file = createFile(DEFAULT_FILE_NAME);
		FileInputStream fileIn = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		return reader;
	}
	
	private static BufferedWriter createTempFileWriter() throws FileNotFoundException {
		File file = createFile(DEFAULT_TEMP_FILE_NAME);
		FileOutputStream fileOut = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		return writer;
	}

	//This method extracts all relevant fields from an Event and stores them as a String, each String line is an Event
	public static String extractEventToString(Event event){
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
	
	private static boolean isDescriptionExist(Event event){
		return !(event.getDescription().length()==0);
	}
	
	private static boolean isCategoryExist(Event event){
		return !(event.getCategory().length()==0);
	}
	
	private static boolean isFromTimeExist(Event event){
		return !(event.getFromTime().length()==0);
	}
	
	private static boolean isToTimeExist(Event event){
		return !(event.getToTime().length()==0);
	}
	
	//Minor formatting of string such that each "field" is enclosed with a "/"
	private static String formatToSaveString(String string){
		return TAGS_LINE_FIELD_SEPARATOR + string + TAGS_LINE_FIELD_SEPARATOR;
	}
	
	public boolean isSaved(ArrayList<Event> events) throws IOException{
		forceCreateDefaultFiles();
		writeToFile(events);
		return isSaveToFile();
	}
	
	private static void writeToFile(ArrayList<Event> events) throws IOException  {
		BufferedWriter tempWriter = createTempFileWriter();
		for(Event event: events){
			String lineString = extractEventToString(event);
			tempWriter.write(lineString);
			tempWriter.newLine();
		}
		tempWriter.close();
	}
	
	//this handles the deletion of files and the subsequent renaming of temporary file to the default filename
	private static boolean isSaveToFile(){
		File file = createFile(DEFAULT_FILE_NAME);
		File tempFile = createFile(DEFAULT_TEMP_FILE_NAME);

		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	public static LinkedList<Event> getEvents() throws IOException{
		BufferedReader defaultFileReader = createDefaultFileReader();
		LinkedList<Event> events = new LinkedList<Event>();
		String fileLineContent;
		while ((fileLineContent = defaultFileReader.readLine()) != null) {
			Event event = getEventFromLine(fileLineContent);
			events.add(event);
		}
		defaultFileReader.close();
		return events;
	}
	
	private static LinkedList<String> getSeparateFields(String fileLineContent){
		LinkedList<String> separatedContents = new LinkedList<String>(Arrays.asList(fileLineContent.split(TAGS_LINE_FIELD_SEPARATOR)));
		return separatedContents;
	}
	
	private static Event getEventFromLine(String fileLineContent){
		LinkedList<String> fileLineContentSeparated = getSeparateFields(fileLineContent);
		Event event = new Event(EMPTY_STRING);
		for(String field: fileLineContentSeparated){
			setFields(event, field);
		}
		return event;
	}
	
	//This method is purely for test purposes only
	public static Event testGetEventFromLine(String fileLineContent){
		return getEventFromLine(fileLineContent);
	}
	
	private static void setFields(Event event, String field){
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
	
	private static boolean isTitle(String field){
		return field.contains(TAGS_TITLE);
	}
	
	private static boolean isCategory(String field){
		return field.contains(TAGS_CATEGORY);
	}
	
	private static boolean isDescription(String field){
		return field.contains(TAGS_DESCRIPTION);
	}
	
	private static boolean isFromTime(String field){
		return field.contains(TAGS_FROM_TIME);
	}
	
	private static boolean isToTime(String field){
		return field.contains(TAGS_TO_TIME);
	}
	
	private static String getRemovedTitleTagString(String field){
		String removedTag = field.replace(TAGS_TITLE, EMPTY_STRING);
		return removedTag;
	}
	
	private static String getRemovedCategoryTagString(String field){
		String removedTag = field.replace(TAGS_CATEGORY, EMPTY_STRING);
		return removedTag;
	}
	
	private static String getRemovedDescriptionTagString(String field){
		String removedTag = field.replace(TAGS_DESCRIPTION, EMPTY_STRING);
		return removedTag;
	}
	
	private static String getRemovedFromTagString(String field){
		String removedTag = field.replace(TAGS_FROM_TIME, EMPTY_STRING);
		return removedTag;
	}
	
	private static String getRemovedToTagString(String field){
		String removedTag = field.replace(TAGS_TO_TIME, EMPTY_STRING);
		return removedTag;
	}
}
