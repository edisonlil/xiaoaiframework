package com.xiaoaiframework.spring.mongo.autoconfigure;

import com.xiaoaiframework.spring.mongo.execute.MongoExecute;
import com.xiaoaiframework.spring.mongo.service.ConvertService;
import com.xiaoaiframework.spring.mongo.service.SelectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

    @Bean
    public ConvertService convertService(){
        return new ConvertService();
    }

    @Bean
    public SelectService selectService(){
        return new SelectService();
    }
}
