package org.deed.client.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.deed.client.future.DeedFuture;
import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * client handler demo简单处理 可以考虑封装业务线程池
 * 
 * @author hejian
 *
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<DeedResponse> {
	private Logger								logger		= LoggerFactory.getLogger(RpcClientHandler.class);
	// 持有DeedFuture
	public static final Map<Long, DeedFuture>	PENDINGRPC	= new ConcurrentHashMap<Long, DeedFuture>();
	// 线程安全保证内存可见性
	private volatile Channel					channel;

	@Override
	protected void channelRead0(ChannelHandlerContext chc, DeedResponse response) throws Exception {
		// 处理RPC远程调用返回值
		long requestId = response.getRequestId();
		DeedFuture future = PENDINGRPC.get(requestId);
		if (null == future) {
			logger.error("channel read  not find Future");
		} else {
			future.done(response);
			// 移除对象 GC
			PENDINGRPC.remove(requestId);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		this.channel = ctx.channel();
	}

	/**
	 * 发送RPC远程调用请求
	 * 
	 * @param request
	 * @return
	 */
	public DeedFuture sendRequest(DeedRequest request) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		DeedFuture rpcFuture = new DeedFuture(request);
		PENDINGRPC.put(request.getInvoikeId(), rpcFuture);
		try {
			channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					countDownLatch.countDown();
				}
			});
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rpcFuture;
	}
}
