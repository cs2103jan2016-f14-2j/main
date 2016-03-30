package org.jimple.planner.exceptions;

public class LabelExceedTotalException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3867191497493390625L;

	public LabelExceedTotalException(){}
	
	public LabelExceedTotalException(String message){
		super(message);
	}
}
