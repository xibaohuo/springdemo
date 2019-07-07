package com.nana.springdemo.redis;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 缓存操作接口
 */
public interface IRedisCache {

    /************关键字(keys)**************/
    /**
     * 删除key
     * @param keys
     * @return
     * >=0  删除key的个数
     * -1   异常
     */
    int del(String... keys);
    /**
     * 判断key是否存在
     * @param key
     * @return
     * 1  存在
     * 0  不存在
     * -1 异常
     */
    int exists(String key);
    /**
     * 设置过期时间
     * @param key
     * @param seconds
     * @return
     * 1  如果设置了过期时间
     * 0  如果没有设置过期时间，或者不能设置过期时间
     * -1 异常
     */
    int expire(String key, int seconds);
    /**
     * 设置过期时间
     * @param key
     * @param miliSeconds
     * @return
     * 1  如果设置了过期时间
     * 0  如果没有设置过期时间，或者不能设置过期时间
     * -1 异常
     */
    int pexpire(String key, long miliSeconds);
    /**
     * 设置过期时间
     * @param key
     * @param unixTime(秒)
     * @return
     * 1  如果设置了过期时间
     * 0  如果没有设置过期时间，或者不能设置过期时间
     * -1 异常
     */
    int expireAt(String key, long unixTime);
    /**
     * 设置过期时间
     * @param key
     * @param miliUnixTime(毫秒)
     * @return
     * 1  如果设置了过期时间
     * 0  如果没有设置过期时间，或者不能设置过期时间
     * -1 异常
     */
    int pexpireAt(String key, long miliUnixTime);
    /**
     * 移出key的过期时间
     * @param key
     * @return
     * 1  当生存时间移除成功
     * 0  key 不存在或 key 没有设置生存时间
     * -1 异常
     */
    int persist(String key);

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间
     * @param key
     * @return
     * 发生异常时，返回-3
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     */
    int ttl(String key);

    /**
     * 以毫秒为单位，返回给定 key 的剩余生存时间
     * @param key
     * @return
     * 发生异常时，返回-3
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     */
    long pttl(String key);

    /************字符串(String)**************/
    /**
     * 追加一个值到key上
     * @param key
     * @param value
     * @return
     * >=0 返回append后字符串值（value）的长度
     * -1  异常
     */
    int append(String key, String value);
    /**
     * 整数原子减1
     * @param key
     * @param valueOfErr 如果发生异常时，函数会将该值返回
     * @return
     * 正常 减小之后的value
     * 异常 valueOfError
     */
    long decr(String key, long valueOfErr);
    /**
     * 原子减指定的整数
     * @param key
     * @param byValue
     * @param valueOfErr 如果发生异常时，函数会将该值返回
     * @return
     * 正常 减小之后的value
     * 异常 valueOfError
     */
    long decrBy(String key, long byValue, long valueOfErr);
    /**
     * 整数原子加1
     * @param key
     * @param valueOfErr 如果发生异常时，函数会将该值返回
     * @return
     * 正常 增加之后的value
     * 异常 valueOfError
     */
    long incr(String key, long valueOfErr);
    /**
     * 原子加指定的整数
     * @param key
     * @param byValue
     * @param valueOfErr 如果发生异常时，函数会将该值返回
     * @return
     * 正常 增加之后的value
     * 异常 valueOfError
     */
    long incrBy(String key, long byValue, long valueOfErr);
    /**
     * 获取字符串
     * @param key
     * @param valueOfErr 异常时返回
     * @return
     * 正常 redis中的值（redis中不存在时，返回null）
     * 异常 valueOfErr
     */
    String getString(String key, String valueOfErr);
    /**
     * 获取字符串
     * @param key
     * @return
     * 正常 redis中的值（redis中不存在时，返回null）
     * 异常 null
     */
    String getString(String key);

    /** 获取字符串并设置过期时间
     * @param key
     * @param second
     * @return
     */
    String getString(String key, int second);
    /**
     * 获取整数值
     * @param key
     * @param valueOfNotExist 不存在时返回
     * @param valueOfErr 异常时返回
     * @return
     * 正常 redis中的值（redis中不存在时，返回valueOfNotExist）
     * 异常 valueOffErr
     */
    long getLong(String key, long valueOfNotExist, long valueOfErr);
    /**
     * 获取整数值
     * @param key
     * @return
     * 正常 redis中的值（redis中不存在时，返回0）
     * 异常 0
     */
    long getLong(String key);

