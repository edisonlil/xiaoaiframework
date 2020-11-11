package com.xiaoaifreamwork.util.base;


import com.xiaoaiframework.util.base.StrUtil;
import org.junit.Test;

/**
 * 字符工具测试用例
 * @author lee
 */
public class StrUtilTest {

    /**
     * 首字母转大写
     */
    @Test
    public void upperFirst(){
       System.out.println(StrUtil.upperFirst("adc"));
    }

    /**
     * 首字母转小写
     */
    @Test
    public void lowerFirst(){
        System.out.println(StrUtil.lowerFirst("ABC"));
    }

    /**
     * 去掉前缀
     */
    @Test
    public void removePrefix(){
        String[] prefix = {"pre_","_log"};
        System.out.println(StrUtil.removePrefix("pre_name",prefix));
    }

    /**
     * 去掉后缀
     */
    @Test
    public void removeSuffix(){
        String fileName = "suffix.log";
        System.out.println(StrUtil.removeSuffix(fileName));
    }
}
