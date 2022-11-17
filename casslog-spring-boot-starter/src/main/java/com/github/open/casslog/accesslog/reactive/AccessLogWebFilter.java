package com.github.open.casslog.accesslog.reactive;


import com.github.open.casslog.accesslog.PathMatcherUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class AccessLogWebFilter implements WebFilter {

    private ServerAccessLogBuilder builder = new ServerAccessLogBuilder();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        long start = System.currentTimeMillis();

        return chain.filter(exchange).doFinally(signalType -> {

            ServerHttpRequest request = exchange.getRequest();
            if (PathMatcherUtil.ignoredPath(request.getPath().value())) {
                return;
            }

            builder.build(exchange, start).write();

        });
    }
}
