package bid.adonis.lau.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * KafkaSSLConsumer
 *
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/4/12 9:34
 */
public class KafkaSSLConsumerTest {

    String kafkaIpList;
    String topic;
    String password;

    public KafkaSSLConsumerTest(String ipList, String topic, String password) {
        this.kafkaIpList = ipList;
        this.topic = topic;
        this.password = password;
    }

    private Properties getSSLProperties() {
        Properties producerProps = new Properties();
        producerProps.put("group.id", "groupID");
        producerProps.put("bootstrap.servers", kafkaIpList);
        producerProps.put("security.protocol", "SSL");
//        producerProps.put("ssl.truststore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/client.truststore.jks");
//        producerProps.put("ssl.truststore.location", "/data/meepoAppData/workspace/user/meepo_job/meepo4_inner/package/client.truststore.jks");
        producerProps.put("ssl.truststore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/user_jks/meepo_job/client.truststore.jks");
        producerProps.put("ssl.truststore.password", this.password);
//        producerProps.put("ssl.keystore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/client.keystore.jks");
//        producerProps.put("ssl.keystore.location", "/data/meepoAppData/workspace/user/meepo_job/meepo4_inner/package/client.keystore.jks");
        producerProps.put("ssl.keystore.location", "/data/meepoAppData/meepo-executor/plugins/kafka/user_jks/meepo_job/client.keystore.jks");
        producerProps.put("ssl.keystore.password", this.password);
        producerProps.put("ssl.key.password", this.password);
        producerProps.put("auto.offset.reset", "latest");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        producerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return producerProps;
    }

    public void startConsume() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(getSSLProperties());
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(record.value());
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("**************** start **************** ");
        KafkaSSLConsumerTest kafkaSSLConsumer = new KafkaSSLConsumerTest(args[0], args[1], args[2]);
        kafkaSSLConsumer.startConsume();
    }
}
