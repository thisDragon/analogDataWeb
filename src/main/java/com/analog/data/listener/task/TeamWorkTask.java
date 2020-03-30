package com.analog.data.listener.task;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.analog.data.entity.Item;
import com.analog.data.exception.ParamException;
import com.analog.data.listener.DataListener;
import com.analog.data.util.DateUtils;

import jodd.util.StringUtil;

public class TeamWorkTask extends TimerTask{

	private static Logger logger = LoggerFactory.getLogger(TeamWorkTask.class);
	private Timer timer;
	private Item item;
	
	public TeamWorkTask(Timer timer,Item item) {
		this.timer = timer;
		this.item = item;
	}
	
	@Override
	public void run() {
		boolean isPass = true;
		// 判断是否结束(时间段范围是否上报完成,完成则停止)
		
		if (isPass) {
			String tel = item.getTel();
			String player = item.getTask().getPlayer();
			String taskTitle = item.getTask().getTaskTitle();
			String context = item.getTask().getContext();
			
			if (StringUtil.isEmpty(tel)) throw new ParamException("配置文件的终端号不能为空");
			if (StringUtil.isEmpty(player)) throw new ParamException("参与人不能为空");
			if (StringUtil.isEmpty(taskTitle)) throw new ParamException("协同任务标题不能为空");
			if (StringUtil.isEmpty(context)) throw new ParamException("协同任务内容不能为空");
			
			String[] tels = tel.split(";");
			List<String> players = Arrays.asList(player.split(";"));
			for (String terminal : tels) {
				try {
					DataListener.teamWorkService.insertTeamWork(terminal,players, null, null,DateUtils.getCurrentDayFirstMoment().getTime(),DateUtils.getCurrentDayLastMoment().getTime(),taskTitle,context);
					logger.info("----------终端:"+terminal+"开始任务上报:"+DateUtils.date2Str(new Date(), DateUtils.NORMAL_FORMAT)+"----------");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}else{
			logger.info("结束协同任务上报");
			this.timer.cancel();
		}
	}
}
