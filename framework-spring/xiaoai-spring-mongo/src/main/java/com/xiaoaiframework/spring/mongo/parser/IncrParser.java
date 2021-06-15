package com.xiaoaiframework.spring.mongo.parser;

import com.xiaoaiframework.spring.mongo.annotation.IncrCount;
import com.xiaoaiframework.spring.mongo.annotation.Set;
import com.xiaoaiframework.spring.mongo.annotation.action.Incr;
import com.xiaoaiframework.spring.mongo.annotation.action.Join;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.UpdateContext;
import com.xiaoaiframework.util.base.ObjectUtil;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class IncrParser implements OperationParser {



    @Override
    public void parsing(MongoContext context) {

        Object[] objects = context.getObjects();


        Incr incr = context.getMethod().getAnnotation(Incr.class);
        if(incr == null){
            return;
        }

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();
        Number incrCount = incr.incrCount();
        for (int i = 0; i < objects.length; i++) {
            Annotation annotation = annotations[i][0];
            if (annotation instanceof IncrCount && ObjectUtil.isNumber(objects[i])) {
                incrCount = (Number) objects[i];
                break;
            }
        }

        UpdateContext updateContext = (UpdateContext) context;
        updateContext.setUpdate( new Update().inc(incr.name(),incrCount));
        
    }
}
