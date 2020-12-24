package com.xiaoaiframework.spring.mongo.autoconfigure;

import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.spring.mongo.executor.SimpleExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * mongo自動化配置
 * @author edison
 */
@ComponentScan("com.xiaoaiframework.spring.mongo.**.*")
@Import(MongoMappingScannerRegistrar.class)
public class MongoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Executor.class)
    public Executor executor(){
        return new SimpleExecutor();
    }

}
