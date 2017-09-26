package org.deed.client.demo.service;

public interface UserService {
	String getName(long id);
	
	boolean setName(String name);
	
	public java.util.List<UserDO> list();
}
