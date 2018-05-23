package team.stephen.sunshine.web.schedule;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Email;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.util.common.LogRecod;
import team.stephen.sunshine.util.helper.FtpClientFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author Stephen
 */
@Component
public class Runner implements CommandLineRunner {
    @Autowired
    private Consumer consumer;
    @Autowired
    private MailService mailService;

    private Executor emailExecutor;

    @Value("${kafka.consumer.thread.core.pool.size}")
    private int corePoolSize;
    @Value("${kafka.consumer.thread.maxmum.pool.size}")
    private int maximumPoolSize = 20;
    /**
     * 存活时间，单位分钟
     */
    @Value("${kafka.consumer.thread.keep.alive.time}")
    private int keeAliveTime = 30;

    @Value("${sunshine.ftp.host}")
    private String ftpHost;
    @Value("${sunshine.ftp.port}")
    private int ftpPort;
    @Value("${sunshine.ftp.pool.size}")
    private int ftpClientoolSize;
    @Value("${sunshine.ftp.username}")
    private String ftpUserName;
    @Value("${sunshine.ftp.password}")
    private String ftpPassword;

    @Override
    public void run(String... strings) {
        FtpClientFactory.init(ftpHost, ftpPort, ftpClientoolSize, ftpUserName, ftpPassword);
        emailExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
                TimeUnit.MINUTES, new SynchronousQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
        //单元测试时需注释掉此处代码
        consume(Lists.newArrayList(Topic.EMAIL.getName(), Topic.LOG.getName()));
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
