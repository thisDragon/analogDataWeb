package com.analog.data.service;

public interface IAgimisReportService {

	public abstract void sendPostPosition(String tel, Double lon, Double lat,String centerUrl,String userName,Long milliseconds);
}
