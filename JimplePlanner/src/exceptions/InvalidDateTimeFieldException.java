package exceptions;

@SuppressWarnings("serial")
public class InvalidDateTimeFieldException extends Exception{

	public InvalidDateTimeFieldException() {}
	
	public InvalidDateTimeFieldException(String message) {
		super(message);
	}
	
}
