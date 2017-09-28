package org.deed.core.monit;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;

/**
 * 监控Timer
 * @author hejian
 *
 */
public class MonitorTimer {
	
	@PostConstruct
	public void print() throws Exception {
		System.out.println(">>>>>>>>>>>>>> print >>>>>>>>>>>>>>>");
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(;;) {
					if(MonitorManage.MONITMAP.size() >0) {
						MonitorManage.MONITMAP.forEach((k,v)-> {
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("RPC接口调用信息 : " + v);
						});
					}
				}
			}
		}).start();
	}
}
