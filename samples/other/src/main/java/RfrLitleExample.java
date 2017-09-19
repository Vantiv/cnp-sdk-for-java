package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.AccountUpdateFileRequestData;
import com.cnp.sdk.generate.RFRRequest;
import java.util.Calendar;
public class RfrCnpExample {
    public static void main(String[] args) {
        String merchantId = "0180";
        String requestFileName = "cnpSdk-testRFRFile-fileConfigSFTP.xml";
        RFRRequest rfrRequest = new RFRRequest();
        AccountUpdateFileRequestData data = new AccountUpdateFileRequestData();
        data.setMerchantId(merchantId);
        data.setPostDay(Calendar.getInstance());
        rfrRequest.setAccountUpdateFileRequestData(data);
         
        CnpRFRFileRequest request = new CnpRFRFileRequest(requestFileName, rfrRequest); 
        try{
            CnpRFRFileResponse response = request.sendToCnpSFTP();
            String message=response.getCnpRFRResponse().getRFRResponseMessage();
        }
        catch(Exception e){       
        }
    } 
}
