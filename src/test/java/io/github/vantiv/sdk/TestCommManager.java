package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.Test;

public class TestCommManager {

    public final static String site1Url = "https://multisite1.com";
    public final static String site2Url = "https://multisite2.com";
    public final static String legacyUrl = "https://legacy.com";
    
	@Test
	public void testInstanceLegacy() throws Exception {
	    Properties config = new Properties();
	    config.put("url", legacyUrl);
	    config.put("multiSite", "false");
	    config.put("printMultiSiteDebug", "true");
        CommManager.reset();
	    CommManager cmg = CommManager.instance(config);
	    assertNotNull(cmg);
	    assertFalse(cmg.doMultiSite);
	    assertEquals(legacyUrl,cmg.legacyUrl);

	    Properties config2 = new Properties();
	    config2.put("url", "https://nowhere.com");
	    config2.put("multiSite", "false");
	    config2.put("printMultiSiteDebug", "true");
	    CommManager cmg2 = CommManager.instance(config2);
	    assertEquals(legacyUrl, cmg2.legacyUrl);  // should be same manager as previous
	}

    @Test
    public void testInstanceMultiSite() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "4");
        config.put("maxHoursWithoutSwitch", "48");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(cmg.multiSiteThreshold, 4);
        assertEquals(cmg.multiSiteUrls.size(), 2);
    }
    
    @Test
    public void testInstanceMultiSiteNoUrls() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertFalse(cmg.doMultiSite);
    }

    @Test
    public void testInstanceMultiSiteDefaultProps() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "102");
        config.put("maxHoursWithoutSwitch", "500");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(5,cmg.multiSiteThreshold);
        assertEquals(48,cmg.maxHoursWithoutSwitch);
    }

    @Test
    public void testInstanceMultiSiteOutOfRange() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
    }

    @Test
    public void testFindUrl_Legacy() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "false");
        config.put("printMultiSiteDebug", "false");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertFalse(cmg.doMultiSite);
        RequestTarget rt = cmg.findUrl();
        assertEquals(legacyUrl,rt.getUrl());
    }

    @Test
    public void testFindUrl_MultiSite1() throws Exception {
        // 
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "4");
        config.put("maxHoursWithoutSwitch", "48");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        RequestTarget rt = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt.getUrl());
        assertTrue(rt.getUrl().equals(site1Url) || rt.getUrl().equals(site2Url));
    }

    @Test
    public void testFindUrl_MultiSite2() throws Exception {
        // test that url is switched when errors reach threshold
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "false");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "3");
        config.put("maxHoursWithoutSwitch", "48");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(cmg.multiSiteThreshold, 3);

        RequestTarget rt1 = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt1.getUrl());
        cmg.reportResult(rt1, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        RequestTarget rt2 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt2.getUrl());
        cmg.reportResult(rt2, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        RequestTarget rt3 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt3.getUrl());
        cmg.reportResult(rt3, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        assertEquals(cmg.errorCount, 3);

        RequestTarget rt4 = cmg.findUrl();
        assertFalse(rt4.getUrl().equals(rt1.getUrl()));
    }

    @Test
    public void testFindUrl_MultiSite3() throws Exception {
        // test that url is switched when errors reach threshold and switched again after errors
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "false");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "3");
        config.put("maxHoursWithoutSwitch", "48");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(cmg.multiSiteThreshold, 3);

        RequestTarget rt1 = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt1.getUrl());
        cmg.reportResult(rt1, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        RequestTarget rt2 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt2.getUrl());
        cmg.reportResult(rt2, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        RequestTarget rt3 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt3.getUrl());
        cmg.reportResult(rt3, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        assertEquals(cmg.errorCount, 3);

        RequestTarget rt4 = cmg.findUrl();
        assertFalse(rt4.getUrl().equals(rt1.getUrl()));

        RequestTarget rt10 = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt10.getUrl());
        cmg.reportResult(rt10, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, 401);
        RequestTarget rt11 = cmg.findUrl();
        assertEquals(rt10.getUrl(), rt11.getUrl());
        cmg.reportResult(rt11, CommManager.REQUEST_RESULT_CONNECTION_FAILED, 0);
        RequestTarget rt12 = cmg.findUrl();
        assertEquals(rt11.getUrl(), rt12.getUrl());
        cmg.reportResult(rt12, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        assertEquals(cmg.errorCount, 3);

        RequestTarget rt13 = cmg.findUrl();
        assertFalse(rt13.getUrl().equals(rt11.getUrl()));
        assertTrue(rt13.getUrl().equals(rt1.getUrl()));
    }

    @Test
    public void testFindUrl_MultiSite4() throws Exception {
        // test that url is not switched when errors reported but then succes resets count
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "false");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "3");
        config.put("maxHoursWithoutSwitch", "0");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(cmg.multiSiteThreshold, 3);

        RequestTarget rt1 = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt1.getUrl());
        cmg.reportResult(rt1, CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT, 0);
        RequestTarget rt2 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt2.getUrl());
        cmg.reportResult(rt2, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, 200);
        assertEquals(0, cmg.errorCount);
        
        RequestTarget rt3 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt3.getUrl());
        cmg.reportResult(rt3, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, 301);
        assertEquals(0, cmg.errorCount);
    }


    @Test
    public void testFindUrl_MultiSiteMaxHours() throws Exception {
        // test that url is switched when number of hours since last switch exceeds threshold
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "true");
        config.put("printMultiSiteDebug", "true");
        config.put("multiSiteUrl1", site1Url);
        config.put("multiSiteUrl2", site2Url);
        config.put("multiSiteErrorThreshold", "3");
        config.put("maxHoursWithoutSwitch", "4");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertTrue(cmg.doMultiSite);
        assertEquals(cmg.multiSiteThreshold, 3);

        RequestTarget rt1 = cmg.findUrl();
        assertEquals(cmg.multiSiteUrls.get(cmg.currentMultiSiteUrlIndex), rt1.getUrl());
        cmg.reportResult(rt1, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, 200);
        RequestTarget rt2 = cmg.findUrl();
        assertEquals(rt1.getUrl(), rt2.getUrl());
        cmg.reportResult(rt2, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, 200);
        
        // set last switch time to 6 hours earlier
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(cmg.lastSiteSwitchTime);
        gc.add(Calendar.HOUR_OF_DAY, -6);
        cmg.lastSiteSwitchTime = gc.getTimeInMillis();
        
        RequestTarget rt3 = cmg.findUrl();
        assertFalse(rt3.getUrl().equals(rt1.getUrl()));
    }

    @Test
    public void testReportResult_NotMultiSite() throws Exception {
        Properties config = new Properties();
        config.put("url", legacyUrl);
        config.put("multiSite", "false");
        config.put("printMultiSiteDebug", "true");
        CommManager.reset();
        CommManager cmg = CommManager.instance(config);
        assertNotNull(cmg);
        assertFalse(cmg.doMultiSite);
        assertEquals(legacyUrl,cmg.legacyUrl);
        cmg.reportResult(new RequestTarget("",1),  1,  0);
    }

}
