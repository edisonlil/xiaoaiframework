package com.xiaoaiframework.util.math;

/**
 * 数学计算基础工具类
 * @author edison
 */
public class MathUtil {


    /**
     * 计算count的n次方
     * @param count
     * @param n
     * @return
     */
    public static double pow(double count,double n){
        return Math.pow(count, n);
    }


    public static long floorDiv(long x, int y) {
        return floorDiv(x, (long)y);
    }

    public static long floorDiv(long x, long y) {
        long r = x / y;
        if ((x ^ y) < 0L && r * y != x) {
            --r;
        }

        return r;
    }
}
