package zone.medolia.login.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import zone.medolia.login.redis.key.KeyPrefix;

@Service
@Slf4j
public class RedisService {

    JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取值
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的 key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置值
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0)
                return false;

            // 生成真正的 key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) jedis.set(realKey, str);
            else jedis.setex(realKey, seconds, str);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断值是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除单个 key
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey); // 错误类型会返回 -1
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * beanToStr() 参数校验，对于不同类型的 value：
     * 1. null 返回 null；
     * 2. 对于整形或长整形，返回字符串形式的值
     * 3. 对于字符串，原样返回
     * 4. 其他情况，返回 JSON 化的 value
     */
    public static <T> String beanToString(T value) {
        if (value == null) return null;

        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class || clazz == long.class || clazz == Long.class)
            return "" + value;
        else if (clazz == String.class) return (String) value;
        else return JSON.toJSONString(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String str, Class<T> clazz) {
        // 判断是否为空
        if (str == null || str.length() <= 0 || clazz == null)
            return null;

        // 判断整型、字符串（有可能是已经缓存的页面）
        if (clazz == int.class || clazz == Integer.class)
            return (T) Integer.valueOf(str);
        else if (clazz == long.class || clazz == Long.class)
            return (T) Long.valueOf(str);
            // 须返回 String 格式的 html 页面
        else if (clazz == String.class)
            return (T) str;
        else
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

}
