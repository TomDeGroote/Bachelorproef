package exceptions;

@SuppressWarnings("serial")
public class OutOfTimeException extends Exception{
	public OutOfTimeException(String message){
		super(message);
	}
}
