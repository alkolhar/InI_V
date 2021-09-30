package message;

public class UnregisterMessage extends Message {
	private static final long serialVersionUID = -6699849272113765128L;
	
	public UnregisterMessage (String user) {
		super.user = user;
	} 
	
	public String toString () {
		return "Checkout " + user;
	} 
	
} 