package org.deed.montior;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.OpenOption;

import javax.annotation.PostConstruct;

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
