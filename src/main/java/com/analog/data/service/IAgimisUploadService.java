package com.analog.data.service;

import java.io.IOException;

public interface IAgimisUploadService {

	/**
	 * 
	* @Title: sendEvent
	* @Description: 事件上报
	* @param @param terminal
	* @param @param lon
	* @param @param lat
	* @param @param centerUrl
	* @param @param userName
	* @param @throws IOException    参数
	* @return void    返回类型
	* @throws
	 */
	public abstract void sendEvent(String terminal,double lon, double lat,String centerUrl,String userName,Long time,String remark) throws IOException;
}
