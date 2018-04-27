package bid.adonis.lau.thread;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/4/16 15:21
 */
public class ThreadAndFile {

    public void processFiles(){
        File directory = FileUtils.getFile("D:\\data\\program_test_file\\theard_and_file\\input");
        File[] files = directory.listFiles();

        /**
         * 1.在使用有界队列的时候：若有新的任务需要执行，如果线程池实际线程数小于corePoolSize核心线程数的时候，则优先创建线程。
         * 若大于corePoolSize时，则会将多余的线程存放在队列中，
         * 若队列已满，且最请求线程小于maximumPoolSize的情况下，则自定义的线程池会创建新的线程，
         * 若队列已满，且最请求线程大于maximumPoolSize的情况下，则执行拒绝策略，或其他自定义方式。
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(// 自定义一个线程池
                10, // coreSize
                20, // maxSize
                60, // 60s
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(3) // 有界队列，容量是3个
        );
    }
}
