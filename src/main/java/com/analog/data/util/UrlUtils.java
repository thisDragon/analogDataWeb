package com.analog.data.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;

import jodd.util.Base64;

/**
 * 
* @ClassName: UrlUtils
* @Description: url工具类
* @author yangjianlong
* @date 2019年9月5日
*
 */
public class UrlUtils {
	//中心调度员登录接口
	public static final String loginUrl = "/dispatcher/login";
	//中心调度员保活接口
	public static final String keepActiveUrl = "/dispatcher/heartbeat";
	//位置上报接口
	public static final String agimisReportUrl = "/analogData/agimis/report";
	//事件上报接口(即图片上报和视频上报)
	public static final String agimisUploadUrl = "/analogData/agimis/upload";
	//巡检上报接口
	public static final String patrolupUrl = "/analogData/agimis/jobresource/patrolup";
	//获取巡检列表接口
	public static final String getinspectionlistUrl = "/analogData/jobresource/getinspectionlist";
	//发布协同任务(拦截器未过滤)
	public static final String insertTeamWorkUrl ="/analogData/jobRes/insertTeamWork";
	public static final String insertTeamWorkRun = "keshihua.operation.JobTwoSynergy.twAddCallback";
	public static final String insertTeamWorkModuleName = "JobTwoSynergy";
	//获取资源类型(拦截器未过滤)
	public static final String queryResouseInfoUrl = "/jobRes/queryResouseInfo";
	public static final String queryResouseInfoRun = "keshihua.operation.JobTwoSynergy.setManageList";
	public static final String queryResouseInfoModuleName = "JobTwoSynergy";
	//获取业务字典列表(拦截器未过滤)
	public static final String getDataDicTreeUrl ="/datadict/getDataDicTree";
	public static final String getDataDicTreeRun = "keshihua.operation.Dictionary.m_getPicleftList";
	public static final String getDataDicTreeModuleName = "Dictionary";
	//地图访问地址
	public static final String mapUrl = "http://122.152.207.214:8080/w8/mapSerMng/mapcrl";
	/**
	* @Title: getRequestParamsBySid
	* @Description: 获取请求参数(注意:该请求参数是未拦截器过滤的请求所必须的参数格式)
	* @param @param requestMap
	* @param @param centerUrl
	* @param @param userName
	* @param @param dataConfig
	* @param @param jedisHash
	* @param @return    参数
	* @return Map<String,String>    返回类型
	* @throws
	 */
	public static Map<String, String> getRequestParamsBySid(Map<String, String> requestMap,String centerUrl,String userName,DataConfig dataConfig,JedisUtil.Hash jedisHash){
		String key = "";
		if (centerUrl == null && userName == null) {
			key ="userInfo_"+dataConfig.getCenterAddr()+"_"+dataConfig.getUser();
		}else{
			key ="userInfo_"+centerUrl+"_"+userName;
		}
		Map<String, String> keyMap = jedisHash.hgetAll(key);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("s", "1");
		resultMap.put("r", "keshihua.m_reLoadDatas");
		resultMap.put("f", "f"+String.valueOf((long)((Math.random()*9+1)*1000000000L)));
		resultMap.put("ssid", keyMap.get("sid"));
		resultMap.put("comid", keyMap.get("orgIds"));
		resultMap.put("c", "1");
		//这个d参数是主要传递的请求参数
		requestMap.put("orgid", keyMap.get("orgIds"));
		requestMap.put("levelid", keyMap.get("levelid"));
		requestMap.put("sid", keyMap.get("sid"));
		resultMap.put("d", getDStr(requestMap));
		//这个参数在分区上目前是无效的
		resultMap.put("isEncrypt", "true");
		
		return resultMap;
	}
	
	/**
	* @Title: getDStr
	* @Description: 获取参数d的字符串参数集
	* @param @param map
	* @param @return    参数
	* @return String    返回类型
	* @throws
	 */
	private static String getDStr(Map<String, String> map){
		StringBuffer dStr = new StringBuffer();
		
		for (String key : map.keySet()) {
			dStr.append(key);
			dStr.append("=");
			dStr.append(map.get(key));
			dStr.append("&");
		}

		String string = "";
		try {
			string = new String(Base64.encodeToString(dStr.toString().substring(0, dStr.length()-1).getBytes("UTF-8")) );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return string;
	}
	
	/**
	 * 
	* @Title: getRequestUrl
	* @Description: 获取访问的分区请求地址(这类是已经被拦截器过滤的请求)
	* @param @param centerUrl
	* @param @param userName
	* @param @param dataConfig
	* @param @param jedisHash
	* @param @param urlSuffix
	* @param @return    参数
	* @return String    返回类型
	* @throws
	 */
	public static String getRequestUrl(String centerUrl,String userName,DataConfig dataConfig,JedisUtil.Hash jedisHash,String urlSuffix){
		
		String reportUrl = "";
		String key = "";
		if (centerUrl == null && userName == null) {
			key ="userInfo_"+dataConfig.getCenterAddr()+"_"+dataConfig.getUser();
		}else{
			key ="userInfo_"+centerUrl+"_"+userName;
		}
		Map<String, String> keyMap = jedisHash.hgetAll(key);
		reportUrl = "http://"+keyMap.get("ip")+":"+keyMap.get("port")+keyMap.get("partitionProject")+urlSuffix;
		
		return reportUrl;
	}
}
