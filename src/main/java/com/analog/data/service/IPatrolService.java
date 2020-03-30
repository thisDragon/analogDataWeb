package com.analog.data.service;

public interface IPatrolService {

	public abstract boolean patrolUp(String tel, Double lon, Double lat,String centerUrl,String userName,String remark,Long time) throws Exception;
}
