package src.ddpsc.exceptions;

public class InvalidPasswordException extends Exception{
	
	private static final long serialVersionUID = 8484706791846541302L;
	public InvalidPasswordException(){
		//default?
		super("Password must be atleast 7 characters long, and contain one number or special character.");
	}
	public InvalidPasswordException(String m){
		super(m);
	}
}
