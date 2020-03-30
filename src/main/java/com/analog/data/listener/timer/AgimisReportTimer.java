package com.analog.data.listener.timer;

import java.util.Timer;

import com.analog.data.entity.Item;
import com.analog.data.listener.DataListener;
import com.analog.data.listener.task.AgimisReportTask;

/**
* @ClassName: AgimisReportTimer
* @Description: 定位上报定时器
* @author yangjianlong
* @date 2019年9月6日
*
 */
public class AgimisReportTimer {
	
	private Timer timer;
	public AgimisReportTimer(Item item) {
		timer = new Timer();
		timer.schedule(new AgimisReportTask(timer,item), 0, DataListener.dataConfig.getInterval());
	}
}
