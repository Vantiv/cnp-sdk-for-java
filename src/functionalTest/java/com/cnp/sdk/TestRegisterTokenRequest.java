package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Properties;

import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import com.cnp.sdk.generate.*;



public class TestRegisterTokenRequest {

    private static CnpOnline cnp;
    private final long TIME_STAMP = System.currentTimeMillis();


    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testRegisterTokenRequestOnline() throws Exception {
        RegisterTokenRequestType registerTokenRequest = new RegisterTokenRequestType();
        registerTokenRequest.setId("id");
        registerTokenRequest.setAccountNumber("4100280140123000");
        assertEquals("801", cnp.registerToken(registerTokenRequest).getResponse());
    }

    @Test
    public void testRegisterTokenRequestBatch() throws Exception {
            String requestFileName = "cnpSdk-testBatchFile-MECHA-" + TIME_STAMP + ".xml";
            CnpBatchFileRequest request = new CnpBatchFileRequest(
                    requestFileName);

            Properties configFromFile = request.getConfig();

            // pre-assert the config file has required param values
            assertEquals("payments.vantivprelive.com",
                    configFromFile.getProperty("batchHost"));
            // assertEquals("15000", configFromFile.getProperty("batchPort"));

            CnpBatchRequest batch = request.createBatch(configFromFile.getProperty("merchantId"));

            // card
            CardType card = new CardType();
            card.setNumber("4100280140123000");
            card.setExpDate("1210");
            card.setType(MethodOfPaymentTypeEnum.VI);

            // billto address
            Contact contact = new Contact();
            contact.setName("Bob");
            contact.setCity("Lowell");
            contact.setState("MA");
            contact.setEmail("Bob@cnp.com");

//            Authorization auth = new Authorization();
//            auth.setReportGroup("Planets");
//            auth.setOrderId("12344");
//            auth.setAmount(106L);
//            auth.setOrderSource(OrderSourceType.ECOMMERCE);
//            auth.setCard(card);
//            auth.setId("id");
//            LodgingInfo lodgingInfo = new LodgingInfo();
//            lodgingInfo.setRoomRate(106L);
//            lodgingInfo.setRoomTax(0L);
//            LodgingCharge lodgingCharge = new LodgingCharge();
//            lodgingCharge.setName(LodgingExtraChargeEnum.RESTAURANT);
//            lodgingInfo.getLodgingCharges().add(lodgingCharge);
//            auth.setLodgingInfo(lodgingInfo);
//            batch.addTransaction(auth);
//
//            Sale sale = new Sale();
//            sale.setReportGroup("Planets");
//            sale.setOrderId("12344");
//            sale.setAmount(6000L);
//            sale.setOrderSource(OrderSourceType.ECOMMERCE);
//            sale.setCard(card);
//            sale.setId("id");
//            batch.addTransaction(sale);
            

            RegisterTokenRequestType registerTokenRequestType = new RegisterTokenRequestType();
            registerTokenRequestType.setReportGroup("Planets");
            registerTokenRequestType.setOrderId("132344");
            registerTokenRequestType.setAccountNumber("4100280140123000");
            registerTokenRequestType.setId("id");
            batch.addTransaction(registerTokenRequestType);
            
            CnpBatchFileResponse fileResponse = request.sendToCnpSFTP();
            CnpBatchResponse batchResponse = fileResponse
                    .getNextCnpBatchResponse();
        
            int txns = 0;

            while (batchResponse
                    .processNextTransaction(new CnpResponseProcessor() {
                        public void processAuthorizationResponse(
                                AuthorizationResponse authorizationResponse) {
                        }

                        public void processCaptureResponse(
                                CaptureResponse captureResponse) {
                        }

                        public void processForceCaptureResponse(
                                ForceCaptureResponse forceCaptureResponse) {
                        }

                        public void processCaptureGivenAuthResponse(
                                CaptureGivenAuthResponse captureGivenAuthResponse) {
                        }

                        public void processSaleResponse(SaleResponse saleResponse) {
                            
                        }

                        public void processCreditResponse(
                                CreditResponse creditResponse) {
                        }

                        public void processEcheckSalesResponse(
                                EcheckSalesResponse echeckSalesResponse) {
                        }

                        public void processEcheckCreditResponse(
                                EcheckCreditResponse echeckCreditResponse) {
                        }

                        public void processEcheckVerificationResponse(
                                EcheckVerificationResponse echeckVerificationResponse) {
                        }

                        public void processEcheckRedepositResponse(
                                EcheckRedepositResponse echeckRedepositResponse) {
                        }

                        public void processAuthReversalResponse(
                                AuthReversalResponse authReversalResponse) {
                        }

                        public void processRegisterTokenResponse(
                                RegisterTokenResponse registerTokenResponse) {
                            assertEquals("801", registerTokenResponse.getResponse());

                        }

                        public void processAccountUpdate(
                                AccountUpdateResponse accountUpdateResponse) {
                        }

                        public void processUpdateSubscriptionResponse(
                                UpdateSubscriptionResponse updateSubscriptionResponse) {
                        }

                        public void processCancelSubscriptionResponse(
                                CancelSubscriptionResponse cancelSubscriptionResponse) {
                        }

                        public void processUpdateCardValidationNumOnTokenResponse(
                                UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse) {
                        }

                        public void processCreatePlanResponse(
                                CreatePlanResponse createPlanResponse) {
                        }

                        public void processUpdatePlanResponse(
                                UpdatePlanResponse updatePlanResponse) {
                        }

                        public void processActivateResponse(
                                ActivateResponse activateResponse) {
                        }

                        public void processDeactivateResponse(
                                DeactivateResponse deactivateResponse) {
                        }

                        public void processLoadResponse(LoadResponse loadResponse) {
                        }

                        public void processUnloadResponse(
                                UnloadResponse unloadResponse) {
                        }

                        public void processBalanceInquiryResponse(
                                BalanceInquiryResponse balanceInquiryResponse) {
                        }

                        public void processEcheckPreNoteSaleResponse(
                                EcheckPreNoteSaleResponse echeckPreNoteSaleResponse) {
                        }

                        public void processEcheckPreNoteCreditResponse(
                                EcheckPreNoteCreditResponse echeckPreNoteCreditResponse) {
                        }

                        public void processSubmerchantCreditResponse(
                                SubmerchantCreditResponse submerchantCreditResponse) {
                        }

                        public void processPayFacCreditResponse(
                                PayFacCreditResponse payFacCreditResponse) {
                        }

                        public void processVendorCreditResponse(
                                VendorCreditResponse vendorCreditResponse) {
                        }

                        public void processReserveCreditResponse(
                                ReserveCreditResponse reserveCreditResponse) {
                        }

                        public void processPhysicalCheckCreditResponse(
                                PhysicalCheckCreditResponse checkCreditResponse) {
                        }

                        public void processSubmerchantDebitResponse(
                                SubmerchantDebitResponse submerchantDebitResponse) {
                        }

                        public void processPayFacDebitResponse(
                                PayFacDebitResponse payFacDebitResponse) {
                        }

                        public void processVendorDebitResponse(
                                VendorDebitResponse vendorDebitResponse) {
                        }

                        public void processReserveDebitResponse(
                                ReserveDebitResponse reserveDebitResponse) {
                        }

                        public void processPhysicalCheckDebitResponse(
                                PhysicalCheckDebitResponse checkDebitResponse) {
                        }

                        public void processFundingInstructionVoidResponse(
                                FundingInstructionVoidResponse fundingInstructionVoidResponse) {
                        }

                        public void processGiftCardAuthReversalResponse(GiftCardAuthReversalResponse giftCardAuthReversalResponse) {
                        }

                        public void processGiftCardCaptureResponse(GiftCardCaptureResponse giftCardCaptureResponse) {
                        }

                        public void processGiftCardCreditResponse(GiftCardCreditResponse giftCardCreditResponse) {
                        }

                        public void processFastAccessFundingResponse(FastAccessFundingResponse fastAccessFundingResponse) {
                        }

                        public void processTranslateToLowValueTokenResponse(TranslateToLowValueTokenResponse translateToLowValueTokenResponse){
                        }
                    })) {
                txns++;
            }

            assertEquals(1, txns);
        }
    
}