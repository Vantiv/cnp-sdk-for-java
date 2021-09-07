package io.github.vantiv.sdk;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;

public class TestSetup {

    private String preliveInputSequence;

    @Before
    public void setup() {
        preliveInputSequence = "";
        preliveInputSequence += "testUser" + System.lineSeparator(); // username
        preliveInputSequence += "testPassword" + System.lineSeparator(); // password
        preliveInputSequence += "testId" + System.lineSeparator(); // merchantId
        preliveInputSequence += "prelive" + System.lineSeparator(); // environment
        preliveInputSequence += "3000" + System.lineSeparator(); // TCP timeout
        preliveInputSequence += System.lineSeparator(); // request path
        preliveInputSequence += System.lineSeparator(); // response path
        preliveInputSequence += System.lineSeparator(); // SFTP username
        preliveInputSequence += System.lineSeparator(); // SFTP password
        preliveInputSequence += System.lineSeparator(); // SFTP timeout
        preliveInputSequence += System.lineSeparator(); // proxy host
        preliveInputSequence += System.lineSeparator(); // proxy port
        preliveInputSequence += System.lineSeparator(); // PGP encryption mode
    }

    @Test
    public void testInitializeConfigFile_PredefinedEnvironmentNoEncryption() {

        BufferedReader input = new BufferedReader(new StringReader(preliveInputSequence));

        try {
            Properties config = Setup.initializeConfigFile(input);
            assertEquals("testUser", config.getProperty("username"));
            assertEquals("testPassword", config.getProperty("password"));
            assertEquals("testId", config.getProperty("merchantId"));
            assertEquals("https://payments.vantivprelive.com/vap/communicator/online", config.getProperty("url"));
            assertEquals("payments.vantivprelive.com", config.getProperty("batchHost"));
            assertEquals("15000", config.getProperty("batchPort"));
            assertEquals("https://payments.east.vantivprelive.com/vap/communicator/online", config.getProperty("multiSiteUrl1"));
            assertEquals("https://payments.west.vantivprelive.com/vap/communicator/online", config.getProperty("multiSiteUrl2"));
            assertEquals("false", config.getProperty("multiSite"));
            assertEquals("5", config.getProperty("multiSiteErrorThreshold"));
            assertEquals("48", config.getProperty("maxHoursWithoutSwitch"));
            assertEquals("false", config.getProperty("printMultiSiteDebug"));
            assertEquals("true", config.getProperty("batchUseSSL"));
            assertEquals("3000", config.getProperty("batchTcpTimeout"));
            assertEquals("", config.getProperty("batchRequestFolder"));
            assertEquals("", config.getProperty("batchResponseFolder"));
            assertEquals("", config.getProperty("sftpUsername"));
            assertEquals("", config.getProperty("sftpPassword"));
            assertEquals("7200000", config.getProperty("sftpTimeout"));
            assertEquals("", config.getProperty("proxyHost"));
            assertEquals("", config.getProperty("proxyPort"));
            assertEquals("500", config.getProperty("timeout"));
            assertEquals("Default Report Group", config.getProperty("reportGroup"));
            assertEquals("false", config.getProperty("printxml"));
            assertEquals("500000", config.getProperty("maxAllowedTransactionsPerFile"));
            assertEquals("100000", config.getProperty("maxTransactionsPerBatch"));
            assertEquals("false", config.getProperty("useEncryption"));
            assertEquals("", config.getProperty("PublicKeyPath"));
            assertEquals("", config.getProperty("PrivateKeyPath"));
            assertEquals("", config.getProperty("gpgPassphrase"));
            assertEquals("false", config.getProperty("deleteBatchFiles"));
        } catch (IOException e) {
            fail("IOException thrown when none expected");
        }
    }

    @Test
    public void testInitializeConfigFile_StoreConfig() {
        BufferedReader input = new BufferedReader(new StringReader(preliveInputSequence));

        try {
            Properties config = Setup.initializeConfigFile(input);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            config.store(baos, "");
            baos.close();
        } catch (IOException e) {
            fail("IOException thrown when none expected");
        }
    }

