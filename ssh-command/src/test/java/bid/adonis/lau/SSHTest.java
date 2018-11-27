package bid.adonis.lau;


import bid.adonis.lau.ssh.SSHUtils;
import bid.adonis.lau.utils.LogsUtils;
import com.jcraft.jsch.JSchException;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/1 10:05
 */
public class SSHTest {

    @Test
    public void getFileLines() throws IOException, JSchException {

        String path = "/root/anaconda-ks.cfg";
        Long firstFewLines = 0L;
        Long nextFewLines = 100L;

        String command = "echo `cat " + path + " | wc -l`";

        String number = LogsUtils.getNumberOfFileLines(command);
        System.out.println(number);
        System.out.println();
        System.out.println();
        System.out.println();
        String logs = LogsUtils.getExecutorLogs(path, 0L, 100L);
        System.out.println(logs);
    }

    @Test
    public void chineseTest(){
        String command = "echo \"中文测试\"";

        String s = SSHUtils.exeCommand(command, "centos", "adonis", "adonis.lau");
        System.out.println(s);
    }
}
