package com.xiaoaiframework.spring.feign;

public class FeignDefinition {

   private String name = "FeignClient";

   private String qualifier = "";

   private String url = "";

   private boolean decode404 = false;

   private Class<?>[] configuration = {};

   private Class<?> client = void.class;

   private Class<?> fallback = void.class;

   private Class<?> fallbackFactory = void.class;

   private String path = "";

   private boolean primary = true;


   public String getName() {
      return name;
   }

   public String getQualifier() {
      return qualifier;
   }

   public String getUrl() {
      return url;
   }

   public boolean isDecode404() {
      return decode404;
   }

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

   public String getPath() {
      return path;
   }

   public boolean isPrimary() {
      return primary;
   }

   public void setName(String name) {
      this.name = name;
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

   public void setConfiguration(Class<?>[] configuration) {
      this.configuration = configuration;
   }

   public void setClient(Class<?> client) {
      this.client = client;
   }

   public void setFallback(Class<?> fallback) {
      this.fallback = fallback;
   }

   public void setFallbackFactory(Class<?> fallbackFactory) {
      this.fallbackFactory = fallbackFactory;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void setPrimary(boolean primary) {
      this.primary = primary;
   }


}
