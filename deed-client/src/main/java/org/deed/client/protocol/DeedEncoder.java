package org.deed.client.protocol;

import org.deed.client.utils.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class DeedEncoder extends MessageToByteEncoder<Object> {
	//目标对象
	private Class<?> genericClass;
	
	public DeedEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}
	
	@Override
	protected void encode(ChannelHandlerContext chc, Object object, ByteBuf out) throws Exception {
		if (genericClass.isInstance(object)) {
            byte[] data = SerializationUtil.serialize(object);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
	}
}
