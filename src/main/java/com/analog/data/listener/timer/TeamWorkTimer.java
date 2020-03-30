package com.analog.data.listener.timer;

import java.util.Timer;

import com.analog.data.entity.Item;
import com.analog.data.listener.task.TeamWorkTask;

/**
* @ClassName: TeamWorkTimer
* @Description: 协同任务定时器
* @author yangjianlong
* @date 2019年9月6日
*
 */
public class TeamWorkTimer {

	private Timer timer;
	public TeamWorkTimer(Item item) {
		timer = new Timer();
		timer.schedule(new TeamWorkTask(timer,item), 0, item.getTask().getInterval());
	}
}
