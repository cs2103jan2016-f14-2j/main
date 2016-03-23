package org.jimple.planner.logic;

@SuppressWarnings("serial")
public class InvalidFromAndToTime extends Exception {
	public InvalidFromAndToTime() {
	}
	
	public InvalidFromAndToTime(String message)	{
		super(message);
	}

}
