package com.github.open.casslog.accesslog.servlet;

import com.github.open.casslog.accesslog.PathMatcherUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.LOWEST_PRECEDENCE - 5)
public class AccessLogFilter extends GenericFilterBean {

    private final ServletAccessLogBuilder builder = new ServletAccessLogBuilder();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        long start = System.currentTimeMillis();

        chain.doFilter(servletRequest, servletResponse);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (PathMatcherUtil.ignoredPath(request.getRequestURI())) {
            return;
        }

        builder.build(request, response, start).write();

    }

}
