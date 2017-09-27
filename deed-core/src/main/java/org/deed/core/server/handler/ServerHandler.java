package org.deed.core.server.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	
	//缓存client channel 引用
	private static final Map<Long, Object> handlerMap = new ConcurrentHashMap<>(16);
 
	private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	//server端业务线程池
	private static ExecutorService threadPoolExecutor = null;
	
	static {
		threadPoolExecutor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext chc, DeedRequest request) throws Exception {
		/*final ReentrantLock lock = new ReentrantLock();
		lock.lockInterruptibly();
		try {
			handlerMap.put(request.getInvoikeId(), chc);
		} finally {
			lock.unlock();
		}*/
		threadPoolExecutor.execute(new InvokeWorker(request,chc));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		//exception hanlder TODO
		cause.printStackTrace();
		ctx.close();
	}

}
