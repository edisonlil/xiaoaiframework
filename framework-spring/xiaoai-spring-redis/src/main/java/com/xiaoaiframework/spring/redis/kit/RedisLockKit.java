package com.xiaoaiframework.spring.redis.kit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiaoaiframework.spring.redis.lock.RedisLock;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author edison
 */
public class RedisLockKit extends RedisKit {

    /**
     * 加入缓存提高性能
     */
    private final Cache<String, RedisLock> LOCK_CACHE;
    {
        //初始化缓存
        LOCK_CACHE = Caffeine.newBuilder()
                .weakKeys().weakValues()
                //初始大小
                .initialCapacity(100)
                //最大数量
                .maximumSize(1000)
                .build();
    }



    public RedisLockKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    /**
     * 获取一个分布式锁
     * @param key
     * @return
     */
    public RedisLock getLock(String key){
        return getLock(key, 5);
    }


    /**
     * 获取一个分布式锁
     * @param key
     * @param lockMillisecondEx 毫秒
     * @return
     */
    public RedisLock getLock(String key, int lockMillisecondEx){

        RedisLock redisLock = LOCK_CACHE.getIfPresent(key);
        if(redisLock == null){
            synchronized (LOCK_CACHE){
                if(redisLock == null){
                    redisLock = new RedisLock(redisTemplate,key,lockMillisecondEx);
                    LOCK_CACHE.put(key,redisLock);
                }
            }

        }
        return redisLock;
    }
}
