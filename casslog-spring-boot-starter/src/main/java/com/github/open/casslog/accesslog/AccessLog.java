package com.github.open.casslog.accesslog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLog {
    public static final String ACCESSLOG_TOKEN_EXPIRESIN_KEY = "extend.accesslog.tokenExpiresIn";
    public static final String ACCESSLOG_ACTUAL_SERVERID_KEY = "extend.accesslog.actualServerId";
    private static Logger log = LoggerFactory.getLogger(AccessLog.class);
    private static final String LOG_FORMAT = "[%s] [%s] %s - \"%s %s %s %s\" $$%s$$ \"%s\" \"%s\" - %s, %s, %s, %s";
    private String accessId;
    private String ipAddress;
    private String accessUser;
    private String requestMethod;
    private String requestUri;
    private String protocol;
    private String status;
    private String queryString;
    private String referer;
    private String browser;
    private int size = 0;
    private int latency = 0;
    private String tokenExpiresIn;
    private String actualServerId;

    public AccessLog() {
        this.accessId = "";
    }

    public void write() {
        log.info("[{}] [{}] {} - \"{} {} {} {}\" $${}$$ \"{}\" \"{}\" - {}, {}, {}, {}",
                this.accessId, this.ipAddress, this.accessUser, this.requestMethod, this.requestUri,
                this.protocol, this.status, this.queryString, this.referer, this.browser,
                this.size, this.latency, this.tokenExpiresIn, this.actualServerId);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setAccessUser(String accessUser) {
        this.accessUser = accessUser;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public void setTokenExpiresIn(String tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public void setActualServerId(String actualServerId) {
        this.actualServerId = actualServerId;
    }
}
