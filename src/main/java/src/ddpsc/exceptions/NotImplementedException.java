package src.ddpsc.exceptions;

public class NotImplementedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5517758235406469002L;
	public NotImplementedException(){
		super("Method not implemented");
	}
	
	public NotImplementedException(String m){
		super(m);
	}
}
