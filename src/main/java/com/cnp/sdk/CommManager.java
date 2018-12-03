package com.cnp.sdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class CommManager {

    public final static int REQUEST_RESULT_RESPONSE_RECEIVED = 1;
    public final static int REQUEST_RESULT_CONNECTION_FAILED = 2;
    public final static int REQUEST_RESULT_RESPONSE_TIMEOUT = 3;
    
    private static CommManager manager = null;
    
    protected Properties configuration;
    protected boolean doMultiSite = false;
    protected String legacyUrl;
    protected List<String> multiSiteUrls = new ArrayList<>();
    protected int errorCount = 0;
    protected int currentMultiSiteUrlIndex = 0;
    protected int multiSiteThreshold = 5;
    protected long lastSiteSwitchTime = 0;
    protected int maxHoursWithoutSwitch = 48;
    protected SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected boolean printDebug = false;
    
    public static CommManager instance(Properties config) {
        if ( manager == null ) {
            manager = new CommManager(config);
        }
        return manager;
    }
    
    public static void reset() {
        manager = null;
    }
    
	private CommManager(Properties config) {
	    configuration = config;
	    legacyUrl = configuration.getProperty("url");
	    doMultiSite = Boolean.valueOf(configuration.getProperty("multiSite", "false"));
	    printDebug = Boolean.valueOf(configuration.getProperty("printMultiSiteDebug", "false"));
	    
	    if ( doMultiSite ) {
	        for( int x=1; x < 3; x++ ) {
	            String siteUrl = configuration.getProperty("multiSiteUrl" + x);
	            if ( siteUrl == null ) {
	                break;
	            }
	            multiSiteUrls.add(siteUrl);
	        }
	        if ( multiSiteUrls.size() == 0 ) {
	            doMultiSite = false;
	        }
	        else {
                Collections.shuffle(multiSiteUrls);  // shuffle to randomize which one is selected first
	            currentMultiSiteUrlIndex = 0;
	            errorCount = 0;
	            String threshold = configuration.getProperty("multiSiteErrorThreshold");
	            if ( threshold != null ) {
	                int t = Integer.parseInt(threshold);
	                if ( t > 0 && t < 100 ) {
	                    multiSiteThreshold = t;
	                }
	            }
	            String maxHours = configuration.getProperty("maxHoursWithoutSwitch");
	            if ( maxHours != null ) {
	                int t = Integer.parseInt(maxHours);
	                if ( t >= 0 && t < 300 ) {
	                    maxHoursWithoutSwitch = t;
	                }	            
	            }
	            lastSiteSwitchTime = System.currentTimeMillis();
	        }
	    }
	}

	public synchronized RequestTarget findUrl() {
	    String url = legacyUrl;
	    if ( doMultiSite ) {
	        boolean switchSite = false;
	        String switchReason = "";
	        String currentUrl = multiSiteUrls.get(currentMultiSiteUrlIndex);
	        if ( errorCount < multiSiteThreshold ) {
	            if (maxHoursWithoutSwitch > 0) {
	                long diffSinceSwitch = (System.currentTimeMillis() - lastSiteSwitchTime) / 3600000;
	                if ( diffSinceSwitch > maxHoursWithoutSwitch ) {
	                    switchReason = " more than " + maxHoursWithoutSwitch + " hours since last switch";
	                    switchSite = true;
	                }
	            }
	        }
	        else {
                switchReason = " consecutive error count has reached threshold of " + multiSiteThreshold;
	            switchSite = true;
	        }

	        if ( switchSite ) {
	            currentMultiSiteUrlIndex++;
	            if ( currentMultiSiteUrlIndex >= multiSiteUrls.size() ) {
	                currentMultiSiteUrlIndex = 0;
	            }
	            url = multiSiteUrls.get(currentMultiSiteUrlIndex);
	            errorCount = 0;
	            if ( printDebug ) {
	                Date switchDate = new Date();
	                lastSiteSwitchTime = switchDate.getTime();
	                System.out.println(dateFormatter.format(switchDate) + "  Switched to " + url + " because " + switchReason);
	            }
	        }
	        else {
	            url = currentUrl;
	        }
	    }
	    if ( printDebug ) {
	        System.out.println("Selected URL: " + url);
	    }
	    return new RequestTarget(url,currentMultiSiteUrlIndex);
	}
	
    public synchronized void reportResult(RequestTarget target, int result, int statusCode) {
        if ( target.getRequestTime() < lastSiteSwitchTime || !doMultiSite ) {
            return;
        }
        switch(result) {
            case REQUEST_RESULT_RESPONSE_RECEIVED:
                if ( statusCode == 200 ) {
                    errorCount = 0;
                }
                else if ( statusCode >= 400 ) {
                    errorCount++;
                }
                break;
            case REQUEST_RESULT_CONNECTION_FAILED:
                errorCount++;
                break;
            case REQUEST_RESULT_RESPONSE_TIMEOUT:
                errorCount++;
                break;
        }
    }
}
