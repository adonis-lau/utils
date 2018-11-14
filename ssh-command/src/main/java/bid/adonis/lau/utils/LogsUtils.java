package bid.adonis.lau.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于读取执行器的运行日志
 *
 * @author: Adonis Lau
 * @date: 2018/11/9 16:09
 */
public class LogsUtils {

    /**
     * 获取指定行数下的执行器日志
     *
     * @param path          日志文件绝对路径
     * @param firstFewLines
     * @param nextFewLines
     * @return
     */
    public static String getExecutorLogs(String path, Long firstFewLines, Long nextFewLines) throws JSchException, IOException {

        String command = "tail -400 " + path;

        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "121.204.142.2", 22);
        session.setConfig("StrictHostKeyChecking", "no");

        session.setPassword("Ideal@132.");
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");

        channelExec.disconnect();
        session.disconnect();

        return out;
    }

    /**
     * 获取指定行数下的执行器日志
     *
     * @param command          要执行的命令
     * @return
     */
    public static String getNumberOfFileLines(String command) throws JSchException, IOException {
        // TODO 连接到执行器所在的机器并执行获取日志的命令
        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "121.204.142.2", 22);
        session.setConfig("StrictHostKeyChecking", "no");

        session.setPassword("Ideal@132.");
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");

        channelExec.disconnect();
        session.disconnect();

        return out;
    }
}
