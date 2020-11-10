package com.xiaoaifreamwork.spring.redis.kit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author edison
 */
public class RedisStringKit extends RedisKit {

    ValueOperations operations;

    public RedisStringKit(RedisTemplate redisTemplate){
        super(redisTemplate);
        operations = redisTemplate.opsForValue();
    }

    /**
     * 添加string
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        operations.set(key, value);
    }

    /**
     * 添加string 设置过期时间
     * @param key
     * @param value
     * @param expireTime 秒
     */
    public void add(String key, Object value, int expireTime) {
        if (expireTime < 0) {
            return;
        }
        operations.set(key, value);
        expire(key, expireTime);
    }


    /**
     * 添加string 设置过期时间
     * @param key 键
     * @param value 值
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     */
    public void add(String key, Object value, int expireTime, TimeUnit timeUnit) {
        if (expireTime < 0) {
            return;
        }
        operations.set(key, value);
        expire(key,expireTime,timeUnit);
    }

    /**
     * 获取指定key的value
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        return (T) operations.get(key);
    }


    /**
     * 获取指定key前缀的所有value,并删除
     *
     * @param keyPrefix
     * @return
     */
    public <T> List<T> getAllAndDel(String keyPrefix) {

        List<String> keys = keys(keyPrefix + "*");
        List<String> keyDels = new ArrayList<>(KEY_DEL_BATCH);
        List<T> vlaues = new ArrayList<>(keys.size());
        for (Iterator<String> ite = keys.iterator(); ite.hasNext(); ) {
            String key = ite.next();
            vlaues.add(get(key));
            keyDels.add(key);
            if (keyDels.size() >= KEY_DEL_BATCH) {
                deleteBatch(keyDels);
                keyDels = new ArrayList<>(KEY_DEL_BATCH);
            }
            ite.remove();
        }
        deleteBatch(keyDels);
        return vlaues;
    }

    /**
     * 获取指定key前缀的所有value
     *
     * @param keyPrefix
     * @return
     */
    public <T> List<T> all(String keyPrefix) {

        List<String> keys = keys(keyPrefix + "*");
        List<T> vlaues = new ArrayList<>(keys.size());
        for (Iterator<String> ite = keys.iterator(); ite.hasNext(); ) {
            String key = ite.next();
            vlaues.add(get(key));
            ite.remove();
        }
        return vlaues;
    }

    /**
     * 删除指定key前缀的所有value
     * @param keyPrefix
     * @return
     */
    public void delLikePrefix(String keyPrefix) {

        List<String> keys = keys(keyPrefix + "*");
        List<String> keyDels = new ArrayList<>(KEY_DEL_BATCH);
        for (Iterator<String> ite = keys.iterator(); ite.hasNext(); ) {
            String key = ite.next();
            keyDels.add(key);
            if (keyDels.size() >= KEY_DEL_BATCH) {
                deleteBatch(keyDels);
                keyDels = new ArrayList<>(KEY_DEL_BATCH);
            }
            ite.remove();
        }
        deleteBatch(keyDels);

    }

}
