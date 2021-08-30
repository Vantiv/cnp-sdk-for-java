package io.github.vantiv.sdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.junit.Test;

public class TestCommManagerMultiThreaded {
	
    private List<PerformanceTestThread> testPool = new ArrayList<>();

    private int threadCount = 100;
    private int cycleCount = 1000;
    
//    public static void main(String[] args) throws Exception {
//        TestCommManagerMultiThreaded test = new TestCommManagerMultiThreaded();
//        test.performTest();
//    }
    
    @Test
    public void testMultiThreaded() {
        
        Properties config = new Properties();
        FileInputStream fileInputStream = null;
        try {
            // init the CommManager
            fileInputStream = new FileInputStream((new Configuration()).location());
            config.load(fileInputStream);
            config.setProperty("proxyHost", "inetproxy.infoftps.com");
            config.setProperty("proxyPort", "8080");
            config.setProperty("multiSite", "true");
            config.setProperty("printxml", "false");
            config.setProperty("printMultiSiteDebug", "false");
            CommManager.instance(config);

            // create the threads and start them
            for(int x=0; x < threadCount; x++) {
                PerformanceTestThread t = new PerformanceTestThread(1000+x, config);
                testPool.add(t);
            }
                
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if ( fileInputStream != null ) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        performTest();
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
        
        long threadId;
        long requestCount = 0;
        long successCount = 0;
        long failedCount = 0;
        Properties config = null;
        
        public PerformanceTestThread(long idNumber, Properties props) {
            threadId = idNumber;
            config = props;
        }
        
        @Override
        public void run() {
            Random rand = new Random();
            long startTime = System.currentTimeMillis();
            long totalTransactionTime = 0;
            for( int n=0; n < cycleCount; n++ ) {
                requestCount++;
                RequestTarget target = CommManager.instance(config).findUrl();
                try {
                    long sleepTime = 100 + rand.nextInt(500);
                    totalTransactionTime += sleepTime;
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                CommManager.instance(config).reportResult(target, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED,  200);
            }
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + threadId + " completed. Total Requests:" + requestCount + "  Elapsed Time:" + (duration/1000) + " secs    Average Txn Time:" + (totalTransactionTime/requestCount) + " ms");
        }
        
     }

}

