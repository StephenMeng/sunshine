package team.stephen.sunshine.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import team.stephen.sunshine.constant.WebConfig;

/**
 * Created by stephen on 2017/7/15.
 */
@Configuration
public class JedisConfiguration {
    @Bean
    public JedisPool jedisPool(){
        return new JedisPool(WebConfig.REDIS_URL,WebConfig.REDIS_PORT);
    }
}
