package com.cnp.sdk;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Setup {
    /* List of environments for the configuration. */
    private enum EnvironmentConfiguration {
        SANDBOX("sandbox", "https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online", "prelive.litle.com", "15000"),
        PRELIVE("prelive", "https://payments.vantivprelive.com/vap/communicator/online", "payments.vantivprelive.com", "15000"),
        POSTLIVE("postlive", "https://payments.vantivpostlive.com/vap/communicator/online", "payments.vantivpostlive.com", "15000"),
        PRODUCTION("production", "https://payments.vantivcnp.com/vap/communicator/online", "payments.vantivcnp.com", "15000"),
        OTHER("other", "You will be asked for all the values", null, null);

        private final String key;
        private final String onlineUrl;
        private final String batchUrl;
        private final String batchPort;

        private EnvironmentConfiguration(final String key, final String online, final String batch, final String port) {
            this.key = key;
            this.onlineUrl = online;
            this.batchUrl = batch;
            this.batchPort = port;
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
        Properties config = new Properties();
        PrintStream configFile = new PrintStream(file);
        String lastUserInput;

        BufferedReader stdin = new BufferedReader
                (new InputStreamReader(System.in));

        System.out.println("Welcome to Vantiv eCommerce Java_SDK");
        System.out.print("Please input your presenter user name: ");
        config.put("username", stdin.readLine());
        System.out.print("Please input your presenter password: ");
        config.put("password", stdin.readLine());
        System.out.print("Please input your merchantId: ");
        config.put("merchantId", stdin.readLine());
        boolean badInput = false;
        do{
            if( badInput ){
                System.out.println("====== Invalid choice entered ======");
            }
            System.out.println("Please choose an environment from the following list (example: 'prelive'):");
            for (final EnvironmentConfiguration environConfig : EnvironmentConfiguration.values()) {
                System.out.println(String.format("\t%s => %s", environConfig.getKey(), environConfig.getOnlineUrl()));
            }
            lastUserInput = stdin.readLine();
            EnvironmentConfiguration environSelected = EnvironmentConfiguration.fromValue(lastUserInput);
            if (environSelected == null) {
                // error condition
                badInput = true;
            } else if (EnvironmentConfiguration.OTHER.equals(environSelected)) {
                // user wants to enter custom values
                System.out.println("Please input the URL for online transactions (ex: https://www.testantivcnp.com/sandbox/communicator/online):");
                config.put("url", stdin.readLine());
                System.out.println("Please input the Host name for batch transactions (ex: payments.vantivcnp.com):");
                config.put("batchHost", stdin.readLine());
                System.out.println("Please input the port number for batch transactions (ex: 15000):");
                config.put("batchPort", stdin.readLine());
                badInput = false;
            } else {
                // standard predefined cases
                config.put("url", environSelected.getOnlineUrl());
                config.put("batchHost", environSelected.getBatchUrl());
                config.put("batchPort", environSelected.getBatchPort());
                badInput = false;
            }
        } while( badInput );

        System.out.print("Values set for host: ");
        System.out.print("\n\tURL for online transactions: " + config.getProperty("url"));
        System.out.print("\n\tHost for batch transactions: " + config.getProperty("batchHost"));
        System.out.print("\n\tPort for batch transactions: " + config.getProperty("batchPort") + "\n");

        config.put("batchUseSSL", "true");
        System.out.print("Please input the batch TCP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = stdin.readLine();
        config.put("batchTcpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));

        System.out.print("\nBatch SDK generates files for Requests and Responses. You may leave these blank if you do not plan to use \nbatch processing. Please input the absolute path to the folder with write permissions for: \n");
        System.out.print("\tRequests: ");
        config.put("batchRequestFolder", stdin.readLine());

        System.out.print("\tResponses: ");
        config.put("batchResponseFolder", stdin.readLine());

        System.out.print("\nPlease input your credentials for sFTP access for batch delivery. You may leave these blank if you do not plan to use sFTP.\n");
        System.out.print("\tUsername: ");
        config.put("sftpUsername", stdin.readLine());
        System.out.print("\tPassword: ");
        config.put("sftpPassword", stdin.readLine());
        System.out.print("Please input the sFTP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = stdin.readLine();
        config.put("sftpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));


        System.out.print("\nPlease input the proxy host, if no proxy hit enter: ");
        lastUserInput = stdin.readLine();
        config.put("proxyHost", (lastUserInput == null ? "" : lastUserInput));
        System.out.print("Please input the proxy port, if no proxy hit enter: ");
        lastUserInput = stdin.readLine();
        config.put("proxyPort", (lastUserInput == null ? "" : lastUserInput));
        //default http timeout set to 500 ms
        config.put("timeout", "500");
        config.put("reportGroup", "Default Report Group");
        config.put("printxml", "false");

        config.put("maxAllowedTransactionsPerFile", "500000");
        config.put("maxTransactionsPerBatch", "100000");

        System.out.print("Use PGP encryption for batch files? (No encryption by default): ");
        lastUserInput = stdin.readLine();
        if ("true".equals(lastUserInput) || "yes".equals(lastUserInput) || "y".equals(lastUserInput)) {
            config.put("useEncryption", "true");

            System.out.print("Please input path to Vantiv's public key (for encryption of batch requests) : ");
            lastUserInput = stdin.readLine();
            config.put("VantivPublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant public key (To keep temp file encrypted) : ");
            lastUserInput = stdin.readLine();
            config.put("PublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant private key (for decryption of batch responses) : ");
            lastUserInput = stdin.readLine();
            config.put("PrivateKeyPath", lastUserInput);

            System.out.print("Passphrase for decryption : ");
            config.put("gpgPassphrase", stdin.readLine());
        } else {
            config.put("useEncryption", "false");
            config.put("PublicKeyPath", "");
            config.put("PrivateKeyPath", "");
            config.put("gpgPassphrase", "");
        }

        config.put("deleteBatchFiles", "false");

        config.store(configFile, "");
        System.out.println("The Cnp configuration file has been generated, the file is located at " + file.getAbsolutePath());

        configFile.close();
    }

}
