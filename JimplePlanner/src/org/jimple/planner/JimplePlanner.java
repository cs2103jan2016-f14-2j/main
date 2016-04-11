package org.jimple.planner;

import org.jimple.planner.ui.UiComponent;
import org.jimple.planner.logfilehandler.LogFileHandler;

public class JimplePlanner {
	
	public static void main(String[] args){
		LogFileHandler.createDirectoryToLogFile();
		LogFileHandler.setFileHandler();
		UiComponent Jimple = new UiComponent();
		Jimple.begin(args);
	}
	
}