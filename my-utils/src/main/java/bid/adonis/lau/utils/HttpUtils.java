package bid.adonis.lau.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @author: Adonis Lau
 * @date: 2018/10/23 16:06
 */
public class HttpUtils {

    /**
     * http post request
     *
     * @param uri      request uri
     * @param paramMap request parameter
     * @return
     * @throws IOException
     */
    public static String post(String uri, Map<String, String> paramMap) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        Response response = Request.Post(uri).bodyForm(params).socketTimeout(10000).connectTimeout(10000).execute();
        return response.handleResponse(new BasicResponseHandler());
    }

    /**
     * http get request
     *
     * @param uri request uri
     * @return request parameter
     * @throws IOException
     */
    public static String get(String uri) throws IOException {
        Response response = Request.Get(uri).socketTimeout(10000).connectTimeout(10000).execute();
        return response.handleResponse(new BasicResponseHandler());
    }

    /**
     * http post request with inputStream
     *
     * @param uri      URI
     * @param paramMap 参数列表
     * @param fileIS   文件输入流
     * @return
     * @throws IOException
     */
    public static String uploadFile(String uri, Map<String, String> paramMap, InputStream fileIS, String fileName) throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", fileIS, ContentType.APPLICATION_OCTET_STREAM, fileName);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.APPLICATION_JSON);
        }
        HttpEntity multipart = builder.build();

        Response response = Request.Post(uri).body(multipart).socketTimeout(10000).connectTimeout(10000).execute();
        return response.handleResponse(new BasicResponseHandler());
    }

    /**
     * http post request with file
     *
     * @param uri      URI
     * @param paramMap 参数列表
     * @param file     文件
     * @return
     * @throws IOException
     */
    public static String uploadFile(String uri, Map<String, String> paramMap, File file) throws IOException {
        FileInputStream fileIS = StreamUtils.getFileInputStream(file);
        return uploadFile(uri, paramMap, fileIS, file.getName());
    }


    public static String uploadFileToOSS(String uri, Map<String, String> paramMap, InputStream fileIS, String fileName) {
        CloseableHttpClient client = null;
        String result = null;
        try {
            client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uri);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //addTextBody(String,String)该方法可以传入参数，例如请求的地址需要接受一个id
            //builder.addTextBody("id", "1");
            builder.addTextBody("param", paramMap.get("param"));
            //addBinaryBody()该方法传入二进制内容，可以传入InputStream，File, 参数三是传入的类型，参数四是文件名称
            builder.addBinaryBody("file", fileIS, ContentType.MULTIPART_FORM_DATA, fileName);
            httpPost.setEntity(builder.build());
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 取回服务器端的响应结果
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
