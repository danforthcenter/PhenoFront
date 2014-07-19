package src.ddpsc.exceptions;

@SuppressWarnings("serial")
public class UsernameExistsException extends Exception
{
	public UsernameExistsException()
	{
		super("Username already exists. Try again.");
	}

	public UsernameExistsException(String message)
	{
		super(message);
	}

}
