package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;

import org.deed.client.protocol.DeedRequest;
import org.deed.core.boot.AbstractDeedServce;
import org.deed.core.boot.DefaultAbstractDeedServce;
import org.deed.core.register.DefaultAbstractRegister;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

public class DefaultInvoke implements Invoke {

	@Override
	public Object invoke(Object object) throws InvocationTargetException {
		if(object.getClass().isAssignableFrom(DeedRequest.class)) {
			DeedRequest request = (DeedRequest) object;
			String interfaceName = request.getInterfaceName();
			//可以直接spring application 获取，无需再次缓存
			Object bean = DefaultAbstractRegister.REGISTERSERVICE.get(interfaceName);
			FastClass fastClass = FastClass.create(bean.getClass());
			FastMethod method = fastClass.getMethod(request.getMethod(), request.getParameterTypes());
			return method.invoke(bean, request.getParameters());
		}
		return null;
	}

}
