package src.ddpsc.exceptions;

/**
 * Thrown if either the input or returned user data is incomplete or invalid.
 * 
 * Incompleteness and invalidity are to be determined by the user data class itself.
 * 
 * @author shill, cjmcentee
 */
@SuppressWarnings("serial")
public class UserException extends Exception
{
	public UserException()
	{
		super("The user data is incomplete or invalid.");
	}
	
	public UserException(String message)
	{
		super(message);
	}
}
