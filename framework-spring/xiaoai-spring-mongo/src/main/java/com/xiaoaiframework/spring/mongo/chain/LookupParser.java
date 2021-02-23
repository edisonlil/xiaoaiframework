package com.xiaoaiframework.spring.mongo.chain;

import com.xiaoaiframework.spring.mongo.annotation.Join;
import com.xiaoaiframework.spring.mongo.context.AggregateSelectContext;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Component;


@Component
public class LookupParser extends ConditionParser  {

    @Override
    public void parsing(MongoContext context) {


        if(context instanceof AggregateSelectContext){

            AggregateSelectContext aggregateSelectContext = (AggregateSelectContext) context;

            Join join = context.getMethod().getAnnotation(Join.class);

            LookupOperation lookupOperation = LookupOperation.newLookup()
                    .from(join.slave().getSimpleName())
                    .localField(join.localField())
                    .foreignField(join.foreignField())
                    .as(join.as());
            aggregateSelectContext.addOperation(lookupOperation);
        }


    }
}
