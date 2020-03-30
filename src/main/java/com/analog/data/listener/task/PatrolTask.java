package com.analog.data.listener.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.analog.data.cache.CacheKey;
import com.analog.data.entity.DataConfig;
import com.analog.data.entity.Item;
import com.analog.data.exception.ParamException;
import com.analog.data.listener.DataListener;
import com.analog.data.util.DateUtils;

import jodd.util.StringUtil;

public class PatrolTask extends TimerTask{

	private static Logger logger = LoggerFactory.getLogger(PatrolTask.class);
	private Timer timer;
	private Item item;
	
	public PatrolTask(Timer timer,Item item) {
		this.timer = timer;
		this.item =item;
	}
	
	@Override
	public void run() {
		boolean isPass = true;
		// 判断是否结束(时间段范围是否上报完成,完成则停止)
		
		if (isPass) {
			DataConfig dataConfig= DataListener.dataConfig;
			String tel = item.getTel();
			String path = item.getPath();
			String remark = item.getPatrol().getRemark();
			if (StringUtil.isEmpty(tel)) throw new ParamException("配置文件的终端号不能为空");
			if (StringUtil.isEmpty(path)) throw new ParamException("配置文件的路线不能为空");
			
			String[] tels = tel.split(";");
			String[] paths = path.split(";");
			Integer polling = dataConfig.getPolling();
			
			for (String terminal : tels) {
				Integer index = Integer.parseInt(DataListener.jedisStrings.get(CacheKey.PatrolKey+terminal));
				setCache(polling, index, paths, terminal);
				
				String poi = paths[index];
				try {
					DataListener.patrolService.patrolUp(terminal, Double.parseDouble(poi.split(",")[0]), Double.parseDouble(poi.split(",")[1]),null,null,remark,null);
					logger.info("----------终端:"+terminal+"开始巡检上报:"+DateUtils.date2Str(new Date(), DateUtils.NORMAL_FORMAT)+"----------");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}else{
			logger.info("结束巡检上报");
			this.timer.cancel();
		}
	}

	/**
	* @Title: setCache
	* @Description: 用缓存来控制轮询方式(设置的轮询索引都是下次需要访问的索引数)
	* @param @param polling
	* @param @param index
	* @param @param paths
	* @param @param terminal    参数
	* @return void    返回类型
	* @throws
	 */
	private void setCache(Integer polling, Integer index,String[] paths,String terminal){
		//循环轮询
		if (polling == 0) {
			if ( index == paths.length-1 ) {
				DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(0));
			}else{
				DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(index+1));
			}
		//往返轮询
		}else if (polling == 1) {
			String operState = DataListener.jedisStrings.get(CacheKey.PatrolOperStateKey+terminal);
			
			if (operState.equals("add")) {
				if ( index == paths.length-1 ) {
					DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(index-1));
					DataListener.jedisStrings.set(CacheKey.PatrolOperStateKey+terminal, "delete");
				}else{
					DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(index+1));
				}
			}else if(operState.equals("delete")){
				if ( index == 0 ) {
					DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(1));
					DataListener.jedisStrings.set(CacheKey.PatrolOperStateKey+terminal, "add");
				}else{
					DataListener.jedisStrings.set(CacheKey.PatrolKey+terminal,String.valueOf(index-1));
				}
			}
		}
	}
}
