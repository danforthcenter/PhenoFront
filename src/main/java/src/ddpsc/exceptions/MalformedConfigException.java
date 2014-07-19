package src.ddpsc.exceptions;


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
