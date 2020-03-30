package com.analog.data.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.exception.ParamException;
import com.analog.data.service.ITeamWorkService;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.UrlUtils;

@Service
public class TeamWorkService implements ITeamWorkService {

	private static final Logger logger = LoggerFactory.getLogger(TeamWorkService.class);
	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private JedisUtil.Hash jedisHash;
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertTeamWork(String tel, List<String> players, String centerUrl, String userName, Long jobStartTime, Long jobEndTime,String taskTitle,String context) throws ClientProtocolException, IOException {
		
		String key = "";
		if (centerUrl == null && userName == null) {
			key ="userInfo_"+dataConfig.getCenterAddr()+"_"+dataConfig.getUser();
		}else{
			key ="userInfo_"+centerUrl+"_"+userName;
		}
		Map<String, String> keyMap = jedisHash.hgetAll(key);
		
		Map<String, Object> queryResouseInfo = queryResouseInfo(centerUrl,userName,keyMap,dataConfig,jedisHash);
		Object dataObj = queryResouseInfo.get("data");
		if (dataObj == null) {
			return ;
		}
		List<Map<String, String>> data = (List<Map<String, String>>) dataObj;
		Random ra =new Random();
		String jobIds =data.get(ra.nextInt(data.size())).get("id");
		String orgGroupId = keyMap.get("levelid");
		String leader = "{\"terminal\":\""+tel+"\",\"terName\":\""+tel+"\"}";
		List<Map<String, String>> playersStr = new ArrayList<Map<String, String>>();
		for (String player : players) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("terminal", player);
			m.put("terName", player);
			playersStr.add(m);
		}
		
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("orgGroupId", orgGroupId );
		map.put("jobIds", jobIds);
		map.put("jobTheme", taskTitle);
		map.put("jobContent", context);
		map.put("jobStartTime", jobStartTime);
		map.put("jobEndTime", jobEndTime);
		map.put("leader", leader );
		//map.put("participant", "[{\"terminal\":\"13696812203\",\"terName\":\"杨建龙2.68\"}]");
		map.put("participant", JSON.toJSON(playersStr));
		
		Map<String, String> requestMap =new HashMap<String, String>();
		String requestUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.insertTeamWorkUrl);
		
		requestMap.put("jsonParamStr", JSON.toJSONString(map));
		requestMap.put("run", UrlUtils.insertTeamWorkRun);
		requestMap.put("modulename", UrlUtils.insertTeamWorkModuleName);
		
		Map<String, String> requestParamsBySid = UrlUtils.getRequestParamsBySid(requestMap, centerUrl,userName, dataConfig, jedisHash);
		
		String httpClientGet = HttpRequest.httpClientGetOfPartition(requestUrl, requestParamsBySid);
		Map<String, Object> parseObject = JSON.parseObject(httpClientGet, JSONObject.class);
		if (false == parseObject.get("code").toString().equals("1")) {
			throw new ParamException("新增协同任务失败");
		}
	}
	
	/**
	* @Title: queryResouseInfo
	* @Description: 获取作业资源列表,用于新增协同时候的参数
	* @param @param key
	* @param @param dataConfig
	* @param @param jedisHash
	* @param @return
	* @param @throws ClientProtocolException
	* @param @throws IOException    参数
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	private Map<String, Object> queryResouseInfo(String centerUrl, String userName,Map<String, String> keyMap,DataConfig dataConfig,JedisUtil.Hash jedisHash) throws ClientProtocolException, IOException{
		Map<String, String> requestMap =new HashMap<String, String>();
		String requestUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.queryResouseInfoUrl);
		
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("orgGroupId", keyMap.get("levelid"));
		map.put("resourceName", "");
		map.put("resourceType", "");
		map.put("attendanceFlag", "");
		map.put("inspectionFlag", "");
		map.put("railFlag", "");
		map.put("gridMonitoringFlag", "");
		map.put("dataValidFlag", "");
		map.put("startTime", "");
		map.put("endTime", "");
		map.put("currPage", "1");
		map.put("pageSize", "8");
		
		requestMap.put("jsonParamStr", JSON.toJSONString(map));
		requestMap.put("run", UrlUtils.queryResouseInfoRun);
		requestMap.put("modulename", UrlUtils.queryResouseInfoModuleName);
		
		Map<String, String> requestParamsBySid = UrlUtils.getRequestParamsBySid(requestMap,centerUrl, userName, dataConfig, jedisHash);
		
		String httpClientGet = HttpRequest.httpClientGetOfPartition(requestUrl, requestParamsBySid);
		Map<String, Object> parseObject = JSON.parseObject(httpClientGet, JSONObject.class);
		if (parseObject.get("code").toString().equals("1") == false) {
			logger.error("获取作业资源列表失败:"+parseObject);
			return null;
		}
		return parseObject;
	}

}
