package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.RealtimeIncrementalAuthorization;

public class TestRealtimeIncrementalAuth {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    //v12.42 changes to test Realtime incremental auth
    //v12.43 CumulativeAmount,OriginalTransactionAmount
    @Test
    public void authWithRealTimeIncrementalAuth() throws Exception {
        RealtimeIncrementalAuthorization realAuth = new RealtimeIncrementalAuthorization();
        realAuth.setReportGroup("русский中文");
        realAuth.setId("id");
        realAuth.setCnpTxnId(98767l);
        realAuth.setOrderId("12344");
        realAuth.setAmount(106L);
        realAuth.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000000");
        card.setExpDate("1210");
        realAuth.setCard(card);
        realAuth.setOriginalNetworkTransactionId("123");
        realAuth.setOriginalRetrievalReferenceNumber("456");
        realAuth.setCumulativeAmount(5000L);
        realAuth.setOriginalTransactionAmount(4800L);
        AuthorizationResponse response = cnp.realTimeIncrementalAuth(realAuth);
        assertEquals("русский中文", response.getReportGroup());
        assertEquals("sandbox", response.getLocation());
    }
}