package org.deed.core.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.deed.client.server.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultAbstractRegister extends Register implements ApplicationContextAware, InitializingBean {

	private ApplicationContext				applicationContext;

	public static final Map<String, Object>	REGISTERSERVICE	= new ConcurrentHashMap<>(16);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//不兼容老版本spring2
		this.applicationContext = applicationContext;
	}

	public Object getBean(Class<?> classzz) {
		return applicationContext.getBean(classzz);
	}

	@Override
	public void register(Object o) {
		System.out.println("发布RPC服务 " + o.getClass().getAnnotation(RpcService.class).value().getName());
		REGISTERSERVICE.put(o.getClass().getAnnotation(RpcService.class).value().getName(), o);
		// 注册nameservice 服务列表
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 处理annontation
		Map<String, Object> rpcService = applicationContext.getBeansWithAnnotation(RpcService.class);
		if (null != rpcService && rpcService.size() > 0) {
			rpcService.values().forEach((o) -> {
				// 简单缓存 后续可以使用模板方法子类实现发布RPC服务。支持多种nameServer
				register(o);
			});
		}
	}
}
