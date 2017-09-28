package org.deed.core.boot;

import java.net.InetAddress;
import org.deed.client.protocol.DeedDecoder;
import org.deed.client.protocol.DeedEncoder;
import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;
import org.deed.core.server.handler.ServerHandler;
import org.springframework.beans.factory.annotation.Value;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class DefaultAbstractDeedServce extends AbstractDeedServce{
	@Value("${server.address}")
	private static final String address = "127.0.0.1:18888";
	
	@Override
	public void start() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss, worker);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
					//字符串解码 和 编码
					.addLast(new DeedDecoder(DeedRequest.class))
					.addLast(new DeedEncoder(DeedResponse.class))
					.addLast(new ServerHandler());
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
			
			
			String[] array = address.split(":");
			String host = array[0];
			try {
				host = InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				e.printStackTrace();
			}
			int port = Integer.parseInt(array[1]);
			ChannelFuture future = serverBootstrap.bind(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		}
		
	}

}
