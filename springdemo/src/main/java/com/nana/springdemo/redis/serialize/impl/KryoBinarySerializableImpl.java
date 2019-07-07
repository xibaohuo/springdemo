package com.nana.springdemo.redis.serialize.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.nana.springdemo.redis.serialize.IBinarySerializable;

/**
 * Kryo序列化
 *
 */
public class KryoBinarySerializableImpl implements IBinarySerializable {
    @Override
    public <T> byte[] toBinary(T t) {
        Kryo kryo = new Kryo();
        Output output = new Output(256, 4096);
        kryo.writeObject(output, t);
        byte[] data = output.toBytes();
        output.flush();
        output.close();
        return data;
    }

    @Override
    public <T> T toObject(byte[] data, Class<T> clazz) {
        Kryo kryo = new Kryo();
        Input input = new Input(data);
        T t = kryo.readObject(input, clazz);
        return t;
    }
}
