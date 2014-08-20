package com.gamedserver.exceptions;

import javax.xml.ws.WebFault;

import com.gamedserver.exceptions.faults.UnauthorizedFault;

@WebFault(faultBean = "com.gamedserver.exceptions.faults.UnauthorizedFault", name = "UnauthorizedFault")
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 1L;
	private UnauthorizedFault faultInfo;
	
	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(String message, UnauthorizedFault faultInfo) {
		super(message);
		
		this.faultInfo = faultInfo;
	}
	
	public UnauthorizedException(String message, UnauthorizedFault faultInfo, Throwable cause) {
		super(message, cause);
		
		this.faultInfo = faultInfo;
	}
	
	public UnauthorizedFault getFaultInfo() {
		return faultInfo;
	}
}
