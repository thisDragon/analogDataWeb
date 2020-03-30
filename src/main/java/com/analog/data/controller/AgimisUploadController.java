package com.analog.data.controller;

import java.util.ArrayList;
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
import com.analog.data.service.IAgimisUploadService;
import com.analog.data.util.Constants;
import com.analog.data.util.DateUtils;
import com.analog.data.util.HttpServletRequestUtil;

import jodd.util.StringUtil;

/**
* @ClassName: AgimisUploadController
* @Description: 事件上报
* @author yangjianlong
* @date 2019年9月11日
*
 */
@Controller
public class AgimisUploadController {

	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private IAgimisUploadService agimisUploadService;
	
	@RequestMapping(value = "/agimis/upload", method = { RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> upload(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String terminals = HttpServletRequestUtil.getString(request, "terminals");
		String poi = HttpServletRequestUtil.getString(request, "poi");
		String centerUrl = HttpServletRequestUtil.getString(request, "centerUrl");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String reportTime = HttpServletRequestUtil.getString(request, "reportTime");
		String remark = HttpServletRequestUtil.getString(request, "remark");
		
		try {
			if (StringUtil.isEmpty(terminals)) throw new ParamException("termianls不能为空");
			if (StringUtil.isEmpty(poi)) throw new ParamException("poi不能为空");
			if (StringUtil.isEmpty(centerUrl)) throw new ParamException("centerUrl不能为空");
			if (StringUtil.isEmpty(userName)) throw new ParamException("userName不能为空");
			if (StringUtil.isEmpty(reportTime)) throw new ParamException("reportTime不能为空");
			if (StringUtil.isEmpty(remark)) throw new ParamException("remark不能为空");
			
			if (dataConfig.getCenterAddr().equals("centerUrl") && dataConfig.getUser().equals(userName)) {
				throw new ParamException("模拟用户不能和配置用户为同一个");
			}
			String[] tels = terminals.split(";");
			long time = Long.parseLong(reportTime);
			
			//输入日志到界面控制台
			List<String> logs = new ArrayList<String>();
			for (int i = 0; i < tels.length; i++) {
				double lon = Double.parseDouble(poi.split(",")[0]);
				double lat = Double.parseDouble(poi.split(",")[1]);
				agimisUploadService.sendEvent(tels[i], lon, lat, centerUrl, userName,time,remark);
				
				StringBuffer sb = new StringBuffer();
				sb.append("事件上报-------->");
				sb.append("终端号码为:");
				sb.append(tels[i]);
				sb.append(",");
				sb.append("在时间为:");
				sb.append(DateUtils.date2Str(new Date(time), DateUtils.NORMAL_FORMAT));
				sb.append(",");
				sb.append("在经度为:");
				sb.append(lon);
				sb.append(",");
				sb.append("纬度为:");
				sb.append(lat);
				sb.append("的位置进行了一次事件上报;");
				
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
