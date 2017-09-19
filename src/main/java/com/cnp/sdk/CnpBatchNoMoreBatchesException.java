package com.cnp.sdk;


public class CnpBatchNoMoreBatchesException extends CnpBatchException {

	private static final long serialVersionUID = 1L;

	public CnpBatchNoMoreBatchesException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpBatchNoMoreBatchesException(String message) {
		super(message);
	}

}
