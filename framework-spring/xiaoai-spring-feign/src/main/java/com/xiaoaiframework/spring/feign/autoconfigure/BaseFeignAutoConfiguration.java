package com.xiaoaiframework.spring.feign.autoconfigure;

import com.xiaoaiframework.spring.feign.configuration.properties.CustomFeignClientProperties;
import com.xiaoaiframework.spring.feign.converter.CustomMappingJackson2HttpMessageConverter;
import com.xiaoaiframework.spring.feign.processor.CustomFeignClientPropertiesPostProcessor;
import com.xiaoaiframework.spring.feign.registrar.CustomFeignClientRegistrar;
import feign.Logger;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(CustomFeignClientProperties.class)
public class BaseFeignAutoConfiguration {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public static CustomFeignClientRegistrar customFeignClientRegistrar(){
        return new CustomFeignClientRegistrar();
    }

    @Bean
    public static CustomFeignClientPropertiesPostProcessor customFeignClientPropertiesPostProcessor(){
        return new CustomFeignClientPropertiesPostProcessor();
    }


    /**
     * 兼容feign接受到字符串的json时不能转为对象
     * @return
     */
    @Bean
    public Decoder feignDecoder() {

        ObjectFactory<HttpMessageConverters> objectFactory = () ->
                new HttpMessageConverters(new CustomMappingJackson2HttpMessageConverter());
        return new SpringDecoder(objectFactory);

    }
}
