package org.deed.client.protocol;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DeedDecoder extends ByteToMessageDecoder {
	// 目标class
	private Class<?> genericClass;

	public DeedDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 私有协议check
		if (in.readableBytes() < 4) {
			return;
		}
		
		in.markReaderIndex();
		// 初始化数组大小
		int dataLength = in.readInt();

		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}

		byte[] data = new byte[dataLength];

		in.readBytes(data);
		// 序列化目标对象
		Object obj = org.deed.client.utils.SerializationUtil.deserialize(data, genericClass);
		out.add(obj);
	}

}
