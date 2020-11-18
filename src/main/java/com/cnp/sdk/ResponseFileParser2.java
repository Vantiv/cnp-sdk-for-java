package com.cnp.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseFileParser2 {
    private Map<String, Matcher> tagMap = new HashMap<>();
    private CharSequence bufferedFile;

    public ResponseFileParser2(File responseFile) throws IOException {
        FileInputStream is = new FileInputStream(responseFile);
        FileChannel channel = is.getChannel();
        ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        bufferedFile = Charset.forName("UTF-8").newDecoder().decode(byteBuffer);
        channel.close();
        is.close();
    }

    public String getNextTag(String tagToLookFor) {
        if (!tagMap.containsKey(tagToLookFor)) {
            // Regex that first tries to find <tagToLookFor ... /> but if it can't,
            //     resorts to searching for <tagToLookFor ...></tagToLookFor>
            Pattern pattern = Pattern.compile("<" + tagToLookFor + "(([^<>]*?/>)|(.*?</" + tagToLookFor + ">))");
            tagMap.put(tagToLookFor, pattern.matcher(bufferedFile));
        }

        // todo cnpResponse and batchResponse should return just the tags (no insides), probs for performance
        // what about the transaction response stuff that is hard coded in the old version?
        // also, handle the case where transactionResponse passed as tagToLookFor should do any of the next transaction responses?

        Matcher matcher = tagMap.get(tagToLookFor);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new CnpBatchException("No more " + tagToLookFor + " tags left");
    }
}
