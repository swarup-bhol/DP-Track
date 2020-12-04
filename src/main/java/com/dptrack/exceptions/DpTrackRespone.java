package com.dptrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DpTrackRespone {

	@ExceptionHandler(value = InvalidCredentialException.class)
	public ResponseEntity<Object> invalidCredential(InvalidCredentialException exception) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		return new ResponseEntity<>("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
