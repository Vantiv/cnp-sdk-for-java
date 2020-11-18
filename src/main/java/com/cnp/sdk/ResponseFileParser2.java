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
    private static String TRANSACTION_RESPONSE_REGEX = "<(\\w+Response)>.*?</\\1>";
    private static String GENERATE_PACKAGE_NAME = "com.cnp.sdk.generate";

    private Map<String, Matcher> tagMap = new HashMap<>();
    private Matcher transactionResponseMatcher;
    private CharSequence bufferedFile;

    /**
     * Creates a response file parser based off of the given batch response file
     * @param responseFile the batch response file to parse
     * @throws IOException in case any file-handling issues occur
     */
    public ResponseFileParser2(File responseFile) throws IOException {
        FileInputStream is = new FileInputStream(responseFile);
        FileChannel channel = is.getChannel();
        ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        bufferedFile = Charset.forName("UTF-8").newDecoder().decode(byteBuffer);
        channel.close();
        is.close();
        transactionResponseMatcher = Pattern.compile(TRANSACTION_RESPONSE_REGEX, Pattern.DOTALL).matcher(bufferedFile);
    }

    /**
     * In short, this method gets the next tag of the specified name
     * If the tag is a cnpResponse or batchResponse, the insides of the tag are left out for performance reasons
     * Otherwise, the full tag and all of its data are returned as xml
     * @param tagToLookFor the tag to search for in the xml response
     * @return the xml of the next instance of tagToLookFor found
     */
    public String getNextTag(String tagToLookFor) {
        if (!tagMap.containsKey(tagToLookFor)) {
            // Regex that first tries to find <tagToLookFor ... /> but if it can't,
            //     resorts to searching for <tagToLookFor ...></tagToLookFor>
            Pattern pattern = Pattern.compile("<(" + tagToLookFor + ")(([^<>]*?/>)|(.*?</\\1>))", Pattern.DOTALL);
            tagMap.put(tagToLookFor, pattern.matcher(bufferedFile));
        }

        // todo cnpResponse and batchResponse should return just the tags (no insides), probs for performance

        Matcher matcher = tagMap.get(tagToLookFor);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new CnpBatchException("No more " + tagToLookFor + " tags left");
    }

    /**
     * Gets the next transaction response in the batch response file
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
            catch (ClassNotFoundException ignored) {
            }
        }
        throw new CnpBatchException("No more transaction responses found");
    }
}
