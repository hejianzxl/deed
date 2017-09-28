package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;

import org.deed.client.protocol.DeedRequest;
import org.deed.core.register.DefaultAbstractRegister;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

public class CglibInvoke extends DefaultAbstractInvoke{

	@Override
	public Object execute(DeedRequest request) throws InvocationTargetException {
		String interfaceName = request.getInterfaceName();
		//可以直接spring application 获取，无需再次缓存
		Object bean = DefaultAbstractRegister.REGISTERSERVICE.get(interfaceName);
		FastClass fastClass = FastClass.create(bean.getClass());
		FastMethod method = fastClass.getMethod(request.getMethod(), request.getParameterTypes());
		return method.invoke(bean, request.getParameters());
	}

}
