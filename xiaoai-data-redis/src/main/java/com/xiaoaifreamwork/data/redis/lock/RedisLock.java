package com.xiaoaifreamwork.data.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * redis 分布式锁实现
 * @version 1
 * @author edison
 */
public class RedisLock implements Lock {

    static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    ThreadLocal threadLocal = new ThreadLocal();

    protected RedisTemplate<String, Object> redisTemplate;

    ValueOperations<String, String> valueOperations;

    Timer rechargeTimer = new Timer();

    String key;


        /*
           -- KEYS[1] = lockName
           -- ARGV[1] = value
           -- ARGV[2] = 超时时间

           "if(redis.call('EXISTS',KEYS[1]) == 0) then
            redis.call('HSETNX',KEYS[1],ARGV[1],1)
            redis.call('PEXPIRE',KEYS[1],ARGV[2])
            return nil;
           elseif (redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) then
            redis.call('HINCRBY', KEYS[1], ARGV[1], 1);
            redis.call('PEXPIRE', KEYS[1], ARGV[2]);
            return nil;
           else
            return redis.call('PTTL', KEYS[1]);
           end;"

           -- KEYS[1] = lockName
           -- ARGV[1] = value
           -- ARGV[2] = 超时时间

           "if(redis.call('EXISTS',KEYS[1]) == 0) then
            return 1;
           elseif ((redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) and (tonumber(redis.call('HMGET', KEYS[1], ARGV[1])[1]) > 1)) then
            redis.call('HINCRBY', KEYS[1], ARGV[1], -1);
            redis.call('PEXPIRE', KEYS[1], ARGV[2]);
            return 1;
           end;
           return redis.call('DEL',KEYS[1])"

           -- KEYS[1] = lockName
           -- ARGV[1] = value
           -- ARGV[2] = 超时时间


           -- 查看锁是否存在，返回1即为存在
           if (redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) then
           redis.call('PEXPIRE', KEYS[1], ARGV[2]);
           return 1;
           end;
           return 0;
     */

    /**
     *   加锁的脚本
     *   1.保证加锁的原子性 使用lua脚本
     */
    static final String LOCK_SCRIPT = "if(redis.call('EXISTS',KEYS[1]) == 0) then" +
            "            redis.call('HSETNX',KEYS[1],ARGV[1],1)" +
            "            redis.call('PEXPIRE',KEYS[1],ARGV[2])" +
            "            return nil;" +
            "           elseif (redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) then" +
            "            redis.call('HINCRBY', KEYS[1], ARGV[1], 1);" +
            "            redis.call('PEXPIRE', KEYS[1], ARGV[2]);" +
            "            return nil;" +
            "           else" +
            "            return redis.call('PTTL', KEYS[1]);" +
            "           end;";

    static final String UN_LOCK_SCRIPT = "if(redis.call('EXISTS',KEYS[1]) == 0) then" +
            "            return 1;" +
            "           elseif ((redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) " +
            "               and (tonumber(redis.call('HMGET', KEYS[1], ARGV[1])[1]) > 1)) then" +
            "            redis.call('HINCRBY', KEYS[1], ARGV[1], -1);" +
            "            redis.call('PEXPIRE', KEYS[1], ARGV[2]);" +
            "            return 1;" +
            "           end;" +
            "           return redis.call('DEL',KEYS[1])";

    static final String RECHARGE_SCRIPT = "if (redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1) then" +
            "           redis.call('PEXPIRE', KEYS[1], ARGV[2]);" +
            "           return 1;" +
            "           end;" +
            "           return 0;";



    /**
     * 锁自动过期的毫秒数
     */
    private int lockMillisecondEx = 5000;




    public RedisLock(RedisTemplate redisTemplate, String key, int lockMillisecondEx){
       this(redisTemplate,key);
       this.lockMillisecondEx = lockMillisecondEx;
    }

    public RedisLock(RedisTemplate redisTemplate, String key){
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
        this.key = key;
    }


    /**
     * 设置锁，设置成功返回null,否则返回锁超时时间(毫秒为单位)
     * @return
     */
    private Integer setLock(){

        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Integer.class);
        redisScript.setScriptText(LOCK_SCRIPT);

        //设置key
        List keys = CollectionUtils.arrayToList(new String[]{key});
        //设置value
        String value = key+":"+ UUID.randomUUID().toString();

