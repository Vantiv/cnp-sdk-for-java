package io.github.vantiv.sdk;

public class RequestTarget {

    private String targetUrl = null;
    private int urlIndex = 0;
    private long requestTime = System.currentTimeMillis();
    
    public RequestTarget(String url, int index) {
        targetUrl = url;
        urlIndex = index;
    }
    public String getUrl() {
        return targetUrl;
    }
    public long getRequestTime() {
        return requestTime;
    }
    public int getUrlIndex() {
        return urlIndex;
    }
}
