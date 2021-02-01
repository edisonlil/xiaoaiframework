package com.xiaoaiframework.spring.mongo.autoconfigure;

import com.xiaoaiframework.spring.mongo.MongoExecute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * mongo自動化配置
 * @author edison
 */
@ComponentScan("com.xiaoaiframework.spring.mongo.**.*")
@Import(MongoMappingScannerRegistrar.class)
public class MongoAutoConfiguration {


    @Bean
    public MongoExecute mongoExecute(MongoTemplate template){

        return new MongoExecute(template);

    }

}
