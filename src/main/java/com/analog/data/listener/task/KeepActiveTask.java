package com.analog.data.listener.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.analog.data.listener.DataListener;
import com.analog.data.util.DateUtils;

public class KeepActiveTask extends TimerTask{

	private static Logger logger = LoggerFactory.getLogger(KeepActiveTask.class);
	private Timer timer;
	
	public KeepActiveTask(Timer timer) {
		this.timer = timer;
	}
	
	@Override
	public void run() {
		boolean isPass = true;
		
		if (isPass) {
			try {
				DataListener.keepActiveService.KeepActive(DataListener.dataConfig.getCenterAddr(),DataListener.dataConfig.getUser());
				logger.info("----------开始保活:"+DateUtils.date2Str(new Date(), DateUtils.NORMAL_FORMAT)+"----------");
			} catch (Exception e) {
				isPass=false;
				e.printStackTrace();
			}
		}else{
			logger.info("关闭保活");
			this.timer.cancel();
		}
		
		logger.info("");
	}

}
