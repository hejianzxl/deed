package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.deed.client.protocol.DeedRequest;
import org.deed.core.boot.AbstractDeedServce;
import org.deed.core.boot.DefaultAbstractDeedServce;
import org.deed.core.monit.MonitorManage;
import org.deed.core.register.DefaultAbstractRegister;


public abstract class DefaultAbstractInvoke implements Invoke {
	
	@Override
	public Object invoke(Object object) throws InvocationTargetException {
		long startTime = System.currentTimeMillis();
		if(object.getClass().isAssignableFrom(DeedRequest.class)) {
			DeedRequest request = (DeedRequest) object;
			Object result = execute(request);
			MonitorManage.monitor(request,startTime);
			return result;
		}
		return null;
	}
	
	
	public abstract Object execute(DeedRequest request) throws InvocationTargetException;

}
