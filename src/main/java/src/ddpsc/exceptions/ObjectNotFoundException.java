package src.ddpsc.exceptions;

/**
 * Thrown if a database object could not be found.
 * 
 * @author shill, cjmcentee
 */
@SuppressWarnings("serial")
public class ObjectNotFoundException extends Exception
{
	public ObjectNotFoundException()
	{
		super("The database object could not be found.");
	}
	
	public ObjectNotFoundException(String message)
	{
		super(message);
	}
}
