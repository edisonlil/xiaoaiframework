package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.parser.OperationParser;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author edison
 */
@Component
public class ConditionParserChain implements OperationParserChain {

    List<OperationParser> parsers;


    @Autowired
    public void setParsers(ObjectFactory<List<OperationParser>> factory) {
        this.parsers = factory.getObject();
    }

    @Override
    public void doParsing(MongoContext context) {

        for (OperationParser parser : parsers) {
            parser.parsing(context);
        }
    }


}
