package com.analog.data.entity;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DataConfig {
	
	private boolean use;
	private String centerAddr;
	private String user;
	private String password;
	private Integer polling;
	private Integer interval;
	private List<Item> items;
	
	public boolean isUse() {
		return use;
	}
	public void setUse(boolean use) {
		this.use = use;
	}
	public String getCenterAddr() {
		return centerAddr;
	}
	public void setCenterAddr(String centerAddr) {
		this.centerAddr = centerAddr;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPolling() {
		return polling;
	}
	public void setPolling(Integer polling) {
		this.polling = polling;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
}
