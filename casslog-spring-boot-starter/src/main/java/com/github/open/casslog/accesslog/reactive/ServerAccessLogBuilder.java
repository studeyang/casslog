package com.github.open.casslog.accesslog.reactive;

import com.github.open.casslog.accesslog.AccessLog;
import org.springframework.http.server.reactive.AbstractServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.netty.http.HttpOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerAccessLogBuilder {

    public AccessLog build(ServerWebExchange exchange, long start) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        AccessLog accessLog = new AccessLog();

        accessLog.setRequestMethod(request.getMethodValue());
        accessLog.setRequestUri(getRequestURI(exchange));
        accessLog.setQueryString(getQueryStr(exchange));
        accessLog.setProtocol(getProtocolText(request));
        accessLog.setStatus(null == response.getStatusCode() ? "0" : String.valueOf(response.getStatusCode().value()));
        accessLog.setSize((int) response.getHeaders().getContentLength());

        accessLog.setAccessUser("");
        accessLog.setIpAddress("");
        accessLog.setReferer("");
        accessLog.setBrowser("");
        accessLog.setTokenExpiresIn("");
        accessLog.setActualServerId("");

        accessLog.setLatency((int) (System.currentTimeMillis() - start));

        return accessLog;
    }

    private String getRequestURI(ServerWebExchange exchange) {
        String requestPattern = getRequestAttribute(exchange,
                "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern",
                "");
        if (StringUtils.hasText(requestPattern) && !requestPattern.contains("*")) {
            return exchange.getRequest().getPath().contextPath() + requestPattern;
        } else {
            return exchange.getRequest().getPath().pathWithinApplication().value();
        }
    }

    private String getQueryStr(ServerWebExchange exchange) {

        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();


        Map<String, String> uriVariables = getRequestAttribute(exchange,
                "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables",
                null);

        if (null != uriVariables) {
            for (Map.Entry<String, String> entry : uriVariables.entrySet()) {
                List<String> list = new ArrayList<>();
                list.add(entry.getValue());
                params.put(entry.getKey(), list);
            }
        }

        List<String> varList = new ArrayList<>();
        for (String key : params.keySet()) {
            for (String value : params.get(key)) {
                varList.add(key + "=" + value);
            }
        }

        return String.join("&", varList);
    }


    private <T> T getRequestAttribute(ServerWebExchange exchange, String attrName, T defaultValue) {
        if (null != exchange.getAttribute(attrName)) {
            return exchange.getAttribute(attrName);
        } else {
            return defaultValue;
        }
    }

    private String getProtocolText(ServerHttpRequest request) {
        String text = "HTTP/--";
        if (request instanceof AbstractServerHttpRequest) {
            Object requestObject = ((AbstractServerHttpRequest) request).getNativeRequest();
            if (requestObject instanceof HttpOperations) {
                text = ((HttpOperations) requestObject).version().text();
            }
        }
        return text;
    }
}
