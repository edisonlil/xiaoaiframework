package com.xiaoaifreamwork.util.bean;

import com.xiaoaiframework.util.bean.BeanUtil;
import com.xiaoaiframework.util.base.ReflectUtil;
import org.junit.Test;

/**
 * 测试bean工具类
 * @author lee
 */
public class BeanUtilTest {

    /**
     * bean转map
     */
    @Test
    public void beanToMap(){

        Object car = new Object(){
           public String id;
        };
        ReflectUtil.setFieldValue(car,"id","abc");
        System.out.println(BeanUtil.beanToMap(car));

    }

}
