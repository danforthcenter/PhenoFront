package src.ddpsc.exceptions;


@SuppressWarnings("serial")
public class InvalidPasswordException extends Exception
{
	public InvalidPasswordException()
	{
		// Default.
		super("Password must be non-empty.");
	}

	public InvalidPasswordException(String m)
	{
		super(m);
	}
}
