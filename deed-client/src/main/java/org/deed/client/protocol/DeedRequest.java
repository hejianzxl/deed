package org.deed.client.protocol;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求体信息
 * @author hejian
 *
 */
public class DeedRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2391853750750332831L;
	
	private static volatile AtomicInteger invokeIdGenerator = new AtomicInteger(1);
	//请求ID
	private final long invoikeId;
	//方法名称
	private String method;
	//接口名称
	private  String interfaceName;
	//参数类型
	private Class<?>[]	parameterTypes;
	//参数
	private Object[]	parameters;
	
	public DeedRequest() {
		this(invokeIdGenerator.incrementAndGet());
	}
	
	public DeedRequest(long invoikeId) {
		this.invoikeId = invoikeId;
	}
	
	public long invokeId() {
        return invoikeId;
    }

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

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public long getInvoikeId() {
		return invoikeId;
	}
	
}
