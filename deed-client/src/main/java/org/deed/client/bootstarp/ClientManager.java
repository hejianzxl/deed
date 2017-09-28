package org.deed.client.bootstarp;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import org.deed.client.handler.RpcClientHandler;
import org.deed.client.protocol.DeedDecoder;
import org.deed.client.protocol.DeedEncoder;
import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientManager {
	private Logger												logger				= LoggerFactory.getLogger(ClientManager.class);
	private static final CopyOnWriteArrayList<RpcClientHandler>	connectedHandlers	= new CopyOnWriteArrayList<>();
	private ReentrantLock										lock				= new ReentrantLock();
	//本地缓存服务列表
	private static HashSet<InetSocketAddress>	newAllServerNodeSet	= new HashSet<InetSocketAddress>();
	
	private ClientManager() {
		// ignore
	}

	private static class Client {
		private static final ClientManager CLIENT_MANAGER = new ClientManager();
	}

	public static ClientManager getInstance() {
		return Client.CLIENT_MANAGER;
	}
	
	public RpcClientHandler getHandler() {
		return connectedHandlers.get(0);
	}
	
	/**
	 * 链接服务端
	 * @throws InterruptedException 
	 */
	public void start(final InetSocketAddress socketAddress){
		EventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		final CountDownLatch clientWatch = new CountDownLatch(1);
		bootstrap.group(worker).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline cp = ch.pipeline();
				cp.addLast(new DeedEncoder(DeedRequest.class));
				cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
				cp.addLast(new DeedDecoder(DeedResponse.class));
				cp.addLast(new RpcClientHandler());
			}
		});
		ChannelFuture channelFuture = bootstrap.connect(socketAddress);
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					logger.debug("Successfully connect to remote server. remote peer = " + socketAddress);
					RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
					connectedHandlers.add(handler);
					clientWatch.countDown();
				}
			}
		});
		try {
			clientWatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 随机轮询
	 * @return
	 */
	public InetSocketAddress randomRpcServer() {
		int rdIndex = new Random().nextInt(newAllServerNodeSet.size());
		int index = 0;
		for(Iterator it=newAllServerNodeSet.iterator();it.hasNext();) {
		   if(rdIndex == index) {
			   return (InetSocketAddress) it.next();
		   }
			index++;
		}
		return null;
	}
	
	/**
	 * 注销服务
	 * @param address
	 */
	public void removeServer(InetSocketAddress address) {
		newAllServerNodeSet.remove(address);
	}
}
