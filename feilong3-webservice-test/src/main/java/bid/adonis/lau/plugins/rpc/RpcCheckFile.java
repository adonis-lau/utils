package bid.adonis.lau.plugins.rpc;

import chinatelecom.feilong.scheduler.utils.DateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 10:24
 */
public class RpcCheckFile {
    private static final Log logger = LogFactory.getLog(RpcCheckFile.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        // 参数1，文件路径
        String filePath = "test.txt";
        // 参数2，时间间隔
        int timeInterval = 2;

        if (args != null && args.length == 2){
            if (StringUtils.isBlank(args[0])){
                filePath = args[0];
            }
            if (StringUtils.isBlank(args[1])){
                timeInterval = Integer.parseInt(args[1]);
            }
        }

        // 获取当前进程的ID
        String PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        logger.info(PID);
        logger.info("$[check_pid]=" + PID);
        logger.info("--------------------------------------------------------");

        File file = FileUtils.getFile(filePath);
        logger.info(file.getCanonicalPath());

        long end = DateUtils.addMinutes(new Date(), timeInterval).getTime();

        for (long currentTimeMillis = System.currentTimeMillis(); currentTimeMillis < end; currentTimeMillis = System.currentTimeMillis()) {
            Process process = Runtime.getRuntime().exec("tail -1 " + filePath);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                logger.info("current context is : " + line);
                if ("END".equals(line.trim())) {
                    System.exit(0);
                }
            }
            input.close();
            Thread.sleep(1000 * 10);
        }
        System.exit(-1);
    }
}
