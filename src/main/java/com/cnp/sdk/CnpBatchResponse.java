package com.cnp.sdk;

import java.io.StringReader;

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
	    String txnXml = "";
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
	    
	    if(objToRet instanceof SaleResponse){
            processor.processSaleResponse((SaleResponse) objToRet);
        } else if (objToRet instanceof AuthorizationResponse){
            processor.processAuthorizationResponse((AuthorizationResponse) objToRet);
        } else if (objToRet instanceof CreditResponse){
            processor.processCreditResponse((CreditResponse) objToRet);
        } else if (objToRet instanceof RegisterTokenResponse){
            processor.processRegisterTokenResponse((RegisterTokenResponse) objToRet);
        } else if (objToRet instanceof CaptureGivenAuthResponse){
            processor.processCaptureGivenAuthResponse((CaptureGivenAuthResponse) objToRet);
        } else if (objToRet instanceof ForceCaptureResponse){
            processor.processForceCaptureResponse( (ForceCaptureResponse) objToRet);
        } else if (objToRet instanceof AuthReversalResponse){
            processor.processAuthReversalResponse( (AuthReversalResponse) objToRet);
        } else if (objToRet instanceof CaptureResponse){
            processor.processCaptureResponse((CaptureResponse) objToRet);
        } else if (objToRet instanceof EcheckVerificationResponse){
            processor.processEcheckVerificationResponse( (EcheckVerificationResponse) objToRet);
        } else if (objToRet instanceof EcheckCreditResponse){
            processor.processEcheckCreditResponse( (EcheckCreditResponse) objToRet);
        } else if (objToRet instanceof EcheckRedepositResponse){
            processor.processEcheckRedepositResponse( (EcheckRedepositResponse) objToRet);
        } else if (objToRet instanceof EcheckSalesResponse){
            processor.processEcheckSalesResponse((EcheckSalesResponse) objToRet);
        } else if (objToRet instanceof AccountUpdateResponse){
            processor.processAccountUpdate((AccountUpdateResponse) objToRet);
        } else if (objToRet instanceof EcheckPreNoteSaleResponse){
            processor.processEcheckPreNoteSaleResponse((EcheckPreNoteSaleResponse) objToRet);
        } else if (objToRet instanceof EcheckPreNoteCreditResponse){
            processor.processEcheckPreNoteCreditResponse((EcheckPreNoteCreditResponse) objToRet);
        } else if (objToRet instanceof UpdateSubscriptionResponse) {
            processor.processUpdateSubscriptionResponse((UpdateSubscriptionResponse)objToRet);
        } else if (objToRet instanceof CancelSubscriptionResponse) {
            processor.processCancelSubscriptionResponse((CancelSubscriptionResponse)objToRet);
        } else if (objToRet instanceof UpdateCardValidationNumOnTokenResponse) {
            processor.processUpdateCardValidationNumOnTokenResponse((UpdateCardValidationNumOnTokenResponse)objToRet);
        } else if (objToRet instanceof SubmerchantCreditResponse) {
            processor.processSubmerchantCreditResponse((SubmerchantCreditResponse)objToRet);
        } else if (objToRet instanceof PayFacCreditResponse) {
            processor.processPayFacCreditResponse((PayFacCreditResponse)objToRet);
        } else if (objToRet instanceof VendorCreditResponse) {
            processor.processVendorCreditRespsonse((VendorCreditResponse)objToRet);
        } else if (objToRet instanceof ReserveCreditResponse) {
            processor.processReserveCreditResponse((ReserveCreditResponse)objToRet);
        } else if (objToRet instanceof PhysicalCheckCreditResponse) {
            processor.processPhysicalCheckCreditResponse((PhysicalCheckCreditResponse)objToRet);
        } else if (objToRet instanceof SubmerchantDebitResponse) {
            processor.processSubmerchantDebitResponse((SubmerchantDebitResponse)objToRet);
        } else if (objToRet instanceof PayFacDebitResponse) {
            processor.processPayFacDebitResponse((PayFacDebitResponse)objToRet);
        } else if (objToRet instanceof VendorDebitResponse) {
            processor.processVendorDebitResponse((VendorDebitResponse)objToRet);
        } else if (objToRet instanceof ReserveDebitResponse) {
            processor.processReserveDebitResponse((ReserveDebitResponse)objToRet);
        } else if (objToRet instanceof PhysicalCheckDebitResponse) {
            processor.processPhysicalCheckDebitResponse((PhysicalCheckDebitResponse)objToRet);
        } else if (objToRet instanceof FundingInstructionVoidResponse) {
            processor.processFundingInstructionVoidResponse((FundingInstructionVoidResponse)objToRet);
        } else if (objToRet instanceof FastAccessFundingResponse) {
			processor.processFastAccessFundingResponse((FastAccessFundingResponse)objToRet);
		}
	    return true;
	}

}
