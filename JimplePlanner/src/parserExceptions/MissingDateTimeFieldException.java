package parserExceptions;

@SuppressWarnings("serial")
public class MissingDateTimeFieldException extends Exception{

	public MissingDateTimeFieldException() {}
	
	public MissingDateTimeFieldException(String message) {
		super(message);
	}
	
}
