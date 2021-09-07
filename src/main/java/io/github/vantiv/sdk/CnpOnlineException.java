package io.github.vantiv.sdk;


public class CnpOnlineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CnpOnlineException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpOnlineException(String message) {
		super(message);
	}

}
