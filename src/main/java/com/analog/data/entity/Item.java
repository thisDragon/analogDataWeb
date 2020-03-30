package com.analog.data.entity;

import org.springframework.stereotype.Component;

@Component
public class Item {

	private String tel;
	private String path;
	private Event event;
	private Patrol patrol;
	private Task task;
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Patrol getPatrol() {
		return patrol;
	}
	public void setPatrol(Patrol patrol) {
		this.patrol = patrol;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
}
