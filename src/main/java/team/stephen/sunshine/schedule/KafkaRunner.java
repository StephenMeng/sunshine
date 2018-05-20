package team.stephen.sunshine.schedule;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.util.LogRecod;

import java.util.List;


@Component
public class KafkaRunner implements CommandLineRunner {
    @Autowired
    private Consumer consumer;

    @Override
    public void run(String... strings) throws Exception {
        consume(Lists.newArrayList(Topic.EMAIL.getName(), Topic.LOG.getName()));
    }

    private void consume(List<String> topics) {
        consumer.subscribe(topics);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord record : records) {
                    Topic topic=Topic.getTopic(record.topic());
                    LogRecod.print(topic + "\t" + record.key() + ":" + record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
