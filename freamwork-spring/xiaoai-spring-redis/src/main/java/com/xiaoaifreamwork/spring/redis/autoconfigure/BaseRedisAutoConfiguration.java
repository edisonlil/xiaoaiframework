package com.xiaoaifreamwork.spring.redis.autoconfigure;



import com.xiaoaifreamwork.spring.redis.configuration.RedisConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @program: framework
 * @author: edison
 * @create: 2020/04/09
 */
@Import(RedisConfiguration.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class BaseRedisAutoConfiguration {
}