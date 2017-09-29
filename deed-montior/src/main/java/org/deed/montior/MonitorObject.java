package org.deed.montior;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控描述对象
 * @author hejian
 *
 */
public class MonitorObject implements Serializable{
	
	private AtomicInteger ATOMICINTEGER = new AtomicInteger(0);

	/**
	 * 
	 */
	private static final long serialVersionUID = 5682799617912589639L;

	//调用方法名称
	private String method;
	
	//调用接口名称
	private String interfaceName;
	
	//本次远程调用请求唯一标示ID
	private long requestId;
	
	//调用次数
	private long invokeCount;
	
	//调用执行时间
	private transient long invokeTime;
	
	//响应时间
	private transient long reponseTime;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public long getInvokeTime() {
		return invokeTime;
	}

	public void setInvokeTime(long invokeTime) {
		this.invokeTime = invokeTime;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public long getInvokeCount() {
		return invokeCount;
	}

	public void setInvokeCount() {
		this.invokeCount = ATOMICINTEGER.incrementAndGet();
	}

	public long getReponseTime() {
		return reponseTime;
	}

	public void setReponseTime(long reponseTime) {
		this.reponseTime = reponseTime;
	}

	@Override
	public String toString() {
		return "MonitorObject [method=" + method + ", interfaceName=" + interfaceName + ", requestId=" + requestId
				+ ", invokeCount=" + invokeCount + ", invokeTime=" + invokeTime + ", reponseTime=" + reponseTime + "毫秒]";
	}
}
