package bid.adonis.lau;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/1 10:05
 */
public class AdonisTest {


    public void stringTest() {
        System.out.println("\\s+");
    }


    @Test
    public void postTest() {
        String url = "http://121.204.142.2:8984/auth-web/oauth/token";
        String content = "client_id=bc755169-3fee-4d7c-87e3-02d7bd1865d9&client_secret=158a9ade7d0049068af1535715911152&grant_type=client_credentials&redirect_uri=http://121.204.142.181:8848&username=lds";

        HashMap<String, String> param = new HashMap<>();
        param.put("client_id", "bc755169-3fee-4d7c-87e3-02d7bd1865d9");
        param.put("client_secret", "158a9ade7d0049068af1535715911152");
        param.put("grant_type", "client_credentials");
        param.put("redirect_uri", "http://121.204.142.181:8848");
        param.put("username", "lds");


//        String post = post(url, content);
        String post = doPost(url, param);
        System.out.println(post);
    }

    private String post(String url, String content) {
        try {
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置连接超时
            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(30 * 1000);
            connection.connect();

            System.out.println("responseCode = " + connection.getResponseCode());

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(content);
            out.flush();
            out.close();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            connection.disconnect();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String doPost(String url, Map params) {

        BufferedReader in = null;
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));

                //System.out.println(name +"-"+value);
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            } else {   //
                System.out.println("状态码：" + code);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Test
    public void createFile() {
        try {
            for (int j = 0; j < 1; j++) {
                FileWriter fileWriter = new FileWriter("D:\\data\\tmp\\source" + (j + 1) + ".txt");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < 100; i++) {
                    bufferedWriter.write("AGO\u0005Angola\u0005Africa\u000512345678\u0005中文测试\u00051975\u000512878000\u000538.3\u00056648\u00057984\u0005Angola\u0005Republic\u0005JosÃ© Eduardo dos Santos\u000556\u0005AO\n");
                    bufferedWriter.write("AGO\u0005Angola\u0005Africa\u0005\u0005乱码测试\u00051975\u000512878000\u000538.3\u00056648\u00057984\u0005Angola\u0005Republic\u0005JosÃ© Eduardo dos Santos\u000556\u0005AO\n");
                    bufferedWriter.write("AGO\u0005Angola\u0005Africa\u0005中文\u00051246700\u00051975\u000512878000\u000538.3\u00056648\u00057984\u0005Angola\u0005Republic\u0005JosÃ© Eduardo dos Santos\u000556\u0005AO\n");
                    bufferedWriter.write("AGO\u0005Angola\u0005Africa\u0005yingwen\u00051246700\u00051975\u000512878000\u000538.3\u00056648\u00057984\u0005Angola\u0005Republic\u0005JosÃ© Eduardo dos Santos\u000556\u0005AO\n");
                }
                bufferedWriter.close();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void arrayTest() {
        String str = "AGO\u0005Angola\u0005Africa\u000512345678\u00051246700\u00051975\u000512878000\u000538.3\u00056648\u00057984\u0005Angola\u0005Republic\u0005JosÃ© Eduardo dos Santos\u000556\u0005AO";
        String[] split = str.split("\u0005", -1);
        split[3] = "q28374rfhioufwdfg32orydskgfbjqwgf7o8iwgakfj2q3uofgewkhfbkqewugfofobdakhbvk";
        System.out.println(split[3]);
    }

    @Test
    public void stringSplit() {
        String str = "738187682|0|C|NOU| 452874553|1022|70002|1|4|021|18017873198|18017873198|021|10659864|10659864|20180201191145|0|1|1|c|10|||004528745530001100||474328519||||CSMS|||||2018-02-01 19:16:22|0|1  ";
        String[] split = str.split("\\|");
        StringBuffer buffer = new StringBuffer();
//        String resultStr = StringUtils.join(split, StringEscapeUtils.unescapeJava("\u0005"));

        for (int i = 0; i < split.length; i++) {
            buffer.append(split[i]);
            if (i != split.length - 1) {
                //在字段之间从新添加分隔符
                buffer.append(StringEscapeUtils.unescapeJava("\\|"));
            }
        }

        for (String s : split) {
            System.out.println(s);
        }

        System.out.println(str);
        System.out.println(buffer.toString());
        System.out.println(str.equals(buffer.toString()));

    }

    @Test
    public void splitTest() {
        String str = "738187682|0|C|NOU| 452874553|1022|70002|1|4|021|18017873198|18017873198|021|10659864|10659864|20180201191145|0|1|1|c|10|||004528745530001100||474328519||||CSMS|||||2018-02-01 19:16:22|0|1  ";
        String[] split = str.split("\\|", -1);
        System.out.println(split.length);
        String str2 = "|0|C|| 452874553|1022|70002|1|4|021||18017873198|021|10659864|10659864|20180201191145|0|1|1|c|10|||004528745530001100||474328519||||CSMS|||||||";
        String[] split2 = str2.split("\\|", -1);
        System.out.println(split2.length);
    }

    @Test
    public void stringLengthTest() {
        String str = "";
        Long len = 0L;
        try {
            while (true){
                str += "a";
                len += 1;
            }
        } finally {
            System.out.println(len);
        }

    }
}
