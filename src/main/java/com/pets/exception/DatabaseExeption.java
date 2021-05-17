package com.pets.exception;

public class DatabaseExeption extends Exception {

	private static final long serialVersionUID = -3196936574345559173L;

	public DatabaseExeption() {
		super();
	}

	public DatabaseExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DatabaseExeption(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseExeption(String message) {
		super(message);
	}

	public DatabaseExeption(Throwable cause) {
		super(cause);
	}

}
