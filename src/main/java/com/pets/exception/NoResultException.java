package com.pets.exception;

public class NoResultException extends Exception {

	public NoResultException() {
	}

	public NoResultException(String message) {
		super(message);
	}

	public NoResultException(Throwable cause) {
		super(cause);
	}

	public NoResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
