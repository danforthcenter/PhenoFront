package src.ddpsc.exceptions;

/**
 * Thrown when a user is attempting to change their username, or an admin is attempting to
 * add a user, but the username already exists with another user.
 * 
 * @author shill, cjmcentee
 */
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
