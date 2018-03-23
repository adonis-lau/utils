package chinatelecom.feilong.meepo.webservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Consts;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author Adonis Lau
 * @Eamil adonis.lau.dev@gmail.com
 * @Date Created in 2017/11/9 17:49
 */
public class WebService {

    private static String URL = "";

    public static void main(String[] args) {
        URL = args[0];
        for (int i = 0; i < Integer.valueOf(args[2]); i++) {
            String executionId = jobExecute(args[1]);
            System.out.println("生成的 executionId 为：" + executionId);
            for (int j = 0; j < Integer.valueOf(args[2]); j++) {
                getJobStatus(executionId);
            }
        }
    }

    private static String jobExecute(String jobName) {
        String executionId = null;
        try {
            String url = "http://" + URL +"/webservice/job_execute.do";
            Form form = Form.form();
            executionId = String.format("flow_%s_%s", System.currentTimeMillis(), RandomStringUtils.randomAlphabetic(8).toLowerCase());
            System.out.println(executionId);
            form.add("params", "{\"jobName\":\"" + jobName + "\",\"executionId\":\"" + executionId + "\",\"jobParams\":{\"a\":\"中文测试\"}}");
            Request request = Request.Post(url).bodyForm(form.build(), Consts.UTF_8);
            Response response = request.socketTimeout(10000).connectTimeout(10000).execute();
            System.out.println(response.returnContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return executionId;
    }

    private static void getJobStatus(String executionId) {
        try {
            String url = "http://" + URL + "/webservice/job_check.do";
            System.out.println(executionId);
            Form form = Form.form();
            form.add("params", "{\"executionId\":\"" + executionId + "\"}");
            Request request = Request.Post(url).bodyForm(form.build());
            Response response = request.socketTimeout(10000).connectTimeout(10000).execute();
            System.out.println(response.returnContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
