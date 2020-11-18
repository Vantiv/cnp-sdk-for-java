package com.cnp.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cnp.sdk.generate.CnpTransactionInterface;

/**
 * Parses a batch response file by exposing a getNextTag and getNextTransactionResponse
 * In an ideal world, we would just parse an entire file at once into the associated objects using the xml library
 * However, batch files can get extremely large, so for efficiency, it is best to parse them bit by bit with a buffer
 */
public class ResponseFileParser2 {
    private static final String TRANSACTION_RESPONSE_REGEX = "<(\\w+Response)>.*?</\\1>";
    private static final String GENERATE_PACKAGE_NAME = "com.cnp.sdk.generate";
    private static final String BATCH_RESPONSE_FILE_ENCODING = "UTF-8";

    private Map<String, Matcher> tagMap = new HashMap<>();
    private Matcher transactionResponseMatcher;
    private CharSequence bufferedFile;

    /**
     * Creates a response file parser based off of the given batch response file
     *
     * @param responseFile the batch response file to parse
     * @throws IOException in case any file-handling issues occur
     */
    public ResponseFileParser2(File responseFile) throws IOException {
        FileInputStream is = new FileInputStream(responseFile);
        FileChannel channel = is.getChannel();
        ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        bufferedFile = Charset.forName(BATCH_RESPONSE_FILE_ENCODING).newDecoder().decode(byteBuffer);
        channel.close();
        is.close();
        transactionResponseMatcher = Pattern.compile(TRANSACTION_RESPONSE_REGEX, Pattern.DOTALL).matcher(bufferedFile);
    }

    /**
     * In short, this method gets the next tag of the specified name
     * If the tag is a cnpResponse or batchResponse, the insides of the tag are left out for performance reasons
     * Otherwise, the full tag and all of its data are returned as xml
     *
     * @param tagToLookFor the tag to search for in the xml response
     * @return the xml of the next instance of tagToLookFor found
     */
    public String getNextTag(String tagToLookFor) {
        // Remove the (inside) xml contents of tags known to have very large children for performance
        boolean removeXmlContents = tagToLookFor.equals("cnpResponse") || tagToLookFor.equals("batchResponse");
        if (!tagMap.containsKey(tagToLookFor)) {
            // Regex that first tries to find <tagToLookFor ... /> but if it can't,
            //     resorts to searching for <tagToLookFor ...></tagToLookFor>
            String regex = "<(" + tagToLookFor + ")(([^<>]*?/>)|(.*?</\\1>))";
            if (removeXmlContents) {
                // Handle the special 2 cases where we don't want the insides of the xml for large responses
                // This regex only matches the starting tag, but nothing inside or the closing tag
                regex = "<" + tagToLookFor + ".*?>";
            }
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            tagMap.put(tagToLookFor, pattern.matcher(bufferedFile));
        }

        Matcher matcher = tagMap.get(tagToLookFor);
        if (matcher.find()) {
            // If the regex removed the contents & closing tag, add it back manually here, else return the entire match
            return removeXmlContents ? matcher.group() + "</" + tagToLookFor + ">" : matcher.group();
        }
        throw new CnpBatchException("No more " + tagToLookFor + " tags left");
    }

    /**
     * Gets the next transaction response in the batch response file
     *
     * @return the xml of the next transaction response in the batch response file
     */
    public String getNextTransactionResponse() {
        while (transactionResponseMatcher.find()) {
            String responseXml = transactionResponseMatcher.group();
            String responseName = transactionResponseMatcher.group(1);
            // Converts a response name based on the xml tag into its full class name
            // Example: authorizationResponse becomes com.cnp.sdk.generate.AuthorizationResponse
            String responseClassName = GENERATE_PACKAGE_NAME + "." +
                    responseName.substring(0, 1).toUpperCase() + responseName.substring(1);
            try {
                Class clazz = Class.forName(responseClassName);
                boolean isTransaction = Arrays.asList(clazz.getInterfaces()).contains(CnpTransactionInterface.class);
                if (isTransaction) {
                    return responseXml;
                }
            }
            catch (ClassNotFoundException ignored) { // no generated class for tag; ignore and continue to next match
            }
        }
        throw new CnpBatchException("No more transaction responses found");
    }
}
