package src.ddpsc.exceptions;

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
