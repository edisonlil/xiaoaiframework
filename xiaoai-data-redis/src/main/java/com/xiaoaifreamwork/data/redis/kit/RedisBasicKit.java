package com.xiaoaifreamwork.data.redis.kit;

import com.framework.infrastructure.redis.kit.base.RedisKit;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 提供最基础的操作
 * @author edison
 */
public class RedisBasicKit extends RedisKit {

    public RedisBasicKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }
}
