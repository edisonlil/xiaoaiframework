package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.context.MongoContext;



/**
 * @author edison
 */
public interface OperationParser {

   void parsing(MongoContext context);

}
