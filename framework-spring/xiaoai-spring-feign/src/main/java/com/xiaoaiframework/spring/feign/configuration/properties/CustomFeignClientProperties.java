package com.xiaoaiframework.spring.feign.configuration.properties;

import com.xiaoaiframework.spring.feign.FeignDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties("feign.custom.client")
public class CustomFeignClientProperties {

    List<FeignDefinition> definition;

    public List<FeignDefinition> getDefinition() {
        return definition;
    }

    public void setDefinition(List<FeignDefinition> definition) {
        this.definition = definition;
    }
}
