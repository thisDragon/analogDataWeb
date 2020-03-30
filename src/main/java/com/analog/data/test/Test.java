package com.analog.data.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.util.CfgKeyUtils;
import com.analog.data.util.DateUtils;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.MD5Utils;


public class Test {

	public static void main(String[] args) throws Exception {
		//login();
		//KeepActive();
		//System.out.println(String.valueOf((long)((Math.random()*9+1)*1000000000L)));
		//System.out.println(DateUtils.date2Str(new Date(1567958400000L), DateUtils.DEFAULT_FORMAT));
		//System.out.println(new Date().getTime());
		//System.out.println((int)(1+Math.random()*(10)));
		Random ra =new Random();
		System.out.println(ra.nextInt(1));
		
	}

	
	//登录
	private static void login(){
		String url = "http://192.168.2.68:6661/center-web3.0/dispatcher/login";
		boolean flag = false;// 分区是否正常
		String params = "";
		params += "dEnName=" + "test003" + "&";
		params += "dPassword=" + MD5Utils.getMd5("88888888") ;
		//params += "partition=" + "192.168.2.68";
		String jsonp = HttpRequest.sendGet(url, params);// 参数不支持中文
		Pattern pattern = Pattern.compile("run\\((.*?)\\)");
		Matcher matcher = pattern.matcher(jsonp);
		if (matcher.find()) {
			JSONObject object = JSON.parseObject(matcher.group(1));
			String sid = object.getString("sid");
			int code = object.getIntValue("code");
			String partitionState = object.getString("partitionState");
			if (code == 1 && partitionState.equals("[true]")) {
				flag = true;
			}
			System.out.println("sid:"+sid);
		}
		System.out.println(flag);
	}
	
	//调度员保持活性(一小时调用一次)
	private static void KeepActive(){
		String url = "http://192.168.2.68:6661/center-web3.0/dispatcher/heartbeat";
		String params = "";
		params += "username=" + "test003" + "&";
		params += "sid=" + "e678153317b44b64982b12672600a07b" + "&";
		params += "cfgkey=" + CfgKeyUtils.getCfgKey("test003");
		String jsonp = HttpRequest.sendGet(url, params);// 参数不支持中文
		Pattern pattern = Pattern.compile("run\\((.*?)\\)");
		Matcher matcher = pattern.matcher(jsonp);
		if (matcher.find()) {
			JSONObject object = JSON.parseObject(matcher.group(1));
			int code = object.getIntValue("code");
			if (code == 1 ) {
				System.out.println("1");
			}else{
				System.out.println("0");
			}
		}
	}
	
	//位置上报
	
	//事件上报
	
	//巡检上报
	
	//任务上报
	
	
	
}
