package io.github.vantiv.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NoHttpResponseException;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Communication {

    private static final String[] SUPPORTED_PROTOCOLS = new String[]{"TLSv1.3", "TLSv1.2"};
    public static final String CONTENT_TYPE_TEXT_XML_UTF8 = "text/xml; charset=UTF-8";
    private CloseableHttpClient httpClient;
    private final int KEEP_ALIVE_DURATION = 8000;
    private final int DEFAULT_RETRY_INTERVAL = 5000;
    private static final String NEUTER_STR = "NEUTERED";

    public Communication() {
        try {
            String protocol = getBestProtocol(SSLContext.getDefault().getSupportedSSLParameters().getProtocols());
            if (protocol == null) {
                throw new IllegalStateException("No supported TLS protocols available");
            }
            SSLContext ctx = SSLContexts.custom().setProtocol(protocol).build();
            ConnectionSocketFactory plainSocketFactory = new PlainConnectionSocketFactory();
            LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", plainSocketFactory)
                    .register("https", sslSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);

            HttpRequestRetryStrategy requestRetryStrategy = new DefaultHttpRequestRetryStrategy(0, TimeValue.ofMilliseconds(DEFAULT_RETRY_INTERVAL));
            // Vantiv will a close an idle connection, so we define our Keep-alive strategy to be below that threshold
            ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
                public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return TimeValue.ofMilliseconds(KEEP_ALIVE_DURATION);
                }
            };
            httpClient = HttpClients.custom().setConnectionManager(connManager)
                    .setRetryStrategy(requestRetryStrategy)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
        }
        catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static String getBestProtocol(final String[] availableProtocols) {
        String bestProtocol = null;
        if (availableProtocols == null || availableProtocols.length == 0) {
            return null;
        }
        List<String> availableProtocolsList = Arrays.asList(availableProtocols);
        for (String supportedProtocol : SUPPORTED_PROTOCOLS) {
            if (availableProtocolsList.contains(supportedProtocol)) {
                bestProtocol = supportedProtocol;
                break;
            }
        }
        return bestProtocol;
    }

    public String requestToServer(String xmlRequest, Properties configuration) {
        String xmlResponse = null;
        String proxyHost = configuration.getProperty("proxyHost");
        String proxyPort = configuration.getProperty("proxyPort");
        boolean httpKeepAlive = Boolean.valueOf(configuration.getProperty("httpKeepAlive", "false"));
        int httpTimeout = Integer.valueOf(configuration.getProperty("timeout", "6000"));
        HttpHost proxy;
        RequestConfig requestConfig;
        if (proxyHost != null && proxyHost.length() > 0 && proxyPort != null && proxyHost.length() > 0) {
            proxy = new HttpHost(proxyHost, Integer.valueOf(proxyPort));
            requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                    .setProxy(proxy)
                    .setResponseTimeout(Timeout.ofMilliseconds(httpTimeout))
                    .setConnectTimeout(Timeout.ofMilliseconds(httpTimeout))
                    .build();
        }
        else {
            requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                    .setResponseTimeout(Timeout.ofMilliseconds(httpTimeout))
                    .setConnectTimeout(Timeout.ofMilliseconds(httpTimeout))
                    .build();
        }

        RequestTarget reqTarget = CommManager.instance(configuration).findUrl();
        HttpPost post = new HttpPost(reqTarget.getUrl());
        post.setHeader("Content-Type", CONTENT_TYPE_TEXT_XML_UTF8);
        if (!httpKeepAlive) {
            post.setHeader("Connection", "close");
        }

        post.setConfig(requestConfig);
        HttpEntity entity = null;
        try {
            boolean printxml = "true".equalsIgnoreCase(configuration.getProperty("printxml"));
            boolean neuterXml = "true".equalsIgnoreCase(configuration.getProperty("neuterXml"));

            if (printxml) {
                printXml(xmlRequest, neuterXml);
            }

            post.setEntity(new StringEntity(xmlRequest, Charset.forName("UTF-8")));
            ClassicHttpResponse response = httpClient.execute(post);
            entity = response.getEntity();
            CommManager.instance(configuration).reportResult(reqTarget, CommManager.REQUEST_RESULT_RESPONSE_RECEIVED, response.getCode());
            if (response.getCode() != 200) {
                throw new CnpOnlineException(response.getCode() + ":" +
                        response.getReasonPhrase());
            }
            xmlResponse = EntityUtils.toString(entity, "UTF-8");
            if (printxml) {
                if (neuterXml) {
                    xmlResponse = neuterXml(xmlResponse);
                }
                System.out.println("Response XML: " + xmlResponse);
            }
        }
        catch (IOException e) {
            int result = CommManager.REQUEST_RESULT_CONNECTION_FAILED;
            if (e instanceof NoHttpResponseException) {
                result = CommManager.REQUEST_RESULT_RESPONSE_TIMEOUT;
            }
            CommManager.instance(configuration).reportResult(reqTarget, result, 0);
            throw new CnpOnlineException("Exception connection to Vantiv eCommerce", e);
        }
        catch (ParseException e) {
            throw new CnpOnlineException("Exception while parsing HttpEntity", e);
        }
        finally {
            if (entity != null) {
                EntityUtils.consumeQuietly(entity);
            }
            post.abort();
        }
        return xmlResponse;
    }

    /**
     * This method sends the request file to Vantiv eCommerce's sFTP server
     * @param requestFile   request file to be sent to Vantiv
     * @param configuration configuration Properties to use for processing
     * @throws IOException exceptions coming out of the sFTP actions
     */

    public void sendCnpRequestFileToSFTP(File requestFile, Properties configuration) throws IOException {
        Session session = setupSession(configuration);

        Channel channel;

        try {
            channel = session.openChannel("sftp");
            channel.connect();
        }
        catch (JSchException e) {
            throw new CnpBatchException("Exception connection to Vantiv eCommerce : " +
                    "error in session.openChannel/channel.connect", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;
        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                "true");

        if (printxml) {
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }

        try {
            sftp.put(requestFile.getAbsolutePath(), "inbound/" + requestFile.getName() + ".prg");
            sftp.rename("inbound/" + requestFile.getName() + ".prg", "inbound/" + requestFile.getName() + ".asc");
        }
        catch (SftpException e) {
            throw new CnpBatchException("Exception in sFTP operation", e);
        }

        channel.disconnect();
        session.disconnect();
    }

    /**
     * Grabs the response file from Vantiv eCommerce's sFTP server. This method is blocking!
     * It will continue to poll until the timeout has elapsed or the file has been retrieved!
     *
     * @param requestFile   request file associated with the response
     * @param responseFile  response file to be retrieved
     * @param configuration configuration Properties
     * @throws IOException exceptions coming out of the sFTP actions
     */
    public void receiveCnpRequestResponseFileFromSFTP(File requestFile, File responseFile, Properties configuration) throws IOException {
        Session session = setupSession(configuration);

        Channel channel;

        try {
            channel = session.openChannel("sftp");
            channel.connect();
        }
        catch (JSchException e) {
            throw new CnpBatchException("Exception connection to Vantiv eCommerce : " +
                    "error in session.openChannel/channel.connect", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;

        Long start = System.currentTimeMillis();
        Long timeout = Long.parseLong(configuration.getProperty("sftpTimeout"));
        System.out.println("Retrieving from sFTP...");
        while (System.currentTimeMillis() - start < timeout) {
            try {
                Thread.sleep(45000);
            }
            catch (InterruptedException e) {
                System.out.println(e);
            }
            boolean success = true;

            try {
                sftp.get("outbound/" + requestFile.getName() + ".asc", responseFile.getAbsolutePath());
            }
            catch (SftpException e) {
                success = false;
                System.out.println(e);
            }

            if (success) {
                try {
                    sftp.rm("outbound/" + requestFile.getName() + ".asc");
                }
                catch (SftpException e) {
                    throw new CnpBatchException("Exception in sFTP operation", e);
                }
                break;
            }
            System.out.print(".");
        }
        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                "true");
        if (printxml) {
            BufferedReader reader = new BufferedReader(new FileReader(responseFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }

        channel.disconnect();
        session.disconnect();
    }

    private Session setupSession(Properties configuration) {
        String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");
        String portStr = configuration.getProperty("sftpPort");

        int port = -1;
        if (portStr != null && portStr != "") {
            try {
                port = Integer.parseInt(portStr);
            }
            catch (NumberFormatException e) {
                throw new CnpBatchException("Exception parsing sftpPort from config", e);
            }
        }

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch;
        Session session;

        try {
            jsch = new JSch();
            if (port != -1) {
                session = jsch.getSession(username, hostname, port);
            }
            else {
                session = jsch.getSession(username, hostname);
            }

            session.setConfig(config);
            session.setPassword(password);

            session.connect();
        }
        catch (JSchException e) {
            throw new CnpBatchException("Exception connection to Vantiv eCommerce : error in session.connect", e);
        }

        return session;
    }

    /* Method to neuter out sensitive information from xml */
    public String neuterXml(String xml) {
        if (xml == null) {
            return xml;
        }

        xml = xml.replaceAll("<accNum>.*</accNum>", "<accNum>" + NEUTER_STR + "</accNum>");
        xml = xml.replaceAll("<user>.*</user>", "<user>" + NEUTER_STR + "</user>");
        xml = xml.replaceAll("<password>.*</password>", "<password>" + NEUTER_STR + "</password>");
        xml = xml.replaceAll("<track>.*</track>", "<track>" + NEUTER_STR + "</track>");
        xml = xml.replaceAll("<number>.*</number>", "<number>" + NEUTER_STR + "</number>");
        xml = xml.replaceAll("<cardValidationNum>.*</cardValidationNum>", "<cardValidationNum>" + NEUTER_STR + "</cardValidationNum>");
        return xml;
    }

    public String printXml(String xmlRequest, boolean neuterXml) {
        String xmlToLog = xmlRequest;
        if (neuterXml) {
            xmlToLog = neuterXml(xmlToLog);
        }
        System.out.println("Request XML: " + xmlToLog);

        return xmlRequest;
    }

}
