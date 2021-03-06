package org.deed.core.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.deed.client.demo.service.UserDO;
import org.deed.client.demo.service.UserService;

@org.deed.client.server.annotation.RpcService(value = UserService.class)
public class UserServiceImpl implements UserService{
	private String name;

	@Override
	public String getName(long id) {
		try {
			//模拟调用mysql or 业务逻辑处理
			Thread.sleep(new Random().nextInt(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public boolean setName(String name) {
		try {
			//模拟调用mysql or 业务逻辑处理
			Thread.sleep(new Random().nextInt(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.name = name;
		return true;
	}
	
	@Override
	public List<UserDO> list() {
		List<UserDO> lists = new ArrayList<UserDO>();
		UserDO use1 = new UserDO();
		use1.setId(1);
		use1.setName("hejian");
		use1.setSex(0);
		
		UserDO user2 = new UserDO();
		user2.setId(2);
		user2.setName("shanghai");
		user2.setSex(1);
		
		lists.add(use1);
		lists.add(user2);
		return lists;
	}
	
}
