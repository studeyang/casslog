package com.github.open.casslog.autoconfigure;

import com.github.open.casslog.accesslog.AccessLogConfigExtend;
import com.github.open.casslog.accesslog.reactive.AccessLogWebFilter;
import com.github.open.casslog.accesslog.servlet.AccessLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "casslog.accessLogEnabled")
public class AccessLogAutoConfiguration {

    @Bean
    public AccessLogConfigExtend accessLogConfigExtend() {
        return new AccessLogConfigExtend();
    }

    @Configuration
    @ConditionalOnClass(javax.servlet.ServletRequest.class)
    static class ServletAccessLogFilterConfiguration {
        @Bean
        public AccessLogFilter accessLogFilter() {
            return new AccessLogFilter();
        }
    }

    @Configuration
    @ConditionalOnClass(org.springframework.web.reactive.DispatcherHandler.class)
    static class AccessLogWebFilterConfiguration {
        @Bean
        public AccessLogWebFilter accessLogWebFilter() {
            return new AccessLogWebFilter();
        }
    }

}
