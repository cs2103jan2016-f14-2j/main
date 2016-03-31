package org.jimple.planner.exceptions;

@SuppressWarnings("serial")
public class InvalidFromAndToTimeException extends Exception {
	public InvalidFromAndToTimeException() {
	}
	
	public InvalidFromAndToTimeException(String message)	{
		super(message);
	}

}
