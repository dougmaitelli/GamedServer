package com.gamedserver.exceptions;

import javax.xml.ws.WebFault;

import com.gamedserver.exceptions.faults.InvalidUserFault;

@WebFault(faultBean = "com.gamedserver.exceptions.faults.InvalidUserFault", name = "InvalidUserFault")
public class InvalidUserException extends Exception {

	private static final long serialVersionUID = 1L;
	private InvalidUserFault faultInfo;
	
	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String message, InvalidUserFault faultInfo) {
		super(message);
		
		this.faultInfo = faultInfo;
	}
	
	public InvalidUserException(String message, InvalidUserFault faultInfo, Throwable cause) {
		super(message, cause);
		
		this.faultInfo = faultInfo;
	}
	
	public InvalidUserFault getFaultInfo() {
		return faultInfo;
	}
}
