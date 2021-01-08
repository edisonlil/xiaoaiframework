package com.xiaoaiframework.util.concurrent;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 限制锁存器
 * 共享锁存器允许有限次数地获取锁存器，此后所有随后的获取锁存器的请求都将被放入FIFO队列中，直到返回其中一个共享。
 * @author edison
 */
public class LimitLatch {


    Logger log;
    
    /**
     * 同步锁
     */
    private final Sync sync;
    /**
     * 当前计数
     */
    private final AtomicLong count;
    /**
     * 限制
     */
    private volatile long limit;
    /**
     * 是否忽略limit
     */
    private volatile boolean released = false;


    private class Sync extends AbstractQueuedSynchronizer{

        private static final long serialVersionUID = 1L;

        public Sync() {
        }

        @Override
        protected int tryAcquireShared(int ignored) {
            long newCount = count.incrementAndGet();
            if (!released && newCount > limit) {
                // Limit exceeded
                count.decrementAndGet();
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            count.decrementAndGet();
            return true;
        }

    }



    public LimitLatch(long limit){

        this.limit = limit;
        this.count = new AtomicLong(0);
        this.sync = new Sync();

    }

    /**
     * 返回锁存器的当前计数
     * @return the current count for latch
     */
    public long getCount() {
        return count.get();
    }

    /**
     * 获取当前限制
     * @return the limit
     */
    public long getLimit() {
        return limit;
    }


    /**
     * 设置新的限制。 如果降低了限制，则可能会有一段时间获取锁存器的份额多于限制。
     * 在这种情况下，将不再发行锁存器的份额，直到已返回足够的份额以将所获取的锁存器的数量减少到新限制以下为止。
     * 如果增加了限制，则在下一个请求闩锁之前，可能不会向队列中当前存在的线程发出新的可用共享之一。
     * @param limit
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * 如果一个共享锁存器可用，则获取一个共享锁存器；如果当前没有可用的共享锁存器，则等待一个共享锁存器。
     * 
     * @throws InterruptedException If the current thread is interrupted
     */
    public void countUpOrAwait() throws InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug("Counting up["+Thread.currentThread().getName()+"] latch="+getCount());
        }
        sync.acquireSharedInterruptibly(1);
    }
    /**
     * 释放共享的闩锁，使其可用于其他线程。
     * @return the previous counter value
     */
    public long countDown() {
        sync.releaseShared(0);
        long result = getCount();
        if (log.isDebugEnabled()) {
            log.debug("Counting down["+Thread.currentThread().getName()+"] latch="+result);
        }
        return result;
    }

    /**
     * 释放所有等待的线程，并导致 {@link #limit}被忽略，直到调用{@link #reset()}为止。
     * @return <code>true</code> if release was done
     */
    public boolean releaseAll() {
        released = true;
        return sync.releaseShared(0);
    }

    /**
     * 重置锁存器并将共享采集计数器初始化为零。
     * @see #releaseAll()
     */
    public void reset() {
        this.count.set(0);
        released = false;
    }

    /**
     * 返回true ，如果有至少一个线程等待获取共享锁，否则返回false 。
     * @return <code>true</code> if threads are waiting
     */
    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    /**
     * 提供对等待获取此有限共享闩锁的线程列表的访问。
     * @return a collection of threads
     */
    public Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }
}
