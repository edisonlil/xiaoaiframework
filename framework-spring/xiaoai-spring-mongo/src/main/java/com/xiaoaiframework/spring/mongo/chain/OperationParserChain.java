package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.context.MongoContext;


/**
 * 操作解析链
 * @author edison
 */
public interface OperationParserChain {

    void doParsing(MongoContext context);

}
