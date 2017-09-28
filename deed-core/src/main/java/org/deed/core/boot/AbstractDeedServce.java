package org.deed.core.boot;

import org.springframework.beans.factory.InitializingBean;

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
