package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;

public class performanceTestSDKMultiThreaded {
	
    private List<PerformanceTestThread> testPool = new ArrayList<>();

    static String merchantId = "07103229";

    public static void main(String[] args) throws Exception {
        performanceTestSDKMultiThreaded test = new performanceTestSDKMultiThreaded();
        test.performTest();
    }
    
    public performanceTestSDKMultiThreaded() {
        
        // create the threads and start them
        for(int x=0; x < 50; x++) {
            PerformanceTestThread t = new PerformanceTestThread(1000+x);
            testPool.add(t);
        }
        
 	}
    
    public void performTest() {
        for(PerformanceTestThread t : testPool ) {
            t.start();
        }
        
        // wait for them to finish
        boolean allDone = false;
        while(!allDone) {
            int doneCount = 0;
            for(PerformanceTestThread t : testPool ) {
                if ( t.isAlive() == false ) {
                    doneCount++;
                }
            }
            if ( doneCount == testPool.size() ) {
                allDone = true;
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("All test threads have completed");
    }
    
    class PerformanceTestThread extends Thread {
        
        CnpOnline cnp;
        long threadId;
        long requestCount = 0;
        long successCount = 0;
        long failedCount = 0;
        
        public PerformanceTestThread(long idNumber) {
            threadId = idNumber;
            Properties config = new Properties();
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream((new Configuration()).location());
                config.load(fileInputStream);
                config.setProperty("proxyHost", "inetproxy.infoftps.com");
                config.setProperty("proxyPort", "8080");
                config.setProperty("multiSite", "true");
                config.setProperty("printxml", "false");
                config.setProperty("printMultiSiteDebug", "false");
                cnp = new CnpOnline(config);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            Random rand = new Random();
            long startTime = System.currentTimeMillis();
            long totalTransactionTime = 0;
            for( int n=0; n < 1000; n++ ) {
                totalTransactionTime += doCycle();
                try {
                    long sleepTime = rand.nextInt(50);
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + threadId + " completed. Total Requests:" + requestCount + "  Success:" + successCount + "  Failed:" + failedCount + "  Elapsed Time:" + (duration/1000) + " secs    Average Txn Time:" + (totalTransactionTime/requestCount) + " ms");
        }
        
        private long doCycle() {
            requestCount++;
            Authorization authorization = new Authorization();
            authorization.setReportGroup("123456");
            authorization.setOrderId("" + threadId + "-" + System.currentTimeMillis());
            authorization.setAmount(106L);
            authorization.setOrderSource(OrderSourceType.ECOMMERCE);
            authorization.setId("id" + threadId);
            CardType card = new CardType();
            card.setType(MethodOfPaymentTypeEnum.VI);
            card.setNumber("4100000000000000");
            card.setExpDate("1210");
            authorization.setCard(card);

            long startTime = System.currentTimeMillis();
            AuthorizationResponse response = cnp.authorize(authorization);
            long responseTime = System.currentTimeMillis() - startTime;
            assertEquals("123456",response.getReportGroup());
            if ( response.getResponse().equals("000") ) {
                successCount++;
            }
            else {
                failedCount++;
            }
            return responseTime;
        }
    }

}