        //2.0应该给锁设置过期时间,避免了加锁的客户端发生故障后锁一直不被释放,锁在5秒后自动失效。
        Integer result = redisTemplate.execute(redisScript,keys,value,lockMillisecondEx);

        if(result == null){
            //设置线程获取该key的表示
            threadLocal.set(value);
            recharge();
            return result;
        }

        return result;

    }

    /**
     * 定时任务续签
     */
    private void recharge(){

        rechargeTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(threadLocal.get() == null){
                    return;
                }
                DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
                redisScript.setResultType(Boolean.class);
                redisScript.setScriptText(RECHARGE_SCRIPT);
                //设置key
                List keys = CollectionUtils.arrayToList(new String[]{key});
                boolean result = redisTemplate.execute(redisScript,keys,threadLocal.get(),lockMillisecondEx);
                if(!result){
                    return;
                }

                recharge();

            }
        },lockMillisecondEx / 3);

    }

    /**
     * 获取锁
     */
    @Override
    public void lock() {

        //TODO 2.1 如果给锁加上过期时间,那么这个过期时间阈值应该设置多少合适?
        // 如果业务执行时间大于锁的过期时间,那么导致锁提前失效该怎么解决?
        //1.保证加锁的原子性 使用lua脚本
        //如果获取锁失败

        Integer expire = setLock();
        if (expire == null){
            return;
        }
        if(expire > 0){
            try {
                //休眠指定时长后竞争锁
                TimeUnit.MILLISECONDS.sleep(expire);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(expire == -1){//如果过期时间为-1L则代表锁不可能被释放,死锁。
            throw new RuntimeException("当前竞争的锁资源过期时间为永久...");
        }
        lock();
    }

    /**
     * 当通过该方法获取锁时,如果线程正在等待获取锁,则这个线程能够响应中断,即中断线程的等待状态。
     * 例如,当两个线程同时通过该方法想获取某个锁时,假如此时线程A获取到了锁,
     * 而线程B在等待,那么对线程B调用Thread.currentThread().interrupt(),方法能够中断线程B的等待过程。
     *
     * 由于该方法的声明中抛出了异常,所以该方法必须放在try块中或者在调用。
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {

        //如果线程被中断了则返回true,并清除线程的中断状态
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        lock();
    }

    /**
     * 尝试获取锁,如果获取成功则返回true,相反获取不到锁则返回false(即锁已被其他线程获取);
     * 该方法无论如何都会立即返回,在获取不到锁时不会一直在那等待。
     * @return
     */
    @Override
    public boolean tryLock() {
        return setLock() == null;
    }

    /**
     * 尝试获取锁,如果获取成功则返回true,相反获取不到锁则返回false(即锁已被其他线程获取);
     * 该方法允许在获取不到锁的时候等待一定的时间,在时间期限之内如何还拿不到锁,就返回false,同时可以响应中断。;
     * 如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
     * @param time 等待时间
     * @param unit 时间单位
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        long nanosTimeout = unit.toNanos(time);
        if (nanosTimeout <= 0L) {
            return false;
        }
        final long deadline = System.nanoTime() + nanosTimeout;

        while (true){

            nanosTimeout = deadline - System.nanoTime();
            if (nanosTimeout <= 0L) {
                return false;
            }
            Integer expire = setLock();
            if(expire == null){
                return true;
            }
            //如果获取锁失败则查询过期时间。
            //如果过期时间为-1L则代表锁不可能被释放,死锁。
            if((System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(expire)) > deadline){
                return false;
            } else if(expire > 0){
                TimeUnit.MILLISECONDS.sleep(expire);
            }else if(expire == -1){
                throw new RuntimeException("当前竞争的锁资源过期时间为永久...");
            }

        }


    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {

        //1.锁住线程后如何知晓是该持有锁的线程去解锁 生成一个随机字符串,
        // 且保证在足够长的一段时间内,在所有客户端的获取锁的请求中都是唯一的。
        //2.保持解锁的原子性

        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(UN_LOCK_SCRIPT);

        //设置key
        List keys = CollectionUtils.arrayToList(new String[]{key});

        boolean result = redisTemplate.execute(redisScript,keys,threadLocal.get(),lockMillisecondEx);
        threadLocal.remove();
        if(result) {
            //解锁成功
            logger.info(Thread.currentThread().getName()+"解锁成功,当前锁标识为{}",key);

        }else{
            //解锁失败
            logger.error(Thread.currentThread().getName()+"解锁失败,当前锁标识为{}",key);
        }


    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
