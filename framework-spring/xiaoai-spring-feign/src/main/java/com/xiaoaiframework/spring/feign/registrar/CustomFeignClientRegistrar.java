package com.xiaoaiframework.spring.feign.registrar;

import com.xiaoaiframework.spring.feign.FeignDefinition;
import com.xiaoaiframework.util.base.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 自定义FeignClient注册器
 * @author edison
 */
public class CustomFeignClientRegistrar implements EnvironmentAware, ResourceLoaderAware, BeanFactoryAware {

    ResourceLoader resourceLoader;

    Environment environment;

    BeanFactory beanFactory;

    public void register(FeignDefinition feignDefinition){

        validate(feignDefinition);
        registerClientConfiguration((BeanDefinitionRegistry) beanFactory,feignDefinition.getName(),feignDefinition.getConfiguration());
        registerFeignClient((BeanDefinitionRegistry) beanFactory, feignDefinition);

    }

    private void registerFeignClient(BeanDefinitionRegistry registry,FeignDefinition feignDefinition){

        Class feignClientFactoryBeanClass = getFeignClientFactoryBeanClass();

        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(feignClientFactoryBeanClass);

        definition.addPropertyValue("url", resolve(feignDefinition.getUrl()));
        definition.addPropertyValue("path", getPath(feignDefinition.getPath()));
        definition.addPropertyValue("name", feignDefinition.getName());
        definition.addPropertyValue("contextId", feignDefinition.getName());
        definition.addPropertyValue("type", feignDefinition.getClient());
        definition.addPropertyValue("decode404", feignDefinition.isDecode404());
        definition.addPropertyValue("fallback", feignDefinition.getFallback());
        definition.addPropertyValue("fallbackFactory", feignDefinition.getFallbackFactory());
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String alias = feignDefinition.getName() + "FeignClient";
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        // has a default, won't be
        boolean primary = feignDefinition.isPrimary();
        beanDefinition.setPrimary(primary);
        String qualifier = feignDefinition.getQualifier();
        if (StringUtils.hasText(qualifier)) {
            alias = qualifier;
        }
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,feignDefinition.getName(),
                new String[] { alias });
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name,
                                             Object configuration) {

        Class feignClientSpecificationClass = getFeignClientSpecification();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(feignClientSpecificationClass);
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(configuration);

        registry.registerBeanDefinition(
                name + "." + feignClientSpecificationClass.getSimpleName(),
                builder.getBeanDefinition());
    }

    static String getPath(String path) {
        if (StringUtils.hasText(path)) {
            path = path.trim();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }
    
    private Class getFeignClientSpecification(){
        return ReflectUtil.forName("org.springframework.cloud.openfeign.FeignClientSpecification");
    }
    private Class getFeignClientFactoryBeanClass(){
        return  ReflectUtil.forName("org.springframework.cloud.openfeign.FeignClientFactoryBean");
    }

    private void validate(FeignDefinition feignDefinition) {
        validateFallback(feignDefinition.getFallback());
    }
    /**
     * 校验fallback
     * @param c
     */
    static void validateFallback(final Class c) {
        Assert.isTrue(!c.isInterface(),
                "Fallback class must implement the interface annotated by @FeignClient");
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
