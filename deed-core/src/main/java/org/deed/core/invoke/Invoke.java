package org.deed.core.invoke;

import java.lang.reflect.InvocationTargetException;

public interface Invoke {
	public Object invoke(Object request) throws InvocationTargetException;
}
