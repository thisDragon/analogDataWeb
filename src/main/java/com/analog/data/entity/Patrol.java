package com.analog.data.entity;

import org.springframework.stereotype.Component;

@Component
public class Patrol {

	private Integer interval;
	private String remark;
	
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
