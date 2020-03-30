package com.analog.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.service.IPatrolService;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.UrlUtils;

/**
 * 
* @ClassName: PatrolService
* @Description: 巡检服务
* @author yangjianlong
* @date 2019年9月10日
*
 */
@Service
public class PatrolService implements IPatrolService{

	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private JedisUtil.Hash jedisHash;
	
	@Override
	public boolean patrolUp(String tel, Double lon, Double lat, String centerUrl, String userName,String remark,Long time) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String key = "";
		String orgid = "";
		String attendid = "";
		if (centerUrl == null && userName == null) {
			key ="userInfo_"+dataConfig.getCenterAddr()+"_"+dataConfig.getUser();
		}else{
			key ="userInfo_"+centerUrl+"_"+userName;
		}
		Map<String, String> keyMap = jedisHash.hgetAll(key);
		orgid = keyMap.get("orgIds");
		map.put("terminal", tel);
		map.put("orgid", orgid );
		//通过巡检列表获取配置id
		keyMap.put("terminal", tel);
		List<Map<String, String>> inspectionList = getInspectionList(centerUrl,userName,keyMap,  dataConfig, jedisHash);
		if (inspectionList.size() != 0) {
			Random ra =new Random();
			attendid =inspectionList.get(ra.nextInt(inspectionList.size())).get("ID");
		}else{
			return false;
		}
		
		map.put("attendid", attendid);
		map.put("remark", remark);
		map.put("lon", String.valueOf(lon));
		map.put("lat", String.valueOf(lat));
		if (time == null) {
			time = new Date().getTime();
		}
		map.put("time", time);
		
		String reportUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.patrolupUrl);
		HttpRequest.sendPostOfBody(reportUrl, JSON.toJSONString(map));
		return true;
	}
	
	/**
	 * @throws Exception 
	* @Title: getInspectionList
	* @Description: 获取巡检列表
	* @param @param terminal
	* @param @param orgid
	* @param @return    参数
	* @return List<Map<String,String>>    返回类型
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getInspectionList(String centerUrl, String userName,Map<String, String> keyMap,DataConfig dataConfig,JedisUtil.Hash jedisHash) throws Exception{
		String requestUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.getinspectionlistUrl);
		
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("terminal", keyMap.get("terminal"));
		requestMap.put("orgid", keyMap.get("orgIds"));
		requestMap.put("curpage", "1");
		requestMap.put("pagesize", "15");
		//这里由于该接口只是用于模拟数据,没有对应模块和回调函数,所以随意选取了一个可用的模块和回调函数
		requestMap.put("run", UrlUtils.queryResouseInfoRun);
		requestMap.put("modulename", UrlUtils.queryResouseInfoModuleName);
		
		Map<String, String> requestParamsBySid = UrlUtils.getRequestParamsBySid(requestMap, centerUrl, userName, dataConfig, jedisHash);
		String httpClientGet = HttpRequest.httpClientGetOfPartition(requestUrl, requestParamsBySid);
		Map<String, Object> parseObject = JSON.parseObject(httpClientGet, JSONObject.class);
		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		if (parseObject.get("code").toString().equals("1")) {
			resultList = (List<Map<String, String>>) parseObject.get("list");
		}
		
		return resultList;
	}

}