    /**
     * 获取整数值并设置过期时间
     * @param key
     * @return
     * 正常 redis中的值（redis中不存在时，返回0）
     * 异常 0
     */
    long getLong(String key, int second);
    /**
     * 获取对象
     * @param key
     * @param clazz
     * @return
     * 正常 redis中存的值（redis中不存在时，返回null）
     * 异常 valueOfErr
     */
    <E> E get(String key, Class<E> clazz, E valueOfErr);
    /**
     * 获取对象
     * @param key
     * @param clazz
     * @return
     * 正常 redis中存的值（redis中不存在时，返回null）
     * 异常 null
     */
    <E> E get(String key, Class<E> clazz);

    /**
     * 获取对象并设置过期时间
     * @param key
     * @param clazz
     * @return
     * 正常 redis中存的值（redis中不存在时，返回null）
     * 异常 null
     */
    <E> E get(String key, Class<E> clazz, int second);
    /**
     * 设置并获取设置前字符串
     * @param key
     * @param value
     * @param valueOfErr 异常时返回
     * @return
     * 正常 redis中设置前的值（redis中不存在时，返回null）
     * 异常 valueOfErr
     */
    String getSetString(String key, String value, String valueOfErr);
    /**
     * 设置并获取设置前字符串
     * @param key
     * @param value
     * @return
     * 正常 redis中设置前的值（redis中不存在时，返回null）
     * 异常 null
     */
    String getSetString(String key, String value);
    /**
     * 设置并获取设置前整数值
     * @param key
     * @param value
     * @param valueOfErr 异常时返回
     * @return
     * 正常 redis中设置前的值（redis中不存在时，返回0）
     * 异常 valueOffErr
     */
    long getSetLong(String key, long value, long valueOfErr);
    /**
     * 设置并获取设置前整数值
     * @param key
     * @param value
     * @return
     * 正常 redis中设置前的值（redis中不存在时，返回0）
     * 异常 0
     */
    long getSetLong(String key, long value);
    /**
     * 设置并获取对象
     * @param key
     * @param value
     * @param valueOfErr
     * @param clazz
     * @return
     * 正常 redis中设置前存的值（redis中不存在时，返回null）
     * 异常 valueOfErr
     */
    <E> E getSet(String key, E value, E valueOfErr, Class<E> clazz);
    /**
     * 设置并获取设置前对象
     * @param key
     * @param value
     * @param clazz
     * @return
     * 正常 redis中设置前存的值（redis中不存在时，返回null）
     * 异常 null
     */
    <E> E getSet(String key, E value, Class<E> clazz);
    /**
     * mget字符串
     * @param keys
     * @return
     * 正常 map,key为keys中的元素,如果对应的key不存在时，其对应的值为null
     * 异常 返回的map为null
     */
    Map<String, String> mgetString(String... keys);
    /**
     * mget整数
     * @param keys
     * @return
     * 正常 map,key为keys中的元素,如果对应的key不存在时，其对应的值为null
     * 异常 返回的map为null
     */
    Map<String, Long> mgetLong(String... keys);
    /**
     * mget对象
     * @param keys
     * @param clazz
     * @return
     * 正常 map,key为keys中的元素
     * 异常 返回的map为null
     */
    <E> Map<String, E> mget(Class<E> clazz, String... keys);

    /**
     * mset字符串
     * @param keyValues
     * @return
     * >=0 正常
     * <0  异常
     */
    int msetString(Map<String, String> keyValues);
    /**
     * mset整数
     * @param keyValues
     * @return
     * >=0 正常
     * <0  异常
     */
    int msetLong(Map<String, Long> keyValues);
    /**
     * mset对象
     * @param keyValues
     * @return
     * >=0 正常
     * <0  异常
     */
    <E> int mset(Map<String, E> keyValues);
    /**
     * msetnx字符串
     * @param keyValues
     * @return
     * 1  正常且所有key被set
     * 0  正常，但是至少有一个key存在
     * -1 异常
     */
    int msetNxString(Map<String, String> keyValues);
    /**
     * msetnx整数
     * @param keyValues
     * @return
     * 1  正常且所有key被set
     * 0  正常，但是至少有一个key存在
     * -1 异常
     */
    int msetNxLong(Map<String, Long> keyValues);
    /**
     * msetnx对象
     * @param keyValues
     * @return
     * 1  正常且所有key被set
     * 0  正常，但是至少有一个key存在
     * -1 异常
     */
    <E> int msetNx(Map<String, E> keyValues);
    /**
     * 设置key-value并设置过期时间（单位：毫秒）
     * @param key
     * @param value
     * @param milliSeconds
     * @return
     * >=0 正常
     * -1  异常
     */
    int psetEx(String key, String value, int milliSeconds);
    /**
     * 设置key-value并设置过期时间（单位：毫秒）
     * @param key
     * @param value
     * @param milliSeconds
     * @return
     * >=0 正常
     * -1  异常
     */
    int psetEx(String key, long value, int milliSeconds);
    /**
     * 设置key-value并设置过期时间（单位：毫秒）
     * @param key
     * @param value
     * @param milliSeconds
     * @return
     * >=0 正常
     * -1  异常
     */
    <E> int psetEx(String key, E value, int milliSeconds);

