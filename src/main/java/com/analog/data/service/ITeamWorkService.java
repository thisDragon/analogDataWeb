package com.analog.data.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public interface ITeamWorkService {

	/**
	* @Title: insertTeamWork
	* @Description: 新增协同任务
	* @param @param tel
	* @param @param players
	* @param @param centerUrl
	* @param @param userName
	* @param @throws ClientProtocolException
	* @param @throws IOException    参数
	* @return void    返回类型
	* @throws
	 */
	public abstract void insertTeamWork(String tel, List<String> players,String centerUrl,String userName,Long jobStartTime,Long jobEndTime,String taskTitle,String context) throws ClientProtocolException, IOException;
}
