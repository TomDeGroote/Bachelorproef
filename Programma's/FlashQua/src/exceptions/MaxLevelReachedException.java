package exceptions;

@SuppressWarnings("serial")
public class MaxLevelReachedException extends Exception{
	public MaxLevelReachedException(String message){
		super(message);
	}
}
