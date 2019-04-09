package bid.adonis.lau.mongodb;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.helpers.Util;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

/**
 * @author: Adonis Lau
 * @date: 2019/4/8 10:40
 */
public class MongoLogbackAppender extends AppenderBase<LoggingEvent> {
    private static List<String> LEVELS = Lists.newArrayList("TRACE", "DEBUG", "INFO", "WARN", "ERROR");
    protected Layout<ILoggingEvent> layout;
    private MongodbConnectionSource connectionSource;
    private String collection;

    @Override
    protected void append(LoggingEvent event) {
        try {
            if (StringUtils.isEmpty(connectionSource.getCollection())) {
                collection = context.getProperty("flowExecutionId");
                if (StringUtils.isEmpty(collection)) {
                    collection = "undefined";
                    Util.report("mongo collection is not set in LoggerContext, use default collection 'undefined'");
                }
                connectionSource.setCollection(collection);
            }

            Document doc = new Document();
            String threadName = event.getThreadName();
            doc.put("thread", threadName);
            doc.put("name", event.getLoggerName());
            InetAddress ia = InetAddress.getLocalHost();
            String hostname = ia.getHostName();
            doc.put("hostname", hostname);
            doc.put("timestamp", new Date(event.getLoggerContextVO().getBirthTime()));
            if (threadName.startsWith("error") || threadName.startsWith("input")) {
                String message = event.getMessage();
                //添加日志基本匹配
                String l = threadName.startsWith("error") ? "STDERR" : "STDOUT";
                for (String level : LEVELS) {
                    if (message.contains(level)) {
                        l = level;
                        break;
                    }
                }
                //添加异常信息匹配
                if (message.startsWith("Exception in thread")) {
                    l = "ERROR";
                }
                //添加异常堆栈匹配
                if (message.matches("\\s+at (\\w+\\.)+.*\\w+\\(.*\\)")) {
                    l = "ERROR";
                }
                doc.put("message", event.getMessage() + "\n");
                doc.put("level", l);
            } else {
                doc.put("message", layout.doLayout(event));
                doc.put("level", event.getLevel().toString());
            }
            MongoCollection<Document> coll = connectionSource.getDBCollection();
            coll.insertOne(doc);
        } catch (Exception e) {
            Util.report(e.getMessage(), e);
        }
    }

    public void setConnectionSource(MongodbConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }
}