    /**
     * 设置key-value
     * @param key
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    int set(String key, String value);
    /**
     * 设置key-value
     * @param key
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    int set(String key, long value);
    /**
     * 设置key-value
     * @param key
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    <E> int set(String key, E value);

    /**
     * 设置key-value并设置过期时间（单位：秒）
     * @param key
     * @param value
     * @param seconds
     * @return
     * >=0 正常
     * -1  异常
     */
    int setEx(String key, String value, int seconds);
    /**
     * 设置key-value并设置过期时间（单位：秒）
     * @param key
     * @param value
     * @param seconds
     * @return
     * >=0 正常
     * -1  异常
     */
    int setEx(String key, long value, int seconds);
    /**
     * 设置key-value并设置过期时间（单位：秒）
     * @param key
     * @param value
     * @param seconds
     * @return
     * >=0 正常
     * -1  异常
     */
    <E> int setEx(String key, E value, int seconds);

    /**
     * 设置key-value(只有当该键不存在才设置)
     * @param key
     * @param value
     * @return
     * 1  key被set
     * 0  key没有被set
     * -1  异常
     */
    int setNx(String key, String value);
    /**
     * 设置key-value(只有当该键不存在才设置)
     * @param key
     * @param value
     * @return
     * 1  key被set
     * 0  key没有被set
     * -1  异常
     */
    int setNx(String key, long value);
    /**
     * 设置key-value(只有当该键不存在才设置)
     * @param key
     * @param value
     * @return
     * 1  key被set
     * 0  key没有被set
     * -1  异常
     */
    <E> int setNx(String key, E value);

    /************哈希(hashs)**************/
    /**
     * 删除一个或多个哈希域
     * @param key
     * @param fields
     * @return
     * >=0 删除域的个数
     * -1  异常
     */
    int hdel(String key, String... fields);
    /**
     * 判断给定域是否存在于哈希集中
     * @param key
     * @param field
     * @return
     * 1  哈希集中含有该字段
     * 0  哈希集中不含有该存在字段，或者key不存在
     * -1 异常
     */
    int hexists(String key, String field);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @param valueOfErr
     * @return
     * 正常 返回对应值（不存在时返回null）
     * 异常 valueOfErr
     */
    String hgetString(String key, String field, String valueOfErr);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @paramvalue
     * @return
     * 正常 返回对应值（不存在时返回null）
     * 异常 valueOfErr
     */
    Object hget(String key, Integer field);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @return
     * 正常 返回对应值（不存在时返回null）
     * 异常 null
     */
    String hgetString(String key, String field);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @param valueOfErr
     * @return
     * 正常 返回对应值（不存在时返回0）
     * 异常 valueOfErr
     */
    long hgetLong(String key, String field, long valueOfErr);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @return
     * 正常 返回对应值（不存在时返回0）
     * 异常 0
     */
    long hgetLong(String key, String field);
    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     * @param key
     * @param field
     * @return
     * 正常 返回对应值（不存在时返回null）
     * 异常 null
     */
    <E> E hget(String key, String field, Class<E> clazz);
    /**
     * 从哈希集中读取全部的域和值
     * @param key
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    Map<String, String> hgetAllOfString(String key);
    /**
     * 从哈希集中读取全部的域和值
     * @param key
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    Map<String, Long> hgetAllOfLong(String key);
    /**
     * 从哈希集中读取全部的域和值
     * @param key
     * @param clazz
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    <E> Map<String, E> hgetAll(String key, Class<E> clazz);
    /**
     * 将哈希集中指定域的值增加1
     * @param key
     * @param field
     * @param valueOfErr
     * @return
     * 正常 增值操作执行后的该字段的值
     * 异常 valueOfErr
     */
    long hincr(String key, String field, long valueOfErr);
    /**
     * 将哈希集中指定域的减少1
     * @param key
     * @param field
     * @param valueOfErr
     * @return
     * 正常 增值操作执行后的该字段的值
     * 异常 valueOfErr
     */
    long hdecr(String key, String field, long valueOfErr);
    /**
     * 将哈希集中指定域的值增加给定的数字
     * @param key
     * @param field
     * @param increment
     * @param valueOfErr
     * @return
     * 正常 增值操作执行后的该字段的值
     * 异常 valueOfErr
     */
    long hincrby(String key, String field, long increment, long valueOfErr);
    /**
     * 获取hash的所有字段
     * @param key
     * @return
     * 正常 哈希集中的字段列表，当 key 指定的哈希集不存在时返回空列表
     * 异常 null
     */
    Set<String> hkeys(String key);
    /**
     * 获取hash里所有字段的数量
     * @param key
     * @return
     * 正常 哈希集中字段的数量，当 key 指定的哈希集不存在时返回 0
     * 异常 -1
     */
    int hlen(String key);

