package com.nana.springdemo.redis.impl;


import com.nana.springdemo.redis.IRedisCache;
import com.nana.springdemo.redis.IRedisResourcePoolAdapter;
import com.nana.springdemo.redis.serialize.IBinarySerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;


/**
 * redis缓存操作接口实现类
 */
public class RedisCacheImpl implements IRedisCache {

    private static Logger log = LoggerFactory.getLogger(RedisCacheImpl.class);


    private IRedisResourcePoolAdapter redisResourcePoolAdapter;
    private IBinarySerializable binarySerializable;

    public RedisCacheImpl(IRedisResourcePoolAdapter redisResourcePoolAdapter, IBinarySerializable binarySerializable) {
        this.redisResourcePoolAdapter = redisResourcePoolAdapter;
        this.binarySerializable = binarySerializable;
    }

    public void setRedisResourcePoolAdapter(
            IRedisResourcePoolAdapter redisResourcePoolAdapter) {
        this.redisResourcePoolAdapter = redisResourcePoolAdapter;
    }

    public void setBinarySerializable(IBinarySerializable binarySerializable) {
        this.binarySerializable = binarySerializable;
    }

    private static interface RedisCmd<T> {
        public T run(Jedis jedis) throws UnsupportedEncodingException;
    }

    /**
     * 增加keys参数，异常的时候打印key，有利于问题排查
     * 上边的方法得一个个的修改。。
     *
     * @param runner
     * @param valueOfErr
     * @param keys
     * @param <T>
     * @return
     */
    public <T> T runCmd(RedisCmd<T> runner, T valueOfErr, String... keys) {
        Jedis jedis = null;
        try {
            jedis = redisResourcePoolAdapter.getResource();
            return runner.run(jedis);
        } catch (Exception e) {
//            log.error("redis exception,keys:" + StringUtils.join(Arrays.asList(keys), ","), e);
//            log.error("redis exception,keys:" + keys, e);
            return valueOfErr;
        } finally {
            if (jedis != null) {
                try {
                    //Jedis的close方法会自行判断是需要调用returnResource还是returnBrokenResource
                    jedis.close();
                } finally {

                }
            }
        }
    }

    public <T> T runCmd(RedisCmd<T> runner, T valueOfErr) {
        return this.runCmd(runner, valueOfErr, null);
    }

