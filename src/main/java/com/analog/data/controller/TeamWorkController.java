package com.analog.data.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.analog.data.entity.DataConfig;
import com.analog.data.exception.ParamException;
import com.analog.data.service.ITeamWorkService;
import com.analog.data.util.Constants;
import com.analog.data.util.DateUtils;
import com.analog.data.util.HttpServletRequestUtil;

import jodd.util.StringUtil;

/**
* @ClassName: TeamWorkController
* @Description: 协同任务
* @author yangjianlong
* @date 2019年9月11日
*
 */
@Controller
public class TeamWorkController {

	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private ITeamWorkService teamWorkService;
	
	/**
	* @Title: insertTeamWork
	* @Description: 新增协同任务
	* @param @param request
	* @param @return    参数
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	@RequestMapping(value = "/jobRes/insertTeamWork", method = { RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> insertTeamWork(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String terminals = HttpServletRequestUtil.getString(request, "terminals");
		String centerUrl = HttpServletRequestUtil.getString(request, "centerUrl");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String players = HttpServletRequestUtil.getString(request, "players");
		String jobStartTime = HttpServletRequestUtil.getString(request, "jobStartTime");
		String jobEndTime = HttpServletRequestUtil.getString(request, "jobEndTime");
		String taskTitle = HttpServletRequestUtil.getString(request, "taskTitle");
		String context = HttpServletRequestUtil.getString(request, "context");
		
		try {
			if (StringUtil.isEmpty(terminals)) throw new ParamException("termianls不能为空");
			if (StringUtil.isEmpty(centerUrl)) throw new ParamException("centerUrl不能为空");
			if (StringUtil.isEmpty(userName)) throw new ParamException("userName不能为空");
			if (StringUtil.isEmpty(players)) throw new ParamException("players不能为空");
			if (StringUtil.isEmpty(jobStartTime)) throw new ParamException("jobStartTime不能为空");
			if (StringUtil.isEmpty(jobEndTime)) throw new ParamException("jobEndTime不能为空");
			if (StringUtil.isEmpty(taskTitle)) throw new ParamException("taskTitle不能为空");
			if (StringUtil.isEmpty(context)) throw new ParamException("context不能为空");
			
			if (dataConfig.getCenterAddr().equals("centerUrl") && dataConfig.getUser().equals(userName)) {
				throw new ParamException("模拟用户不能和配置用户为同一个");
			}
			String[] tels = terminals.split(";");
			Long startTime = Long.parseLong(jobStartTime);
			Long endTime = Long.parseLong(jobEndTime);
			List<String> player = Arrays.asList(players.split(";"));
			//输入日志到界面控制台
			List<String> logs = new ArrayList<String>();
			for (int i = 0; i < tels.length; i++) {
				teamWorkService.insertTeamWork(tels[i], player, centerUrl, userName, startTime, endTime, taskTitle, context);
				
				StringBuffer sb = new StringBuffer();
				sb.append("协同任务-------->");
				sb.append("终端号码为:");
				sb.append(tels[i]);
				sb.append(",");
				sb.append("在开始时间为:");
				sb.append(DateUtils.date2Str(new Date(startTime), DateUtils.NORMAL_FORMAT));
				sb.append(",");
				sb.append("结束时间为:");
				sb.append(DateUtils.date2Str(new Date(endTime), DateUtils.NORMAL_FORMAT));
				sb.append("的时间段");
				sb.append("进行了一次协同任务;");
				
				logs.add(sb.toString());
			}
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.DATA, logs);
		} catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
		}
		
		return resultMap;
	}
}
