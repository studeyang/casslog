package com.github.open.casslog.accesslog.servlet;

import com.github.open.casslog.accesslog.AccessLog;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ServletAccessLogBuilder {

    public AccessLog build(HttpServletRequest request, HttpServletResponse response, long start) {
        AccessLog accessLog = new AccessLog();

        accessLog.setRequestMethod(request.getMethod());
        accessLog.setRequestUri(getRequestURI(request));
        accessLog.setQueryString(getQueryStr(request));
        accessLog.setProtocol(request.getProtocol());
        accessLog.setStatus(null == response ? "0" : String.valueOf(response.getStatus()));
        accessLog.setSize(getContentLength(response));

        accessLog.setAccessUser("");
        accessLog.setIpAddress("");
        accessLog.setReferer("");
        accessLog.setBrowser("");
        accessLog.setTokenExpiresIn("");
        accessLog.setActualServerId("");

        accessLog.setLatency((int) (System.currentTimeMillis() - start));

        return accessLog;
    }

    private String getRequestURI(HttpServletRequest request) {
        String requestPattern = getRequestAttribute(request,
                "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern",
                "");
        if (StringUtils.hasText(requestPattern) && !requestPattern.contains("*")) {
            return request.getContextPath() + requestPattern;
        } else {
            return request.getRequestURI();
        }
    }

    private String getQueryStr(HttpServletRequest request) {

        String queryString = request.getQueryString();
        if (queryString == null) {
            queryString = "";
        }

        Map<String, String> uriVariables = getRequestAttribute(request,
                "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables",
                null);

        if (null != uriVariables) {
            for (Map.Entry<String, String> entry : uriVariables.entrySet()) {
                if (StringUtils.hasText(queryString)) {
                    queryString = entry.getKey() + "=" + entry.getValue() + "&" + queryString;
                } else {
                    queryString = entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        return queryString;
    }


    private <T> T getRequestAttribute(HttpServletRequest request, String attrName, T defaultValue) {
        if (null != request.getAttribute(attrName)) {
            return (T) request.getAttribute(attrName);
        } else {
            return defaultValue;
        }
    }

    private int getContentLength(HttpServletResponse response) {
        int size = 0;
        if (null != response.getHeader(HttpHeaders.CONTENT_LENGTH)) {
            size = Integer.parseInt(response.getHeader(HttpHeaders.CONTENT_LENGTH));
        }
        return size;
    }
}
