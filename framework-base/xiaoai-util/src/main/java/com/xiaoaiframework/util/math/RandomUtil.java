package com.xiaoaiframework.util.math;

import java.util.Random;

/**
 * 随机数
 * @author edison
 */
public class RandomUtil {

    static Random rand = new Random();

    private RandomUtil(){
    }


    /**
     * 返回下一个伪随机数，它是此随机数生成器序列中均匀分布的int值。
     * nextInt的一般约定是一个int值是伪随机生成并返回的。所有2 32个可能的int值都是（近似）相等的概率产生的。
     * nextInt方法由Random类实现，就像通过以下方式实现一样
     * @return
     */
    public static int randomInt(){
        return rand.nextInt();
    }

    /**
     * 返回 0 ~ max 的随机数
     * @param max
     * @return
     */
    public static int randomInt(int max){
        return rand.nextInt(max);
    }


    /**
     * 返回 min ~ max 的随机数
     * @param max
     * @return
     */
    public static int randomInt(int min,int max){
        return rand.nextInt(max)+min;
    }



}
