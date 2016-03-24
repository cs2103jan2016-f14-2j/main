package org.jimple.planner.logic;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class LogicClock {

	public LogicClock() {
	}
	
	public String getCurrentTime()	{		
		String currentTime = new String();
		currentTime += LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " ";
		currentTime += LocalDateTime.now().getDayOfMonth() + " ";
		currentTime += LocalDateTime.now().getMonth() + " ";
		currentTime += LocalDateTime.now().getHour() + ":";
		currentTime += LocalDateTime.now().getMinute();
		return currentTime;
	}
	
}
