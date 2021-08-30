package io.github.vantiv.sdk;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.*;

/**
 * Implement this interface in order to process transactions on CnpBatchResponse objects with the .processNextTransaction method.
 * Consider auto generating this file in the future
 * @author ahammond
 */
@SuppressWarnings("unused") // These methods are called via reflection
public interface CnpResponseProcessor {

    void processAuthorizationResponse(AuthorizationResponse authorizationResponse);

    void processCaptureResponse(CaptureResponse captureResponse);

    void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse);

    void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse);

    void processSaleResponse(SaleResponse saleResponse);

    void processCreditResponse(CreditResponse creditResponse);

    void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse);

    void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse);

    void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse);

    void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse);

    void processAuthReversalResponse(AuthReversalResponse authReversalResponse);

    void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse);

    void processAccountUpdateResponse(AccountUpdateResponse accountUpdateResponse);

    void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse);

    void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse);

    void processUpdateCardValidationNumOnTokenResponse(
            UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse);

    void processCreatePlanResponse(CreatePlanResponse createPlanResponse);

    void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse);

    void processActivateResponse(ActivateResponse activateResponse);

    void processDeactivateResponse(DeactivateResponse deactivateResponse);

    void processLoadResponse(LoadResponse loadResponse);

    void processUnloadResponse(UnloadResponse unloadResponse);

    void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse);

    void processEcheckPreNoteSaleResponse(EcheckPreNoteSaleResponse echeckPreNoteSaleResponse);
    
    void processEcheckPreNoteCreditResponse(EcheckPreNoteCreditResponse echeckPreNoteCreditResponse);
    
    void processSubmerchantCreditResponse(SubmerchantCreditResponse submerchantCreditResponse);
    
    void processPayFacCreditResponse(PayFacCreditResponse payFacCreditResponse);
    
    void processVendorCreditResponse(VendorCreditResponse vendorCreditResponse);
    
    void processReserveCreditResponse(ReserveCreditResponse reserveCreditResponse);
    
    void processPhysicalCheckCreditResponse(PhysicalCheckCreditResponse checkCreditResponse);
    
    void processSubmerchantDebitResponse(SubmerchantDebitResponse submerchantDebitResponse);
    
    void processPayFacDebitResponse(PayFacDebitResponse payFacDebitResponse);
    
    void processVendorDebitResponse(VendorDebitResponse vendorDebitResponse);
    
    void processReserveDebitResponse(ReserveDebitResponse reserveDebitResponse);
    
    void processPhysicalCheckDebitResponse(PhysicalCheckDebitResponse checkDebitResponse);
    
    void processFundingInstructionVoidResponse(FundingInstructionVoidResponse fundingInstructionVoidResponse);
    
    void processGiftCardAuthReversalResponse(GiftCardAuthReversalResponse giftCardAuthReversalResponse);
    
    void processGiftCardCaptureResponse(GiftCardCaptureResponse giftCardCaptureResponse);
    
    void processGiftCardCreditResponse(GiftCardCreditResponse giftCardCreditResponse);

    void processFastAccessFundingResponse(FastAccessFundingResponse fastAccessFundingResponse);

    void processTranslateToLowValueTokenResponse(TranslateToLowValueTokenResponse translateToLowValueTokenResponse);

    void processCustomerCreditResponse(CustomerCreditResponse customerCreditResponse);

    void processCustomerDebitResponse(CustomerDebitResponse customerDebitResponse);

    void processPayoutOrgCreditResponse(PayoutOrgCreditResponse payoutOrgCreditResponse);

    void processPayoutOrgDebitResponse(PayoutOrgDebitResponse payoutOrgDebitResponse);

    void processDepositTransactionReversalResponse(DepositTransactionReversalResponse depositTransactionReversalResponse);

    void processRefundTransactionReversalResponse(RefundTransactionReversalResponse refundTransactionReversalResponse);
}