    /**
     * 获取hash里面指定字段的值
     * @param key
     * @param fields
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    Map<String, String> hmgetOfString(String key, String... fields);
    /**
     * 获取hash里面指定字段的值
     * @param key
     * @param fields
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    Map<String, Long> hmgetOfLong(String key, String... fields);
    /**
     * 获取hash里面指定字段的值
     * @param key
     * @param clazz
     * @param fields
     * @return
     * 正常 返回值（key为field）
     * 异常 null
     */
    <E> Map<String, E> hmget(String key, Class<E> clazz, String... fields);
    /**
     * hmset字符串
     * @param key
     * @param fieldValues
     * @return
     * >=0 正常
     * <0  异常
     */
    int hmsetString(String key, Map<String, String> fieldValues);
    /**
     * hmset整数
     * @param key
     * @param fieldValues
     * @return
     * >=0 正常
     * <0  异常
     */
    int hmsetLong(String key, Map<String, Long> fieldValues);
    /**
     * hmset对象
     * @param key
     * @param fieldValues
     * @return
     * >=0 正常
     * <0  异常
     */
    <E> int hmset(String key, Map<String, E> fieldValues);
    /**
     * 设置hash里面一个字段的值
     * @param key
     * @param field
     * @param value
     * @return
     * 1  field是一个新的字段
     * 0  field原来在map里面已经存在
     * -1 异常
     */
    int hset(String key, String field, String value);

    /**
     * 设置hash里面一个字段的值
     * @param key
     * @param field
     * @param value
     * @return
     * 1  field是一个新的字段
     * 0  field原来在map里面已经存在
     * -1 异常
     */
    int hset(String key, String field, long value);
    /**
     * 设置hash里面一个字段的值
     * @param key
     * @param field
     * @param value
     * @return
     * 1  field是一个新的字段
     * 0  field原来在map里面已经存在
     * -1 异常
     */
    <E> int hset(String key, String field, E value);
    /**
     * 设置hash里面一个字段的值
     * @param key
     * @param field
     * @param value
     * @return
     * 1  field是一个新的字段
     * 0  field原来在map里面已经存在
     * -1 异常
     */
    int hset(String key, Integer field, double value);
    /**
     * 设置hash的一个字段，只有当这个字段不存在时有效
     * @param key
     * @param field
     * @param value
     * @return
     * 1  如果字段是个新的字段，并成功赋值
     * 0  如果哈希集中已存在该字段，没有操作被执行
     * -1 异常
     */
    int hsetnx(String key, String field, String value);
    /**
     * 设置hash的一个字段，只有当这个字段不存在时有效
     * @param key
     * @param field
     * @param value
     * @return
     * 1  如果字段是个新的字段，并成功赋值
     * 0  如果哈希集中已存在该字段，没有操作被执行
     * -1 异常
     */
    int hsetnx(String key, String field, long value);
    /**
     * 设置hash的一个字段，只有当这个字段不存在时有效
     * @param key
     * @param field
     * @param value
     * @return
     * 1  如果字段是个新的字段，并成功赋值
     * 0  如果哈希集中已存在该字段，没有操作被执行
     * -1 异常
     */
    <E> int hsetnx(String key, String field, E value);
    /**
     * 获得hash的所有值
     * @param key
     * @return
     * 正常 哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表
     * 异常 null
     */
    List<String> hvalsOfString(String key);
    /**
     * 获得hash的所有值
     * @param key
     * @return
     * 正常 哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表
     * 异常 null
     */
    List<Long> hvalsOfLong(String key);
    /**
     * 获得hash的所有值
     * @param key
     * @return
     * 正常 哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表
     * 异常 null
     */
    <E> List<E> hvals(String key, Class<E> clazz);



