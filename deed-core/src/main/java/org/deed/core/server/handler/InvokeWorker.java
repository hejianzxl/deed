package org.deed.core.server.handler;

import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;
import org.deed.core.invoke.CglibInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class InvokeWorker implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(InvokeWorker.class);
	private DeedRequest request;
	private ChannelHandlerContext chc;
	
	public InvokeWorker(DeedRequest request,ChannelHandlerContext chc) {
		this.request = request;
		this.chc = chc;
	}
	
	@Override
	public void run() {
		DeedResponse response = new DeedResponse();
		response.setRequestId(request.getInvoikeId());
		try {
			Object result = CglibInvoke.class.newInstance().invoke(request);
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

}
