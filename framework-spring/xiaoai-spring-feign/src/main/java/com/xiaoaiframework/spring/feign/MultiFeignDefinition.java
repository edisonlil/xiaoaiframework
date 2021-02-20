package com.xiaoaiframework.spring.feign;

import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.base.StrUtil;

import java.util.*;

public class MultiFeignDefinition {

    private String prefix = "";

    private String qualifier = "";

    private String url = "";

    private boolean decode404 = false;

    private String path = "";

    private boolean primary = true;

    List<Config> configs;



    public String getQualifier() {
        return qualifier;
    }

    public String getUrl() {
        return url;
    }

    public boolean isDecode404() {
        return decode404;
    }


    public String getPath() {
        return path;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDecode404(boolean decode404) {
        this.decode404 = decode404;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public static class Config{

        private Class<?>[] configuration = {};

        private Class<?> client = void.class;

        private Class<?> fallback = void.class;

        private Class<?> fallbackFactory = void.class;


        public Class<?>[] getConfiguration() {
            return configuration;
        }

        public Class<?> getClient() {
            return client;
        }

        public Class<?> getFallback() {
            return fallback;
        }

        public Class<?> getFallbackFactory() {
            return fallbackFactory;
        }


        public void setConfiguration(Class<?>[] configuration) {
            this.configuration = configuration;
        }

        public void setClient(Class<?> client) {
            this.client = client;
        }

        public void setClient(String client) {
            this.client = ReflectUtil.forName(client);
        }

        public void setFallback(Class<?> fallback) {
            this.fallback = fallback;
        }

        public void setFallback(String fallback) {
            this.fallback = ReflectUtil.forName(fallback);;
        }

        public void setFallbackFactory(Class<?> fallbackFactory) {
            this.fallbackFactory = fallbackFactory;
        }

        public void setFallbackFactory(String fallbackFactory) {
            this.fallbackFactory = ReflectUtil.forName(fallbackFactory);;
        }
    }


    public List<FeignDefinition> getFeignDefinitions(){


        List<FeignDefinition> list = new ArrayList<>(configs.size());
        for (Config config : configs) {
            FeignDefinition feignDefinition = new FeignDefinition();
            feignDefinition.setName(prefix+ config.client.getSimpleName());
            feignDefinition.setQualifier(qualifier);
            feignDefinition.setUrl(url);
            feignDefinition.setDecode404(decode404);
            feignDefinition.setPath(path);
            feignDefinition.setPrimary(primary);
            feignDefinition.setClient(config.client);
            feignDefinition.setConfiguration(config.configuration);
            feignDefinition.setFallback(config.fallback);
            feignDefinition.setFallbackFactory(config.fallbackFactory);
            list.add(feignDefinition);
        }


        return list;

    }


}
