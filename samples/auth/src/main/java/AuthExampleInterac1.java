package io.github.vantiv.sdk.samples;

import io.github.vantiv.sdk.CnpOnline;
import io.github.vantiv.sdk.generate.*;

//Authorization
public class AuthExampleInterac1 {

    public static void main(String[] args) {
        Authorization auth = new Authorization();
        auth.setOrderId("1");
        auth.setAmount(10010L);
        auth.setOrderSource(OrderSourceType.ECOMMERCE);
        Contact billToAddress = new Contact();
        billToAddress.setName("John Smith");
        billToAddress.setAddressLine1("1 Main St.");
        billToAddress.setCity("Burlington");
        billToAddress.setState("MA");
        billToAddress.setCountry(CountryTypeEnum.US);
        billToAddress.setZip("01803-3747");
        auth.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("375001010000003");
        card.setExpDate("0112");
        card.setCardValidationNum("349");
        card.setType(MethodOfPaymentTypeEnum.IC);
        auth.setCard(card);
        auth.setId("id");
        FraudCheckType value =new FraudCheckType();
        //authenticationValue length greater than 56 and less than 512
        value.setAuthenticationValue(" 4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA=");
        //authenticationValue length greater than 512
        //value.setAuthenticationValue("4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA4YGKgiCTQuBU7sMKRCFr5LJmFDSqTEnSpb8/lXAHqCD6/IGw3YMBAIQBAIUBAIZTWgpQdgIQJQNwVoFvXyQDJgkwXyoCASSfAgYAAAABEwBfIAIgL58ZBgAAAAAAAL9JIoEgBwABBJQ1AgAAAAAgJDUCAAAAAACHCAAAAAAAAAAAkAA=");
        auth.setCardholderAuthentication(value);
        AuthorizationResponse response = new CnpOnline().authorize(auth);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());

        // In your sample, you can ignore this
        if(!response.getMessage().equals("Approved"))
            throw new RuntimeException(" The AuthSample does not give the right response");

    }
}
