package com.dptrack.exceptions;

public class InvalidCredentialException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public InvalidCredentialException(String msg ,Throwable cause){
		super(msg, cause);
	}
	public InvalidCredentialException(String msg){
		super(msg);
	}
}
