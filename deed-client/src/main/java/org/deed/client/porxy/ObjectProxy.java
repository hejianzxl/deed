package org.deed.client.porxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.deed.client.bootstarp.ClientManager;
import org.deed.client.future.DeedFuture;
import org.deed.client.handler.RpcClientHandler;
import org.deed.client.protocol.DeedRequest;

public class ObjectProxy<T> implements InvocationHandler {
	
	private Class<T> classzz;

	public ObjectProxy(Class<T> classzz) {
		this.classzz = classzz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		if(Object.class == method.getDeclaringClass()) {
			if ("equals".equals(name)) {
				return proxy == args[0];
			} else if ("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			} else if ("toString".equals(name)) {
				return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy))
						+ ", with InvocationHandler " + this;
			} else {
				throw new IllegalStateException(String.valueOf(method));
			}
		}
		
		//创建请求数据
		DeedRequest request = DeedRequest.class.newInstance();
		request.setInterfaceName(method.getDeclaringClass().getName());
		request.setMethod(name);
		request.setParameters(args);
		request.setParameterTypes(method.getParameterTypes());
		
		//获取client handler channel 发送请求数据
		RpcClientHandler rpcClientHandler = ClientManager.getInstance().getHandler();
		DeedFuture future = rpcClientHandler.sendRequest(request);
		//等待响应
		return future.get();
	}
}
