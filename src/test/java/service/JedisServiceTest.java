package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.service.common.impl.JedisService;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class JedisServiceTest {
    @Autowired
    private JedisService jedisService;
    static List<Integer> apple = new ArrayList<>();

    @Test
    public void testNotLock() {

        String lockKey = String.format(JedisConst.LOCK_KEY, "lock");
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    apple.add(apple.size());
                }
            }).start();
        }
        LogRecord.print(apple);
    }

    @Test
    public void testLock() {
        String lockKey = String.format(JedisConst.LOCK_KEY, "lock");
        long cu = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                String identifier = jedisService.lock(lockKey, 1000, 6000);
                if (identifier != null) {
                    apple.add(apple.size());
                    jedisService.unLock(lockKey, identifier);
                    LogRecord.print(System.currentTimeMillis() - cu);
                } else {
                    LogRecord.print("获取锁失败");
                }

            }).start();
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogRecord.print(apple);
    }
}
