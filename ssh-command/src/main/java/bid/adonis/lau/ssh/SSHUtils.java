package bid.adonis.lau.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 用于读取执行器的运行日志
 *
 * @author: Adonis Lau
 * @date: 2018/11/9 16:09
 */
public class SSHUtils {

    /**
     * 通过SSH远程执行Shell命令
     *
     * @param command  命令
     * @param host     目标主机地址
     * @param username 目标主机用户名
     * @param password 目标主机密码
     * @param port     目标主机的SSH端口
     * @param encoding 返回的IO流编码
     * @return
     */
    public static String exeCommand(String command, String host, String username, String password, Integer port, Charset encoding) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");

            session.setPassword(password);
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            String out = IOUtils.toString(in, encoding);

            channelExec.disconnect();
            session.disconnect();
            return out;
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过SSH远程执行Shell命令
     * 目标主机SSH端口默认为22
     *
     * @param command  命令
     * @param host     目标主机地址
     * @param username 目标主机用户名
     * @param password 目标主机密码
     * @return
     */
    public static String exeCommand(String command, String host, String username, String password, Charset encoding) {
        return exeCommand(command, host, username, password, 22, encoding);
    }

    /**
     * 通过SSH远程执行Shell命令
     * 返回的IO流编码默认为UTF-8
     *
     * @param command  命令
     * @param host     目标主机地址
     * @param username 目标主机用户名
     * @param password 目标主机密码
     * @return
     */
    public static String exeCommand(String command, String host, String username, String password, Integer port) {
        return exeCommand(command, host, username, password, port, Charset.defaultCharset());
    }

    /**
     * 通过SSH远程执行Shell命令
     * 目标主机SSH端口默认为22
     * 返回的IO流编码默认为UTF-8
     *
     * @param command  命令
     * @param host     目标主机地址
     * @param username 目标主机用户名
     * @param password 目标主机密码
     * @return
     */
    public static String exeCommand(String command, String host, String username, String password) {
        return exeCommand(command, host, username, password, 22, Charset.defaultCharset());
    }
}
