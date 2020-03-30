package com.analog.data.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.analog.data.cache.JedisUtil;
import com.analog.data.service.IDispatcherService;
import com.analog.data.util.CfgKeyUtils;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.MD5Utils;
import com.analog.data.util.UrlUtils;

/**
 * 
* @ClassName: DispatcherService
* @Description: 调度员服务
* @author yangjianlong
* @date 2019年9月2日
*
 */
@Service
public class DispatcherService implements IDispatcherService{
	
	@Autowired
	private JedisUtil.Hash jedisHash;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	public Map<String, String> login(Map<String, String> map) throws Exception{
		Map<String, String> resultMap = new HashMap<String, String>();
		String centerUrl = map.get("centerUrl");
		String userName = map.get("userName");
		String password = map.get("password");
		
		String params = "";
		params += "dEnName=" + userName + "&";
		params += "dPassword=" + MD5Utils.getMd5(password) ;
		String jsonp = HttpRequest.sendGet(centerUrl+UrlUtils.loginUrl, params);// 参数不支持中文
		Pattern pattern = Pattern.compile("run\\((.*?)\\)");
		Matcher matcher = pattern.matcher(jsonp);
		if (matcher.find()) {
			JSONObject object = JSON.parseObject(matcher.group(1));
			int code = object.getIntValue("code");
			String partitionState = object.getString("partitionState");
			if (code == 1 && partitionState.equals("[true]")) {
				resultMap = JSONObject.parseObject(object.toJSONString().replaceAll("\\[", "").replaceAll("\\]", ""), new TypeReference<Map<String, String>>(){});
				
				jedisHash.hmset("userInfo_"+centerUrl+"_"+userName, nullToEmpty(resultMap));
			}else{
				jedisKeys.del("userInfo_"+centerUrl+"_"+userName);
			}
		}
		
		return resultMap;
	}

	@Override
	public Map<String, String> KeepActive(Map<String, String> map) throws Exception{
		String centerUrl = map.get("centerUrl");
		String userName = map.get("userName");
		if (jedisKeys.exists("userInfo_"+centerUrl+"_"+userName)) {
			Map<String, String> centerInfo = jedisHash.hgetAll("centerInfo");
			
			String params = "";
			params += "username=" + userName + "&";
			params += "sid=" + centerInfo.get("sid") + "&";
			params += "cfgkey=" + CfgKeyUtils.getCfgKey(userName);
			String jsonp = HttpRequest.sendGet(centerUrl+UrlUtils.keepActiveUrl, params);// 参数不支持中文
			Pattern pattern = Pattern.compile("run\\((.*?)\\)");
			Matcher matcher = pattern.matcher(jsonp);
			if (matcher.find()) {
				JSONObject object = JSON.parseObject(matcher.group(1));
				int code = object.getIntValue("code");
				if (code == 1 ) {
					
				}else{
					//失活重新登录
					login(map);
				}
			}
		}else{
			//未登录进行登录
			login(map);
		}
		return null;
	}

	
	private Map<String, String> nullToEmpty(Map<String, String> map) {
		Set<String> set = map.keySet();
		if(set != null && !set.isEmpty()) {
			for(String key : set) {
				if(map.get(key) == null){ 
					map.put(key, ""); 
				}
			}
		}
		return map;
	}
}
