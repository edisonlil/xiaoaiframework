package com.xiaoaifreamwork.util.base;

/**
 * 字符串工具类
 * @author edison
 * @version 1.0.0
 */
public class StrUtil {

    private StrUtil() {
        throw new AssertionError("utility class must not be instantiated");
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String upperFirst(String str) {
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String lowerFirst(String str) {
        char[] cs=str.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);
    }

    /**
     * 是否为空字符串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){

        if(str == null){
            return true;
        }
        return str.trim().isEmpty();
    }
}
