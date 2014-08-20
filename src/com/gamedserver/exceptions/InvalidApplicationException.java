package com.gamedserver.exceptions;

import javax.xml.ws.WebFault;

import com.gamedserver.exceptions.faults.InvalidApplicationFault;

@WebFault(faultBean = "com.gamedserver.exceptions.faults.InvalidApplicationFault", name = "InvalidApplicationFault")
public class InvalidApplicationException extends Exception {

	private static final long serialVersionUID = 1L;
	private InvalidApplicationFault faultInfo;
	
	public InvalidApplicationException() {
		super();
	}

	public InvalidApplicationException(String message, InvalidApplicationFault faultInfo) {
		super(message);
		
		this.faultInfo = faultInfo;
	}
	
	public InvalidApplicationException(String message, InvalidApplicationFault faultInfo, Throwable cause) {
		super(message, cause);
		
		this.faultInfo = faultInfo;
	}
	
	public InvalidApplicationFault getFaultInfo() {
		return faultInfo;
	}
}
