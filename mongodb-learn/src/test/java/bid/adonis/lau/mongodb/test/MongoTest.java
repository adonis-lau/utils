package bid.adonis.lau.mongodb.test;

import bid.adonis.lau.mongodb.MongodbConnectionSource;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.junit.Test;
import org.slf4j.helpers.Util;

import java.net.InetAddress;
import java.util.Date;

/**
 * @author: Adonis Lau
 * @date: 2019/4/8 10:29
 */
@Slf4j
public class MongoTest {
    private MongodbConnectionSource connectionSource = new MongodbConnectionSource();
    private String collection;

    @Test
    public void insert() {
        try {
            if (StringUtils.isEmpty(connectionSource.getCollection())) {
                String flowExecutionId = String.format("flow_%s_%s", System.currentTimeMillis(),
                        RandomStringUtils.randomAlphabetic(8).toLowerCase());
                log.info(flowExecutionId);
                collection = flowExecutionId;
                if (StringUtils.isEmpty(collection)) {
                    collection = "undefined";
                    Util.report("mongo collection is not set in LoggerContext, use default collection 'undefined'");
                }
                connectionSource.setCollection(collection);
            }

            Document doc = new Document();
            doc.put("thread", "threadName");
            doc.put("name", "name");
            InetAddress ia = InetAddress.getLocalHost();
            String hostname = ia.getHostName();
            doc.put("hostname", hostname);
            doc.put("timestamp", new Date());

            doc.put("message", "message");
            doc.put("level", "level");
            MongoCollection<Document> coll = connectionSource.getDBCollection();
            coll.insertOne(doc);
        } catch (Exception e) {
            Util.report(e.getMessage(), e);
        }
    }
}
