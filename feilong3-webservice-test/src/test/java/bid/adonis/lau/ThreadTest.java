package bid.adonis.lau;

import bid.adonis.lau.plugins.rpc.RpcWriteFile;
import bid.adonis.lau.plugins.rpc.ThreadPool;
import bid.adonis.lau.plugins.rpc.WriteFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void breakTest() {
        int mm = 10;
        for (int i = 0; i < mm; i++) {
            if (i < 50){
                System.out.println("aaa");
                if (i > 3){
                    continue;
                }
                System.out.println("bb");
            }
        }
    }

    @Test
    public void listTest() {
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add("aa");
        list.add(null);
        list.add(null);
        list.add("");
        list.add(null);
        System.out.println(list.size());
        for (String s : list) {
            System.out.println(s);
        }
    }
}