    /************列表(Lists)**************/
    /**
     * 获取一个元素，通过其索引列表
     * @param key
     * @param index
     * @param valueOfErr
     * @return
     * 正常 请求的对应元素，或者当 index 超过范围的时候返回 nil
     * 异常 valueOfErr
     */
    String lindexOfString(String key, int index, String valueOfErr);
    /**
     * 获取一个元素，通过其索引列表
     * @param key
     * @param index
     * @return
     * 正常 请求的对应元素，或者当 index 超过范围的时候返回null
     * 异常 null
     */
    String lindexOfString(String key, int index);
    /**
     * 获取一个元素，通过其索引列表
     * @param key
     * @param index
     * @param valueOfErr
     * @return
     * 正常 请求的对应元素，或者当 index 超过范围的时候返回null
     * 异常 valueOfErr
     */
    long lindexOfLong(String key, int index, long valueOfErr);
    /**
     * 获取一个元素，通过其索引列表
     * @param key
     * @param index
     * @return
     * 正常 请求的对应元素，或者当 index 超过范围的时候返回0
     * 异常 -1
     */
    long lindexOfLong(String key, int index);
    /**
     * 获取一个元素，通过其索引列表
     * @param key
     * @param index
     * @param clazz
     * @return
     * 正常 请求的对应元素，或者当 index 超过范围的时候返回null
     * 异常 null
     */
    <E> E lindex(String key, int index, Class<E> clazz);
    /**
     * 获得队列(List)的长度
     * @param key
     * @return
     * >=0 key对应的list的长度
     * -1  异常
     */
    int llen(String key);
    /**
     * 从队列的左边出队一个元素
     * @param key
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回 nil
     * 异常 null
     */
    String lpopOfString(String key);
    /**
     * 从队列的左边出队一个元素
     * @param key
     * @param valueOfErr
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回 nil
     * 异常 valueOfErr
     */
    String lpopOfString(String key, String valueOfErr);
    /**
     * 从队列的左边出队一个元素
     * @param key
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回0
     * 异常 -1
     */
    long lpopOfLong(String key);
    /**
     * 从队列的左边出队一个元素
     * @param key
     * @param valueOfErr
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回0
     * 异常 valueOfErr
     */
    long lpopOfLong(String key, long valueOfErr);
    /**
     *  从队列的左边出队一个元素
     * @param key
     * @param clazz
     * @return
     */
    <E> E lpop(String key, Class<E> clazz);
    /**
     * 从队列的左边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpush(String key, String value);
    /**
     * 从队列的左边入队一个或多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpush(String key, String... values);
    /**
     * 从队列的左边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpush(String key, long value);
    /**
     * 从队列的左边入队一个或多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpush(String key, long... values);
    /**
     * 从队列的左边入队
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int lpush(String key, E value);
    /**
     * 从队列的左边入队多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int lpush(String key, List<E> values);
    /**
     * 当队列存在时，从队到左边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpushx(String key, String value);
    /**
     * 当队列存在时，从队到左边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int lpushx(String key, long value);
    /**
     * 当队列存在时，从队到左边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int lpushx(String key, E value);
    /**
     *从列表中获取指定返回的元素
     * @param key
     * @param start
     * @param stop
     * @return
     * 正常 指定范围里的列表元素
     * 异常 null
     */
    List<String> lrangeOfString(String key, int start, int stop);
    /**
     *从列表中获取指定返回的元素
     * @param key
     * @param start
     * @param stop
     * @return
     * 正常 指定范围里的列表元素
     * 异常 null
     */
    List<Long> lrangeOfLong(String key, int start, int stop);
    /**
     *从列表中获取指定返回的元素
     * @param key
     * @param start
     * @param stop
     * @return
     * 正常 指定范围里的列表元素
     * 异常 null
     */
    <E> List<E> lrange(String key, int start, int stop, Class<E> clazz);
    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
     *	count > 0: 从头往尾移除值为 value 的元素。
     *	count < 0: 从尾往头移除值为 value 的元素。
     *	count = 0: 移除所有值为 value 的元素。
     * @param key
     * @param count
     * @param value
     * @return
     * 正常 被移除的元素个数
     * 异常 -1
     */
    int lrem(String key, int count, String value);
    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
     *	count > 0: 从头往尾移除值为 value 的元素。
     *	count < 0: 从尾往头移除值为 value 的元素。
     *	count = 0: 移除所有值为 value 的元素。
     * @param key
     * @param count
     * @param value
     * @return
     * 正常 被移除的元素个数
     * 异常 -1
     */
    int lrem(String key, int count, long value);
    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
     *	count > 0: 从头往尾移除值为 value 的元素。
     *	count < 0: 从尾往头移除值为 value 的元素。
     *	count = 0: 移除所有值为 value 的元素。
     * @param key
     * @param count
     * @param value
     * @return
     * 正常 被移除的元素个数
     * 异常 -1
     */
    <E> int lrem(String key, int count, E value);
    /**
     * 设置队列里面一个元素的值
     * @param key
     * @param index
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    int lset(String key, int index, String value);
    /**
     * 设置队列里面一个元素的值
     * @param key
     * @param index
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    int lset(String key, int index, long value);
    /**
     * 设置队列里面一个元素的值
     * @param key
     * @param index
     * @param value
     * @return
     * >=0 正常
     * -1  异常
     */
    <E> int lset(String key, int index, E value);
    /**
     *修剪到指定范围内的清单
     * @param key
     * @param start
     * @param stop
     * @return
     * >=0 正常
     * -1  异常
     */
    int ltrim(String key, int start, int stop);
    /**
     * 从队列的右边出队一个元素
     * @param key
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回 nil
     * 异常 null
     */
    String rpopOfString(String key);
    /**
     * 从队列的右边出队一个元素
     * @param key
     * @param valueOfErr
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回 nil
     * 异常 valueOfErr
     */
    String rpopOfString(String key, String valueOfErr);
    /**
     * 从队列的右边出队一个元素
     * @param key
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回0
     * 异常 -1
     */
    long rpopOfLong(String key);
    /**
     * 从队列的右边出队一个元素
     * @param key
     * @param valueOfErr
     * @return
     * 正常 返回第一个元素的值，或者当 key 不存在时返回0
     * 异常 valueOfErr
     */
    long rpopOfLong(String key, long valueOfErr);
    /**
     *  从队列的右边出队一个元素
     * @param key
     * @param clazz
     * @return
     */
    <E> E rpop(String key, Class<E> clazz);
    /**
     * 从队列的右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpush(String key, String value);
    /**
     * 从队列的右边入队一个或多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpush(String key, String... values);
    /**
     * 从队列的右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpush(String key, long value);
    /**
     * 从队列的右边入队一个或多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpush(String key, long... values);
    /**
     * 从队列的右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int rpush(String key, E value);
    /**
     * 从队列的右边入队多个元素
     * @param key
     * @param values
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int rpush(String key, List<E> values);
    /**
     * 当队列存在时，从队到右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpushx(String key, String value);
    /**
     * 当队列存在时，从队到右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    int rpushx(String key, long value);
    /**
     * 当队列存在时，从队到右边入队一个元素
     * @param key
     * @param value
     * @return
     * 正常 在 push 操作后的 list 长度
     * 异常 -1
     */
    <E> int rpushx(String key, E value);


