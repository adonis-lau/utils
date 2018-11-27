package bid.adonis.lau.plugins.rpc;

import chinatelecom.feilong.scheduler.utils.DateUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author: Adonis Lau
 * @date: 2018/11/26 17:49
 */
public class WriteFile2 extends Thread {

    /**
     * 文件路径
     */
    private String filePath = "/data/tmp/test.txt";
    /**
     * 时间间隔
     */
    private int timeInterval = 2;

    public WriteFile2(String filePath, int timeInterval) {
        this.filePath = filePath;
        this.timeInterval = timeInterval;
    }

    @Override
    public void run() {
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
    }
}
