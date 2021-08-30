package io.github.vantiv.sdk;


public class CnpBatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CnpBatchException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpBatchException(String message) {
		super(message);
	}

}
