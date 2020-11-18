package com.xiaoaiframework.spring.log4j2.autoconfigure;


import com.xiaoaiframework.spring.log4j2.LogInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE -10)
public class LoggingAutoConfiguration {
    /**
     * 配置loginterceptor
     * @return
     */
    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }
}
