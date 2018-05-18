package team.stephen.sunshine.util.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import team.stephen.sunshine.util.LogRecod;

/**
 * Created by stephen on 2017/7/15.
 */
@Component
public class JedisAdapter {
    @Autowired
    private JedisPool jedisPool;

    public String set(final String key, final String value) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(new JedisCallBack<String>() {
            @Override
            public String handle(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    public String get(final String key) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(new JedisCallBack<String>() {
            @Override
            public String handle(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public boolean exists(String key) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(new JedisCallBack<Boolean>() {
            @Override
            public Boolean handle(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    class JedisTemplate {
        private Jedis jedis;

        public JedisTemplate() {

        }

        public <T> T execute(JedisCallBack<T> callBack) {
            jedis = null;
            try {
                jedis = jedisPool.getResource();
                return callBack.handle(jedis);
            } catch (Exception e) {
                LogRecod.error(e.getMessage());
            } finally {
                jedis.close();
            }
            return null;
        }
    }

    interface JedisCallBack<T> {
        T handle(Jedis jedis);
    }
}
