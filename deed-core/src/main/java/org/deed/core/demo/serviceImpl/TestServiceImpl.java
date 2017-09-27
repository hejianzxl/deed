package org.deed.core.demo.serviceImpl;

import org.deed.client.demo.service.TestService;
import org.deed.client.server.annotation.RpcService;

@RpcService(TestService.class)
public class TestServiceImpl implements TestService {

	@Override
	public void print(String result) {
		System.out.println("TestServiceImpl print " + result);
	}	
}
