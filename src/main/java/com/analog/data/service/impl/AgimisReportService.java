package com.analog.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.service.IAgimisReportService;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.UrlUtils;

/**
 * 
* @ClassName: AgimisReportService
* @Description: 定位上报
* @author yangjianlong
* @date 2019年9月10日
*
 */
@Service
public class AgimisReportService implements IAgimisReportService {

	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private JedisUtil.Hash jedisHash;
	
	@Override
	public void sendPostPosition(String tel, Double lon, Double lat, String centerUrl, String userName,Long milliseconds) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> androiddata = new ArrayList<Map<String, Object>>();
		Map<String, Object> androiddataMap = new HashMap<String, Object>();
		
		if (milliseconds == null) {
			milliseconds = new Date().getTime();
		}

		// 上报安卓数据
		androiddataMap.put("gpsstatus", 6);
		androiddataMap.put("hv", 0);
		androiddataMap.put("lon", lon);
		androiddataMap.put("lat", lat);
		androiddataMap.put("milliseconds", String.valueOf(milliseconds));
		androiddataMap.put("precision", 100);
		androiddata.add(androiddataMap);

		map.put("version", "keshihuaVersion:47");
		map.put("gpsswitch", "1");
		map.put("tel", tel);
		map.put("androiddata", androiddata);
		String param = JSON.toJSONString(map);
		
		String reportUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.agimisReportUrl);
		
		HttpRequest.sendPost(reportUrl, param.toString());
	}
}