    @Override
    public int del(final String... keys) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.del(keys).intValue();
            }
        }, -1);
    }

    @Override
    public int exists(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.exists(key) ? 1 : 0;
            }
        }, -1);
    }

    @Override
    public int expire(final String key, final int seconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.expire(key, seconds).intValue();
            }
        }, -1);
    }

    @Override
    public int pexpire(final String key, final long miliSeconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.pexpire(key, miliSeconds).intValue();
            }
        }, -1);
    }

    @Override
    public int expireAt(final String key, final long unixTime) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.expireAt(key, unixTime).intValue();
            }
        }, -1);
    }

    @Override
    public int pexpireAt(final String key, final long miliUnixTime) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.pexpireAt(key, miliUnixTime).intValue();
            }
        }, -1);
    }

    @Override
    public int persist(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.persist(key).intValue();
            }
        }, -1);
    }

    @Override
    public int ttl(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.ttl(key).intValue();
            }
        }, -3);
    }

    @Override
    public long pttl(final String key) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.pttl(key);
            }
        }, -3L);
    }

    @Override
    public int append(final String key, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.append(key, value).intValue();
            }
        }, -1);
    }

    @Override
    public long decr(final String key, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.decr(key);
            }
        }, valueOfErr);
    }

    @Override
    public long decrBy(final String key, final long byValue, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.decrBy(key, byValue);
            }
        }, valueOfErr);
    }

    @Override
    public long incr(final String key, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.incr(key);
            }
        }, valueOfErr);
    }

    @Override
    public long incrBy(final String key, final long byValue, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.incrBy(key, byValue);
            }
        }, valueOfErr);
    }

    @Override
    public String getString(final String key, String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.get(key);
            }
        }, valueOfErr);
    }

    @Override
    public String getString(final String key) {
        return this.getString(key, null);
    }

    @Override
    public long getLong(final String key, final long valueOfNotExist, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String s = jedis.get(key);
                if (s == null || s.length() == 0) {
                    return valueOfNotExist;
                } else {
                    try {
                        long value = Long.valueOf(s);
                        return value;
                    } catch (NumberFormatException e) {
                        log.error("redis getLong s:{},异常:{}", s, e);
                    }
                    return valueOfErr;
                }
            }
        }, valueOfErr);
    }

    @Override
    public long getLong(final String key) {
        return this.getLong(key, 0, 0);
    }

    @Override
    public <E> E get(final String key, final Class<E> clazz, E valueOfErr) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] data = jedis.get(key.getBytes());
                if (data != null) {
                    return binarySerializable.toObject(data, clazz);
                }
                return null;
            }
        }, valueOfErr);
    }

    @Override
    public <E> E get(String key, Class<E> clazz) {
        return this.get(key, clazz, null);
    }

    @Override
    public String getSetString(final String key, final String value, String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.getSet(key, value);
            }
        }, valueOfErr);
    }

    @Override
    public String getSetString(String key, String value) {
        return this.getSetString(key, value, null);
    }

    @Override
    public long getSetLong(String key, long value, long valueOfErr) {
        String v = this.getSetString(key, "" + value, "" + valueOfErr);
        if (v != null && v.length() > 0) {
            return Long.parseLong(v);
        }
        return 0;
    }

    @Override
    public long getSetLong(String key, long value) {
        return this.getSetLong(key, value, 0);
    }

    @Override
    public <E> E getSet(final String key, final E value, E valueOfErr, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] iData = binarySerializable.toBinary(value);
                byte[] oData = jedis.getSet(key.getBytes(), iData);
                return binarySerializable.toObject(oData, clazz);
            }
        }, valueOfErr);
    }

    @Override
    public <E> E getSet(String key, E value, Class<E> clazz) {
        return this.getSet(key, value, null, clazz);
    }

    @Override
    public Map<String, String> mgetString(final String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, String>>() {
            public Map<String, String> run(Jedis jedis) {
                List<String> list = jedis.mget(keys);
                return transforKeyArrStringListToStringMap(keys, list);
            }
        }, null);
    }

    @Override
    public Map<String, Long> mgetLong(final String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                List<String> list = jedis.mget(keys);
                return transforKeyArrValueListToLongMap(keys, list);
            }
        }, null);
    }

    @Override
    public <E> Map<String, E> mget(final Class<E> clazz, final String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, E>>() {
            public Map<String, E> run(Jedis jedis) {
                byte[][] byteKeys = transforStringArrayToByte(keys);
                List<byte[]> retBytes = jedis.mget(byteKeys);
                return transforKeyArrValueListToObjMap(keys, retBytes, clazz);
            }
        }, null);
    }

    @Override
    public int msetString(final Map<String, String> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.mset(transforStringMapToStringArr(keyValues));
                return 0;
            }
        }, -1);
    }

    @Override
    public int msetLong(final Map<String, Long> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.mset(transforLongMapToStringArr(keyValues));
                return 0;
            }
        }, -1);
    }

    @Override
    public <E> int mset(final Map<String, E> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.mset(transforObjMapToBytesArr(keyValues));
                return 0;
            }
        }, -1);
    }

    @Override
    public int msetNxString(final Map<String, String> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.msetnx(transforStringMapToStringArr(keyValues)).intValue();
            }
        }, -1);
    }

    @Override
    public int msetNxLong(final Map<String, Long> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.msetnx(transforLongMapToStringArr(keyValues)).intValue();
            }
        }, -1);
    }

    @Override
    public <E> int msetNx(final Map<String, E> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.msetnx(transforObjMapToBytesArr(keyValues)).intValue();
            }
        }, -1);
    }

    @Override
    public int psetEx(final String key, final String value, final int milliSeconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.psetex(key, milliSeconds, value);
                return 0;
            }
        }, -1);
    }

    @Override
    public int psetEx(String key, long value, int milliSeconds) {
        return psetEx(key, "" + value, milliSeconds);
    }

    @Override
    public <E> int psetEx(final String key, final E value, final int milliSeconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.psetex(key.getBytes(), milliSeconds, binarySerializable.toBinary(value));
                return 0;
            }
        }, -1);
    }

    @Override
    public int set(final String key, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.set(key, value);
                return 0;
            }
        }, -1);
    }

    @Override
    public int set(String key, long value) {
        return this.set(key, "" + value);
    }

    @Override
    public <E> int set(final String key, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.set(key.getBytes(), binarySerializable.toBinary(value));
                return 0;
            }
        }, -1);
    }

    @Override
    public int setEx(final String key, final String value, final int seconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.setex(key, seconds, value);
                return 0;
            }
        }, -1);
    }

    @Override
    public int setEx(String key, long value, int seconds) {
        return this.setEx(key, "" + value, seconds);
    }

    @Override
    public <E> int setEx(final String key, final E value, final int seconds) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.setex(key.getBytes(), seconds, binarySerializable.toBinary(value));
                return 0;
            }
        }, -1);
    }

    @Override
    public int setNx(final String key, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.setnx(key, value).intValue();
            }
        }, -1);
    }

    @Override
    public int setNx(String key, long value) {
        return this.setNx(key, "" + value);
    }

    @Override
    public <E> int setNx(final String key, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.setnx(key.getBytes(), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public int hdel(final String key, final String... fields) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hdel(key, fields).intValue();
            }
        }, -1);
    }

    @Override
    public int hexists(final String key, final String field) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hexists(key, field) ? 1 : 0;
            }
        }, -1);
    }

    @Override
    public String hgetString(final String key, final String field, String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.hget(key, field);
            }
        }, valueOfErr);

    }

    @Override
    public String hgetString(String key, String field) {
        return this.hgetString(key, field, null);
    }

    @Override
    public long hgetLong(final String key, final String field, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String v = jedis.hget(key, field);
                if (v == null || v.length() == 0) {
                    return 0L;
                }
                return Long.valueOf(v);
            }
        }, valueOfErr);
    }

    @Override
    public long hgetLong(String key, String field) {
        return this.hgetLong(key, field, 0);
    }

    @Override
    public <E> E hget(final String key, final String field, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] v = jedis.hget(key.getBytes(), field.getBytes());
                if (v == null || v.length == 0) {
                    return null;
                }
                return binarySerializable.toObject(v, clazz);
            }
        }, null);
    }

    @Override
    public Object hget(final String key, final Integer field) {
        return this.runCmd(new RedisCmd<Object>() {
            public Object run(Jedis jedis) {
                byte[] v = jedis.hget(key.getBytes(), binarySerializable.toBinary(field));
                if (v == null || v.length == 0) {
                    return null;
                }
                return binarySerializable.toObject(v, Double.class);
            }
        }, null);
    }

    @Override
    public Map<String, String> hgetAllOfString(final String key) {
        return this.runCmd(new RedisCmd<Map<String, String>>() {
            public Map<String, String> run(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        }, null);
    }

    @Override
    public Map<String, Long> hgetAllOfLong(final String key) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Map<String, String> sMap = jedis.hgetAll(key);
                return transforStringMapToLongMap(sMap);
            }
        }, null);
    }

    @Override
    public <E> Map<String, E> hgetAll(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<Map<String, E>>() {
            public Map<String, E> run(Jedis jedis) {

                Map<byte[], byte[]> bytesMap = jedis.hgetAll(key.getBytes());
                return transforBytesMapToObjMap(bytesMap, clazz);
            }
        }, null);
    }

    @Override
    public long hincr(String key, String field, long valueOfErr) {
        return this.hincrby(key, field, 1, valueOfErr);
    }

    @Override
    public long hdecr(String key, String field, long valueOfErr) {
        return this.hincrby(key, field, -1, valueOfErr);
    }

    @Override
    public long hincrby(final String key, final String field, final long increment,
                        long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                return jedis.hincrBy(key, field, increment);
            }
        }, valueOfErr);
    }

    @Override
    public Set<String> hkeys(final String key) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.hkeys(key);
            }
        }, null);
    }

    @Override
    public int hlen(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hlen(key).intValue();
            }
        }, -1);
    }

    @Override
    public Map<String, String> hmgetOfString(final String key, final String... fields) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, String>>() {
            public Map<String, String> run(Jedis jedis) {
                List<String> list = jedis.hmget(key, fields);
                return transforKeyArrStringListToStringMap(fields, list);
            }
        }, null);
    }

    @Override
    public Map<String, Long> hmgetOfLong(final String key, final String... fields) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                List<String> list = jedis.hmget(key, fields);
                return transforKeyArrValueListToLongMap(fields, list);
            }
        }, null);
    }

    @Override
    public <E> Map<String, E> hmget(final String key, final Class<E> clazz,
                                    final String... fields) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        return this.runCmd(new RedisCmd<Map<String, E>>() {
            public Map<String, E> run(Jedis jedis) {
                List<byte[]> list = jedis.hmget(key.getBytes(), transforStringArrayToByte(fields));
                return transforKeyArrValueListToObjMap(fields, list, clazz);
            }
        }, null);
    }

    @Override
    public int hmsetString(final String key, final Map<String, String> fieldValues) {
        if (fieldValues == null || fieldValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.hmset(key, fieldValues);
                return 0;
            }
        }, -1);
    }

    @Override
    public int hmsetLong(final String key, final Map<String, Long> fieldValues) {
        if (fieldValues == null || fieldValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.hmset(key, transforLongMapToStringMap(fieldValues));
                return 0;
            }
        }, -1);
    }

    @Override
    public <E> int hmset(final String key, final Map<String, E> fieldValues) {
        if (fieldValues == null || fieldValues.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.hmset(key.getBytes(), transforObjMapToBytesMap(fieldValues));
                return 0;
            }
        }, -1);
    }

    @Override
    public int hset(final String key, final String field, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hset(key, field, value).intValue();
            }
        }, -1);
    }

    @Override
    public int hset(String key, String field, long value) {
        return this.hset(key, field, "" + value);
    }

    @Override
    public <E> int hset(final String key, final String field, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hset(key.getBytes(), field.getBytes(), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public int hset(String key, Integer field, final double value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hset(key.getBytes(), binarySerializable.toBinary(field), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public int hsetnx(final String key, final String field, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hsetnx(key, field, value).intValue();
            }
        }, -1);
    }

    @Override
    public int hsetnx(String key, String field, long value) {
        return this.hsetnx(key, field, "" + value);
    }

    @Override
    public <E> int hsetnx(final String key, final String field, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.hsetnx(key.getBytes(), field.getBytes(), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public List<String> hvalsOfString(final String key) {
        return this.runCmd(new RedisCmd<List<String>>() {
            public List<String> run(Jedis jedis) {
                return jedis.hvals(key);
            }
        }, null);
    }

    @Override
    public List<Long> hvalsOfLong(final String key) {
        return this.runCmd(new RedisCmd<List<Long>>() {
            public List<Long> run(Jedis jedis) {
                List<String> tmpList = jedis.hvals(key);
                return transforStringListToLong(tmpList);
            }
        }, null);
    }

    @Override
    public <E> List<E> hvals(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<List<E>>() {
            public List<E> run(Jedis jedis) {
                List<byte[]> tmpList = jedis.hvals(key.getBytes());
                return transforBytesListToObj(tmpList, clazz);
            }
        }, null);
    }

    @Override
    public String lindexOfString(final String key, final int index, String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.lindex(key, index);
            }
        }, valueOfErr);
    }

    @Override
    public String lindexOfString(String key, int index) {
        return this.lindexOfString(key, index, null);
    }

    @Override
    public long lindexOfLong(final String key, final int index, final long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String v = jedis.lindex(key, index);
                if (v != null && v.length() > 0) {
                    return Long.valueOf(v);
                }
                return valueOfErr;
            }
        }, valueOfErr);
    }

    @Override
    public long lindexOfLong(String key, int index) {
        return lindexOfLong(key, index, 0L);
    }

    @Override
    public <E> E lindex(final String key, final int index, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] v = jedis.lindex(key.getBytes(), index);
                if (v != null && v.length > 0) {
                    return binarySerializable.toObject(v, clazz);
                }
                return null;
            }
        }, null);
    }

    @Override
    public int llen(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.llen(key).intValue();
            }
        }, -1);
    }

    @Override
    public String lpopOfString(final String key) {
        return this.lpopOfString(key, null);
    }

    @Override
    public String lpopOfString(final String key, String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.lpop(key);
            }
        }, valueOfErr);
    }

    @Override
    public long lpopOfLong(String key) {
        return this.lpopOfLong(key, 0L);
    }

    @Override
    public long lpopOfLong(final String key, long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String v = jedis.lpop(key);
                if (v != null && v.length() > 0) {
                    return Long.valueOf(v);
                }
                return 0L;
            }
        }, valueOfErr);
    }

    @Override
    public <E> E lpop(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] data = jedis.lpop(key.getBytes());
                if (data != null && data.length > 0) {
                    return binarySerializable.toObject(data, clazz);
                }
                return null;
            }
        }, null);
    }

    @Override
    public int lpush(final String key, final String value) {
        return this.lpush(key, new String[]{value});
    }

    @Override
    public int lpush(final String key, final String... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpush(key, values).intValue();
            }
        }, -1);
    }

    @Override
    public int lpush(final String key, final long value) {
        return this.lpush(key, new long[]{value});
    }

    @Override
    public int lpush(final String key, final long... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpush(key, transforLongArrToString(values)).intValue();
            }
        }, -1);
    }

    @Override
    public <E> int lpush(final String key, final E value) {
        List<E> list = new ArrayList<E>();
        list.add(value);
        return this.lpush(key, list);
    }

    @Override
    public <E> int lpush(final String key, final List<E> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpush(key.getBytes(), transforObjArrayToBytes(values)).intValue();
            }
        }, -1);
    }

    @Override
    public int lpushx(final String key, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpushx(key, value).intValue();
            }
        }, -1);
    }

    @Override
    public int lpushx(final String key, final long value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpushx(key, "" + value).intValue();
            }
        }, -1);
    }

    @Override
    public <E> int lpushx(final String key, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lpushx(key.getBytes(), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public List<String> lrangeOfString(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<List<String>>() {
            public List<String> run(Jedis jedis) {
                return jedis.lrange(key, start, stop);
            }
        }, null);
    }

    @Override
    public List<Long> lrangeOfLong(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<List<Long>>() {
            public List<Long> run(Jedis jedis) {
                List<String> tmpList = jedis.lrange(key, start, stop);
                return transforStringListToLong(tmpList);
            }
        }, null);
    }

    @Override
    public <E> List<E> lrange(final String key, final int start, final int stop, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<List<E>>() {
            public List<E> run(Jedis jedis) {
                List<byte[]> tmpList = jedis.lrange(key.getBytes(), start, stop);
                return transforBytesListToObj(tmpList, clazz);
            }
        }, null);
    }

    @Override
    public int lrem(final String key, final int count, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lrem(key, count, value).intValue();
            }
        }, -1);
    }

    @Override
    public int lrem(final String key, final int count, final long value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lrem(key, count, value + "").intValue();
            }
        }, -1);
    }

    @Override
    public <E> int lrem(final String key, final int count, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.lrem(key.getBytes(), count, binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public int lset(final String key, final int index, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.lset(key, index, value);
                return 0;
            }
        }, -1);
    }

    @Override
    public int lset(final String key, final int index, final long value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.lset(key, index, value + "");
                return 0;
            }
        }, -1);
    }

    @Override
    public <E> int lset(final String key, final int index, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.lset(key.getBytes(), index, binarySerializable.toBinary(value));
                return 0;
            }
        }, -1);
    }

    @Override
    public int ltrim(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                jedis.ltrim(key, start, stop);
                return 0;
            }
        }, -1);
    }

    @Override
    public String rpopOfString(final String key) {
        return this.rpopOfString(key, null);
    }

    @Override
    public String rpopOfString(final String key, final String valueOfErr) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.rpop(key);
            }
        }, valueOfErr);
    }

    @Override
    public long rpopOfLong(String key) {
        return this.rpopOfLong(key, 0L);
    }

    @Override
    public long rpopOfLong(final String key, final long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String v = jedis.rpop(key);
                if (v == null || v.length() == 0) {
                    return valueOfErr;
                }
                return Long.valueOf(v);
            }
        }, valueOfErr);
    }

    @Override
    public <E> E rpop(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] v = jedis.rpop(key.getBytes());
                if (v != null && v.length > 0) {
                    return binarySerializable.toObject(v, clazz);
                }
                return null;
            }
        }, null);
    }

    @Override
    public int rpush(final String key, final String value) {
        return this.rpush(key, new String[]{value});
    }

    @Override
    public int rpush(final String key, final String... values) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.rpush(key, values).intValue();
            }
        }, -1);
    }

    @Override
    public int rpush(String key, long value) {
        return this.rpush(key, new long[]{value});
    }

    @Override
    public int rpush(String key, long... values) {
        return this.rpush(key, this.transforLongArrToString(values));
    }

    @Override
    public <E> int rpush(final String key, E value) {
        List<E> list = new ArrayList<E>();
        list.add(value);
        return this.rpush(key, list);
    }

    @Override
    public <E> int rpush(final String key, final List<E> values) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.rpush(key.getBytes(), transforObjArrayToBytes(values)).intValue();
            }
        }, -1);
    }

    @Override
    public int rpushx(final String key, final String value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.rpushx(key, value).intValue();
            }
        }, -1);
    }

    @Override
    public int rpushx(String key, long value) {
        return this.rpushx(key, "" + value);
    }

    @Override
    public <E> int rpushx(final String key, final E value) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.rpushx(key.getBytes(), binarySerializable.toBinary(value)).intValue();
            }
        }, -1);
    }

    @Override
    public int sadd(final String key, final String value) {
        return this.sadd(key, new String[]{value});
    }

    @Override
    public int sadd(final String key, final String... values) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.sadd(key, values).intValue();
            }
        }, -1);
    }

    @Override
    public int sadd(String key, long value) {
        return this.sadd(key, new long[]{value});
    }

    @Override
    public int sadd(String key, long... values) {
        return this.sadd(key, this.transforLongArrToString(values));
    }

    @Override
    public <E> int sadd(final String key, final E value) {
        List<E> list = new ArrayList<E>();
        list.add(value);
        return this.sadd(key, list);
    }

    @Override
    public <E> int sadd(final String key, final List<E> values) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.sadd(key.getBytes(), transforObjArrayToBytes(values)).intValue();
            }
        }, -1);
    }

    @Override
    public int scard(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.scard(key).intValue();
            }
        }, -1);
    }

    @Override
    public int sismember(final String key, final String member) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.sismember(key, member) ? 1 : 0;
            }
        }, -1);
    }

    @Override
    public int sismember(final String key, final long member) {
        return this.sismember(key, "" + member);
    }

    @Override
    public Set<String> smembersOfString(final String key) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.smembers(key);
            }
        }, null);
    }

    @Override
    public Set<Long> smembersOfLong(final String key) {
        return this.runCmd(new RedisCmd<Set<Long>>() {
            public Set<Long> run(Jedis jedis) {
                Set<String> ret = jedis.smembers(key);
                return transforStringSetToLong(ret);
            }
        }, null);
    }

    @Override
    public <E> Set<E> smembers(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<Set<E>>() {
            public Set<E> run(Jedis jedis) {
                Set<byte[]> ret = jedis.smembers(key.getBytes());
                return transforBytesSetToObj(ret, clazz);
            }
        }, null);
    }

    @Override
    public String spopOfString(final String key) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.spop(key);
            }
        }, null);
    }

    @Override
    public long spopOfLong(final String key) {
        return this.spopOfLong(key, -1L);
    }

    @Override
    public long spopOfLong(final String key, final long valueOfErr) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String v = jedis.spop(key);
                if (v != null && v.length() > 0) {
                    return Long.valueOf(v);
                }
                return 0L;
            }
        }, valueOfErr);
    }

    @Override
    public <E> E spop(final String key, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<E>() {
            public E run(Jedis jedis) {
                byte[] v = jedis.spop(key.getBytes());
                if (v != null && v.length > 0) {
                    return binarySerializable.toObject(v, clazz);
                }
                return null;
            }
        }, null);
    }

    @Override
    public String srandomMemberOfString(final String key) {
        return this.runCmd(new RedisCmd<String>() {
            public String run(Jedis jedis) {
                return jedis.srandmember(key);
            }
        }, null);
    }

    @Override
    public List<String> srandomMemberOfString(final String key, final int count) {
        return this.runCmd(new RedisCmd<List<String>>() {
            public List<String> run(Jedis jedis) {
                return jedis.srandmember(key, count);
            }
        }, null);
    }

    @Override
    public long srandomMemberOfLong(final String key) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                String r = jedis.srandmember(key);
                if (r != null && r.length() > 0) {
                    return Long.valueOf(r);
                }
                return 0L;
            }
        }, -1L);
    }

    @Override
    public List<Long> srandomMemberOfLong(final String key, final int count) {
        return this.runCmd(new RedisCmd<List<Long>>() {
            public List<Long> run(Jedis jedis) {
                List<String> list = jedis.srandmember(key, count);
                return transforStringListToLong(list);
            }
        }, null);
    }

    @Override
    public int srem(final String key, final String member) {
        return this.srem(key, new String[]{member});
    }

    @Override
    public int srem(final String key, final String... members) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.srem(key, members).intValue();
            }
        }, -1);
    }

    @Override
    public int srem(final String key, final long member) {
        return this.srem(key, new long[]{member});
    }

    @Override
    public int srem(final String key, final long... members) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.srem(key, transforLongArrToString(members)).intValue();
            }
        }, -1);
    }

    @Override
    public <E> int srem(final String key, final E member) {
        List<E> list = new ArrayList<E>();
        list.add(member);
        return this.srem(key, list);
    }

    @Override
    public <E> int srem(final String key, final List<E> members) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.srem(key.getBytes(), transforObjArrayToBytes(members)).intValue();
            }
        }, -1);
    }

    @Override
    public int zadd(final String key, final String member, final double score) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zadd(key, score, member).intValue();
            }
        }, -1);
    }

    @Override
    public <E> int zadd(final String key, final E member, final double score) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zadd(key.getBytes(), score, binarySerializable.toBinary(member)).intValue();
            }
        }, -1);
    }

    @Override
    public int zadd(final String key, final Map<String, Double> memerScoreMap) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zadd(key, memerScoreMap).intValue();
            }
        }, -1);
    }

    @Override
    public int zadd(final String key, final long member, final double score) {
        return this.zadd(key, "" + member, score);
    }

    @Override
    public int zcard(final String key) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zcard(key).intValue();
            }
        }, -1);
    }

    @Override
    public int zcount(final String key, final double min, final double max) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zcount(key, min, max).intValue();
            }
        }, -1);
    }

    @Override
    public int zcount(final String key, final long min, final long max) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zcount(key, min, max).intValue();
            }
        }, -1);
    }

    @Override
    public double zincrBy(final String key, final String member, final double increment) {
        return this.runCmd(new RedisCmd<Double>() {
            public Double run(Jedis jedis) {
                return jedis.zincrby(key, increment, member);
            }
        }, -1D);
    }

    @Override
    public double zincrBy(final String key, final String member, final long increment) {
        return this.runCmd(new RedisCmd<Double>() {
            public Double run(Jedis jedis) {
                return jedis.zincrby(key, increment, member);
            }
        }, -1D);
    }

    @Override
    public Set<String> zrange(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrange(key, start, stop);
            }
        }, null);
    }

    @Override
    public <E> Set<E> zrange(final String key, final int start, final int stop, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<Set<E>>() {
            public Set<E> run(Jedis jedis) {
                return transforBytesSetToObj(jedis.zrange(key.getBytes(), start, stop), clazz);
            }
        }, null);
    }

    @Override
    public Map<String, Double> zrangeWithDoubleScore(final String key,
                                                     final int start, final int stop) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeWithScores(key, start, stop);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrangeWithLongScore(final String key,
                                                 final int start, final int stop) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeWithScores(key, start, stop);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public <E> Set<E> zrevrange(final String key, final int start, final int stop, final Class<E> clazz) {
        return this.runCmd(new RedisCmd<Set<E>>() {
            public Set<E> run(Jedis jedis) {
                return transforBytesSetToObj(jedis.zrevrange(key.getBytes(), start, stop), clazz);
            }
        }, null);
    }


    @Override
    public Set<String> zrevrange(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrevrange(key, start, stop);
            }
        }, null);

    }

    @Override
    public Map<String, Double> zrevrangeWithDoubleScore(final String key,
                                                        final int start, final int stop) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeWithScores(key, start, stop);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrevrangeWithLongScore(final String key,
                                                    final int start, final int stop) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeWithScores(key, start, stop);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max,
                                     final int offset, final int count) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        }, null);
    }

    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        }, null);
    }

    @Override
    public Set<String> zrangeByScore(String key, long min, long max,
                                     int offset, int count) {
        return this.zrangeByScore(key, (double) min, (double) max, offset, count);
    }

    @Override
    public Set<String> zrangeByScore(String key, long min, long max) {
        return this.zrangeByScore(key, (double) min, (double) max);
    }

    @Override
    public Map<String, Double> zrangeByScoreWithScore(final String key,
                                                      final double min, final double max, final int offset, final int count) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Double> zrangeByScoreWithScore(final String key,
                                                      final double min, final double max) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeByScoreWithScores(key, min, max);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrangeByScoreWithScore(final String key,
                                                    final long min, final long max, final int offset, final int count) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrangeByScoreWithScore(final String key,
                                                    final long min, final long max) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrangeByScoreWithScores(key, min, max);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min,
                                        final int offset, final int count) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        }, null);
    }

    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return this.runCmd(new RedisCmd<Set<String>>() {
            public Set<String> run(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min);
            }
        }, null);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, long max, long min,
                                        int offset, int count) {
        return this.zrevrangeByScore(key, (double) max, (double) min, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, long max, long min) {
        return this.zrevrangeByScore(key, (double) max, (double) min);
    }

    @Override
    public Map<String, Double> zrevrangeByScoreWithScore(final String key,
                                                         final double max, final double min, final int offset, final int count) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Double> zrevrangeByScoreWithScore(final String key,
                                                         final double max, final double min) {
        return this.runCmd(new RedisCmd<Map<String, Double>>() {
            public Map<String, Double> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeByScoreWithScores(key, max, min);
                return transforRedisTupleSetToDoubleMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrevrangeByScoreWithScore(final String key,
                                                       final long max, final long min, final int offset, final int count) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public Map<String, Long> zrevrangeByScoreWithScore(final String key,
                                                       final long max, final long min) {
        return this.runCmd(new RedisCmd<Map<String, Long>>() {
            public Map<String, Long> run(Jedis jedis) {
                Set<Tuple> set = jedis.zrevrangeByScoreWithScores(key, max, min);
                return transforRedisTupleSetToLongMap(set);
            }
        }, null);
    }

    @Override
    public int zrank(final String key, final String member) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                Long v = jedis.zrank(key, member);//member不存在时，返回null
                if (v != null) {
                    return v.intValue();
                }
                return -1;
            }
        }, -2);
    }

    @Override
    public int zrevrank(final String key, final String member) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                Long v = jedis.zrevrank(key, member);//member不存在时，返回null
                if (v != null) {
                    return v.intValue();
                }
                return -1;
            }
        }, -2);
    }

    @Override
    public int zrem(final String key, final String... member) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zrem(key, member).intValue();
            }
        }, -1);
    }

    @Override
    public int zremrangeByRank(final String key, final int start, final int stop) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zremrangeByRank(key, start, stop).intValue();
            }
        }, -1);
    }

    @Override
    public int zremrangeByScore(final String key, final double min, final double max) {
        return this.runCmd(new RedisCmd<Integer>() {
            public Integer run(Jedis jedis) {
                return jedis.zremrangeByScore(key, min, max).intValue();
            }
        }, -1);
    }

    @Override
    public int zremrangeByScore(String key, long min, long max) {
        return zremrangeByScore(key, (double) min, (double) max);
    }

    @Override
    public double zscoreOfDouble(final String key, final String member) {
        return this.runCmd(new RedisCmd<Double>() {
            public Double run(Jedis jedis) {
                Double v = jedis.zscore(key, member);
                if (v != null) {
                    return v;
                }
                return -1D;
            }
        }, -1D);
    }

    @Override
    public long zscoreOfLong(final String key, final String member) {
        return this.runCmd(new RedisCmd<Long>() {
            public Long run(Jedis jedis) {
                Double v = jedis.zscore(key, member);
                if (v != null) {
                    return v.longValue();
                }
                return -1L;
            }
        }, -1L);
    }


    //数据转换
    private byte[][] transforStringArrayToByte(String[] values) {
        if (values == null) {
            return null;
        }
        byte[][] ret = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i].getBytes();
        }
        return ret;
    }

    @SuppressWarnings("unused")
    private List<String> transforLongListToString(List<Long> values) {
        if (values == null) {
            return null;
        }
        List<String> retList = new ArrayList<String>();
        for (Long v : values) {
            retList.add(v.toString());
        }
        return retList;
    }

    private String[] transforLongArrToString(long[] values) {
        if (values == null) {
            return null;
        }
        String[] ret = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i] + "";
        }
        return ret;
    }

    private List<Long> transforStringListToLong(List<String> values) {
        if (values == null) {
            return null;
        }
        List<Long> retList = new ArrayList<Long>();
        for (String v : values) {
            retList.add(Long.valueOf(v));
        }
        return retList;
    }

    private Set<Long> transforStringSetToLong(Set<String> values) {
        if (values == null) {
            return null;
        }
        Set<Long> retSet = new HashSet<Long>();
        for (String v : values) {
            retSet.add(Long.valueOf(v));
        }
        return retSet;
    }

    @SuppressWarnings("unused")
    private <E> List<byte[]> transforObjListToBytes(List<E> values) {
        if (values == null) {
            return null;
        }
        List<byte[]> retList = new ArrayList<byte[]>();
        for (E v : values) {
            retList.add(binarySerializable.toBinary(v));
        }
        return retList;

    }

    private <E> byte[][] transforObjArrayToBytes(List<E> values) {
        if (values == null) {
            return null;
        }
        byte[][] ret = new byte[values.size()][];
        for (int i = 0; i < values.size(); i++) {
            ret[i] = binarySerializable.toBinary(values.get(i));
        }
        return ret;
    }

    private <E> List<E> transforBytesListToObj(List<byte[]> values, Class<E> clazz) {
        if (values == null) {
            return null;
        }
        List<E> retList = new ArrayList<E>();
        for (byte[] v : values) {
            retList.add(binarySerializable.toObject(v, clazz));
        }
        return retList;
    }

    private <E> Set<E> transforBytesSetToObj(Set<byte[]> values, Class<E> clazz) {
        if (values == null) {
            return null;
        }
        Set<E> retSet = new LinkedHashSet<>();
        for (byte[] v : values) {
            retSet.add(binarySerializable.toObject(v, clazz));
        }
        return retSet;

    }

    private Map<String, String> transforKeyArrStringListToStringMap(String[] keys, List<String> values) {
        Map<String, String> retMap = new HashMap<String, String>();
        int index = 0;
        for (String key : keys) {
            retMap.put(key, values.get(index++));
        }
        return retMap;
    }

    private Map<String, Long> transforKeyArrValueListToLongMap(String[] keys, List<String> values) {
        Map<String, Long> retMap = new HashMap<String, Long>();
        int index = 0;
        for (String key : keys) {
            String v = values.get(index++);
            retMap.put(key, v == null ? null : Long.valueOf(v));
        }
        return retMap;
    }

    private <E> Map<String, E> transforKeyArrValueListToObjMap(String[] keys, List<byte[]> values, Class<E> clazz) {
        Map<String, E> retMap = new HashMap<String, E>();
        int index = 0;
        for (String key : keys) {
            byte[] bs = values.get(index++);
            retMap.put(key, bs == null ? null : binarySerializable.toObject(bs, clazz));
        }
        return retMap;
    }

    private String[] transforStringMapToStringArr(Map<String, String> values) {
        if (values == null) {
            return null;
        }
        String[] ret = new String[values.size() * 2];
        int index = 0;
        for (Entry<String, String> entry : values.entrySet()) {
            ret[index++] = entry.getKey();
            ret[index++] = entry.getValue();
        }
        return ret;
    }

    private String[] transforLongMapToStringArr(Map<String, Long> values) {
        if (values == null) {
            return null;
        }
        String[] ret = new String[values.size() * 2];
        int index = 0;
        for (Entry<String, Long> entry : values.entrySet()) {
            ret[index++] = entry.getKey();
            ret[index++] = entry.getValue().toString();
        }
        return ret;
    }

    private Map<String, String> transforLongMapToStringMap(Map<String, Long> values) {
        if (values == null) {
            return null;
        }
        Map<String, String> ret = new HashMap<String, String>();
        for (Entry<String, Long> entry : values.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().toString());
        }
        return ret;
    }

    private <E> byte[][] transforObjMapToBytesArr(Map<String, E> values) {
        if (values == null) {
            return null;
        }
        byte[][] ret = new byte[values.size() * 2][];
        int index = 0;
        for (Entry<String, E> entry : values.entrySet()) {
            ret[index++] = entry.getKey().getBytes();
            ret[index++] = binarySerializable.toBinary(entry.getValue());
        }
        return ret;
    }

    private <E> Map<byte[], byte[]> transforObjMapToBytesMap(Map<String, E> values) {
        if (values == null) {
            return null;
        }
        Map<byte[], byte[]> ret = new HashMap<byte[], byte[]>();
        for (Entry<String, E> entry : values.entrySet()) {
            ret.put(entry.getKey().getBytes(), binarySerializable.toBinary(entry.getValue()));
        }
        return ret;
    }

    private Map<String, Long> transforStringMapToLongMap(Map<String, String> sMap) {
        if (sMap == null) {
            return null;
        }
        Map<String, Long> retMap = new HashMap<String, Long>();
        for (Entry<String, String> entry : sMap.entrySet()) {
            String v = entry.getValue();
            if (v == null || v.length() == 0) {
                retMap.put(entry.getKey(), 0L);
            } else {
                retMap.put(entry.getKey(), Long.valueOf(v));
            }
        }
        return retMap;
    }

    private <E> Map<String, E> transforBytesMapToObjMap(Map<byte[], byte[]> bytesMap, Class<E> clazz) {
        if (bytesMap == null) {
            return null;
        }
        Map<String, E> retMap = new HashMap<String, E>();
        for (Entry<byte[], byte[]> entry : bytesMap.entrySet()) {
            byte[] v = entry.getValue();
            if (v == null || v.length == 0) {
                retMap.put(new String(entry.getKey()), null);
            } else {
                retMap.put(new String(entry.getKey()), binarySerializable.toObject(v, clazz));
            }
        }
        return retMap;
    }

    private LinkedHashMap<String, Double> transforRedisTupleSetToDoubleMap(Set<Tuple> set) {
        if (set == null) {
            return null;
        }
        LinkedHashMap<String, Double> retMap = new LinkedHashMap<String, Double>();
        for (Tuple t : set) {
            retMap.put(t.getElement(), t.getScore());
        }
        return retMap;
    }

    private LinkedHashMap<String, Long> transforRedisTupleSetToLongMap(Set<Tuple> set) {
        if (set == null) {
            return null;
        }
        LinkedHashMap<String, Long> retMap = new LinkedHashMap<String, Long>();
        for (Tuple t : set) {
            retMap.put(t.getElement(), Double.valueOf(t.getScore()).longValue());
        }
        return retMap;
    }

    @Override
    public String getString(String key, int second) {
        this.expire(key, second);
        return this.getString(key);
    }

    @Override
    public long getLong(String key, int second) {
        this.expire(key, second);
        return this.getLong(key);
    }

    @Override
    public <E> E get(String key, Class<E> clazz, int second) {
        this.expire(key, second);
        return this.get(key, clazz);
    }

}
