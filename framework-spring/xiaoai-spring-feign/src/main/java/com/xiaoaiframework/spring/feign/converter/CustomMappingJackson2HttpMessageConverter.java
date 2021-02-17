package com.xiaoaiframework.spring.feign.converter;


import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 定制化 MappingJackson2HttpMessageConverter
 * 解决微信小程序返回的内容类型为 content-type: text/plain 时，无法使用json转换器时导致熔断的问题。
 * @author edison
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomMappingJackson2HttpMessageConverter(){

        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        mediaTypeList.add(new MediaType("application", "*+json"));
        setSupportedMediaTypes(mediaTypeList);
    }



}
