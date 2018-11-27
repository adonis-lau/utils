package bid.adonis.lau.plugins.rpc;

import chinatelecom.feilong.scheduler.utils.DateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 16:21
 */
public class WriteFile {
    //    private static final Log logger = LogFactory.getLog(WriteFile.class);
    private ThreadPoolExecutor threadPool = ThreadPool.getThreadPool();
//    private ExecutorService threadPool = Executors.newCachedThreadPool() ;


    public void writeFile(final String filePath, final int timeInterval) {
        threadPool.submit(() -> {
            try {
                File file = FileUtils.getFile(filePath);
//                logger.info(file.getCanonicalPath());

                long end = DateUtils.addMinutes(new Date(), timeInterval).getTime();

                FileUtils.write(file, "START\n", false);
                for (long currentTimeMillis = System.currentTimeMillis(); currentTimeMillis < end; currentTimeMillis = System.currentTimeMillis()) {
                    FileUtils.write(file, DateUtils.getDateTime() + "\n", true);
                    Thread.sleep(1000 * 3);
                }
                FileUtils.write(file, "END", true);
            } catch (IOException | InterruptedException e) {
//                logger.error(e.getMessage(), e);
            }

        });
    }
}
