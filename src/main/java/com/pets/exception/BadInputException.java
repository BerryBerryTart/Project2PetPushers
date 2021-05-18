package com.pets.exception;

public class BadInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1265108870552522833L;

	public BadInputException() {
	}

	public BadInputException(String message) {
		super(message);
	}

	public BadInputException(Throwable cause) {
		super(cause);
	}

	public BadInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
