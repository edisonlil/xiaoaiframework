package com.xiaoaiframework.spring.mongo.autoconfigure;

import com.xiaoaiframework.spring.mongo.executor.Executor;
import com.xiaoaiframework.spring.mongo.executor.SimpleExecutor;
import org.springframework.context.annotation.Bean;

/**
 * mongo自動化配置
 * @author edison
 */
public class MongoAutoConfiguration {

    @Bean
    public Executor executor(){
        return new SimpleExecutor();
    }

}
