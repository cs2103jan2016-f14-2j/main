package parserExceptions;

@SuppressWarnings("serial")
public class DuplicateDateTimeFieldException extends Exception {

	public DuplicateDateTimeFieldException() {}
	
	public DuplicateDateTimeFieldException(String message) {
		super(message);
	}

}
