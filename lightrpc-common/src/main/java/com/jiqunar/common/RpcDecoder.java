package com.jiqunar.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 *
 * @author jieguang.wang
 * @date 2021/1/15 11:34
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 可读的总字节长度
        int readableByteLength = byteBuf.readableBytes();
        // 因为RpcEecoder将对象的字节长度先写入bytebuf里边，占用四个字节
        if (readableByteLength < 4) {
            return;
        }
        // 标记读到的位置
        byteBuf.markReaderIndex();
        // 需要读的总字节长度
        int needReadByteLength = byteBuf.readInt();
        if (needReadByteLength > readableByteLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[needReadByteLength];
        byteBuf.readBytes(bytes);
        Object o = serializer.deserialize(clazz, bytes);
        list.add(o);
    }
}
