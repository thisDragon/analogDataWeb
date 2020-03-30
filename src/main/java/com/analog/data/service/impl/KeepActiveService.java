package com.analog.data.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analog.data.entity.DataConfig;
import com.analog.data.service.IDispatcherService;
import com.analog.data.service.IKeepActiveService;

/**
 * 
* @ClassName: KeepActiveService
* @Description: 调度员保活
* @author yangjianlong
* @date 2019年9月10日
*
 */
@Service
public class KeepActiveService implements IKeepActiveService{
	@Autowired
	private IDispatcherService dispatcherService;
	
	@Override
	public void KeepActive(String centerUrl,String userName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("centerUrl", centerUrl);
		map.put("userName",  userName);
		dispatcherService.KeepActive(map);
	}
}
