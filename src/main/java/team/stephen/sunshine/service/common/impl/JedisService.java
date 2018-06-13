package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by stephen on 2017/7/15.
 */
@Component
public class JedisService {
    @Autowired
    private JedisPool jedisPool;

    private static final Long RELEASE_SUCCESS = 1L;

    public String set(final String key, final String value) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> jedis.set(key, value));
    }

    public String get(final String key) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> jedis.get(key));
    }


    public boolean exists(String key) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> jedis.exists(key));
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param expireTime 超期时间
     * @param waitTime   等待加锁时间
     * @return 是否获取成功
     */
    public String lock(String lockKey, int expireTime, int waitTime) {
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> {
            // 随机生成一个value,用以解锁时确认
            String identifier = UUID.randomUUID().toString();
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (expireTime / 1000);
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + waitTime;

            while (System.currentTimeMillis() < end) {
                if (jedis.setnx(lockKey, identifier) == 1) {
                    jedis.expire(lockKey, lockExpire);
                    return identifier;
                }

                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (jedis.ttl(lockKey) == -1) {
                    jedis.expire(lockKey, lockExpire);
                }
            }
            return null;
        });

    }

    public boolean unLock(String lockKey, String identifier) {
        if (identifier == null) {
            return false;
        }
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(identifier));
            return RELEASE_SUCCESS.equals(result);
        });
    }

    public boolean remove(String key) {
        if (StringUtils.isNull(key)) {
            return false;
        }
        JedisTemplate template = new JedisTemplate();
        return template.execute(jedis -> {
            return  jedis.del(key)>0;
        });
    }


    class JedisTemplate {
        private Jedis jedis;

        JedisTemplate() {
        }

        <T> T execute(JedisCallBack<T> callBack) {
            jedis = null;
            try {
                jedis = jedisPool.getResource();
                return callBack.handle(jedis);
            } catch (Exception e) {
                LogRecord.error(e.getMessage());
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            return null;
        }
    }

    interface JedisCallBack<T> {
        T handle(Jedis jedis);
    }
}
