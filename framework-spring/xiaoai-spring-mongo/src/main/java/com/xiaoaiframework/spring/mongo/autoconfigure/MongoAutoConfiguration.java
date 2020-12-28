package com.xiaoaiframework.spring.mongo.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * mongo自動化配置
 * @author edison
 */
@ComponentScan("com.xiaoaiframework.spring.mongo.**.*")
@Import(MongoMappingScannerRegistrar.class)
public class MongoAutoConfiguration {

}
