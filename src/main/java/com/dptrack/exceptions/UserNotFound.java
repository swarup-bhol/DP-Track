package com.dptrack.exceptions;

public class UserNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFound(String message) {
		super(message);
	}

}
