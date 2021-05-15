package com;

import com.xiaoaiframework.spring.mongo.annotation.Eq;
import com.xiaoaiframework.spring.mongo.constant.ActionType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Test {

    public void find(@Eq(name = "name") String name,@Eq(name = "id") Integer id){
    }

    public static void main(String[] args) throws NoSuchMethodException {

        Object[] objects = new Object[]{"1",1123};

        //判斷方法是什麽類型的
        Method method = Test.class.getMethod("find"
                ,new Class[]{String.class,Integer.class});


        String methodName = method.getName();

        if(methodName.startsWith("find")){

            Query query = new Query();

            Criteria criteria = new Criteria();

            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < objects.length; i++) {

                Annotation annotation = annotations[i][0];
                if(annotation instanceof Eq){
                    Eq eq = (Eq) annotation;
                    if(eq.action() == ActionType.AND){
                        String filedName = eq.name();
                        criteria.and(filedName).is(objects[i]);
                    }
                }
            }

        }


//        try {
//            for (Annotation[] finds : Test.class.getMethod("find"
//                    ,new Class[]{String.class,Integer.class}).getParameterAnnotations()) {
//
//                System.out.println(finds);
//
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }

        /*
          [0][1]
          [[0,1],[0,1]]
         */

    }
}
