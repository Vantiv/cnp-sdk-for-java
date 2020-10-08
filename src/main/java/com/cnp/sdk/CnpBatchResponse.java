package com.cnp.sdk;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cnp.sdk.generate.*;

/**
 * Wrapper class to initialize the batch Responses
 */
public class CnpBatchResponse {
	private BatchResponse batchResponse;
	ResponseFileParser responseFileParser;
	private JAXBContext jc;
	private Unmarshaller unmarshaller;

	private boolean allTransactionsRetrieved = false;

	CnpBatchResponse(BatchResponse batchResponse) {
		setBatchResponse(batchResponse);
	}

	/**
	 * Consumes a ResponseFileParser and sets up the appropriate structure for JAXB marshalling/demarshalling
	 * @param responseFileParser
	 * @throws CnpBatchException
	 */
	public CnpBatchResponse(ResponseFileParser responseFileParser) throws CnpBatchException{
		this.responseFileParser = responseFileParser;
		String batchResponseXML = "";

		try {
			batchResponseXML = responseFileParser.getNextTag("batchResponse");
			//batchResponseXML = "<batchResponse cnpBatchId=\"1431\" merchantId=\"101\" xmlns=\"http://www.vantivcnp.com/schema\"></batchResponse>";
			jc = JAXBContext.newInstance("com.cnp.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			batchResponse = (BatchResponse) unmarshaller.unmarshal(new StringReader(batchResponseXML));
		} catch (JAXBException e) {
			throw new CnpBatchException("There was an exception while trying to unmarshall batchResponse: " + batchResponseXML, e);
		} catch (Exception e) {
			throw new CnpBatchException("There was an unknown error while parsing the response file.", e);
		}
	}

	void setBatchResponse(BatchResponse batchResponse) {
		this.batchResponse = batchResponse;
	}

	BatchResponse getBatchResponse() {
		return this.batchResponse;
	}

	public long getCnpBatchId() {
		return this.batchResponse.getCnpBatchId();
	}

	public String getMerchantId() {
		return this.batchResponse.getMerchantId();
	}

	/**
	 * Retrieves the next transaction from the batch response object.
	 * @return the TransactionType object, or null (if all transactions have been accessed)
	 */
	public CnpTransactionInterface getNextTransaction(){
		if( allTransactionsRetrieved ){
			throw new CnpBatchNoMoreBatchTransactionException("All transactions from this batch have already been retrieved");
		}

		String txnXML = "";

		try {
			txnXML = responseFileParser.getNextTag("transactionResponse");
		} catch (Exception e) {
			allTransactionsRetrieved = true;
			throw new CnpBatchNoMoreBatchTransactionException("All transactions from this batch have already been retrieved");
		}

		try {
			@SuppressWarnings("unchecked")
			TransactionType objToRet = ((JAXBElement<TransactionType>)unmarshaller.unmarshal(new StringReader(txnXML))).getValue();
			return objToRet;
		} catch (JAXBException e) {
			throw new CnpBatchException("There was an exception while trying to unmarshall transactionResponse: " + txnXML, e);
		}
	}

	/**
	 * Parses the next transaction in the batch response and applies the appropos method of the CnpResponseProcessor
	 * (probably an anonymous class) to it.
	 * @param processor
	 * @return true or false, indicating whether another transaction was read.
	 */
	@SuppressWarnings("unchecked")
	public boolean processNextTransaction(CnpResponseProcessor processor){
	    String txnXml;
	    CnpTransactionInterface objToRet;

        try {
            txnXml = responseFileParser.getNextTag("transactionResponse");
        } catch (Exception e) {
            return false;
        }
        
	    try {
	        objToRet = ((JAXBElement<TransactionType>)unmarshaller.unmarshal(new StringReader(txnXml))).getValue();
	    } catch (JAXBException e){
	        throw new CnpBatchException("There was an exception while trying to unmarshall transactionResponse: " + txnXml, e);
	    }

	    // The following code figures out the type of objToRet and calls the corresponding method on processor
        // IMPORTANT: When doing an SDK upgrade, add a process method for any new types into CnpResponseProcessor
		String methodName = "process" + objToRet.getClass().getSimpleName();
		try {
			// First, determine the method we need to call via reflection based on the method name
			Method processMethod = CnpResponseProcessor.class.getMethod(methodName, objToRet.getClass());
			// Next, invoke the method on the processor object with objToRet as the only argument
			processMethod.invoke(processor, objToRet);
			return true;
		} catch (NoSuchMethodException e) {
		    // Called if the CnpResponseProcessor interface is missing the method needed
			// The only reason this would be called is if an SDK upgrade was not done correctly
			throw new CnpBatchException("CnpResponseProcessor is missing a method: " + methodName, e);
		} catch (IllegalAccessException e) {
			// Called if we don't have proper permission to call the method (this should never occur)
			// If this ever occurs, try calling processMethod.setAccessible(true); before invoking it
			throw new CnpBatchException("Illegally accessed " + methodName, e);
		} catch (InvocationTargetException e) {
			// Called if the method in processor throws an exception (the implementing class throws an exception)
			// For future reference, if this exception is raised in:
			//  - A test, then the SDK test is implemented incorrectly (exception was thrown during test)
			//  - A merchant's environment, then they are implementing the process method incorrectly
			throw new CnpBatchException("An uncaught exception occurred in " + methodName, e);
		}
	}
}
