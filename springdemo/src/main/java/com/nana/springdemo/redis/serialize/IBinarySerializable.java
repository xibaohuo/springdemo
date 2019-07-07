package com.nana.springdemo.redis.serialize;

/**
 * 二进制序列化接口
 *
 */
public interface IBinarySerializable {

    /**
     * 对象转化成二进制
     * @param t
     * @return
     */
    public <T> byte[] toBinary(T t);

    /**
     * 二进制转化成对象
     * @param data
     * @param clazz
     * @return
     */
    public <T> T toObject(byte[] data, Class<T> clazz);
}