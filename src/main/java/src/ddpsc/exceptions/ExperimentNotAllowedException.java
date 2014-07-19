package src.ddpsc.exceptions;

@SuppressWarnings("serial")
public class ExperimentNotAllowedException extends Exception
{
	public ExperimentNotAllowedException(String message)
	{
		super(message);
	}
}
