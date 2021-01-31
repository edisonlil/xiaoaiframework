package com.xiaoaiframework.spring.activemq.properties;

import com.xiaoaiframework.spring.activemq.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author edison
 */
@ConfigurationProperties(prefix = "activemq")
public class ActiveMQActivemqProperties {

    /**
     * 多数据源配置
     */
    List<DataSource> dataSources;


    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }
}
