package com.analog.data.listener.timer;

import java.util.Timer;
import com.analog.data.listener.task.KeepActiveTask;
/**
* @ClassName: KeepActiveTimer
* @Description: 保活定时器(调度员一小时就会失活,所以10分钟调用一次保活接口)
* @author yangjianlong
* @date 2019年9月5日
*
 */
public class KeepActiveTimer {
	
	private static final int taskTime = 10*60*1000;//默认10分钟执行一次
	
	private Timer timer;
	public KeepActiveTimer() {
		timer = new Timer();
		timer.schedule(new KeepActiveTask(timer), 0, taskTime );
	}
}
