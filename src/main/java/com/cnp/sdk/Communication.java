package com.cnp.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Communication {

    private static final String[] SUPPORTED_PROTOCOLS = new String[] {"TLSv1.1", "TLSv1.2"};
    private CloseableHttpClient httpClient;
    private final int KEEP_ALIVE_DURATION = 8000;
	private static final String NEUTER_STR = "NEUTERED";

	public Communication() {
		try {

			String protocol = getBestProtocol(SSLContext.getDefault().getSupportedSSLParameters().getProtocols());
			if (protocol == null) {
				throw new IllegalStateException("No supported TLS protocols available");
			}
			SSLContext ctx = SSLContexts.custom().useProtocol(protocol).build();
			ConnectionSocketFactory plainSocketFactory = new PlainConnectionSocketFactory();
			LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", plainSocketFactory)
					.register("https", sslSocketFactory)
					.build();

			BasicHttpClientConnectionManager manager = new BasicHttpClientConnectionManager(registry);

			HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, true);
			// Vantiv will a close an idle connection, so we define our Keep-alive strategy to be below that threshold
			ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					return KEEP_ALIVE_DURATION;
				}
			};
			httpClient = HttpClients.custom().setConnectionManager(manager)
					.setRetryHandler(requestRetryHandler)
					.setKeepAliveStrategy(keepAliveStrategy)
					.build();

		} catch (GeneralSecurityException ex) {
			throw new IllegalStateException(ex);
		}
	}

    private static String getBestProtocol(final String[] availableProtocols) {
        for (String availableProtocol: availableProtocols) {
            for (String supportedProtocol: SUPPORTED_PROTOCOLS) {
                if (supportedProtocol.equals(availableProtocol)) {
                    return availableProtocol;
                }
            }
        }
        return null;
    }

	public String requestToServer(String xmlRequest, Properties configuration) {
		String xmlResponse;
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
					.setSocketTimeout(httpTimeout)
					.setConnectTimeout(httpTimeout)
					.build();
		} else {
			requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
			        .setSocketTimeout(httpTimeout)
                    .setConnectTimeout(httpTimeout)
					.build();
		}

		HttpPost post = new HttpPost(configuration.getProperty("url"));
		post.setHeader("Content-Type", "application/xml;charset=\"UTF-8\"");
		if(!httpKeepAlive) {
			post.setHeader("Connection", "close");
		}

		post.setConfig(requestConfig);
		HttpEntity entity = null;
		try {
			boolean printxml = "true".equalsIgnoreCase(configuration.getProperty("printxml"));
			boolean neuterXml = "true".equalsIgnoreCase(configuration.getProperty("neuterXml"));
			if (printxml) {
				if (neuterXml) {
					xmlRequest = neuterXml(xmlRequest);
				}
				System.out.println("Request XML: " + xmlRequest);
			}
			
			post.setEntity(new StringEntity(xmlRequest,"UTF-8"));
			//System.out.println("POST    "+post);
			HttpResponse response = httpClient.execute(post);
			System.out.println("RESPONSE    "+response); 
			entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new CnpOnlineException(response.getStatusLine().getStatusCode() + ":" +
						response.getStatusLine().getReasonPhrase());
			}
			xmlResponse = EntityUtils.toString(entity,"UTF-8");

			if (printxml) {
				if (neuterXml) {
					xmlResponse = neuterXml(xmlResponse);
				}

				System.out.println("Response XML: " + xmlResponse);
			}
		} catch (IOException e) {
			throw new CnpOnlineException("Exception connection to Vantiv eCommerce", e);
		} finally {
			if (entity != null) {
				EntityUtils.consumeQuietly(entity);
			}
			post.abort();
		}
		return xmlResponse;
	}

	/**
	 * This method sends the request file to Vantiv eCommerce's sFTP server
	 * @param requestFile request file to be sent to Vantiv
	 * @param configuration configuration Properties to use for processing
	 * @throws IOException exceptions coming out of the sFTP actions
	 */
	public void sendCnpRequestFileToSFTP(File requestFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
	    String password = configuration.getProperty("sftpPassword");
	    String hostname = configuration.getProperty("batchHost");

	    java.util.Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");
	    JSch jsch;
	    Session session;
	    try{
    	    jsch = new JSch();
    	    session = jsch.getSession(username, hostname);
    	    session.setConfig(config);
    	    session.setPassword(password);

    	    session.connect();
	    } catch(JSchException e){
	        throw new CnpBatchException("Exception connection to Vantiv eCommerce : error in session.connect", e);
	    }

	    Channel channel;

	    try{
	        channel = session.openChannel("sftp");
	        channel.connect();
	    } catch(JSchException e){
	        throw new CnpBatchException("Exception connection to Vantiv eCommerce : " +
					"error in session.openChannel/channel.connect", e);
	    }

	    ChannelSftp sftp = (ChannelSftp) channel;
	    boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                        "true");

	    if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
	    }

	    try {
            sftp.put(requestFile.getAbsolutePath(), "inbound/" + requestFile.getName() + ".prg");
            sftp.rename("inbound/" + requestFile.getName() + ".prg", "inbound/" +
					requestFile.getName() + ".asc");
        } catch (SftpException e) {
            throw new CnpBatchException("Exception in sFTP operation", e);
        }

	    channel.disconnect();
	    session.disconnect();
	}

	/**
	 * Grabs the response file from Vantiv eCommerce's sFTP server. This method is blocking!
	 * It will continue to poll until the timeout has elapsed or the file has been retrieved!
	 * @param requestFile request file associated with the response
	 * @param responseFile response file to be retrieved
	 * @param configuration configuration Properties
	 * @throws IOException exceptions coming out of the sFTP actions
	 */
	public void receiveCnpRequestResponseFileFromSFTP(File requestFile, File responseFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch;
        Session session;

        try{
            jsch = new JSch();
            session = jsch.getSession(username, hostname);
            session.setConfig(config);
            session.setPassword(password);

            session.connect();
        } catch(JSchException e){
            throw new CnpBatchException("Exception connection to Vantiv eCommerce : error in session.connect", e);
        }

        Channel channel;

        try{
            channel = session.openChannel("sftp");
            channel.connect();
        } catch(JSchException e){
            throw new CnpBatchException("Exception connection to Vantiv eCommerce : " +
					"error in session.openChannel/channel.connect", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;

        Long start = System.currentTimeMillis();
        Long timeout = Long.parseLong(configuration.getProperty("sftpTimeout"));
        System.out.println("Retrieving from sFTP...");
        while(System.currentTimeMillis() - start < timeout){
            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            boolean success = true;

            try{
                sftp.get("outbound/" + requestFile.getName() + ".asc", responseFile.getAbsolutePath());
            } catch(SftpException e){
                success = false;
                System.out.println(e);
            }

            if(success) {
                try {
                    sftp.rm("outbound/" + requestFile.getName() + ".asc");
                } catch (SftpException e) {
                    throw new CnpBatchException("Exception in sFTP operation", e);
                }
                break;
            }
            System.out.print(".");
        }
        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                        "true");
        if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(responseFile));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }

        channel.disconnect();
        session.disconnect();
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
		return xml;

	}

}
