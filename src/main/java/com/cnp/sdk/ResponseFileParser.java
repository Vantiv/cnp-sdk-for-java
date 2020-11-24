package com.cnp.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

import com.cnp.sdk.generate.CnpTransactionInterface;

public class ResponseFileParser implements AutoCloseable {
    private static final String GENERATE_PACKAGE_NAME = "com.cnp.sdk.generate";

    private InputStream in = null;
    private Reader reader = null;
    private Reader buffer = null;

    public ResponseFileParser(File responseFile) {
        try {
            in = new FileInputStream(responseFile);
            reader = new InputStreamReader(in);
            buffer = new BufferedReader(reader);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getNextTag(String tagToLookFor) throws Exception {
        StringBuilder currentStartingTagInFile = new StringBuilder();
        StringBuilder retStringBuf = new StringBuilder();
        StringBuilder currentEndingTagInFile = new StringBuilder();

        boolean startRecordingStartingTag = false;
        boolean startRecordingEndingTag = false;
        boolean startRecordingRetString = false;

        char lastChar = 0;

        String openingTagToLookFor = "<" + tagToLookFor;
        String closingTagToLookFor = "</" + tagToLookFor + ">";

        int r;
        while ((r = buffer.read()) != -1) {
            char ch = (char) r;

            if (startRecordingRetString) {
                retStringBuf.append(ch);

                if (lastChar == '<' && ch == '/') {
                    startRecordingEndingTag = true;
                    currentEndingTagInFile.append(lastChar);
                }
                // override process for elements like cnpResponse and
                // batchResponse
                if ((tagToLookFor.compareToIgnoreCase("batchResponse") == 0 || tagToLookFor
                        .compareToIgnoreCase("cnpResponse") == 0)
                        && ch == '>') {
                    retStringBuf.append(closingTagToLookFor);
                    break;
                }
            }
            // We want to look for startingTag only if we aren't already
            // recording the string to return.
            if (ch == '<' && !startRecordingRetString) {
                startRecordingStartingTag = true;
            }

            if (startRecordingStartingTag) {
                currentStartingTagInFile.append(ch);

                if (okToStartOrStopRecordingString(openingTagToLookFor, currentStartingTagInFile.toString())) {
                    startRecordingRetString = true;
                    retStringBuf.append(currentStartingTagInFile);
                    if (openingTagToLookFor.compareToIgnoreCase("<cnpResponse") != 0) {
                        retStringBuf.append(" xmlns=\"http://www.vantivcnp.com/schema\"");
                    }
                    startRecordingStartingTag = false;
                    currentStartingTagInFile.delete(0,
                            currentStartingTagInFile.length());
                }
                // tag declaration has ended. Safe to discard.
                if (ch == '>') {
                    if (tagToLookFor.compareToIgnoreCase("transactionResponse") == 0
                            && currentStartingTagInFile.toString()
                            .compareToIgnoreCase("</batchResponse>") == 0) {
                        // Presumably this will only happen when the user is
                        // requesting a new transaction info,
                        // but all transactions have been exhausted. i.e. this
                        // one is a batchResponse
                        throw new Exception(
                                "All payments in this batch have already been retrieved.");
                    }
                    startRecordingStartingTag = false;
                    currentStartingTagInFile.delete(0,
                            currentStartingTagInFile.length());
                }
            }

            if (startRecordingEndingTag) {
                currentEndingTagInFile.append(ch);
                if (ch == '>') {
                    startRecordingEndingTag = false;
                    if (okToStartOrStopRecordingString(closingTagToLookFor, currentEndingTagInFile.toString())) {
//						startRecordingRetString = false;
//						currentEndingTagInFile.delete(0,
//						currentEndingTagInFile.length());
                        break;
                    }
                    currentEndingTagInFile.delete(0,
                            currentEndingTagInFile.length());
                }
            }

            lastChar = ch;
        }

        return retStringBuf.toString();
    }

    private boolean okToStartOrStopRecordingString(String tagToLookFor, String currentStartingTagInFile) {
        return (tagToLookFor.contains("transactionResponse") &&
                isTransactionResponse(currentStartingTagInFile.replaceAll("[</>]", "")))
                || tagToLookFor.equalsIgnoreCase(currentStartingTagInFile);
    }

    /**
     * Determines whether the given xml tag name corresponds to a transaction response
     *
     * @param tagName the tag name in the xml to check
     * @return whether or not tagName has a corresponding transaction response class in Java
     */
    static boolean isTransactionResponse(String tagName) {
        // First, we need to check to make sure the tag we are dealing with is actually a response
        if (!Pattern.matches("\\w+Response", tagName)) return false;

        // Converts a response name based on the xml tag into its full class name
        // Example: authorizationResponse becomes com.cnp.sdk.generate.AuthorizationResponse
        String responseClassName = GENERATE_PACKAGE_NAME + "." +
                tagName.substring(0, 1).toUpperCase() + tagName.substring(1);
        Class responseClass;
        try {
            responseClass = Class.forName(responseClassName);
        }
        catch (ClassNotFoundException e) {
            return false;
        }

        // As getInterfaces() only returns the immediate implemented interfaces, we have to perform a BFS
        //     with any super classes as well to determine if we implement CnpTransactionInterface
        LinkedList<Class> queue = new LinkedList<>(Collections.singleton(responseClass));
        while (!queue.isEmpty()) {
            Class clazz = queue.poll();

            Set<Class> supers = new HashSet<>(Arrays.asList(clazz.getInterfaces()));
            if (clazz.getSuperclass() != null) {
                supers.add(clazz.getSuperclass());
            }

            boolean isTransaction = supers.contains(CnpTransactionInterface.class);
            if (isTransaction) return true;

            queue.addAll(supers);
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
        if (reader != null) {
            reader.close();
        }
        if (buffer != null) {
            buffer.close();
        }
    }
}
