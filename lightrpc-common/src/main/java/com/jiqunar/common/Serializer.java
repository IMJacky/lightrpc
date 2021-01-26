package com.jiqunar.common;

/**
 * 序列化接口
 *
 * @author jieguang.wang
 * @date 2021/1/15 11:16
 */
public interface Serializer {
    /**
     * 序列化成二进制
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 将二进制反序列化对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
