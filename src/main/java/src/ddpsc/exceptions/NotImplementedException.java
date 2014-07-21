package src.ddpsc.exceptions;

/**
 * Thrown if a method or class is not yet implemented, but accessed or called.
 * 
 * @author shill
 */
@SuppressWarnings("serial")
public class NotImplementedException extends Exception
{
	public NotImplementedException()
	{
		super("Method not implemented");
	}

	public NotImplementedException(String m)
	{
		super(m);
	}
}
