package com.analog.data.listener.timer;

import java.util.Timer;

import com.analog.data.entity.Item;
import com.analog.data.listener.task.AgimisUploadTask;

/**
* @ClassName: AgimisUploadTimer
* @Description: 事件上报定时器
* @author yangjianlong
* @date 2019年9月6日
*
 */
public class AgimisUploadTimer {

	private Timer timer;
	public AgimisUploadTimer(Item item) {
		timer = new Timer();
		timer.schedule(new AgimisUploadTask(timer,item), 0, item.getEvent().getInterval());
	}
}
