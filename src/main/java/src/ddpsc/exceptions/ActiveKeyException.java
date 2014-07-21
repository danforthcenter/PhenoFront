package src.ddpsc.exceptions;

/**
 * Thrown if a user is attempting to make two downloads from the website at the same time.
 * 
 * @author shill
 */
@SuppressWarnings("serial")
public class ActiveKeyException extends Exception
{
	public ActiveKeyException(String m)
	{
		super(m);
	}
}