    /************集合(Sets)**************/
    /**
     * 添加一个元素到集合(set)里
     * @param key
     * @param value
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    int sadd(String key, String value);
    /**
     * 添加一个或者多个元素到集合(set)里
     * @param key
     * @param values
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    int sadd(String key, String... values);
    /**
     * 添加一个元素到集合(set)里
     * @param key
     * @param value
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    int sadd(String key, long value);
    /**
     * 添加一个或者多个元素到集合(set)里
     * @param key
     * @param values
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    int sadd(String key, long... values);
    /**
     * 添加一个元素到集合(set)里
     * @param key
     * @param value
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    <E> int sadd(String key, E value);
    /**
     * 添加多个元素到集合(set)里
     * @param key
     * @param values
     * @return
     * >=0 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     * -1 　异常
     */
    <E> int sadd(String key, List<E> values);
    /**
     * 获取集合里面的元素数量
     * @param key
     * @return
     * >=0 集合的基数(元素的数量),如果key不存在,则返回 0.
     * -1  异常
     */
    int scard(String key);
    /**
     * 返回成员 member 是否是存储的集合 key的成员.
     * @param key
     * @param member
     * @return
     * 1  member元素是集合key的成员
     * 0  member元素不是key的成员
     * -1 异常
     */
    int sismember(String key, String member);
    /**
     * 返回成员 member 是否是存储的集合 key的成员.
     * @param key
     * @param member
     * @return
     * 1  member元素是集合key的成员
     * 0  member元素不是key的成员
     * -1 异常
     */
    int sismember(String key, long member);
    /**
     * 返回key集合所有的元素
     * @param key
     * @return 集合中的所有元素
     */
    Set<String> smembersOfString(String key);
    /**
     * 返回key集合所有的元素
     * @param key
     * @return 集合中的所有元素
     */
    Set<Long> smembersOfLong(String key);
    /**
     * 返回key集合所有的元素
     * @param key
     * @return 集合中的所有元素
     */
    <E> Set<E> smembers(String key, Class<E> clazz);
    /**
     * 移除并返回一个集合中的随机元素
     * @param key
     * @return
     */
    String spopOfString(String key);
    /**
     * 移除并返回一个集合中的随机元素
     * @param key
     * @return
     */
    long spopOfLong(String key);
    /**
     * 移除并返回一个集合中的随机元素
     * @param key
     * @return
     */
    long spopOfLong(String key, long valueOfErr);
    /**
     * 移除并返回一个集合中的随机元素
     * @param key
     * @param clazz
     * @return
     */
    <E> E spop(String key, Class<E> clazz);

