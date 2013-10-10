package src.ddpsc.exceptions;

public class UsernameExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3599564290439864841L;
	public UsernameExistsException(){
		super("Username already exists. Try again.");
	}
	public UsernameExistsException(String m){
		super(m);
	}

}
