package com.xiaoaiframework.spring.mongo.parser;

import com.xiaoaiframework.spring.mongo.annotation.action.Join;
import com.xiaoaiframework.spring.mongo.context.AggregateContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Component;


/**
 * @author edison
 */
@Component
public class LookupParser  implements OperationParser  {

    @Override
    public void parsing(MongoContext context) {


        if(context instanceof AggregateContext){

            AggregateContext aggregateContext = (AggregateContext) context;

            Join join = context.getMethod().getAnnotation(Join.class);

            LookupOperation lookupOperation = LookupOperation.newLookup()
                    .from(join.slave().getSimpleName())
                    .localField(join.localField())
                    .foreignField(join.foreignField())
                    .as(join.as());
            aggregateContext.addOperation(lookupOperation);
        }


    }
}
