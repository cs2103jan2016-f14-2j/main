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

public class Storage {
	public static String DEFAULT_FILE_NAME = "planner.jim";
	public static String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	public static String TAGS_CATEGORY = " :cat:";
	public static String TAGS_DESCRIPTION = " :desc:";
	public static String TAGS_FROM_TIME = " :from:";
	public static String TAGS_TO_TIME = " :to:";
	
	
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
		String lineString = event.getTitle();
		if(isDescriptionExist(event)){
			lineString = lineString + TAGS_DESCRIPTION + event.getDescription();
		} else if(isCategoryExist(event)) {
			lineString = lineString + TAGS_CATEGORY + event.getCategory();
		} else if (isFromTimeExist(event)){
			lineString = lineString + TAGS_FROM_TIME; //@TODO check implementation for LocalDateTime
		} else if (isToTimeExist(event)){
			lineString = lineString + TAGS_TO_TIME;	//@TODO check implementation for LocalDateTime
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
		return !(event.getFromTime()==null);
	}
	
	private static boolean isToTimeExist(Event event){
		return !(event.getToTime()==null);
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
	
	private static boolean isSaveToFile(){
		File file = createFile(DEFAULT_FILE_NAME);
		File tempFile = createFile(DEFAULT_TEMP_FILE_NAME);

		if(!file.delete() || !tempFile.renameTo(file)){
			return false;
		} else {
			return true;
		}
	}
	
	//@TODO implement a getEvents method
	/*public static ArrayList<Event> getEvents() throws FileNotFoundException{
		BufferedReader defaultFileReader = createDefaultFileReader();
		for
	}*/
	
}
