package com.analog.data.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.analog.data.cache.CacheKey;
import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.entity.Item;
import com.analog.data.exception.ParamException;
import com.analog.data.listener.timer.AgimisReportTimer;
import com.analog.data.listener.timer.AgimisUploadTimer;
import com.analog.data.listener.timer.KeepActiveTimer;
import com.analog.data.listener.timer.PatrolTimer;
import com.analog.data.listener.timer.TeamWorkTimer;
import com.analog.data.service.IAgimisReportService;
import com.analog.data.service.IAgimisUploadService;
import com.analog.data.service.IDispatcherService;
import com.analog.data.service.IKeepActiveService;
import com.analog.data.service.IPatrolService;
import com.analog.data.service.ITeamWorkService;

import jodd.util.StringUtil;

/**
 * 
* @ClassName: DataListener
* @Description: 数据模拟监听器
* @author yangjianlong
* @date 2019年9月5日
*
 */
public class DataListener implements ServletContextListener{

	private static Logger logger = LoggerFactory.getLogger(DataListener.class);
	public static DataConfig dataConfig;
	public static JedisUtil.Hash jedisHash;
	public static JedisUtil.Strings jedisStrings;
	public static JedisUtil.Keys jedisKeys;
	public static IDispatcherService dispatcherService;
	public static IAgimisReportService agimisReportService;
	public static IAgimisUploadService agimisUploadService;
	public static IKeepActiveService keepActiveService;
	public static IPatrolService patrolService;
	public static ITeamWorkService teamWorkService;
	
	/**
	 * 监听器初始化入口
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			//初始化bean
			initBeans(event);
			//初始化缓存
			initCache();
			if (dataConfig.isUse() == false) {
				return;
			}
			//登录
			Map<String, String> loginMap = login();
			if (loginMap.get("code") == null || loginMap.get("code").equals("0")) throw new ParamException("登录失败");
			
			List<Item> items = dataConfig.getItems();
			
			for (Item item : items) {
				//参数校验
				if (StringUtil.isEmpty(item.getPath()) && item.getPath().split(";").length==1) {
					throw new ParamException("位置至少输入2组");
				}
				//保活
				new KeepActiveTimer();
				//位置上报
				new AgimisReportTimer(item);
				//事件上报
				new AgimisUploadTimer(item);
				//巡检上报
				new PatrolTimer(item);
				//协同任务上报
				new TeamWorkTimer(item);
			}
			
		} catch (Exception e) {
			logger.info("监听器初始化异常:"+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	/**
	* @Title: initBeans
	* @Description: 初始化bean
	* @param @param event    参数
	* @return void    返回类型
	* @throws
	 */
	private void initBeans(ServletContextEvent event){
		dataConfig = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(DataConfig.class);
		jedisHash= WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(JedisUtil.Hash.class);
		jedisStrings= WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(JedisUtil.Strings.class);
		jedisKeys= WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(JedisUtil.Keys.class);
		dispatcherService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IDispatcherService.class);
		agimisReportService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IAgimisReportService.class);
		agimisUploadService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IAgimisUploadService.class);
		keepActiveService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IKeepActiveService.class);
		patrolService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(IPatrolService.class);
		teamWorkService = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getBean(ITeamWorkService.class);
	}
	
	/**
	* @Title: initCache
	* @Description: 初始化缓存
	* @param     参数
	* @return void    返回类型
	* @throws
	 */
	private void initCache(){
		List<Item> items = dataConfig.getItems();
		
		for (Item item : items) {
			String[] termianls = item.getTel().split(";");
			//这个0:是每个终端经纬度的循环索引,初始值为0
			for (int i = 0; i < termianls.length; i++) {
				jedisStrings.set(CacheKey.ReportKey+termianls[i], "0");
				jedisStrings.set(CacheKey.ReportOperStateKey+termianls[i], "add");
				jedisStrings.set(CacheKey.EventKey+termianls[i], "0");
				jedisStrings.set(CacheKey.EventOperStateKey+termianls[i], "add");
				jedisStrings.set(CacheKey.PatrolKey+termianls[i], "0");
				jedisStrings.set(CacheKey.PatrolOperStateKey+termianls[i], "add");
				jedisStrings.set(CacheKey.TaskKey+termianls[i], "0");
				jedisStrings.set(CacheKey.TaskOperStateKey+termianls[i], "add");
			}
		}
	}
	
	/**
	 * @throws Exception 
	* @Title: login
	* @Description: 登录
	* @param     参数
	* @return void    返回类型
	* @throws
	 */
	private Map<String, String> login() throws Exception{
		String centerUrl = dataConfig.getCenterAddr();
		String userName = dataConfig.getUser();
		String password = dataConfig.getPassword();
		
		if (StringUtil.isEmpty(centerUrl)) throw new ParamException("中心地址不能为空");
		if (StringUtil.isEmpty(userName)) throw new ParamException("用户名不能为空");
		if (StringUtil.isEmpty(password)) throw new ParamException("密码不能为空");
		if (dataConfig.getPolling()==null) throw new ParamException("轮询配置不能为空");
		if (dataConfig.getInterval()==null) throw new ParamException("时间间隔不能为空");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("centerUrl", centerUrl);
		map.put("userName", userName);
		map.put("password", password);
		
		return dispatcherService.login(map);
	}
}
