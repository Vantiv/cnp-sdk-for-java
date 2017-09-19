package com.cnp.sdk;


public class CnpBatchBatchFullException extends CnpBatchException {

	private static final long serialVersionUID = 1L;

	public CnpBatchBatchFullException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpBatchBatchFullException(String message) {
		super(message);
	}

}