    /**
     * 随机获取一个元素
     * @param key
     * @return
     */
    String srandomMemberOfString(String key);
    /**
     * 随机获取一个元素集合
     * @param key
     * @param count
     * @return
     */
    List<String> srandomMemberOfString(String key, int count);
    /**
     * 随机获取一个元素
     * @param key
     * @return
     */
    long srandomMemberOfLong(String key);
    /**
     * 随机获取一个元素集合
     * @param key
     * @param count
     * @return
     */
    List<Long> srandomMemberOfLong(String key, int count);
    /**
     * 从集合里删除一个member
     * @param key
     * @param member
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    int srem(String key, String member);
    /**
     * 从集合里删除多个member
     * @param key
     * @param members
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    int srem(String key, String... members);
    /**
     * 从集合里删除一个member
     * @param key
     * @param member
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    int srem(String key, long member);
    /**
     * 从集合里删除多个member
     * @param key
     * @param members
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    int srem(String key, long... members);
    /**
     * 从集合里删除一个member
     * @param key
     * @param member
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    <E> int srem(String key, E member);
    /**
     * 从集合里删除多个member
     * @param key
     * @param members
     * @return
     * >=0 从集合中移除元素的个数，不包括不存在的成员
     * -1  异常
     */
    <E> int srem(String key, List<E> members);


    /************有序集合(Sorted Sets)**************/
    /**
     * 添加到有序set的成员，或更新的分数，如果它已经存在
     * @param key
     * @param member
     * @param score
     * @return
     * >=0 返回添加到有序集合中元素的个数，不包括那种已经存在只是更新分数的元素
     * -1  异常
     */
    int zadd(String key, String member, double score);
    /**
     * 添加到有序set的成员，或更新的分数，如果它已经存在
     * @param key
     * @param memerScoreMap
     * @return
     * >=0 返回添加到有序集合中元素的个数，不包括那种已经存在只是更新分数的元素
     * -1  异常
     */
    int zadd(String key, Map<String, Double> memerScoreMap);
    /**
     * 添加到有序set的成员，或更新的分数，如果它已经存在
     * @param key
     * @param member
     * @param score
     * @return
     * >=0 返回添加到有序集合中元素的个数，不包括那种已经存在只是更新分数的元素
     * -1  异常
     */
    int zadd(String key, long member, double score);
    /**
     * 添加到有序set的成员，或更新的分数，如果它已经存在
     * @param key
     * @param member
     * @param score
     * @return
     * >=0 返回添加到有序集合中元素的个数，不包括那种已经存在只是更新分数的元素
     * -1  异常
     */
    <E> int zadd(String key, E member, double score);
    /**
     * 获取一个排序的集合中的成员数量
     * @param key
     * @return
     * >=0 key存在的时候，返回有序集的元素个数，否则返回0
     * -1  异常
     */
    int zcard(String key);
    /**
     * 获取给定值范围内的成员数
     * @param key
     * @param min
     * @param max
     * @return
     */
    int zcount(String key, double min, double max);
    /**
     * 获取给定值范围内的成员数
     * @param key
     * @param min
     * @param max
     * @return
     */
    int zcount(String key, long min, long max);
    /**
     * 增量一名成员在排序设置的评分
     * @param key
     * @param member
     * @param increment
     * @return
     * 	member成员的新score值
     */
    double zincrBy(String key, String member, double increment);
    /**
     * 增量一名成员在排序设置的评分
     * @param key
     * @param member
     * @param increment
     * @return
     * 	member成员的新score值
     */
    double zincrBy(String key, String member, long increment);
    /**
     * 返回的成员在排序设置的范围,分数由低到高
     * @param key
     * @param start
     * @param stop
     * @return
     * 成员列表
     */
    Set<String> zrange(String key, int start, int stop);


