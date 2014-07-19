package src.ddpsc.exceptions;

@SuppressWarnings("serial")
public class UserException extends Exception
{
	public UserException()
	{
		super("The user data is incomplete.");
	}
	
	public UserException(String message)
	{
		super(message);
	}
}
