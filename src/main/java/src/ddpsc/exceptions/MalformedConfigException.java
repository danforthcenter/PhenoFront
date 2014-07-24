package src.ddpsc.exceptions;

/**
 * Thrown if a configuration file is missing required fields.
 * 
 * @author shill, cjmcentee
 */
@SuppressWarnings("serial")
public class MalformedConfigException extends Exception
{
	public MalformedConfigException()
	{
		super("The configuration file has incomplete fields that are necessary for this program.");
	}
	
	public MalformedConfigException(String message)
	{
		super(message);
	}
}