    /**
     * 返回的成员在排序设置的范围,分数由低到高
     * @param key
     * @param start
     * @param stop
     * @return
     * 成员列表
     */
    <E> Set<E> zrange(String key, int start, int stop, final Class<E> clazz);
    /**
     * 返回的成员在排序设置的范围,分数由低到高(含分数)
     * @param key
     * @param start
     * @param stop
     * @return 成员列表及分数
     */
    Map<String, Double> zrangeWithDoubleScore(String key, int start, int stop);
    /**
     * 返回的成员在排序设置的范围,分数由低到高(含分数)
     * @param key
     * @param start
     * @param stop
     * @return 成员列表及分数
     */
    Map<String, Long> zrangeWithLongScore(String key, int start, int stop);
    /**
     * 返回的成员在排序设置的范围,分数由高到低
     * @param key
     * @param start
     * @param stop
     * @return
     * 成员列表
     */
    <E> Set<E> zrevrange(String key, int start, int stop, final Class<E> clazz);
    /**
     * 返回的成员在排序设置的范围,分数由高到低
     * @param key
     * @param start
     * @param stop
     * @return
     * 成员列表
     */
    Set<String> zrevrange(String key, int start, int stop);
    /**
     * 返回的成员在排序设置的范围,分数有由到低(含分数)
     * @param key
     * @param start
     * @param stop
     * @return 成员列表及分数
     */
    Map<String, Double> zrevrangeWithDoubleScore(String key, int start, int stop);
    /**
     * 返回的成员在排序设置的范围,分数由高到低(含分数)
     * @param key
     * @param start
     * @param stop
     * @return 成员列表及分数
     */
    Map<String, Long> zrevrangeWithLongScore(String key, int start, int stop);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<String> zrangeByScore(String key, double min, double max, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrangeByScore(String key, double min, double max);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<String> zrangeByScore(String key, long min, long max, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrangeByScore(String key, long min, long max);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Map<String, Double> zrangeByScoreWithScore(String key, double min, double max, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Map<String, Double> zrangeByScoreWithScore(String key, double min, double max);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Map<String, Long> zrangeByScoreWithScore(String key, long min, long max, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Map<String, Long> zrangeByScoreWithScore(String key, long min, long max);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrevrangeByScore(String key, double max, double min);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<String> zrevrangeByScore(String key, long max, long min, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrevrangeByScore(String key, long max, long min);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Map<String, Double> zrevrangeByScoreWithScore(String key, double max, double min, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Map<String, Double> zrevrangeByScoreWithScore(String key, double max, double min);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Map<String, Long> zrevrangeByScoreWithScore(String key, long max, long min, int offset, int count);
    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param min
     * @param max
     * @return
     */
    Map<String, Long> zrevrangeByScoreWithScore(String key, long max, long min);
    /**
     * 确定在排序集合成员的索引
     * @param key
     * @param member
     * @return
     * >=0 member在集合中的位置
     * -1 member不在集合中或key不存在
     * -2 member异常
     */
    int zrank(String key, String member);
    /**
     * 确定在排序集合成员的索引
     * @param key
     * @param member
     * @return
     * >=0 member在集合中的位置
     * -1 member不在集合中或key不存在
     * -2 member异常
     */
    int zrevrank(String key, String member);
    /**
     * 从排序的集合中删除一个或多个成员
     * @param key
     * @param member
     * @return
     * 返回的是从有序集合中删除的成员个数，不包括不存在的成员
     */
    int zrem(String key, String... member);
    /**
     * 返回的是从有序集合中删除的成员个数，不包括不存在的成员
     * @param key
     * @param start
     * @param stop
     * @return
     * 删除的元素的个数
     */
    int zremrangeByRank(String key, int start, int stop);
    /**
     *删除一个排序的设置在给定的分数所有成员
     * @param key
     * @param min
     * @param max
     * @return
     * 删除的元素的个数
     */
    int zremrangeByScore(String key, double min, double max);
    /**
     *删除一个排序的设置在给定的分数所有成员
     * @param key
     * @param min
     * @param max
     * @return
     * 删除的元素的个数
     */
    int zremrangeByScore(String key, long min, long max);
    /**
     * 获取成员在排序设置相关的比分
     * @param key
     * @param member
     * @return
     */
    double zscoreOfDouble(String key, String member);
    /**
     * 获取成员在排序设置相关的比分
     * @param key
     * @param member
     * @return
     */
    long zscoreOfLong(String key, String member);


}

