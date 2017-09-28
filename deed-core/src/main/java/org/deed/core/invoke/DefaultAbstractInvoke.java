package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;

import org.deed.client.protocol.DeedRequest;
import org.deed.core.boot.AbstractDeedServce;
import org.deed.core.boot.DefaultAbstractDeedServce;
import org.deed.core.register.DefaultAbstractRegister;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

public abstract class DefaultAbstractInvoke implements Invoke {

	@Override
	public Object invoke(Object object) throws InvocationTargetException {
		if(object.getClass().isAssignableFrom(DeedRequest.class)) {
			DeedRequest request = (DeedRequest) object;
			new Thread(new Runnable() {
				@Override
				public void run() {
					//内存监控信息  异步刷盘文件系统内存
					System.out.println("调用次数统计");
				}
			});
			return execute(request);
		}
		return null;
	}
	
	
	public abstract Object execute(DeedRequest request) throws InvocationTargetException;

}
