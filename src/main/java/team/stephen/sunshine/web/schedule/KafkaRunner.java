package team.stephen.sunshine.web.schedule;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Email;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.util.common.LogRecod;

import javax.mail.MessagingException;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author Stephen
 */
@Component
public class KafkaRunner implements CommandLineRunner {
    @Autowired
    private Consumer consumer;
    @Autowired
    private MailService mailService;

    private Executor emailExecutor;
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int KEE_ALIVE_TIME = 30;

    @Override
    public void run(String... strings) {
        emailExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEE_ALIVE_TIME,
                TimeUnit.MINUTES, new SynchronousQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
//        consume(Lists.newArrayList(Topic.EMAIL.getName(), Topic.LOG.getName()));
    }

    private void consume(List<String> topics) {
        consumer.subscribe(topics);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord record : records) {
                    Topic topic = Topic.getTopic(record.topic());
                    if (topic != null) {
                        LogRecod.print(topic + "\t" + record.key() + ":" + record.value());
                        switch (topic) {
                            case EMAIL:
                                try {
                                    String mailStr = record.value().toString();
                                    Email email = JSONObject.parseObject(mailStr, Email.class);
                                    emailExecutor.execute(() -> {
                                        try {
                                            mailService.sendMail(email);
                                        } catch (NullParamException | MessagingException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
