package bid.adonis.lau.plugins.rpc;

import chinatelecom.feilong.scheduler.utils.DateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 10:12
 */
public class RpcWriteFile {

    private static final Log logger = LogFactory.getLog(RpcWriteFile.class);
    private static ThreadPoolExecutor threadPool = ThreadPool.getThreadPool();

    public static void main(String[] args) {
        // 参数1，文件路径
        String filePath = "/data/tmp/test.txt";
        // 参数2，时间间隔
        int timeInterval = 2;

        if (args != null && args.length == 2) {
            if (StringUtils.isBlank(args[0])) {
                filePath = args[0];
            }
            if (StringUtils.isBlank(args[1])) {
                timeInterval = Integer.parseInt(args[1]);
            }
        }

        // 获取当前进程的ID
        String PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        logger.info(PID);
        logger.info("$[write_pid]=" + PID);
        logger.info("--------------------------------------------------------");

        WriteFile writeFile = new WriteFile();
        writeFile.writeFile(filePath, timeInterval);

//        WriteFile2 writeFile2 = new WriteFile2(filePath, timeInterval);
//        writeFile2.start();

    }
}
