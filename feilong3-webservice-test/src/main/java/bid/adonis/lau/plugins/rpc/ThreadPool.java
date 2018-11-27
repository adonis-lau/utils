package bid.adonis.lau.plugins.rpc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 14:51
 */
public class ThreadPool {
    /**
     * 线程池的基本大小
     */
    private static int corePoolSize = 10;
    /**
     * 线程池最大数量
     */
    private static int maximumPoolSizeSize = 50;
    /**
     * 线程活动保持时间
     */
    private static long keepAliveTime = 10;
    /**
     * 任务队列
     */
    private static ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    private ThreadPool() {
    }

    public static ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSizeSize,
                keepAliveTime,
                TimeUnit.MINUTES,
                workQueue,
                new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build());
    }
}
