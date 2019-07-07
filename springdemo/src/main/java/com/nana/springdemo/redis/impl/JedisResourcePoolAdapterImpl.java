package com.nana.springdemo.redis.impl;

import com.nana.springdemo.redis.IRedisResourcePoolAdapter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Jedis实现
 *
 * @author liuqingliang
 * @create 2018-02-11
 */
public class JedisResourcePoolAdapterImpl implements IRedisResourcePoolAdapter {
    private JedisPool jedisPool;

    public JedisResourcePoolAdapterImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Jedis getResource() {
        return jedisPool.getResource();
    }
}
