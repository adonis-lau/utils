package chinatelecom.feilong.meepo.msg;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/30 16:37
 */
public class msg_yueme {

    public static void main(String[] args) {

        String to = "17600000000";
        String context = "短信测试";
        String url = "http://sms/sms/SmsSend";

        String sms = "{'bizEvent':'11','mobilePhone': '${phone}','param': {'from':'${jobName}','platform':'��me','level':'ETL','event_id':'����','description':'meepo��������������${msg}','time':'${time}'}}";
        sms = sms.replaceAll("\\$\\{phone\\}", to);
        sms = sms.replaceAll("\\$\\{jobName\\}", "meepo");
        sms = sms.replaceAll("\\$\\{msg\\}", context);
        sms = sms.replaceAll("\\$\\{time\\}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        System.out.println("the sendMsg is : " + sms.toString());

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            System.out.println("the sendMsg is : " + sms.toString());
            System.out.println("================  sendMsg  ===================");
            System.out.println("================  短信内容" + sms.toString());

            out.print(sms);

            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
            System.out.println("the rusult is : " + result);
            String str1;
            if (result.contains("\"Result\":0")) {
                System.out.println("发送成功");
            }
            System.out.println("发送失败");
        } catch (Exception e) {

            System.out.println("接口连接错误");
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
