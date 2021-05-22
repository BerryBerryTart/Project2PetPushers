package com.pets.exception;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1809894060768929331L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}	
}
