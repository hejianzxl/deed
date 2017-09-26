package org.deed.core;

import java.util.List;

import org.deed.client.RpcClient;
import org.deed.client.demo.service.UserDO;
import org.deed.client.demo.service.UserService;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws InterruptedException {
		RpcClient rpcClient = new RpcClient("127.0.0.1", 18888);
		@SuppressWarnings("static-access")
		UserService userService = rpcClient.create(UserService.class);
		boolean result = userService.setName("hejianyeye");
		System.out.println("userServie set name " +result);
		
		String name = userService.getName(1);
		System.out.println("userService getName " + name );
		
		
		List<UserDO> lists = userService.list();
		lists.forEach(System.out::println);
		
	}
}
