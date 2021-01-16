package com.xiaoaiframework.util.coll;

import com.xiaoaiframework.util.math.RandomUtil;

import java.util.ArrayList;

/**
 * 蓄水池抽样算法实现
 * @author edison
 */
public class SamplingList<E> extends ArrayList<E> {

    /**
     * 抽样数限制
     */
    int limit;


    public SamplingList(int limit){
        super(limit);
        this.limit = limit;
    }


    @Override
    public boolean add(E e) {

        if(size() == limit){
            int random = RandomUtil.randomInt(limit+1);
            if(random <= limit){
                set(random,e);
            }
        }else{
            return super.add(e);
        }
        return true;
    }
}
