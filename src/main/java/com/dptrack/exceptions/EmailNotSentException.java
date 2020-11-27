package com.dptrack.exceptions;

public class EmailNotSentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailNotSentException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailNotSentException(String message) {
		super(message);
	}
}
