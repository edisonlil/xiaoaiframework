package com.xiaoaiframework.spring.xxl.job.autoconfigure;

import com.xiaoaiframework.spring.xxl.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {


    @Bean
    public XxlJobSpringExecutor xxlJob(XxlJobProperties xxlJobProperties){

        if (xxlJobProperties == null) {
            xxlJobProperties = XxlJobProperties.build();
        }

        XxlJobSpringExecutor xxlJobSpringExecutor
                = new XxlJobSpringExecutor();
        xxlJobProperties.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppName(xxlJobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogretentiondays());
        return xxlJobSpringExecutor;
    }


}