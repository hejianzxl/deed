package org.deed.core.server.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;
import org.deed.core.invoke.DefaultInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<DeedRequest> {

	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	private Map<String, Object> handlerMap = new ConcurrentHashMap<>(16);
	//server端业务线程池
	private static ThreadPoolExecutor threadPoolExecutor;
	
	@Override
	protected void channelRead0(ChannelHandlerContext chc, DeedRequest request) throws Exception {
		DeedResponse response = new DeedResponse();
		response.setRequestId(request.getInvoikeId());
		try {
			Object result = DefaultInvoke.class.newInstance().invoke(request);
			response.setResult(result);
		} catch (Exception e) {
			response.setError(e.getMessage());
			logger.error("DefaultInvoke error ", e);
		}
		
		chc.writeAndFlush(response).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				//System.out.println("Send response for request " + request.getInvoikeId());
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		//exception hanlder TODO
		cause.printStackTrace();
		ctx.close();
	}

}
