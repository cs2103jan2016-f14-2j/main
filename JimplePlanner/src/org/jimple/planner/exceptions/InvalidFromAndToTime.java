package org.jimple.planner.exceptions;

@SuppressWarnings("serial")
public class InvalidFromAndToTime extends Exception {
	public InvalidFromAndToTime() {
	}
	
	public InvalidFromAndToTime(String message)	{
		super(message);
	}

}
