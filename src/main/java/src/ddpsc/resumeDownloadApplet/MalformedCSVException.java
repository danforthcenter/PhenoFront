package src.ddpsc.resumeDownloadApplet;

@SuppressWarnings("serial")
class MalformedCSVException extends Exception
{
	public MalformedCSVException(String message)
	{
		super("Malformed CSV. " + message);
	}
}