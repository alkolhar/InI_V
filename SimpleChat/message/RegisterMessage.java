package message;

public class RegisterMessage extends Message {
	private static final long serialVersionUID = 4134675333776963692L;
	
	public RegisterMessage (String user) {
		super.user = user;
	} 
	 
	public String toString () {
		return  "Register " + user;
	} 
} 