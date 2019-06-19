package com.cnp.sdk;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Setup {
    /* List of environments for the configuration. */
    private enum EnvironmentConfiguration {
        SANDBOX("sandbox", "https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online", "payments.vantivprelive.com", "15000",
                "https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online","https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online"),
        PRELIVE("prelive", "https://payments.vantivprelive.com/vap/communicator/online", "payments.vantivprelive.com", "15000",
                "https://payments.east.vantivprelive.com/vap/communicator/online","https://payments.west.vantivprelive.com/vap/communicator/online"),
        POSTLIVE("postlive", "https://payments.vantivpostlive.com/vap/communicator/online", "payments.vantivpostlive.com", "15000",
                "https://payments.east.vantivpostlive.com/vap/communicator/online", "https://payments.west.vantivpostlive.com/vap/communicator/online"),
        PRODUCTION("production", "https://payments.vantivcnp.com/vap/communicator/online", "payments.vantivcnp.com", "15000",
                "https://payments.east.vantivcnp.com/vap/communicator/online", "https://payments.west.vantivcnp.com/vap/communicator/online"),
        OTHER("other", "You will be asked for all the values", null, null, null, null);

        private final String key;
        private final String onlineUrl;
        private final String batchUrl;
        private final String batchPort;
        private final String multiSiteUrl1;
        private final String multiSiteUrl2;

        private EnvironmentConfiguration(final String key, final String online, final String batch, final String port, final String multiSiteUrl1, final String multiSiteUrl2) {
            this.key = key;
            this.onlineUrl = online;
            this.batchUrl = batch;
            this.batchPort = port;
            this.multiSiteUrl1 = multiSiteUrl1;
            this.multiSiteUrl2 = multiSiteUrl2;
        }

        public final String getKey() {
            return this.key;
        }

        public final String getOnlineUrl() {
            return this.onlineUrl;
        }

        public final String getBatchUrl() {
            return this.batchUrl;
        }

        public final String getBatchPort() {
            return this.batchPort;
        }
        
        public final String getMultiSiteUrl1() {
            return this.multiSiteUrl1;
        }

        public final String getMultiSiteUrl2() {
            return this.multiSiteUrl2;
        }

        public static final EnvironmentConfiguration fromValue(final String value) {
            for (final EnvironmentConfiguration environConfig : EnvironmentConfiguration.values()) {
                if (environConfig.getKey().equals(value)) {
                    return environConfig;
                }
            }
            return null;
        }
    }

    /**
     * @param args main args
     * @throws IOException IO exception during setup
     */


    public static void main(String[] args) throws IOException {
        File file = (new Configuration()).location();
        PrintStream configFile = new PrintStream(file);


        BufferedReader stdin = new BufferedReader
                (new InputStreamReader(System.in));

        Properties config = initializeConfigFile(stdin);

        config.store(configFile, "");
        configFile.close();
        System.out.println("The Cnp configuration file has been generated, the file is located at " + file.getAbsolutePath());
    }

    static Properties initializeConfigFile(BufferedReader input) throws IOException {
        String lastUserInput;
        Properties config = new Properties();

        System.out.println("Welcome to Vantiv eCommerce Java_SDK");
        System.out.print("Please input your presenter user name: ");
        config.setProperty("username", input.readLine());
        System.out.print("Please input your presenter password: ");
        config.setProperty("password", input.readLine());
        System.out.print("Please input your merchantId: ");
        config.setProperty("merchantId", input.readLine());
        boolean badInput = false;
        do{
            if( badInput ){
                System.out.println("====== Invalid choice entered ======");
            }
            System.out.println("Please choose an environment from the following list (example: 'prelive'):");
            for (final EnvironmentConfiguration environConfig : EnvironmentConfiguration.values()) {
                System.out.println(String.format("\t%s => %s", environConfig.getKey(), environConfig.getOnlineUrl()));
            }
            lastUserInput = input.readLine();
            EnvironmentConfiguration environSelected = EnvironmentConfiguration.fromValue(lastUserInput);
            if (environSelected == null) {
                // error condition
                badInput = true;
            } else if (EnvironmentConfiguration.OTHER.equals(environSelected)) {
                // user wants to enter custom values
                System.out.println("Please input the URL for online transactions (ex: https://www.testantivcnp.com/sandbox/communicator/online):");
                config.setProperty("url", input.readLine());
                System.out.println("Please input the Host name for batch transactions (ex: payments.vantivcnp.com):");
                config.setProperty("batchHost", input.readLine());
                System.out.println("Please input the port number for batch transactions (ex: 15000):");
                config.setProperty("batchPort", input.readLine());
                config.setProperty("multiSite", "false");
                config.setProperty("printMultiSiteDebug", "false");
                badInput = false;
            } else {
                // standard predefined cases
                config.setProperty("url", environSelected.getOnlineUrl());
                config.setProperty("batchHost", environSelected.getBatchUrl());
                config.setProperty("batchPort", environSelected.getBatchPort());

                config.setProperty("multiSiteUrl1", environSelected.getMultiSiteUrl1());
                config.setProperty("multiSiteUrl2", environSelected.getMultiSiteUrl2());
                config.setProperty("multiSite", "false");
                config.setProperty("multiSiteErrorThreshold", "5");
                config.setProperty("maxHoursWithoutSwitch",  "48");
                config.setProperty("printMultiSiteDebug", "false");
                badInput = false;
            }
        } while( badInput );

        System.out.print("Values set for host: ");
        System.out.print("\n\tURL for online transactions: " + config.getProperty("url"));
        System.out.print("\n\tHost for batch transactions: " + config.getProperty("batchHost"));
        System.out.print("\n\tPort for batch transactions: " + config.getProperty("batchPort") + "\n");

        config.setProperty("batchUseSSL", "true");
        System.out.print("Please input the batch TCP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = input.readLine();
        config.setProperty("batchTcpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));

        System.out.print("\nBatch SDK generates files for Requests and Responses. You may leave these blank if you do not plan to use \nbatch processing. Please input the absolute path to the folder with write permissions for: \n");
        System.out.print("\tRequests: ");
        config.setProperty("batchRequestFolder", input.readLine());

        System.out.print("\tResponses: ");
        config.setProperty("batchResponseFolder", input.readLine());

        System.out.print("\nPlease input your credentials for sFTP access for batch delivery. You may leave these blank if you do not plan to use sFTP.\n");
        System.out.print("\tUsername: ");
        config.setProperty("sftpUsername", input.readLine());
        System.out.print("\tPassword: ");
        config.setProperty("sftpPassword", input.readLine());
        System.out.print("Please input the sFTP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = input.readLine();
        config.setProperty("sftpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));


        System.out.print("\nPlease input the proxy host, if no proxy hit enter: ");
        lastUserInput = input.readLine();
        config.setProperty("proxyHost", (lastUserInput == null ? "" : lastUserInput));
        System.out.print("Please input the proxy port, if no proxy hit enter: ");
        lastUserInput = input.readLine();
        config.setProperty("proxyPort", (lastUserInput == null ? "" : lastUserInput));
        //default http timeout set to 500 ms
        config.setProperty("timeout", "500");
        config.setProperty("reportGroup", "Default Report Group");
        config.setProperty("printxml", "false");

        config.setProperty("maxAllowedTransactionsPerFile", "500000");
        config.setProperty("maxTransactionsPerBatch", "100000");

        System.out.print("Use PGP encryption for batch files? (No encryption by default): ");
        lastUserInput = input.readLine();
        if ("true".equals(lastUserInput) || "yes".equals(lastUserInput) || "y".equals(lastUserInput)) {
            config.setProperty("useEncryption", "true");

            System.out.print("Please input path to Vantiv's public key (for encryption of batch requests) : ");
            lastUserInput = input.readLine();
            config.setProperty("VantivPublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant public key (To keep temp file encrypted) : ");
            lastUserInput = input.readLine();
            config.setProperty("PublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant private key (for decryption of batch responses) : ");
            lastUserInput = input.readLine();
            config.setProperty("PrivateKeyPath", lastUserInput);

            System.out.print("Passphrase for decryption : ");
            config.setProperty("gpgPassphrase", input.readLine());
        } else {
            config.setProperty("useEncryption", "false");
            config.setProperty("PublicKeyPath", "");
            config.setProperty("PrivateKeyPath", "");
            config.setProperty("gpgPassphrase", "");
        }

        config.setProperty("deleteBatchFiles", "false");

        return config;
    }

}
