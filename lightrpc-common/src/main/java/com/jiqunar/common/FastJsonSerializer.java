package com.jiqunar.common;

import com.alibaba.fastjson.JSON;

/**
 * @author jieguang.wang
 * @date 2021/1/15 11:22
 */
public class FastJsonSerializer implements Serializer {
    /**
     * 序列化成二进制
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    /**
     * 将二进制反序列化对象
     *
     * @param clazz
     * @param bytes
     * @return
     */
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
