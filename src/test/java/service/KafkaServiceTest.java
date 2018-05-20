package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.exception.WrongClassTypeException;
import team.stephen.sunshine.service.common.KafkaService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class KafkaServiceTest {
    @Autowired
    private KafkaService kafkaService;
    Topic topic = Topic.EMAIL;

    @Test
    public void testProduce() {
        for (int i = 0; i < 50; i++) {
            try {
                kafkaService.produce(topic, "content:email:" + i);
            } catch (WrongClassTypeException | NullParamException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testProduceLog() {
        for (int i = 0; i < 10; i++) {
            try {
                kafkaService.produce(Topic.LOG, "content:log:" + i);
            } catch (WrongClassTypeException | NullParamException e) {
                e.printStackTrace();
            }
        }
    }

}
