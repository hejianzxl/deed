package org.deed.core.boot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

public abstract class AbstractDeedServce implements InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
	
	/***
	 * 子类实现启动
	 * @throws InterruptedException
	 */
	public abstract void start() throws InterruptedException;
}
