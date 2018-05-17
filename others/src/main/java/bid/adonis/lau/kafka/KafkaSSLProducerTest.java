package bid.adonis.lau.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * KafkaSSLProducer
 *
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/4/12 9:34
 */
public class KafkaSSLProducerTest {

    String kafkaIpList;
    String topic;
    String password;

    public KafkaSSLProducerTest(String kafkaIpList, String topic, String password) {
        this.kafkaIpList = kafkaIpList;
        this.topic = topic;
        this.password = password;
    }

    private Properties getSSLProperties() {
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", kafkaIpList);
        producerProps.put("security.protocol", "SSL");
        producerProps.put("compression.type", "gzip");
        producerProps.put("metadata.fetch.timeout.ms", "1000000");
//        producerProps.put("ssl.truststore.location", "/data/meepoAppData/workspace/user/meepo_job/meepo4_inner/package/client.truststore.jks");
        producerProps.put("ssl.keystore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/user_jks/meepo_job/client.keystore.jks");
        producerProps.put("ssl.truststore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/user_jks/meepo_job/client.truststore.jks");
        producerProps.put("ssl.truststore.password", password);
//        producerProps.put("ssl.keystore.location", "/data/meepoAppData/workspace/user/meepo_job/meepo4_inner/package/client.keystore.jks");
        producerProps.put("ssl.keystore.password", password);
        producerProps.put("ssl.key.password", password);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        producerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return producerProps;
    }


    public void startProduce() throws Exception {
        KafkaProducer producer = new KafkaProducer(getSSLProperties());
        for (int i = 0; i < 50; i++) {
            String value = "消息生产 : " + i;
            Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(topic, topic, value));
            System.out.println("一条消息生成完毕 i: " + i);
            Thread.sleep(500);
        }
        producer.close();
    }

    public static void main(String[] args) throws Exception {
        KafkaSSLProducerTest kafkaSSLProducer = new KafkaSSLProducerTest(args[0], args[1], args[2]);  //brockList    topic	  password
        kafkaSSLProducer.startProduce();
    }

}
