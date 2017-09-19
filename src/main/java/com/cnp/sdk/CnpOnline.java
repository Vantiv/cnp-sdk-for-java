package com.cnp.sdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.cnp.sdk.generate.Activate;
import com.cnp.sdk.generate.ActivateResponse;
import com.cnp.sdk.generate.ActivateReversal;
import com.cnp.sdk.generate.ActivateReversalResponse;
import com.cnp.sdk.generate.AuthReversal;
import com.cnp.sdk.generate.AuthReversalResponse;
import com.cnp.sdk.generate.Authentication;
import com.cnp.sdk.generate.Authorization;
import com.cnp.sdk.generate.AuthorizationResponse;
import com.cnp.sdk.generate.BalanceInquiry;
import com.cnp.sdk.generate.BalanceInquiryResponse;
import com.cnp.sdk.generate.CancelSubscription;
import com.cnp.sdk.generate.CancelSubscriptionResponse;
import com.cnp.sdk.generate.Capture;
import com.cnp.sdk.generate.CaptureGivenAuth;
import com.cnp.sdk.generate.CaptureGivenAuthResponse;
import com.cnp.sdk.generate.CaptureResponse;
import com.cnp.sdk.generate.CreatePlan;
import com.cnp.sdk.generate.CreatePlanResponse;
import com.cnp.sdk.generate.Credit;
import com.cnp.sdk.generate.CreditResponse;
import com.cnp.sdk.generate.Deactivate;
import com.cnp.sdk.generate.DeactivateResponse;
import com.cnp.sdk.generate.DeactivateReversal;
import com.cnp.sdk.generate.DeactivateReversalResponse;
import com.cnp.sdk.generate.DepositReversal;
import com.cnp.sdk.generate.DepositReversalResponse;
import com.cnp.sdk.generate.EcheckCredit;
import com.cnp.sdk.generate.EcheckCreditResponse;
import com.cnp.sdk.generate.EcheckRedeposit;
import com.cnp.sdk.generate.EcheckRedepositResponse;
import com.cnp.sdk.generate.EcheckSale;
import com.cnp.sdk.generate.EcheckSalesResponse;
import com.cnp.sdk.generate.EcheckVerification;
import com.cnp.sdk.generate.EcheckVerificationResponse;
import com.cnp.sdk.generate.EcheckVoid;
import com.cnp.sdk.generate.EcheckVoidResponse;
import com.cnp.sdk.generate.ForceCapture;
import com.cnp.sdk.generate.ForceCaptureResponse;
import com.cnp.sdk.generate.FraudCheck;
import com.cnp.sdk.generate.FraudCheckResponse;
import com.cnp.sdk.generate.FundingInstructionVoid;
import com.cnp.sdk.generate.FundingInstructionVoidResponse;
import com.cnp.sdk.generate.GiftCardAuthReversal;
import com.cnp.sdk.generate.GiftCardAuthReversalResponse;
import com.cnp.sdk.generate.GiftCardCapture;
import com.cnp.sdk.generate.GiftCardCaptureResponse;
import com.cnp.sdk.generate.GiftCardCredit;
import com.cnp.sdk.generate.GiftCardCreditResponse;
import com.cnp.sdk.generate.CnpOnlineRequest;
import com.cnp.sdk.generate.CnpOnlineResponse;
import com.cnp.sdk.generate.Load;
import com.cnp.sdk.generate.LoadResponse;
import com.cnp.sdk.generate.LoadReversal;
import com.cnp.sdk.generate.LoadReversalResponse;
import com.cnp.sdk.generate.PayFacCredit;
import com.cnp.sdk.generate.PayFacCreditResponse;
import com.cnp.sdk.generate.PayFacDebit;
import com.cnp.sdk.generate.PayFacDebitResponse;
import com.cnp.sdk.generate.PhysicalCheckCredit;
import com.cnp.sdk.generate.PhysicalCheckCreditResponse;
import com.cnp.sdk.generate.PhysicalCheckDebit;
import com.cnp.sdk.generate.PhysicalCheckDebitResponse;
import com.cnp.sdk.generate.QueryTransaction;
import com.cnp.sdk.generate.RecurringTransactionResponseType;
import com.cnp.sdk.generate.RefundReversal;
import com.cnp.sdk.generate.RefundReversalResponse;
import com.cnp.sdk.generate.RegisterTokenRequestType;
import com.cnp.sdk.generate.RegisterTokenResponse;
import com.cnp.sdk.generate.ReserveCredit;
import com.cnp.sdk.generate.ReserveCreditResponse;
import com.cnp.sdk.generate.ReserveDebit;
import com.cnp.sdk.generate.ReserveDebitResponse;
import com.cnp.sdk.generate.Sale;
import com.cnp.sdk.generate.SaleResponse;
import com.cnp.sdk.generate.SubmerchantCredit;
import com.cnp.sdk.generate.SubmerchantCreditResponse;
import com.cnp.sdk.generate.SubmerchantDebit;
import com.cnp.sdk.generate.SubmerchantDebitResponse;
import com.cnp.sdk.generate.TransactionTypeWithReportGroup;
import com.cnp.sdk.generate.TransactionTypeWithReportGroupAndPartial;
import com.cnp.sdk.generate.Unload;
import com.cnp.sdk.generate.UnloadResponse;
import com.cnp.sdk.generate.UnloadReversal;
import com.cnp.sdk.generate.UnloadReversalResponse;
import com.cnp.sdk.generate.UpdateCardValidationNumOnToken;
import com.cnp.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import com.cnp.sdk.generate.UpdatePlan;
import com.cnp.sdk.generate.UpdatePlanResponse;
import com.cnp.sdk.generate.UpdateSubscription;
import com.cnp.sdk.generate.UpdateSubscriptionResponse;
import com.cnp.sdk.generate.VendorCredit;
import com.cnp.sdk.generate.VendorCreditResponse;
import com.cnp.sdk.generate.VendorDebit;
import com.cnp.sdk.generate.VendorDebitResponse;
import com.cnp.sdk.generate.VoidResponse;

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

	/**
	 * <script src="https://gist.github.com/2139120.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2174863.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139736.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139803.js"></script>
	 */
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
	/**
	 * <script src="https://gist.github.com/2139739.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139831.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139852.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139856.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139863.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2174943.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139304.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139877.js"></script>
	 */
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

	/**
	 * <script src="https://gist.github.com/2139880.js"></script>
	 */
	public VoidResponse dovoid(com.cnp.sdk.generate.Void v) throws CnpOnlineException {
		CnpOnlineRequest request = createCnpOnlineRequest();
		return dovoid(v, request);
	}

	public VoidResponse dovoid(com.cnp.sdk.generate.Void v, CnpOnlineRequest overrides) throws CnpOnlineException {
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

        request.setTransaction(CnpContext.getObjectFactory().createDepositReversal(depositReversal));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DepositReversalResponse)newresponse.getValue();
    }
    
    public TransactionTypeWithReportGroup queryTransaction(QueryTransaction queryTransaction) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return queryTransaction(queryTransaction, request);
    }

    public TransactionTypeWithReportGroup queryTransaction(QueryTransaction queryTransaction, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setTransaction(CnpContext.getObjectFactory().createQueryTransaction(queryTransaction));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return newresponse.getValue();
    }
    
    public GiftCardCaptureResponse giftCardCapture(GiftCardCapture giftCardCapture) {
    	CnpOnlineRequest request = createCnpOnlineRequest();
        return giftCardCapture(giftCardCapture, request);
    }
    
    public GiftCardCaptureResponse giftCardCapture(GiftCardCapture giftCardCapture, CnpOnlineRequest overrides) {
    	CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

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

        request.setTransaction(CnpContext.getObjectFactory().createSubmerchantDebit(submerchantDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (SubmerchantDebitResponse)newresponse.getValue();
    }
    
    public ReserveCreditResponse submerchantDebit(ReserveCredit reserveCredit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return reserveCredit(reserveCredit, request);
    }
    
    public ReserveCreditResponse reserveCredit(ReserveCredit reserveCredit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

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

        request.setTransaction(CnpContext.getObjectFactory().createVendorCredit(vendorCredit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (VendorCreditResponse)newresponse.getValue();
    }
    
    
    
    
    public VendorDebitResponse physicalCheckDebit(VendorDebit vendorDebit) {
        CnpOnlineRequest request = createCnpOnlineRequest();
        return vendorDebit(vendorDebit, request);
    }
    
    public VendorDebitResponse vendorDebit(VendorDebit vendorDebit, CnpOnlineRequest overrides) {
        CnpOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

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

        request.setTransaction(CnpContext.getObjectFactory().createPhysicalCheckDebit(physicalCheckDebit));
        CnpOnlineResponse response = sendToCnp(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (PhysicalCheckDebitResponse)newresponse.getValue();
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
		}
		else {
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
		}
		else {
			retVal.setMerchantId(request.getMerchantId());
		}
        retVal.setVersion(Versions.XML_VERSION);
		if(request.getMerchantSdk() == null) {
			retVal.setMerchantSdk(Versions.SDK_VERSION);
		}
		else {
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
			System.out.println("config-------------"+config+"\n\n\n");
			String xmlResponse = communication.requestToServer(xmlRequest, config);
			
			if(xmlResponse.contains("http://www.vantivcnp.com/schema")){
			    xmlResponse = xmlResponse.replace("http://www.vantivcnp.com/schema/online", "http://www.vantivcnp.com");
			}

			CnpOnlineResponse response = (CnpOnlineResponse)CnpContext.getJAXBContext().createUnmarshaller().unmarshal(new StringReader(xmlResponse));
			// non-zero responses indicate a problem
			if(!"0".equals(response.getResponse())) {
				if ("2".equals(response.getResponse()) || "3".equals(response.getResponse())) {
					throw new VantivInvalidCredentialException(response.getMessage());
				} else if ("4".equals(response.getResponse())) {
					throw new VantivConnectionLimitExceededException(response.getMessage());
				} else if ("5".equals(response.getResponse())) {
					throw new VantivObjectionableContentException(response.getMessage());
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
