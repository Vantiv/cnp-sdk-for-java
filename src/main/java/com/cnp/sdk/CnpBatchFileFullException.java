package com.cnp.sdk;


public class CnpBatchFileFullException extends CnpBatchException {

	private static final long serialVersionUID = 1L;

	public CnpBatchFileFullException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpBatchFileFullException(String message) {
		super(message);
	}

}
