package com.xiaoaifreamwork.data.redis.kit;

import com.framework.infrastructure.redis.kit.base.RedisKit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * @author edison
 */
public class RedisZSetKit extends RedisKit {

    ZSetOperations operations;

    public RedisZSetKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
        operations = redisTemplate.opsForZSet();
    }


    /**
     * 添加一个value到指定key的排序集中。
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean add(String key, Object value, double score){
        return operations.add(key,value,score);
    }


    /**
     * 查询指定排序集中的value排名
     * 其中有序成员按score值递增(从小到大)顺序排序。排名以0为底，也就是说，score值最小的成员排名为0.
     * @param key
     * @param value
     * @return
     */
    public Long rank(String key, Object value){
        return operations.rank(key,value);
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * @param key
     * @param start 起始
     * @param end 结束
     * @return
     */
    public <T> Set<T> range(String key, long start, long end){
        return operations.range(key,start,end);
    }


    /**
     * 查询指定排序集中的value排名
     * 其中有序成员按score值递增(从大到小)顺序排序。排名以0为底，也就是说，score值最大的成员排名为0.
     * @param key
     * @param value
     * @return
     */
    public Long revRank(String key, Object value){
        return operations.reverseRank(key,value);
    }

    /**
     * 获取指定排序集的大小
     * @param key
     * @return
     */
    public Long card(String key){
        return operations.zCard(key);
    }

    /**
     * 获取指定排序集指定value的分数
     * @param key
     * @param value
     * @return
     */
    public Double score(String key, Object value){
        return operations.score(key, value);
    }


    /**
     * 增量分数
     * @param key
     * @param value
     * @return
     */
    public Double incrScore(String key, Object value, double score){
        return operations.incrementScore(key, value,score);
    }


    /**
     * 返回有序集 key 中,score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
     * @param key
     * @param min 分数值
     * @param max 分数值
     * @return
     */
    public Long count(String key, double min, double max){
        return operations.count(key,min,max);
    }



}
