package com.analog.data.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.analog.data.BaseTest;
import com.analog.data.entity.DataConfig;

public class DispatcherServiceTest extends BaseTest {

	@Autowired
	private IDispatcherService dispatcherService;
	@Autowired
	private DataConfig dataConfig;
	
	@Test
	public void testLogin() {
		try {
			Map<String, String> map =new HashMap<String, String>();
			map.put("centerUrl", "http://192.168.2.68:6661/center-web3.0/");
			map.put("userName", "test003");
			map.put("password", "88888888");
			Map<String, String> login = dispatcherService.login(map);
			assertEquals("1",login.get("code"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testDataConfig() {
		try {
			System.out.println(dataConfig.getCenterAddr());
			System.out.println(dataConfig.getItems().get(1).getEvent().getInterval());
			System.out.println(dataConfig.getItems().get(1).getTask().getTaskTitle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
