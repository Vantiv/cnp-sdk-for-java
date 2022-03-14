package io.github.vantiv.sdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.*;
import io.github.vantiv.sdk.generate.Void;

public class CnpOnline {

	private Properties config;
	private Communication communication;
	private Boolean removeStubs = false;

	/**
	 * Construct a CnpOnline using the configuration specified in $HOME/.cnp_SDK_config.properties
	 */
	public CnpOnline() {

		communication = new Communication();
		FileInputStream fileInputStream = null;

		try {
			config = new Properties();
			fileInputStream = new FileInputStream((new Configuration()).location());
			config.load(fileInputStream);
		} catch (FileNotFoundException e) {
			throw new CnpOnlineException("Configuration file not found." +
					" If you are not using the .cnp_SDK_config.properties file," +
					" please use the " + CnpOnline.class.getSimpleName() + "(Properties) constructor." +
					" If you are using .cnp_SDK_config.properties, you can generate one using java -jar cnp-sdk-for-java-x.xx.jar", e);
		} catch (IOException e) {
			throw new CnpOnlineException("Configuration file could not be loaded.  Check to see if the user running this has permission to access the file", e);
		} finally {
		    if (fileInputStream != null){
		        try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new CnpOnlineException("Configuration FileInputStream could not be closed.", e);
                }
		    }
		}
	}

	public CnpOnline(Properties config) {
		this.config = config;
		communication = new Communication();
	}

    public CnpOnline(Properties config, Boolean removeStubs) {
        this.config = config;
        this.removeStubs = removeStubs;
        communication = new Communication();
    }

	protected void setCommunication(Communication communication) {
		this.communication = communication;
	}

	public AuthorizationResponse authorize(Authorization auth) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return authorize(auth, request);
	}

	public AuthorizationResponse authorize(Authorization auth, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(auth);

		request.setTransaction(CnpContext.getObjectFactory().createAuthorization(auth));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (AuthorizationResponse)newresponse.getValue();
	}

	public AuthReversalResponse authReversal(AuthReversal reversal) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return authReversal(reversal, request);
	}

	public AuthReversalResponse authReversal(AuthReversal reversal, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(reversal);

		request.setTransaction(CnpContext.getObjectFactory().createAuthReversal(reversal));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (AuthReversalResponse)newresponse.getValue();
	}

	public CaptureResponse capture(Capture capture) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return capture(capture, request);
	}

	public CaptureResponse capture(Capture capture, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(capture);

		request.setTransaction(CnpContext.getObjectFactory().createCapture(capture));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (CaptureResponse)newresponse.getValue();
	}

	public CaptureGivenAuthResponse captureGivenAuth(CaptureGivenAuth captureGivenAuth) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return captureGivenAuth(captureGivenAuth, request);
	}

	public CaptureGivenAuthResponse captureGivenAuth(CaptureGivenAuth captureGivenAuth, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(captureGivenAuth);

		request.setTransaction(CnpContext.getObjectFactory().createCaptureGivenAuth(captureGivenAuth));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (CaptureGivenAuthResponse)newresponse.getValue();
	}

	public CreditResponse credit(Credit credit) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return credit(credit, request);
	}

	public CreditResponse credit(Credit credit, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(credit);

		request.setTransaction(CnpContext.getObjectFactory().createCredit(credit));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (CreditResponse)newresponse.getValue();
	}

	public EcheckCreditResponse echeckCredit(EcheckCredit echeckcredit) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return echeckCredit(echeckcredit, request);
	}

	public EcheckCreditResponse echeckCredit(EcheckCredit echeckcredit, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(echeckcredit);

		request.setTransaction(CnpContext.getObjectFactory().createEcheckCredit(echeckcredit));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (EcheckCreditResponse)newresponse.getValue();
	}

	public EcheckRedepositResponse echeckRedeposit(EcheckRedeposit echeckRedeposit) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return echeckRedeposit(echeckRedeposit, request);
	}

	public EcheckRedepositResponse echeckRedeposit(EcheckRedeposit echeckRedeposit, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(echeckRedeposit);

		request.setTransaction(CnpContext.getObjectFactory().createEcheckRedeposit(echeckRedeposit));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (EcheckRedepositResponse)newresponse.getValue();
	}

	public EcheckSalesResponse echeckSale(EcheckSale echeckSale) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return echeckSale(echeckSale, request);
	}

	public EcheckSalesResponse echeckSale(EcheckSale echeckSale, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(echeckSale);

		request.setTransaction(CnpContext.getObjectFactory().createEcheckSale(echeckSale));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (EcheckSalesResponse)newresponse.getValue();
	}

	public EcheckVerificationResponse echeckVerification(EcheckVerification echeckVerification) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return echeckVerification(echeckVerification, request);
	}

	public EcheckVerificationResponse echeckVerification(EcheckVerification echeckVerification, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(echeckVerification);

		request.setTransaction(CnpContext.getObjectFactory().createEcheckVerification(echeckVerification));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (EcheckVerificationResponse)newresponse.getValue();
	}

	public ForceCaptureResponse forceCapture(ForceCapture forceCapture) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return forceCapture(forceCapture, request);
	}

	public ForceCaptureResponse forceCapture(ForceCapture forceCapture, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(forceCapture);

		request.setTransaction(CnpContext.getObjectFactory().createForceCapture(forceCapture));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (ForceCaptureResponse)newresponse.getValue();
	}

	public SaleResponse sale(Sale sale) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return sale(sale, request);
	}

	public SaleResponse sale(Sale sale, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(sale);

		request.setTransaction(CnpContext.getObjectFactory().createSale(sale));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (SaleResponse)newresponse.getValue();
	}
	
	public FraudCheckResponse fraudCheck(FraudCheck fraudCheck) throws CnpOnlineException {
	    CnpOnlineRequest request = createCnpOnlineRequest();
	    return fraudCheck(fraudCheck, request);
	}
	
	public FraudCheckResponse fraudCheck(FraudCheck fraudCheck, CnpOnlineRequest overrides) throws CnpOnlineException {
	    CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
	    fillInReportGroup(fraudCheck);
	    
	    request.setTransaction(CnpContext.getObjectFactory().createFraudCheck(fraudCheck));
	    CnpOnlineResponse response = sendToCnp(request);
	    JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
	    return (FraudCheckResponse)newresponse.getValue();
	}

	public RegisterTokenResponse registerToken(RegisterTokenRequestType tokenRequest) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return registerToken(tokenRequest, request);
	}

	public RegisterTokenResponse registerToken(RegisterTokenRequestType tokenRequest, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(tokenRequest);

		request.setTransaction(CnpContext.getObjectFactory().createRegisterTokenRequest(tokenRequest));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (RegisterTokenResponse)newresponse.getValue();
	}

	public VoidResponse dovoid(Void v) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return dovoid(v, request);
	}

	public VoidResponse dovoid(Void v, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(v);

		request.setTransaction(CnpContext.getObjectFactory().createVoid(v));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (VoidResponse)newresponse.getValue();
	}

	public EcheckVoidResponse echeckVoid(EcheckVoid echeckVoid) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return echeckVoid(echeckVoid, request);
	}

	public EcheckVoidResponse echeckVoid(EcheckVoid echeckVoid, CnpOnlineRequest overrides) throws CnpOnlineException {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(echeckVoid);

		request.setTransaction(CnpContext.getObjectFactory().createEcheckVoid(echeckVoid));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (EcheckVoidResponse)newresponse.getValue();
	}

	public UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnToken(UpdateCardValidationNumOnToken update) {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return updateCardValidationNumOnToken(update, request);
	}

	public UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnToken(UpdateCardValidationNumOnToken update, CnpOnlineRequest overrides) {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(update);

		request.setTransaction(CnpContext.getObjectFactory().createUpdateCardValidationNumOnToken(update));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (UpdateCardValidationNumOnTokenResponse)newresponse.getValue();
	}

    public CancelSubscriptionResponse cancelSubscription(CancelSubscription cancellation) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return cancelSubscription(cancellation, request);
    }

    public CancelSubscriptionResponse cancelSubscription(CancelSubscription cancellation, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(CnpContext.getObjectFactory().createCancelSubscription(cancellation));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (CancelSubscriptionResponse)newresponse.getValue();
    }

    public UpdateSubscriptionResponse updateSubscription(UpdateSubscription update) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return updateSubscription(update, request);
    }

    public UpdateSubscriptionResponse updateSubscription(UpdateSubscription update, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(CnpContext.getObjectFactory().createUpdateSubscription(update));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (UpdateSubscriptionResponse)newresponse.getValue();
    }

    public CreatePlanResponse createPlan(CreatePlan create) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return createPlan(create, request);
    }

    public CreatePlanResponse createPlan(CreatePlan create, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(CnpContext.getObjectFactory().createCreatePlan(create));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (CreatePlanResponse)newresponse.getValue();
    }

    public UpdatePlanResponse updatePlan(UpdatePlan update) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return updatePlan(update, request);
    }

    public UpdatePlanResponse updatePlan(UpdatePlan update, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(CnpContext.getObjectFactory().createUpdatePlan(update));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (UpdatePlanResponse)newresponse.getValue();
    }

    public ActivateResponse activate(Activate activate) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return activate(activate, request);
    }

    public ActivateResponse activate(Activate activate, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(activate);

        request.setTransaction(CnpContext.getObjectFactory().createActivate(activate));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ActivateResponse)newresponse.getValue();
    }

    public DeactivateResponse deactivate(Deactivate deactivate) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return deactivate(deactivate, request);
    }

    public DeactivateResponse deactivate(Deactivate deactivate, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(deactivate);

        request.setTransaction(CnpContext.getObjectFactory().createDeactivate(deactivate));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DeactivateResponse)newresponse.getValue();
    }

    public LoadResponse load(Load load) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return load(load, request);
    }

    public LoadResponse load(Load load, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(load);

        request.setTransaction(CnpContext.getObjectFactory().createLoad(load));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (LoadResponse)newresponse.getValue();
    }

    public UnloadResponse unload(Unload unload) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return unload(unload, request);
    }

    public UnloadResponse unload(Unload unload, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(unload);

        request.setTransaction(CnpContext.getObjectFactory().createUnload(unload));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (UnloadResponse)newresponse.getValue();
    }

    public BalanceInquiryResponse balanceInquiry(BalanceInquiry balanceInquiry) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return balanceInquiry(balanceInquiry, request);
    }

    public BalanceInquiryResponse balanceInquiry(BalanceInquiry balanceInquiry, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(balanceInquiry);

        request.setTransaction(CnpContext.getObjectFactory().createBalanceInquiry(balanceInquiry));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (BalanceInquiryResponse)newresponse.getValue();
    }

    public ActivateReversalResponse activateReversal(ActivateReversal activateReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return activateReversal(activateReversal, request);
    }

    public ActivateReversalResponse activateReversal(ActivateReversal activateReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(activateReversal);

        request.setTransaction(CnpContext.getObjectFactory().createActivateReversal(activateReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ActivateReversalResponse)newresponse.getValue();
    }

    public DeactivateReversalResponse deactivateReversal(DeactivateReversal deactivateReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return deactivateReversal(deactivateReversal, request);
    }

    public DeactivateReversalResponse deactivateReversal(DeactivateReversal deactivateReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(deactivateReversal);

        request.setTransaction(CnpContext.getObjectFactory().createDeactivateReversal(deactivateReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DeactivateReversalResponse)newresponse.getValue();
    }

    public LoadReversalResponse loadReversal(LoadReversal loadReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return loadReversal(loadReversal, request);
    }

    public LoadReversalResponse loadReversal(LoadReversal loadReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(loadReversal);

        request.setTransaction(CnpContext.getObjectFactory().createLoadReversal(loadReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (LoadReversalResponse)newresponse.getValue();
    }

    public UnloadReversalResponse unloadReversal(UnloadReversal unloadReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return unloadReversal(unloadReversal, request);
    }

    public UnloadReversalResponse unloadReversal(UnloadReversal unloadReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(unloadReversal);

        request.setTransaction(CnpContext.getObjectFactory().createUnloadReversal(unloadReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (UnloadReversalResponse)newresponse.getValue();
    }

    public RefundReversalResponse refundReversal(RefundReversal refundReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return refundReversal(refundReversal, request);
    }

    public RefundReversalResponse refundReversal(RefundReversal refundReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(refundReversal);

        request.setTransaction(CnpContext.getObjectFactory().createRefundReversal(refundReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (RefundReversalResponse)newresponse.getValue();
    }

    public DepositReversalResponse depositReversal(DepositReversal depositReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return depositReversal(depositReversal, request);
    }

    public DepositReversalResponse depositReversal(DepositReversal depositReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(depositReversal);

        request.setTransaction(CnpContext.getObjectFactory().createDepositReversal(depositReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DepositReversalResponse)newresponse.getValue();
    }

    public DepositTransactionReversalResponse depositTransactionReversal(DepositTransactionReversal depositTransactionReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return depositTransactionReversal(depositTransactionReversal, request);
    }

    public DepositTransactionReversalResponse depositTransactionReversal(DepositTransactionReversal depositTransactionReversal, CnpOnlineRequest overrides) {
	    CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
	    fillInReportGroup(depositTransactionReversal);

	    request.setTransaction(CnpContext.getObjectFactory().createDepositTransactionReversal(depositTransactionReversal));
	    CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DepositTransactionReversalResponse) newresponse.getValue();
    }

    public RefundTransactionReversalResponse refundTransactionReversal(RefundTransactionReversal refundTransactionReversal) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return refundTransactionReversal(refundTransactionReversal, request);
    }

    public RefundTransactionReversalResponse refundTransactionReversal(RefundTransactionReversal transactionReversal, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(transactionReversal);

        request.setTransaction(CnpContext.getObjectFactory().createRefundTransactionReversal(transactionReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (RefundTransactionReversalResponse) newresponse.getValue();
    }

    public TransactionTypeWithReportGroup queryTransaction(QueryTransaction queryTransaction) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return queryTransaction(queryTransaction, request);
    }

    public TransactionTypeWithReportGroup queryTransaction(QueryTransaction queryTransaction, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(queryTransaction);

        request.setTransaction(CnpContext.getObjectFactory().createQueryTransaction(queryTransaction));
        CnpOnlineResponse response = sendQueryTxnToCnp(request,true);
        JAXBElement<? extends TransactionTypeWithReportGroup> txnTypeWithReportGroup = response.getTransactionResponse();
        return txnTypeWithReportGroup.getValue();
    }
    
    public GiftCardCaptureResponse giftCardCapture(GiftCardCapture giftCardCapture) {
    	CnpOnlineRequest request = createCnpOnlineRequest();
        return giftCardCapture(giftCardCapture, request);
    }
    
    public GiftCardCaptureResponse giftCardCapture(GiftCardCapture giftCardCapture, CnpOnlineRequest overrides) {
    	CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
    	fillInReportGroup(giftCardCapture);

        request.setTransaction(CnpContext.getObjectFactory().createGiftCardCapture(giftCardCapture));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (GiftCardCaptureResponse)newresponse.getValue();
    }
    
    public GiftCardAuthReversalResponse giftCardAuthReversal(GiftCardAuthReversal giftCardAuthReversal) {
    	CnpOnlineRequest request = createCnpOnlineRequest();
        return giftCardAuthReversal(giftCardAuthReversal, request);
    }
    
    public GiftCardAuthReversalResponse giftCardAuthReversal(GiftCardAuthReversal giftCardAuthReversal, CnpOnlineRequest overrides) {
    	CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
    	fillInReportGroup(giftCardAuthReversal);

        request.setTransaction(CnpContext.getObjectFactory().createGiftCardAuthReversal(giftCardAuthReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (GiftCardAuthReversalResponse)newresponse.getValue();
    }
    
    public GiftCardCreditResponse giftCardCredit(GiftCardCredit giftCardCredit) {
    	CnpOnlineRequest request = createCnpOnlineRequest();
        return giftCardCredit(giftCardCredit, request);
    }
    
    public GiftCardCreditResponse giftCardCredit(GiftCardCredit giftCardCredit, CnpOnlineRequest overrides) {
    	CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
    	fillInReportGroup(giftCardCredit);

        request.setTransaction(CnpContext.getObjectFactory().createGiftCardCredit(giftCardCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (GiftCardCreditResponse)newresponse.getValue();
    }

    public PayFacCreditResponse payFacCredit(PayFacCredit payFacCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return payFacCredit(payFacCredit, request);
    }
    
    public PayFacCreditResponse payFacCredit(PayFacCredit payFacCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(payFacCredit);

        request.setTransaction(CnpContext.getObjectFactory().createPayFacCredit(payFacCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PayFacCreditResponse)newresponse.getValue();
    }

    public PayFacDebitResponse payFacDebit(PayFacDebit payFacDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return payFacDebit(payFacDebit, request);
    }
    
    public PayFacDebitResponse payFacDebit(PayFacDebit payFacDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(payFacDebit);

        request.setTransaction(CnpContext.getObjectFactory().createPayFacDebit(payFacDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PayFacDebitResponse)newresponse.getValue();
    }

    public SubmerchantCreditResponse submerchantCredit(SubmerchantCredit submerchantCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return submerchantCredit(submerchantCredit, request);
    }
    
    public SubmerchantCreditResponse submerchantCredit(SubmerchantCredit submerchantCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(submerchantCredit);

        request.setTransaction(CnpContext.getObjectFactory().createSubmerchantCredit(submerchantCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (SubmerchantCreditResponse)newresponse.getValue();
    }

    public SubmerchantDebitResponse submerchantDebit(SubmerchantDebit submerchantDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return submerchantDebit(submerchantDebit, request);
    }
    
    public SubmerchantDebitResponse submerchantDebit(SubmerchantDebit submerchantDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(submerchantDebit);

        request.setTransaction(CnpContext.getObjectFactory().createSubmerchantDebit(submerchantDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (SubmerchantDebitResponse)newresponse.getValue();
    }
    
    public ReserveCreditResponse submerchantCredit(ReserveCredit reserveCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return reserveCredit(reserveCredit, request);
    }
    
    public ReserveCreditResponse reserveCredit(ReserveCredit reserveCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(reserveCredit);

        request.setTransaction(CnpContext.getObjectFactory().createReserveCredit(reserveCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ReserveCreditResponse)newresponse.getValue();
    }

    public ReserveDebitResponse submerchantDebit(ReserveDebit reserveDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return reserveDebit(reserveDebit, request);
    }
    
    public ReserveDebitResponse reserveDebit(ReserveDebit reserveDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(reserveDebit);

        request.setTransaction(CnpContext.getObjectFactory().createReserveDebit(reserveDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ReserveDebitResponse)newresponse.getValue();
    }

    public FundingInstructionVoidResponse fundingInstructionVoid(FundingInstructionVoid fundingInstructionVoid) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return fundingInstructionVoid(fundingInstructionVoid, request);
    }
    
    public FundingInstructionVoidResponse fundingInstructionVoid(FundingInstructionVoid fundingInstructionVoid, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(fundingInstructionVoid);

        request.setTransaction(CnpContext.getObjectFactory().createFundingInstructionVoid(fundingInstructionVoid));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (FundingInstructionVoidResponse)newresponse.getValue();
    }
    
    public VendorCreditResponse vendorCredit(VendorCredit vendorCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return vendorCredit(vendorCredit, request);
    }
    
    public VendorCreditResponse vendorCredit(VendorCredit vendorCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(vendorCredit);

        request.setTransaction(CnpContext.getObjectFactory().createVendorCredit(vendorCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (VendorCreditResponse)newresponse.getValue();
    }

    public VendorDebitResponse physicalCheckDebit(VendorDebit vendorDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return vendorDebit(vendorDebit, request);
    }

    public VendorDebitResponse vendorDebit(VendorDebit vendorDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return vendorDebit(vendorDebit, request);
    }

    public VendorDebitResponse vendorDebit(VendorDebit vendorDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(vendorDebit);

        request.setTransaction(CnpContext.getObjectFactory().createVendorDebit(vendorDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (VendorDebitResponse)newresponse.getValue();
    }

    public PhysicalCheckCreditResponse physicalCheckCredit(PhysicalCheckCredit physicalCheckCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return physicalCheckCredit(physicalCheckCredit, request);
    }
    
    public PhysicalCheckCreditResponse physicalCheckCredit(PhysicalCheckCredit physicalCheckCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(physicalCheckCredit);
        
        request.setTransaction(CnpContext.getObjectFactory().createPhysicalCheckCredit(physicalCheckCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PhysicalCheckCreditResponse)newresponse.getValue();
    }
    
    public PhysicalCheckDebitResponse physicalCheckDebit(PhysicalCheckDebit physicalCheckDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return physicalCheckDebit(physicalCheckDebit, request);
    }
    
    public PhysicalCheckDebitResponse physicalCheckDebit(PhysicalCheckDebit physicalCheckDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(physicalCheckDebit);

        request.setTransaction(CnpContext.getObjectFactory().createPhysicalCheckDebit(physicalCheckDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PhysicalCheckDebitResponse)newresponse.getValue();
    }

	public FastAccessFundingResponse fastAccessFunding(FastAccessFunding fastAccessFunding) {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return fastAccessFunding(fastAccessFunding, request);
	}

	public FastAccessFundingResponse fastAccessFunding(FastAccessFunding fastAccessFunding, CnpOnlineRequest overrides) {
		CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
		fillInReportGroup(fastAccessFunding);

		request.setTransaction(CnpContext.getObjectFactory().createFastAccessFunding(fastAccessFunding));
		CnpOnlineResponse response = sendToCnp(request);
		JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
		return (FastAccessFundingResponse)newresponse.getValue();
	}

	public TranslateToLowValueTokenResponse TranslateToLowValueTokenRequest(TranslateToLowValueTokenRequestType translateToLowValueTokenRequest){
	    CnpOnlineRequest request = createCnpOnlineRequest();
	    return TranslateToLowValueTokenRequest(translateToLowValueTokenRequest, request);
    }

    public TranslateToLowValueTokenResponse TranslateToLowValueTokenRequest(TranslateToLowValueTokenRequestType translateToLowValueTokenRequest,
                                                                            CnpOnlineRequest overridingRequestHeader){
	    CnpOnlineRequest request = fillInMissingFieldsFromConfig(overridingRequestHeader);
	    fillInReportGroup(translateToLowValueTokenRequest);

        request.setTransaction(CnpContext.getObjectFactory().createTranslateToLowValueTokenRequest(translateToLowValueTokenRequest));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (TranslateToLowValueTokenResponse)newresponse.getValue();
    }

    public CustomerCreditResponse customerCredit(CustomerCredit customerCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return customerCredit(customerCredit, request);
    }

    public CustomerCreditResponse customerCredit(CustomerCredit customerCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(customerCredit);

        request.setTransaction(CnpContext.getObjectFactory().createCustomerCredit(customerCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (CustomerCreditResponse)newresponse.getValue();
    }

    public CustomerDebitResponse customerDebit(CustomerDebit customerDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return customerDebit(customerDebit, request);
    }

    public CustomerDebitResponse customerDebit(CustomerDebit customerDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(customerDebit);

        request.setTransaction(CnpContext.getObjectFactory().createCustomerDebit(customerDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (CustomerDebitResponse)newresponse.getValue();
    }

    public PayoutOrgCreditResponse payoutOrgCredit(PayoutOrgCredit payoutOrgCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return payoutOrgCredit(payoutOrgCredit, request);
    }

    public PayoutOrgCreditResponse payoutOrgCredit(PayoutOrgCredit payoutOrgCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(payoutOrgCredit);

        request.setTransaction(CnpContext.getObjectFactory().createPayoutOrgCredit(payoutOrgCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PayoutOrgCreditResponse)newresponse.getValue();
    }

    public PayoutOrgDebitResponse payoutOrgDebit(PayoutOrgDebit payoutOrgDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return payoutOrgDebit(payoutOrgDebit, request);
    }

    public PayoutOrgDebitResponse payoutOrgDebit(PayoutOrgDebit payoutOrgDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(payoutOrgDebit);

        request.setTransaction(CnpContext.getObjectFactory().createPayoutOrgDebit(payoutOrgDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PayoutOrgDebitResponse)newresponse.getValue();
    }

	private CnpOnlineRequest createCnpOnlineRequest() {
		CnpOnlineRequest request = new CnpOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		request.setLoggedInUser(config.getProperty("loggedInUser",null));
		request.setAuthentication(authentication);
		return request;
	}

	private CnpOnlineRequest fillInMissingFieldsFromConfig(CnpOnlineRequest request) {
		CnpOnlineRequest retVal = new CnpOnlineRequest();
		retVal.setAuthentication(new Authentication());

		if(request.getAuthentication() == null) {
			Authentication authentication = new Authentication();
			authentication.setPassword(config.getProperty("password"));
			authentication.setUser(config.getProperty("username"));
			retVal.setAuthentication(authentication);
		} else {

		    if(request.getAuthentication().getUser() == null) {
				retVal.getAuthentication().setUser(config.getProperty("username"));
			}
			else {
				retVal.getAuthentication().setUser(request.getAuthentication().getUser());
			}

			if(request.getAuthentication().getPassword() == null) {
				retVal.getAuthentication().setPassword(config.getProperty("password"));
			}
			else {
				retVal.getAuthentication().setPassword(request.getAuthentication().getPassword());
			}
		}

		if(request.getMerchantId() == null) {
			retVal.setMerchantId(config.getProperty("merchantId"));
		} else {
			retVal.setMerchantId(request.getMerchantId());
		}
        retVal.setVersion(Versions.XML_VERSION);

		if(request.getMerchantSdk() == null) {
			retVal.setMerchantSdk(Versions.SDK_VERSION);
		} else {
			retVal.setMerchantSdk(request.getMerchantSdk());
		}

		if(request.getLoggedInUser() != null) {
			retVal.setLoggedInUser(request.getLoggedInUser());
		}

		return retVal;
	}

	private CnpOnlineResponse sendToCnp(CnpOnlineRequest request) throws CnpOnlineException {
		try {
			StringWriter sw = new StringWriter();
			CnpContext.getJAXBContext().createMarshaller().marshal(request, sw);
			String xmlRequest = sw.toString();

			if(this.removeStubs){
			    xmlRequest = xmlRequest.replaceAll("<[A-Za-z]+\\s*/>", "");
			}
		//	System.out.println("config-------------"+config+"\n\n\n");
			String xmlResponse = communication.requestToServer(xmlRequest, config);
			/**
			 * This was added to accommodate an issue with OpenAccess and possibly VAP where the XML namespace returned
			 * contains the extra "/online".
			 * This issue will be fixed for OpenAccess in Jan 2018
			 */
			if(xmlResponse.contains("http://www.vantivcnp.com/schema/online")){
			    xmlResponse = xmlResponse.replace("http://www.vantivcnp.com/schema/online", "http://www.vantivcnp.com/schema");
			}

			CnpOnlineResponse response = (CnpOnlineResponse)CnpContext.getJAXBContext().createUnmarshaller().unmarshal(new StringReader(xmlResponse));
			// non-zero responses indicate a problem
			if(!"0".equals(response.getResponse())) {
				if ("2".equals(response.getResponse()) || "3".equals(response.getResponse())) {
					throw new CnpInvalidCredentialException(response.getMessage());
				} else if ("4".equals(response.getResponse())) {
					throw new CnpConnectionLimitExceededException(response.getMessage());
				} else if ("5".equals(response.getResponse())) {
					throw new CnpObjectionableContentException(response.getMessage());
				} else {
					throw new CnpOnlineException(response.getMessage());
				}
			}
			return response;
		} catch(JAXBException ume) {
			throw new CnpOnlineException("Error validating xml data against the schema", ume);
		} finally {
		}
	}

    private CnpOnlineResponse sendQueryTxnToCnp(CnpOnlineRequest request, Boolean retrySite) throws CnpOnlineException {
        String xmlResponse;
        CnpOnlineResponse response = null;
        QueryTransactionResponse queryTxnResponse = null;
        String siteAddress;
        String altAddress;
        try {
            StringWriter sw = new StringWriter();
            CnpContext.getJAXBContext().createMarshaller().marshal(request, sw);
            String xmlRequest = sw.toString();

            if (this.removeStubs) {
                xmlRequest = xmlRequest.replaceAll("<[A-Za-z]+\\s*/>", "");
            }
            //	System.out.println("config-------------"+config+"\n\n\n");
            if(retrySite)
                config.setProperty("url", config.getProperty("multiSiteUrl1", config.getProperty("url")));
            else
                config.setProperty("url", config.getProperty("multiSiteUrl2", config.getProperty("url")));

            CommManager.reset();
            xmlResponse = communication.requestToServer(xmlRequest, config);
            try {
                if (xmlResponse.contains("queryTransactionResponse")) {
                    xmlResponse = getSchema(xmlResponse);
                    response = (CnpOnlineResponse) CnpContext.getJAXBContext().createUnmarshaller().unmarshal(new StringReader(xmlResponse));
                    queryTxnResponse = (QueryTransactionResponse) response.getTransactionResponse().getValue();
                    if (queryTxnResponse != null && "151".equals(queryTxnResponse.getResponse())) {
                        if(!retrySite){
                            siteAddress=config.getProperty("multiSiteUrl1")!=null ?" : "+config.getProperty("multiSiteUrl1"): "";
                            altAddress=config.getProperty("multiSiteUrl2")!=null ?" in "+config.getProperty("multiSiteUrl2"): "";
                            queryTxnResponse.setMessage("Original transaction not found"+altAddress+". Site unavailable"+siteAddress);
                            response.setResponse(queryTxnResponse.toString());
                            return response;
                        }
                        else {
                            config.setProperty("url", config.getProperty("multiSiteUrl2", "https://payments.west.vantivprelive.com/vap/communicator/online"));
                            CommManager.reset();
                            xmlResponse = communication.requestToServer(xmlRequest, config);
                        }
                    }
                }
            } catch (CnpOnlineException ex) {
                siteAddress=config.getProperty("multiSiteUrl2")!=null ?" : "+config.getProperty("multiSiteUrl2"): "";
                altAddress=config.getProperty("multiSiteUrl1")!=null ?" in "+config.getProperty("multiSiteUrl1"): "";
                queryTxnResponse.setMessage("Original transaction not found"+altAddress+". Site unavailable"+siteAddress);
                response.setResponse(queryTxnResponse.toString());
                return response;
            }
            /**
             * This was added to accommodate an issue with OpenAccess and possibly VAP where the XML namespace returned
             * contains the extra "/online".
             * This issue will be fixed for OpenAccess in Jan 2018
             */
            xmlResponse = getSchema(xmlResponse);
            response = (CnpOnlineResponse) CnpContext.getJAXBContext().createUnmarshaller().unmarshal(new StringReader(xmlResponse));
            // non-zero responses indicate a problem
            if (!"0".equals(response.getResponse())) {
                if ("2".equals(response.getResponse()) || "3".equals(response.getResponse())) {
                    throw new CnpInvalidCredentialException(response.getMessage());
                } else if ("4".equals(response.getResponse())) {
                    throw new CnpConnectionLimitExceededException(response.getMessage());
                } else if ("5".equals(response.getResponse())) {
                    throw new CnpObjectionableContentException(response.getMessage());
                } else {
                    throw new CnpOnlineException(response.getMessage());
                }
            }
            return response;
        } catch (JAXBException ume) {
            throw new CnpOnlineException("Error validating xml data against the schema", ume);
        } catch (CnpOnlineException ex) {
            if (retrySite) {
             response=sendQueryTxnToCnp(request, false);
             return response;
            }
            throw new CnpOnlineException("Original transaction not found - Site/s unavailable");
        } finally {
        }

    }

    private String getSchema(String xmlResponse) {
        if (xmlResponse.contains("http://www.vantivcnp.com/schema/online")) {
            xmlResponse = xmlResponse.replace("http://www.vantivcnp.com/schema/online", "http://www.vantivcnp.com/schema");
        }
        return xmlResponse;
    }

    private void fillInReportGroup(TransactionTypeWithReportGroup txn) {
		if(txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup"));
		}
	}

	private void fillInReportGroup(TransactionTypeWithReportGroupAndPartial txn) {
		if(txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup"));
		}
	}

}
