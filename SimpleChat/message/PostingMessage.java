package message;

public class PostingMessage extends Message {
	private static final long serialVersionUID = -2645234121515049755L;
	
	private String text;
	
	public PostingMessage (String user, String text) {
		super.user = user;
		this.text = text;
	} 
	
	public String getText () {
		return text;
	} 
	
	public String toString () {
		return user + ": " + text;
	} 

} 