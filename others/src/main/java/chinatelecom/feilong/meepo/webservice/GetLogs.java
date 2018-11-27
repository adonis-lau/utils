package chinatelecom.feilong.meepo.webservice;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author: Adonis Lau
 * @date: 2018/11/23 13:50
 */
public class GetLogs {

    private static String URL = "10.5.27.27/feilong3";

    public static void main(String[] args) {
        getJobLogs("{\"jobName\":\"shell_sleep_20181120160333399\",\"projectId\":\"40288df2631f723801632546c8f60321\",\"executionId\":\"flow_1542792530695_qrowocas\"}");
    }


    private static void getJobLogs(String params) {
        try {
            String url = "http://" + URL + "/webservice/download_log.do";
            System.out.println(params);
            Form form = Form.form();
            form.add("params", params);
            Request request = Request.Post(url).bodyForm(form.build());
            Response response = request.socketTimeout(1000000).connectTimeout(1000000).execute();
            InputStream inputStream = response.returnContent().asStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
