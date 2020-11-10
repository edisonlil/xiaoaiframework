package com.xiaoaifreamwork.data.redis.kit;

import cn.hutool.core.bean.BeanUtil;
import com.framework.infrastructure.redis.kit.base.RedisKit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author edison
 */
public class RedisHashKit extends RedisKit {

    HashOperations operations;

    public RedisHashKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
        operations = redisTemplate.opsForHash();
    }


    /**
     * 向指定hash添加item/value
     * @param key
     * @param item
     * @param value
     */
    public void add(String key, String item, Object value) {
        operations.put(key, item, value);
    }


    /**
     * 添加一个hash
     * @param key
     * @param map
     */
    public void add(String key, Map map) {
        operations.putAll(key, map);
    }

    /**
     * 添加一个hash
     * @param key
     * @param entity
     * @param expireTime
     */
    public <T> void add(String key, T entity, int expireTime) {
        if (expireTime < 0) {
            return;
        }
        add(key,entity);
        expire(key, expireTime);
    }

    /**
     * 添加一个hash
     * @param key
     * @param entity
     */
    public <T> void add(String key, T entity) {
        operations.putAll(key, BeanUtil.beanToMap(entity, false, true));
    }


    /**
     * 向指定的item自增
     * @param key
     * @param item
     * @param count
     */
    public void incrItem(String key, String item, Integer count) {
        operations.increment(key, item, count);
    }

    /**
     * 获取hash键值对
     * @param key
     * @return
     */
    public Map get(String key) {
        return operations.entries(key);
    }


    /**
     * 获取hash键值对
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> entity) {
        return BeanUtil.mapToBean(operations.entries(key), entity, true);
    }

    /**
     * 获取指定key的Hash的指定item的value
     * @param key
     * @param item
     * @param <T>
     * @return
     */
    public <T> T getItem(String key, String item) {
        return (T) operations.get(key, item);
    }


    /**
     * 获取指定key前缀的所有散列表,并删除
     *
     * @param keyPrefix
     * @return
     */
    public List<Map> getAllAndDel(String keyPrefix) {

        List<String> keys = keys(keyPrefix + "*");
        List<String> keyDels = new ArrayList<>(KEY_DEL_BATCH);
        List<Map> maps = new ArrayList<>();

        for (Iterator<String> ite = keys.iterator(); ite.hasNext(); ) {
            String key = ite.next();
            maps.add(get(key));
            keyDels.add(key);
            if (keyDels.size() >= KEY_DEL_BATCH) {
                deleteBatch(keyDels);
                keyDels = new ArrayList<>(KEY_DEL_BATCH);
            }
            ite.remove();
        }
        deleteBatch(keyDels);
        return maps;
    }

    /**
     * 获取指定key前缀的所有散列表,并删除
     * @param keyPrefix
     * @return
     */
    public <T> List<T> getAllAndDel(String keyPrefix, Class<T> entityClass) {

        List<String> keys = keys(keyPrefix + "*");
        List<String> keyDels = new ArrayList<>(KEY_DEL_BATCH);
        List<T> ts = new ArrayList<>();
        for (Iterator<String> ite = keys.iterator(); ite.hasNext(); ) {
            String key = ite.next();
            ts.add(get(key, entityClass));
            keyDels.add(key);
            if (keyDels.size() >= KEY_DEL_BATCH) {
                deleteBatch(keyDels);
                keyDels = new ArrayList<>(KEY_DEL_BATCH);
            }
            ite.remove();
        }
        deleteBatch(keyDels);
        return ts;


    }

}
