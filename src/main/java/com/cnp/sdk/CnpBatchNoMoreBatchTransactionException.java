package com.cnp.sdk;


public class CnpBatchNoMoreBatchTransactionException extends CnpBatchException {

	private static final long serialVersionUID = 1L;

	public CnpBatchNoMoreBatchTransactionException(String message, Exception ume) {
		super(message, ume);
	}

	public CnpBatchNoMoreBatchTransactionException(String message) {
		super(message);
	}

}
