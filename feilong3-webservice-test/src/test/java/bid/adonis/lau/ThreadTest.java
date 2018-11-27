package bid.adonis.lau;

import bid.adonis.lau.plugins.rpc.RpcWriteFile;
import bid.adonis.lau.plugins.rpc.ThreadPool;
import bid.adonis.lau.plugins.rpc.WriteFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 16:42
 */
public class ThreadTest {
    private static final Log logger = LogFactory.getLog(ThreadTest.class);
    private static ThreadPoolExecutor threadPool = ThreadPool.getThreadPool();

    @Test
    public void threadTest() throws InterruptedException {
        // 参数1，文件路径
        String filePath = "/data/tmp/test.txt";
        // 参数2，时间间隔
        int timeInterval = 2;

//        if (args != null && args.length == 2) {
//            if (StringUtils.isBlank(args[0])) {
//                filePath = args[0];
//            }
//            if (StringUtils.isBlank(args[1])) {
//                timeInterval = Integer.parseInt(args[1]);
//            }
//        }

        // 获取当前进程的ID
        String PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        logger.info(PID);
        logger.info("$[write_pid]=" + PID);
        logger.info("--------------------------------------------------------");

        WriteFile writeFile = new WriteFile();
        writeFile.writeFile(filePath, timeInterval);
    }
}
