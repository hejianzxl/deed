package org.deed.client;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.deed.client.bootstarp.ClientManager;
import org.deed.client.porxy.ObjectProxyFactory;

/**
 * 处理注册bean
 * @author hejian
 *
 */
public class RpcClient {
	
	private static String address;
	
	private static int port;
	
	//异步线程池
	private static ThreadPoolExecutor			threadPoolExecutor	= new ThreadPoolExecutor(16, 16, 600L,TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
	
	public RpcClient() {
		this(address, port);
	}

	public RpcClient(String address, int port) {
		this.address = address;
		this.port = port;
		ClientManager.getInstance().start(new InetSocketAddress(address, port));
	}
	
	public void stop() {
		
	}
	
	/**
	 * 代理对象
	 * @param interfaceClass
	 * @return
	 */
	public static <T> T create(Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new ObjectProxyFactory<T>(interfaceClass));
	}
}
