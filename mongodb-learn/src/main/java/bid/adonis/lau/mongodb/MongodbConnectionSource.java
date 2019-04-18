package bid.adonis.lau.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * MongoDB连接类
 *
 * @author: Adonis Lau
 * @date: 2019/4/3 18:17
 */
@Slf4j
public class MongodbConnectionSource {

    private MongoCollection<Document> dbCollection;

    private String host = "192.168.1.140";
    private int port = 44017;
    private String db = "logs";
    private String username = "logs";
    private String password = "12345678";
    private int connectionsPerHost = 1;
    private int threadsAllowedToBlockForConnectionMultiplier = 100;
    private boolean sslEnable = true;
    private String collection;
    private MongoClient mongoClient;
    private boolean init = false;

    protected void init() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\adonis\\Desktop\\ssl2\\mongodb.pem");
//        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\adonis\\Desktop\\ssl2\\mongodb.pem");
//        System.setProperty("javax.net.ssl.trustStorePassword", "");
        System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\adonis\\Desktop\\ssl2\\client.pem");
//        System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\adonis\\Desktop\\ssl2\\client.pem");
//        System.setProperty("javax.net.ssl.keyStorePassword", "");


        String URI = String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s&retryWrites=true", username, password, host, port, db, db);
        log.info(URI);
//        String URI = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/admin?ssl=true&authSource=logs&retryWrites=true";
//        String URI = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + db + "?authSource=" + db + "&retryWrites=true";
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                .connectionsPerHost(connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
                /*开启SSL认证*/
                .sslEnabled(sslEnable)
                /*允许无效的主机名*/
                .sslInvalidHostNameAllowed(true)
                /*.sslContext(getSSLContext())*/;
        MongoClientURI uri = new MongoClientURI(URI, builder);
        mongoClient = new MongoClient(uri);
        init = true;
    }

    private static SSLContext getSSLContext() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");

            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Couldn't create SSL Context for MongoDB connection", e);
            throw new RuntimeException(e);
        }
        return sslContext;
    }


    public MongoCollection<Document> getDBCollection() {
        MongoCollection<Document> dbCollectionHelper = dbCollection;
        if (dbCollectionHelper == null) {
            synchronized (this) {
                dbCollectionHelper = dbCollection;
                if (dbCollectionHelper == null) {
                    try {
                        if (!init) {
                            init();
                        }
//                        dbCollection = mongoClient.getDatabase(db).getCollection(collection);
                        MongoIterable<String> strings = mongoClient.getDatabase(db).listCollectionNames();
                        for (String string : strings) {

                            log.info(string);
                        }
                        dbCollection = mongoClient.getDatabase(db).getCollection(this.collection);
                    } catch (Exception e1) {
                        log.info(e1.getMessage(), e1);
                    }
                }
            }
        }
        return dbCollection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

