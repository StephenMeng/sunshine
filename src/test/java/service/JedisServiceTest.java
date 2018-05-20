package service;


import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sun.rmi.runtime.Log;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.jedis.JedisService;
import team.stephen.sunshine.util.LogRecod;
import team.stephen.sunshine.util.RandomIDUtil;

import java.util.ArrayList;
import java.util.Date;
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
        LogRecod.print(apple);
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
                    LogRecod.print(System.currentTimeMillis() - cu);
                } else {
                    LogRecod.print("获取锁失败");
                }

            }).start();
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogRecod.print(apple);
    }
}
