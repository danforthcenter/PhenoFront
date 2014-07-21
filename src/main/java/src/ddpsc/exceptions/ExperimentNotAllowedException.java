package src.ddpsc.exceptions;

/**
 * Thrown if an experiment does not exist to be accessed, or is not allowed by the
 * user to be accessed.
 * 
 * @author shill, cjmcentee
 */
@SuppressWarnings("serial")
public class ExperimentNotAllowedException extends Exception
{
	public ExperimentNotAllowedException(String message)
	{
		super(message);
	}
}
