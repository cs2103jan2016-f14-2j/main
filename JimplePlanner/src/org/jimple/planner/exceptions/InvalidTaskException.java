package org.jimple.planner.exceptions;
//@@author A0135775W
public class InvalidTaskException extends Exception{

	private static final long serialVersionUID = 4657571341921237215L;
	
	public InvalidTaskException(){}
	
	public InvalidTaskException(String message){
		super(message);
	}
}
