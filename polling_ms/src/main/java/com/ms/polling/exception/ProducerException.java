package com.ms.polling.exception;

public class ProducerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3799510208521202155L;

	public ProducerException() {
		super();
	}

	public ProducerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProducerException(String message) {
		super(message);
	}

}
