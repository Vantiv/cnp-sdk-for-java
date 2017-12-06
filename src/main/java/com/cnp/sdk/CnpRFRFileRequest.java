package com.cnp.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.cnp.sdk.generate.Authentication;
import com.cnp.sdk.generate.CnpRequest;
import com.cnp.sdk.generate.RFRRequest;

public class CnpRFRFileRequest {

    RFRRequest rfrRequest;

    private JAXBContext jc;
    private Properties properties;
    private Communication communication;
    private String requestFileName;
    private File requestFile;
    private File responseFile;
    //private File tempCnpRequestFile;
    private String requestId;
    private Marshaller marshaller;
    private Configuration config = null;


    public CnpRFRFileRequest(String requestFileName, RFRRequest request, Properties properties){
        rfrRequest = request;
        initializeMembers(requestFileName, properties);
        addRFRRequest(request);
    }

    public CnpRFRFileRequest(String requestFileName, RFRRequest request, Configuration config) {
        this.config = config;
        initializeMembers(requestFileName, null);
    }

    public CnpRFRFileRequest(String requestFileName, RFRRequest request){
        rfrRequest = request;
        initializeMembers(requestFileName, null);
        addRFRRequest(request);
    }

    public void initializeMembers(String requestFileName, Properties in_properties) throws CnpBatchException{
        try {
            this.jc = JAXBContext.newInstance("com.cnp.sdk.generate");
            if(config == null){
                config = new Configuration();
            }
            this.communication = new Communication();
            this.requestFileName = requestFileName;
            marshaller = jc.createMarshaller();
            // JAXB_FRAGMENT property required to prevent unnecessary XML info from being printed in the file during marshal.
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            // Proper formatting of XML purely for aesthetic purposes.
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (in_properties == null || in_properties.isEmpty()) {
                this.properties = new Properties();
                this.properties.load(new FileInputStream(config.location()));
            } else {
                fillInMissingFieldsFromConfig(in_properties);
                this.properties = in_properties;
            }

            responseFile = getFileToWrite("batchResponseFolder");

        } catch (FileNotFoundException e) {
            throw new CnpBatchException(
                    "Configuration file not found." +
                            " If you are not using the .cnp_SDK_config.properties file," +
                            " please use the " + CnpBatchFileRequest.class.getSimpleName() + "(String, Properties) constructor." +
                            " If you are using .cnp_SDK_config.properties, you can generate one using java -jar cnp-sdk-for-java-x.xx.jar", e);
        } catch (IOException e) {
            throw new CnpBatchException(
                    "Configuration file could not be loaded.  Check to see if the current user has permission to access the file", e);
        } catch (JAXBException e) {
            throw new CnpBatchException(
                    "Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
        }
    }


    private void addRFRRequest(RFRRequest request){
        try {
            CnpRequest cnpRequest = buildCnpRequest();

            // Code to write to the file directly
            File localFile = getFileToWrite("batchRequestFolder");
            StringWriter writerRequest = new StringWriter();
            StringWriter writerRFR = new StringWriter();
            try {
                marshaller.marshal(cnpRequest, writerRequest);
                marshaller.marshal(rfrRequest, writerRFR);
            } catch (JAXBException e) {
                throw new CnpBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?");
            }
            String xmlRequest = writerRequest.toString();
            xmlRequest = xmlRequest.replace("</cnpRequest>", " ");

            String xmlRFR = writerRFR.toString();

            OutputStream cnpReqWriter = new FileOutputStream(localFile);
            cnpReqWriter.write(xmlRequest.getBytes());
            cnpReqWriter.write(xmlRFR.getBytes());

            cnpReqWriter.write(("</cnpRequest>\n").getBytes());
            // marshaller.marshal(cnpRequest, os);
            requestFile = localFile;
            cnpReqWriter.close();
        } catch (IOException e) {
            throw new CnpBatchException(
                    "Error while creating a batch request file. Check to see if the current user has permission to read and write to "
                            + this.properties.getProperty("batchRequestFolder"), e);
        }
    }

    private CnpRequest buildCnpRequest() {
        Authentication authentication = new Authentication();
        authentication.setPassword(this.properties.getProperty("password"));
        authentication.setUser(this.properties.getProperty("username"));
        CnpRequest cnpRequest = new CnpRequest();

        if(requestId != null && requestId.length() != 0) {
            cnpRequest.setId(requestId);
        }
        cnpRequest.setAuthentication(authentication);
        cnpRequest.setVersion(Versions.XML_VERSION);
        BigInteger numOfBatches = BigInteger.valueOf(0);
        cnpRequest.setNumBatchRequests(numOfBatches);
        return cnpRequest;
    }

    /**
     * Sends the RFR file via sFTP
     * @return An RFR Response File
     * @throws CnpBatchException
     */
    public CnpRFRFileResponse sendToCnpSFTP() throws CnpBatchException{
        try {
            communication.sendCnpRequestFileToSFTP(requestFile, properties);
            communication.receiveCnpRequestResponseFileFromSFTP(requestFile, responseFile, properties);

            CnpRFRFileResponse retObj = new CnpRFRFileResponse(responseFile);
            return retObj;
        } catch (IOException e) {
            throw new CnpBatchException("There was an exception while creating the Cnp Request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
        }
    }

    /**
     * Returns an appropriate file at the locationkey
     * @param locationKey
     * @return File
     */
    public File getFileToWrite(String locationKey) {
        String fileName = this.requestFileName;
        String writeFolderPath = this.properties.getProperty(locationKey);
        File fileToReturn = new File(writeFolderPath + File.separator
                + fileName);

        if (!fileToReturn.getParentFile().exists()) {
            fileToReturn.getParentFile().mkdir();
        }

        return fileToReturn;
    }

    private void fillInMissingFieldsFromConfig(Properties config) throws CnpBatchException{
        Properties localConfig = new Properties();
        boolean propertiesReadFromFile = false;
        try {
            String[] allProperties = { "username", "password", "proxyHost",
                    "proxyPort", "batchHost", "batchPort",
                    "batchTcpTimeout", "batchUseSSL",
                    "maxAllowedTransactionsPerFile", "maxTransactionsPerBatch",
                    "batchRequestFolder", "batchResponseFolder" };
            for (String prop : allProperties) {
                // if the value of a property is not set, look at the Properties member of the class first, and the .properties file next.
                if (config.getProperty(prop) == null) {
                    if ( this.properties != null && this.properties.get(prop) != null ){
                        config.setProperty(prop, this.properties.getProperty(prop));
                    }
                    else{
                        if (!propertiesReadFromFile) {
                            localConfig.load(new FileInputStream((new Configuration()).location()));
                            propertiesReadFromFile = true;
                        }
                        config.setProperty(prop, localConfig.getProperty(prop));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new CnpBatchException("File .cnp_SDK_config.properties was not found. Please run the Setup.java application to create the file at location "+ (new Configuration()).location(), e);
        } catch (IOException e) {
            throw new CnpBatchException("There was an exception while reading the .cnp_SDK_config.properties file.", e);
        }
    }

    Properties getConfig() {
        return this.properties;
    }

    void setResponseFile(File inFile) {
        this.responseFile = inFile;
    }

    public void setCommunication(Communication com) {
        this.communication = com;
    }

    public File getFile() {
        return requestFile;
    }

}