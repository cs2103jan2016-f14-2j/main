package org.jimple.planner.logfilehandler;

import static org.jimple.planner.constants.Constants.EMPTY_STRING;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//@@author A0135808B
public class LogFileHandler {
	//@@author A0135808B
	public static void setFileHandler(){
		try {
			String fileName = getLogFileName();
			FileHandler handler = new FileHandler(fileName, true);
			handler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//@@author A0135808B
	private static String getLogFileName() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String dateStr = dateFormat.format(cal.getTime());
		String fileName = "jimpleLogs"+File.separator+"jimpleLog_"+dateStr+".log";
		return fileName;
	}
	//@@author A0135808B
	public static void createDirectoryToLogFile(){
		String fileName = getLogFileName();
		File file = new File(fileName);
		assert !file.isDirectory();
		String dirPath = file.getAbsolutePath().replaceAll(file.getName(), EMPTY_STRING);
		File dir = new File(dirPath);
		dir.mkdirs();
	}
}
