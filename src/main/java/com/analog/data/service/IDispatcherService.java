package com.analog.data.service;

import java.util.Map;

/**
 * 
* @ClassName: IDispatcherService
* @Description: 调度员服务
* @author yangjianlong
* @date 2019年9月2日
*
 */
public interface IDispatcherService {
	
	/**
	 * 
	* @Title: login
	* @Description: 登录
	* @param @param map
	* @param @return    参数
	* @return Map<String,String>    返回类型
	* @throws
	 */
	public Map<String, String> login(Map<String, String> map) throws Exception;
	
	/**
	 * 
	* @Title: KeepActive
	* @Description: 保持调度员存在状态(1小时内必须调用一次,1小时失活)
	* @param @param map
	* @param @return    参数
	* @return Map<String,String>    返回类型
	* @throws
	 */
	public Map<String, String> KeepActive(Map<String, String> map) throws Exception;
}
