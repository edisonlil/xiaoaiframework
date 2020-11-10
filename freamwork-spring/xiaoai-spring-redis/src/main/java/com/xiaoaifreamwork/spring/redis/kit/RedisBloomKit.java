package com.xiaoaifreamwork.spring.redis.kit;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RedisBloomKit extends RedisKit {


    public RedisBloomKit(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    final String RESERVE_SCRIPT = "return redis.call('bf.reserve',KEYS[1],ARGV[1],ARGV[2])";

    final String ADD_SCRIPT = "return redis.call('bf.add',KEYS[1],ARGV[1])";

    final String EXISTS_SCRIPT = "return redis.call('bf.exists',KEYS[1],ARGV[1])";

    /**
     * 创建一个空的布隆过滤器，具有给定的所需错误率和初始容量。
     * 尽管可以通过创建子过滤器来扩展过滤器，但与初始化时容量合适的等效过滤器相比，它将消耗更多的内存和CPU时间。
     * 哈希函数的数量为-log（error）/ ln（2）^ 2每项的位数为-log（error）/ ln（2）
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 可以在其中找到过滤器的键
     * @param errorRate 误报的期望概率。这应该是介于0到1之间的十进制值。例如，对于期望的误报率0.1％（1000中为1），error_rate应该设置为0.001。该数字越接近零，则每个项目的内存消耗越大，并且每个操作的CPU使用率越高。
     * @param capacity 您打算添加到过滤器中的条目数。添加超过此数量的项目后，性能将开始下降。实际的降级将取决于超出限制的程度。随着条目数量呈指数增长，性能将线性下降。
     * @return
     */
    public Boolean reserve(String key, Double errorRate, Integer capacity){

        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(RESERVE_SCRIPT);

        return redisTemplate.execute(redisScript,new ArrayList<String>(){{add(key);}},errorRate,capacity);
    }


    /**
     * 创建一个空的布隆过滤器,默认的错误率为0.001,初始容量为10000
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 可以在其中找到过滤器的键
     * @return
     */
    public Boolean reserve(String key){
        return reserve(key,0.001,10000);
    }

    /**
     * 创建一个空的布隆过滤器,默认的错误率为0.001,初始容量为10000
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 可以在其中找到过滤器的键
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @return
     */
    public Boolean reserve(String key, int expireTime, TimeUnit timeUnit){

        if(reserve(key)){
            expire(key,expireTime,timeUnit);
            return true;
        }

        return false;
    }


    /**
     * 创建一个空的布隆过滤器，具有给定的所需错误率和初始容量。
     * 尽管可以通过创建子过滤器来扩展过滤器，但与初始化时容量合适的等效过滤器相比，它将消耗更多的内存和CPU时间。
     * 哈希函数的数量为-log（error）/ ln（2）^ 2每项的位数为-log（error）/ ln（2）
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 可以在其中找到过滤器的键
     * @param errorRate 误报的期望概率。这应该是介于0到1之间的十进制值。例如，对于期望的误报率0.1％（1000中为1），error_rate应该设置为0.001。该数字越接近零，则每个项目的内存消耗越大，并且每个操作的CPU使用率越高。
     * @param capacity 您打算添加到过滤器中的条目数。添加超过此数量的项目后，性能将开始下降。实际的降级将取决于超出限制的程度。随着条目数量呈指数增长，性能将线性下降。
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @return
     */
    public Boolean reserve(String key, Double errorRate, Integer capacity, int expireTime, TimeUnit timeUnit){

        if(reserve(key, errorRate, capacity)){
            expire(key,expireTime,timeUnit);
            return true;
        }
        return false;
    }



    /**
     * 将项目添加到布隆过滤器中，如果该过滤器尚不存在，则创建该过滤器。
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 过滤器的名称
     * @param item 要添加的项目
     * @return
     */
    public Boolean add(String key, String item){

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(ADD_SCRIPT);
        return redisTemplate.execute(redisScript,new ArrayList<String>(){{add(key);}},item) == 1;

    }


    /**
     * 将项目添加到布隆过滤器中，如果该过滤器尚不存在，则创建该过滤器。
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 过滤器的名称
     * @param item 要添加的项目
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @return
     */
    public Boolean add(String key, String item, int expireTime, TimeUnit timeUnit){

        if(hasKey(key)){
            return add(key, item);
        }else{
            if(reserve(key, expireTime, timeUnit)){
                return add(key, item);
            }
        }
        return false;

    }


    /**
     * 确定项目是否在布隆过滤器中存在。
     * @api https://oss.redislabs.com/redisbloom/Bloom_Commands/
     * @param key 过滤器的名称
     * @param item 要检查的项目
     * @return
     */
    public boolean exists(String key, String item){

        if(!hasKey(key)){
            return false;
        }

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(EXISTS_SCRIPT);
        return redisTemplate.execute(redisScript,new ArrayList<String>(){{add(key);}},item) == 1;
    }

}
