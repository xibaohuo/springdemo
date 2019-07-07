package com.nana.springdemo.redis;

import redis.clients.jedis.Jedis;


public interface IRedisResourcePoolAdapter {

    /**
     * Get a jedis instance from pool.
     * <p>
     * We do not have a returnResource method, just close the jedis instance
     * returned directly.
     */
    Jedis getResource();
}
