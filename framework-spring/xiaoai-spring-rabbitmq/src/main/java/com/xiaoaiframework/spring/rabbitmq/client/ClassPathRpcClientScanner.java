package com.xiaoaiframework.spring.rabbitmq.client;


import com.xiaoaiframework.spring.rabbitmq.annotation.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * 定义RpcClient类路径扫描器
 * @author edison
 */
public class ClassPathRpcClientScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathRpcClientScanner.class);

    public ClassPathRpcClientScanner(BeanDefinitionRegistry registry) {
        super(registry,false);
    }

    /**
     * 添加包含该注解的过滤器
     */
    void registerFilters(){
        addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        //扫描指定包路径
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        if(beanDefinitionHolders.isEmpty()){
            LOGGER.warn("No @RpcClient was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }else{
            processBeanDefinitions(beanDefinitionHolders);
        }
        return beanDefinitionHolders;
    }


    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitionHolders){

        GenericBeanDefinition rpcClientBeanDefinition;
        for (BeanDefinitionHolder holder : beanDefinitionHolders){
            rpcClientBeanDefinition = (GenericBeanDefinition)holder.getBeanDefinition();
            String beanClassName = rpcClientBeanDefinition.getBeanClassName();
            //获取真实接口class,并作为构造方法的参数
            rpcClientBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            rpcClientBeanDefinition.setBeanClass(RpcClientProxyFactory.class);
            //按照类型注入的方式
            rpcClientBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

}
