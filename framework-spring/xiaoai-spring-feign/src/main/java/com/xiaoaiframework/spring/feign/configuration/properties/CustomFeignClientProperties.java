package com.xiaoaiframework.spring.feign.configuration.properties;

import com.xiaoaiframework.spring.feign.FeignDefinition;
import com.xiaoaiframework.spring.feign.MultiFeignDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;


@ConfigurationProperties("feign.custom.client")
public class CustomFeignClientProperties {

    List<MultiFeignDefinition> multiDefinition;

    List<FeignDefinition> definition;

    public List<FeignDefinition> getDefinition() {
        return definition;
    }

    public void setDefinition(List<FeignDefinition> definition) {
        this.definition = definition;
    }


    public List<MultiFeignDefinition> getMultiDefinition() {
        return multiDefinition;
    }

    public void setMultiDefinition(List<MultiFeignDefinition> multiDefinition) {
        this.multiDefinition = multiDefinition;
    }
}
