package com.xiaoaifreamwork.data.redis.kit;

import com.framework.infrastructure.redis.kit.base.RedisKit;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author edsion
 */
public class RedisListKit extends RedisKit {

    ListOperations operations;

    public RedisListKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
        operations = redisTemplate.opsForList();
    }


    /**
     * 将value追加在list的尾部
     *
     * @param key
     * @param value
     * @return 返回新增元素下标
     */
    public Long rightPush(String key, Object value) {

        return operations.rightPush(key, value);

    }

    /**
     * 将value追加在list的尾部(并设置过期时间)
     *
     * @param key
     * @param value
     * @return 返回新增元素下标
     */
    public Long rightPush(String key, Object value, int expireTime) {

        if (expireTime < 0) {
            return -1L;
        }
        Long count = operations.rightPush(key, value);
        expire(key, expireTime);
        return count;
    }


    /**
     * 批量将values内的子元素追加在list的尾部(并设置过期时间)
     *
     * @param key
     * @param values
     * @return 返回最后元素的下标
     */
    public Long rightPushAll(String key, List values, int expireTime) {

        if (expireTime < 0) {
            return -1L;
        }
        Long count = operations.rightPushAll(key, values);
        expire(key, expireTime);
        return count;

    }

    /**
     * 批量将values内的子元素追加在list的尾部
     *
     * @param key
     * @param values
     * @return 返回最后元素的下标
     */
    public Long rightPushAll(String key, List values) {
        return operations.rightPushAll(key, values);
    }

    /**
     * 删除并返回存储在中的list中的第一个元素。
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T leftPop(String key) {
        return (T) operations.leftPop(key);
    }

    /**
     * 删除并返回存储在中的list中的最后一个元素。
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T rightPop(String key) {
        return (T) operations.rightPop(key);
    }

    /**
     * 返回指定list的长度
     *
     * @param key
     * @return
     */
    public Long size(String key) {
        return operations.size(key);
    }

    /**
     * 返回指定下标的元素
     *
     * @param key
     * @param index
     * @param <T>
     * @return
     */
    public <T> T index(String key, int index) {
        return (T) operations.index(key, index);
    }


    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置 0 到 -1 代表获取所有
     * @return
     */
    public <T> List<T> get(String key, long start, long end) {
        return (List<T>) operations.range(key, start, end);
    }


}
