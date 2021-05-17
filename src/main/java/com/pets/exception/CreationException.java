package com.pets.exception;

public class CreationException extends Exception {

	private static final long serialVersionUID = 2854257598317433629L;

	public CreationException() {
		super();
	}

	public CreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreationException(String message) {
		super(message);
	}

	public CreationException(Throwable cause) {
		super(cause);
	}
	
}
