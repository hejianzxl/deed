package org.deed.client.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import org.deed.client.protocol.DeedRequest;
import org.deed.client.protocol.DeedResponse;

import sun.misc.Unsafe;

public class DeedFuture implements Future<Object>{
	private DeedSync sync;
	transient long startTime;
	private DeedRequest request;
	private DeedResponse response;
    //等待时间
    private final long responseTimeThreshold = 5000;
    
    public DeedFuture(DeedRequest request) {
    	this.request = request;
    	this.startTime = System.currentTimeMillis();
    	this.sync = new DeedSync();
	}
    

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDone() {
		return sync.isDone();
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		//TODO
		//sync.acquire(-1);
		sync.acquire(0);
		if(this.response!=null) {
			return this.response.getResult();
		}else {
			return null;
		}
	}
	
	public void done(DeedResponse respone) {
		this.response = respone;
		sync.release(1);
		long responseTime = System.currentTimeMillis() - startTime;
		//请求响应超时
		if (responseTime > this.responseTimeThreshold) {
            //logger.warn("Service response time is too slow. Request id = " + reponse.getRequestId() + ". Response Time = " + responseTime + "ms");
        }
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
		if(success) {
			if(this.response!=null) {
				return this.response.getResult();
			}
		}else {
			throw new RuntimeException("get respone timeout!");
		}
		return null;
	}
	
	static class DeedSync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1L;
        //future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            return getState() == done ? true : false;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            return getState() == done;
        }
    }
}
