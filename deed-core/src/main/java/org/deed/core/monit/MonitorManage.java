package org.deed.core.monit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.monitor.Monitor;

import org.deed.client.protocol.DeedRequest;

public class MonitorManage {
	private static ExecutorService MONITPOOLEXECUTOR = Executors.newFixedThreadPool(1);
	public static final Map<String, Object> MONITMAP = new ConcurrentHashMap<String, Object>();
	//public static final Set<Map<String, Object>> MONITSET = new HashSet<>();
	
	public static void monitor(DeedRequest request,long startTime) {
		MONITPOOLEXECUTOR.execute(new MonitorThread(request,startTime));
	}
}

class MonitorThread implements Runnable {
	private DeedRequest request;
	private long startTime;
	
	MonitorThread(DeedRequest request, long startTime) {
		this.request = request;
		this.startTime = startTime;
	}
	
	@Override
	public void run() {
		ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();
			monitor();
		} finally {
			lock.unlock();
		}
	}

	private void monitor() {
		MonitorObject monitor = null;
		long endTime = System.currentTimeMillis();
		if(!MonitorManage.MONITMAP.containsKey(request.getInterfaceName()+request.getMethod())) {
			monitor = new MonitorObject();
			monitor.setInterfaceName(request.getInterfaceName());
			monitor.setMethod(request.getMethod());
			monitor.setRequestId(request.getInvoikeId());
			monitor.setInvokeTime(startTime);
			monitor.setReponseTime(endTime - startTime);
			monitor.setInvokeCount();
		}else {
			monitor = (MonitorObject) MonitorManage.MONITMAP.get(request.getInterfaceName()+request.getMethod());
			monitor.setInterfaceName(request.getInterfaceName());
			monitor.setMethod(request.getMethod());
			monitor.setRequestId(request.getInvoikeId());
			monitor.setInvokeTime(startTime);
			monitor.setReponseTime(endTime - startTime);
			monitor.setInvokeCount();
		}
		MonitorManage.MONITMAP.put(request.getInterfaceName()+request.getMethod(), monitor);
	}
}