    @Test
    public void testInitializeConfigFile_BadEnvironmentInput() {
        String badInputSequence = "";
        badInputSequence += "testUser" + System.lineSeparator(); // username
        badInputSequence += "testPassword" + System.lineSeparator(); // password
        badInputSequence += "testId" + System.lineSeparator(); // merchantId
        badInputSequence += "not prelive" + System.lineSeparator(); // environment - bad input
        badInputSequence += "prelive" + System.lineSeparator(); // environment
        badInputSequence += "3000" + System.lineSeparator(); // TCP timeout
        badInputSequence += System.lineSeparator(); // request path
        badInputSequence += System.lineSeparator(); // response path
        badInputSequence += System.lineSeparator(); // SFTP username
        badInputSequence += System.lineSeparator(); // SFTP password
        badInputSequence += System.lineSeparator(); // SFTP timeout
        badInputSequence += System.lineSeparator(); // proxy host
        badInputSequence += System.lineSeparator(); // proxy port
        badInputSequence += System.lineSeparator(); // PGP encryption mode

        BufferedReader badInput = new BufferedReader(new StringReader(badInputSequence));
        BufferedReader goodInput = new BufferedReader(new StringReader(preliveInputSequence));

        try {
            Properties badConfig = Setup.initializeConfigFile(badInput);
            Properties goodConfig = Setup.initializeConfigFile(goodInput);

            ArrayList<String> goodValues = new ArrayList<>();
            ArrayList<String> badValues = new ArrayList<>();

            for (Object value : goodConfig.values()) {
                goodValues.add((String)value);
            }

            for (Object value : badConfig.values()) {
                badValues.add((String)value);
            }

            assertFalse(goodValues.isEmpty());

            assertEquals(goodValues.size(), badValues.size());

            for (int i = 0; i < goodValues.size(); i++) {
                assertEquals(goodValues.get(i), badValues.get(i));
            }
        } catch (IOException e) {
            fail("IOException thrown when no exception expected");
        }
    }

    @Test
    public void testInitializeConfigFile_CustomEnvironmentWithEncryption() {
        String customInputSequence = "";
        customInputSequence += "testUser" + System.lineSeparator(); // username
        customInputSequence += "testPassword" + System.lineSeparator(); // password
        customInputSequence += "testId" + System.lineSeparator(); // merchantId
        customInputSequence += "other" + System.lineSeparator(); // environment
        customInputSequence += "https://www.testurl.com" + System.lineSeparator(); // URL
        customInputSequence += "testBatchHost" + System.lineSeparator(); // batch host
        customInputSequence += "12345" + System.lineSeparator(); // batch port
        customInputSequence += "3000" + System.lineSeparator(); // TCP timeout
        customInputSequence += System.lineSeparator(); // request path
        customInputSequence += System.lineSeparator(); // response path
        customInputSequence += System.lineSeparator(); // SFTP username
        customInputSequence += System.lineSeparator(); // SFTP password
        customInputSequence += System.lineSeparator(); // SFTP timeout
        customInputSequence += System.lineSeparator(); // proxy host
        customInputSequence += System.lineSeparator(); // proxy port
        customInputSequence += "y" + System.lineSeparator(); // PGP encryption mode
        customInputSequence += "testPublicKey1" + System.lineSeparator(); // Vantiv public key path
        customInputSequence += "testPublicKey2" + System.lineSeparator(); // merchant public key path
        customInputSequence += "testPrivateKey" + System.lineSeparator(); // merchant private key path
        customInputSequence += "testPassphrase" + System.lineSeparator(); // decryption passphrase

        BufferedReader input = new BufferedReader(new StringReader(customInputSequence));

        try {
            Properties config = Setup.initializeConfigFile(input);
            assertEquals("testUser", config.getProperty("username"));
            assertEquals("testPassword", config.getProperty("password"));
            assertEquals("testId", config.getProperty("merchantId"));
            assertEquals("https://www.testurl.com", config.getProperty("url"));
            assertEquals("testBatchHost", config.getProperty("batchHost"));
            assertEquals("12345", config.getProperty("batchPort"));
            assertEquals("false", config.getProperty("multiSite"));
            assertEquals("false", config.getProperty("printMultiSiteDebug"));
            assertEquals("true", config.getProperty("useEncryption"));
            assertEquals("testPublicKey1", config.getProperty("VantivPublicKeyPath"));
            assertEquals("testPublicKey2", config.getProperty("PublicKeyPath"));
            assertEquals("testPrivateKey", config.getProperty("PrivateKeyPath"));
            assertEquals("testPassphrase", config.getProperty("gpgPassphrase"));
        } catch (IOException e) {
            fail("IOException thrown when none expected");
        }
    }
}