Vantiv eCommerce Java SDK

#### Warning:
#### All major version changes require recertification to the new version. Once certified for the use of a new version, Vantiv modifies your Merchant Profile, allowing you to submit transaction to the Production Environment using the new version. Updating your code without recertification and modification of your Merchant Profile will result in transaction declines. Please consult you Implementation Analyst for additional information about this process.
About Vantiv eCommerce
------------
[Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) powers the payment processing engines for leading companies that sell directly to consumers through  internet retail, direct response marketing (TV, radio and telephone), and online services. Vantiv eCommerce is the leading authority in card-not-present (CNP) commerce, transaction processing and merchant services.


About this SDK
--------------
The Vantiv eCommerce Java SDK is a Java implementation of the [Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) XML API. This SDK was created to make it as easy as possible to process your payments with Vantiv eCommerce. This SDK utilizes the HTTPS protocol to securely connect to Vantiv eCommerce. Using the SDK requires coordination with the Vantiv eCommerce team in order to be provided with credentials for accessing our systems.

Each Java SDK release supports all of the functionality present in the associated Vantiv eCommerce XML version (e.g., SDK v12.19.1 supports Vantiv eCommerce XML v12.0). Please see the online copy of our XSD for Vantiv eCommerce XML to get more details on what the Vantiv eCommerce payments engine supports.

This SDK was implemented to support the Java programming language and was created by Vantiv eCommerce. Its intended use is for online and batch transaction processing utilizing your account on the Vantiv eCommerce payments engine.

See LICENSE file for details on using this software.

Please contact [Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) to receive valid merchant credentials in order to run tests successfully or if you require assistance in any way.  We are reachable at sdksupport@fisglobal.com

Setup
-----

1. Add MavenCentral repository to your Maven or Gradle build:
	1. For Maven, please read instructions at: https://mvnrepository.com/artifact/io.github.vantiv/cnp-sdk-for-java
	2. For Gradle, add `mavenCentral()` to your `repositories { ... }`
2. Add the dependency
    1. For Maven:
        ```xml
            <dependency>

        

                <groupId>io.github.vantiv</groupId>
                <artifactId>cnp-sdk-for-java</artifactId>
                <version>12.19.1</version>

            </dependency>
        ```

    2. For Gradle:
        ```groovy

            compile(group: 'io.github.vantiv', name: 'cnp-sdk-for-java', version: '12.19.1')

        ```
        
3. Create your configuration file with one of the following
    * Run `java -jar /path/to/cnp-sdk-for-java.jar` and answer the questions, or
    * Add a file `.cnp_SDK_config.properties` to your home directory with the correct properties in it
4. Use it:

```java

import io.github.vantiv.sdk.*;

public class SampleCnpTxn {


    public static void main(String[] args) {

        // Visa $10 Sale
        Sale sale = new Sale();
        sale.setReportGroup("Planets");
        sale.setOrderId("12344");
        sale.setAmount(1000L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        sale.setCard(card);

        // Peform the transaction on the Vantiv eCommerce Platform
        SaleResponse response = new CnpOnline().sale(sale);

        // display result
        System.out.println("Message: " + response.getMessage());
        System.out.println("Vantiv eCommerce Transaction ID: " + response.getCnpTxnId());
    }
}
```

Note (Setup for all older versions than 12.19.1)
-----

1. Add JCenter repository to your Maven or Gradle build:
    1. For Maven, please read instructions at: https://bintray.com/bintray/jcenter
    2. For Gradle, add `jcenter()` to your `repositories { ... }`
2. Add the dependency
    1. For Maven:
        ```xml
            <dependency>

        

                <groupId>com.cnp</groupId>
                <artifactId>cnp-sdk-for-java</artifactId>
                <version>12.0.3</version>

            </dependency>
        ```

    2. For Gradle:
        ```groovy

            compile(group: 'com.cnp', name: 'cnp-sdk-for-java', version: '12.0.3')

        ```

3. Create your configuration file with one of the following
    * Run `java -jar /path/to/cnp-sdk-for-java.jar` and answer the questions, or
    * Add a file `.cnp_SDK_config.properties` to your home directory with the correct properties in it
4. Use it:

```java

import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
public class SampleCnpTxn {


	public static void main(String[] args) {

		// Visa $10 Sale
		Sale sale = new Sale();
		sale.setReportGroup("Planets");
		sale.setOrderId("12344");
		sale.setAmount(1000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		
		// Peform the transaction on the Vantiv eCommerce Platform
		SaleResponse response = new CnpOnline().sale(sale);

		// display result
		System.out.println("Message: " + response.getMessage());
		System.out.println("Vantiv eCommerce Transaction ID: " + response.getCnpTxnId());
	}
}
```
Setting Up OLTP Encryption Functionality
-----------------------------------------
To enable OLTP encryption functionality, follow these steps:

1. Add the following properties to your configuration file (.cnp_SDK_config.properties):

    1. oltpEncryptionPayload (true/false)
    2. oltpEncryptionKeySequence (this is received in the response of encryptionKeyRequest)
    3. oltpEncryptionKeyPath (path where key is stored)

2. Obtain Encryption Key:
    
    Send an encryptionKeyRequest to obtain the encryption key and key sequence.

3. Key Setup:

    Save the encryptionkey in one file.
    Ensure the key setup is done with the values obtained key sequence and path of the encryptionkey file.

4. Processing OLTP Transactions:

     Run any OLTP transaction. The transaction will be processed based on the value of oltpEncryptionPayload.
     If oltpEncryptionPayload is set to true, the transaction payload will be converted to an encrypted payload, and a normal cnpOnlineRequest will be formed.
     The request will be processed to give a plain text/normal response.
     
 By following these steps, you can enable and configure OLTP encryption functionality in the cnp-sdk-for-java project.

5. Below are samples for transactions.

Example for encryptionKeyRequest:

```java
    import com.cnp.sdk.*;
    import com.cnp.sdk.generate.*;
    public class SampleEncryptionKeyRequest {

	public static void main(String[] args) {

        EncryptionKeyRequest request = new EncryptionKeyRequest();
        request.setEncryptionKeyRequest(EncryptionKeyRequestEnum.CURRENT);
		
        // Peform the transaction on the Vantiv eCommerce Platform
        EncryptionKeyResponse response = cnp.encryptionKeyRequest(request.getEncryptionKeyRequest());
        
        // display result
        System.out.println("Key Sequence: " + response.getEncryptionKeySequence());
        System.out.println("Encryption Key : " + response.getEncryptionKey());
	}
    }
```

6.  Below is the sample to set element 'typeOfDigitalCurrency' and 'typeOfDigitalCurrencyEnum'.

    authorization.setTypeOfDigitalCurrency("1");
		

There is one example shown in Note section can be used to validate transaction processing  after doing all setup . 

More examples can be found here [Java Gists](https://gist.github.com/VantivSDK)




