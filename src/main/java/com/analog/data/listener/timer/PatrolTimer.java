package com.analog.data.listener.timer;

import java.util.Timer;

import com.analog.data.entity.Item;
import com.analog.data.listener.task.PatrolTask;

/**
* @ClassName: PatrolTimer
* @Description: 巡检定时器
* @author yangjianlong
* @date 2019年9月6日
*
 */
public class PatrolTimer {

	private Timer timer;
	public PatrolTimer(Item item) {
		timer = new Timer();
		timer.schedule(new PatrolTask(timer,item), 0, item.getPatrol().getInterval());
	}
}
