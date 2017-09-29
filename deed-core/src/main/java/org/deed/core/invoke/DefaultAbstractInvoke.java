package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;
import org.deed.client.protocol.DeedRequest;
import org.deed.montior.MonitorManage;

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
