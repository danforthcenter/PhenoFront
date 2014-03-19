package src.ddpsc.exceptions;

/**
 * Exception representing bad DBconfig files.
 * @author shill
 *
 */
public class MalformedConfigException extends Exception{
	private static final long serialVersionUID = 1L;

	public MalformedConfigException(String message){
		super(message);
	}
}
