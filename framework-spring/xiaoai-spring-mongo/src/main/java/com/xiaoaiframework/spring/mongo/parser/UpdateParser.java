package com.xiaoaiframework.spring.mongo.parser;

import com.xiaoaiframework.spring.mongo.annotation.Set;
import com.xiaoaiframework.spring.mongo.context.MongoContext;
import com.xiaoaiframework.spring.mongo.context.UpdateContext;
import com.xiaoaiframework.util.base.ObjectUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import com.xiaoaiframework.util.coll.CollUtil;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class UpdateParser implements OperationParser {

    
    @Override
    public void parsing(MongoContext context) {

        if(!(context instanceof UpdateContext)){return;}

        Object[] objects = context.getObjects();

        Annotation[][] annotations = context.getMethod().getParameterAnnotations();
        Object update = null;
        for (int i = 0; i < objects.length; i++) {
            Annotation annotation = annotations[i][0];
            if (annotation instanceof Set) {
                update = objects[i];
                break;
            }
        }

        if (update == null) { return; }

        UpdateContext updateContext = (UpdateContext) context;
        updateContext.setUpdate(convertUpdate(update));

    }


    public static Update convertUpdate(Object object){
        return convertUpdate(object,null);
    }

    /**
     * 转换update参数
     * @param object
     * @return
     */
    public static Update convertUpdate(Object object, String... exclude){

        java.util.Set<String> excludeSet = null;
        if(exclude != null && exclude.length > 0){
            excludeSet = CollUtil.newHashSet(exclude);
        }

        Update update = new Update();
        Field[] fields = ReflectUtil.getDeclaredFields(object);
        try {
            for (Field field : fields) {

                String fieldName = field.getName();
                if(excludeSet == null || !excludeSet.contains(fieldName)){
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if(!ObjectUtil.isNull(value)){
                        update = update.set(fieldName,value);
                    }
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return update;
    }

}
