package com.nana.springdemo;


//import com.nana.springdemo.redis.IRedisCache;
//import com.nana.springdemo.redis.IRedisResourcePoolAdapter;
//import com.nana.springdemo.redis.impl.JedisResourcePoolAdapterImpl;
//import com.nana.springdemo.redis.impl.RedisCacheImpl;
//import com.nana.springdemo.redis.serialize.IBinarySerializable;
//import com.nana.springdemo.redis.serialize.impl.KryoBinarySerializableImpl;
import com.nana.springdemo.redis.IRedisCache;
import com.nana.springdemo.redis.IRedisResourcePoolAdapter;
import com.nana.springdemo.redis.impl.JedisResourcePoolAdapterImpl;
import com.nana.springdemo.redis.impl.RedisCacheImpl;
import com.nana.springdemo.redis.serialize.IBinarySerializable;
import com.nana.springdemo.redis.serialize.impl.KryoBinarySerializableImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 缓存相关配置
 *
 * @author liuqingliang
 * @create 2018-02-26
 */

@Configuration
public class CacheConfiguration {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.pool}")
    private int maxActive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.minIdle}")
    private int minIdle;
    @Value("${redis.wait}")
    private int minWait;
    @Value("${redis.timeout}")
    private int timeout;



    @Bean
    public IRedisCache getRedisCache() {

        IBinarySerializable binarySerializable = new KryoBinarySerializableImpl();

        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(maxActive);
        jpc.setMaxIdle(maxIdle);
        jpc.setMinIdle(minIdle);
        jpc.setMaxWaitMillis(minWait);
        jpc.setTestOnBorrow(false);
        jpc.setTestOnReturn(false);
        jpc.setTestWhileIdle(false);
        jpc.setNumTestsPerEvictionRun(-1);
        jpc.setMinEvictableIdleTimeMillis(60000);
        jpc.setTimeBetweenEvictionRunsMillis(30000);

//        JedisPool jp = new JedisPool(jpc, redisHost, redisPort, timeout, redisPassword);
        JedisPool jp = new JedisPool(jpc, redisHost, redisPort, timeout);

        IRedisResourcePoolAdapter rrp = new JedisResourcePoolAdapterImpl(jp);

        return new RedisCacheImpl(rrp, binarySerializable);
    }


}
