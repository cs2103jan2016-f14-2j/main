package org.jimple.planner.exceptions;

@SuppressWarnings("serial")
public class InvalidCommandException extends Exception {

	public InvalidCommandException() {}
	
	public InvalidCommandException(String message) {
		super(message);
	}
	
}
