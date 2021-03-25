package com.xiaoaiframework.spring.rabbitmq.util;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoaiframework.core.base.PageResultBean;
import com.xiaoaiframework.core.base.ResultBean;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @program: framework
 * @author: edison
 * @create: 2019-12-11 15:02
 */
public class ParamsConverter {

    public static Object convertReturnType(Method method, JSONObject result) {
        if(result != null) {
            if(method.getReturnType() == ResultBean.class) {
                ResultBean resultBean = JSON.parseObject(result.toJSONString(), ResultBean.class);
                if(method.getGenericReturnType() instanceof  ParameterizedType){
                    Type[] resultGenericType = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
                    Object resultData = resultBean.getData();
                    if (resultData != null) {
                        if(resultGenericType[0].getTypeName().equals("java.lang.String")){
                            resultBean.setData(resultData.toString());
                        }else{
                            resultBean.setData(JSON.parseObject(resultData.toString(), resultGenericType[0]));
                        }

                    }
                }
                return resultBean;
            }
            if(method.getReturnType() == PageResultBean.class) {
                PageResultBean pageResultBean = JSON.parseObject(result.toJSONString(), PageResultBean.class);
                if(method.getGenericReturnType() instanceof  ParameterizedType) {
                    Type[] resultGenericType = ((ParameterizedType)method.getGenericReturnType() ).getActualTypeArguments();

                    Collection collection = pageResultBean.getData();
                    if (collection != null && collection.size() > 0) {
                        //保证集合还是原有的类型
                        Collection ret = CollUtil.create(pageResultBean.getData().getClass());
                        for (Object item : collection) {
                            if(resultGenericType[0].getTypeName().equals("java.lang.String")){
                                ret.add(item.toString());
                            }else{
                                ret.add(JSON.parseObject(item.toString(), resultGenericType[0]));
                            }
                        }
                        pageResultBean.setData(ret);
                    }
                }
                return pageResultBean;
            }
        }
        return null;
    }


}