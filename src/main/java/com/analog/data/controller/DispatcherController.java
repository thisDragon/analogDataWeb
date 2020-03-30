package com.analog.data.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.exception.ParamException;
import com.analog.data.service.IDispatcherService;
import com.analog.data.util.Constants;
import com.analog.data.util.HttpServletRequestUtil;
import com.analog.data.util.UrlUtils;

import jodd.util.StringUtil;

/**
* @ClassName: DispatcherController
* @Description: 调度员控制类
* @author yangjianlong
* @date 2019年9月11日
*
 */
@Controller
public class DispatcherController {

	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private IDispatcherService dispatcherService;
	@Autowired
	private JedisUtil.Hash jedisHash;
	
	/**
	* @Title: report
	* @Description: 登录
	* @param @param request
	* @param @return
	* @param @throws Exception    参数
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	@RequestMapping(value = "/dispatcher/login")
	@ResponseBody
	private Map<String, Object> login(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String centerUrl = HttpServletRequestUtil.getString(request, "centerUrl");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		
		try {
			if (StringUtil.isEmpty(centerUrl)) throw new ParamException("centerUrl不能为空");
			if (StringUtil.isEmpty(userName)) throw new ParamException("userName不能为空");
			if (StringUtil.isEmpty(password)) throw new ParamException("password不能为空");
			
			if (dataConfig.getCenterAddr().equals("centerUrl") && dataConfig.getUser().equals(userName)) {
				throw new ParamException("模拟用户不能和配置用户为同一个");
			}
			
			Map<String, String> map =new HashMap<String, String>();
			map.put("centerUrl", centerUrl);
			map.put("userName", userName);
			map.put("password", password);
			
			Map<String, String> loginMap = dispatcherService.login(map);
			if (loginMap.get("orgIds") ==null) {
				throw new ParamException("登录失败");
			}
			String reportUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.agimisReportUrl);
			loginMap.put("agimisReportUrl", reportUrl);
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.DATA, loginMap);
			resultMap.put(Constants.MESSAGE, "登录成功");
			
		} catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
			resultMap.put(Constants.DATA, e.getMessage());
		}
		
		return resultMap;
	}
	
	/**
	* @Title: keepActive
	* @Description: 保活
	* @param @param request
	* @param @return
	* @param @throws Exception    参数
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	@RequestMapping(value = "/dispatcher/keepActive")
	@ResponseBody
	private Map<String, Object> keepActive(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String centerUrl = HttpServletRequestUtil.getString(request, "centerUrl");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		
		try {
			if (StringUtil.isEmpty(centerUrl)) throw new ParamException("centerUrl不能为空");
			if (StringUtil.isEmpty(userName)) throw new ParamException("userName不能为空");
			
			if (dataConfig.getCenterAddr().equals("centerUrl") && dataConfig.getUser().equals(userName)) {
				throw new ParamException("模拟用户不能和配置用户为同一个");
			}
			
			Map<String, String> map =new HashMap<String, String>();
			map.put("centerUrl", centerUrl);
			map.put("userName", userName);
			
			dispatcherService.KeepActive(map);
			resultMap.put(Constants.CODE, Constants.NORMAL_CODE);
			resultMap.put(Constants.DATA, "保持调度员活性成功");
			
		} catch (Exception e) {
			resultMap.put(Constants.CODE, Constants.ERROR_CODE);
		}
		
		return resultMap;
	}
	
}
