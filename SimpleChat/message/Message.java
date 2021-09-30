package message;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = -2082704605553089835L;
	
	protected String user;
	
	public String getUser () {
		return user;
	} 
} 