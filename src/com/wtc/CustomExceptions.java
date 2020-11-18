package com.wtc;

public class CustomExceptions{}

/**
 * TableEntry Update Exception as extension of Runtime
 */
class UpdateTableEntryException extends RuntimeException{

	private static final long serialVersionUID = -7343512614748622944L;

	public UpdateTableEntryException(String errorMessage ) {
		super(errorMessage);
	}
}

/**
 * ClassCast Exception as extension of Runtime
 */
class ClassCastException extends RuntimeException{
	
	private static final long serialVersionUID = -5713778250350654669L;

	public ClassCastException(String errorMessage ) {
		super(errorMessage);
	}
}