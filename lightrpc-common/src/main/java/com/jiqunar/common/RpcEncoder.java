package com.jiqunar.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 *
 * @author jieguang.wang
 * @date 2021/1/15 11:31
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> clazz;
    private Serializer serializer;

    public RpcEncoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (clazz != null && clazz.isInstance(o)) {
            byte[] bytes = serializer.serialize(o);
            // 将对象的字节长度先写入bytebuf里边
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
