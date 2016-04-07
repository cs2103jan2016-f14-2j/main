package org.jimple.planner.exceptions;

public class NoSuchTaskWithConflictException extends Exception{
	public NoSuchTaskWithConflictException() {}
	
	public NoSuchTaskWithConflictException(String message) {
		super(message);
	}
}
