package com.analog.data.util;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cari.wing.mapencrpt.MapEncrpt;

public class MapUtil {

	private static final String mapKey = "iQud73O~0VfYtdcC";
	
	public static String getPositionInfo(double lon,double lat){
		String param = "act=getPositionInfo&data="+getScreatMes( lon, lat)+"&run=richclient.system.pics.m_returnPicPositionDetail&flag=0&req=0.4093092935680438";

		return getPositionDetail(HttpRequest.sendGet(UrlUtils.mapUrl, param).replace("richclient.system.pics.m_returnPicPositionDetail", "").replace("(", "").replace(")", ""));
	}
	
	public static final String getScreatMes(double lon,double lat){
		String key = mapKey;
		String info = "";
		
		String lonStr = changeNum(lon);
 		info += to32Hex(lonStr).length()<8?"0"+to32Hex(lonStr):to32Hex(lonStr);
 		String latStr = changeNum(lat);
 		info += to32Hex(latStr).length()<8?"0"+to32Hex(latStr):to32Hex(latStr);
		
 		info = key+to32Hex("1")+info;
 		info +=to32Hex(System.currentTimeMillis()+"");
 		
		return info;
	}
	
	//num 要转换的数 from源数的进制 to要转换成的进制
	public static String to32Hex(String num) {
	    int f=10;
	    int t=32;
	    return new java.math.BigInteger(num, f).toString(t);
	}

	public static String change32To10(String num) {
	    int f=32;
	    int t=10;
	    return new java.math.BigInteger(num, f).toString(t);
	}
	
	public static final String changeNum(double num){
		String numStr = num+"";
	 	String head = numStr.substring(0,numStr.indexOf("."));
	 	double re = 0;
	 	if(head.length()==2){
	 		re = num*Math.pow(10,9);
	 	}else if(head.length()==3){
	 		re = num*Math.pow(10,8);
	 	}
	 	return (new Double(Math.floor(re))).longValue()+"";
	}
	
	public static final String getPositionDetail(String detail){
		JSONArray jSONArray = JSON.parseObject(detail).getJSONArray("data");
		
		JSONObject jsonObject = jSONArray.getJSONObject(0);
		
		String outStr = "在"+ jsonObject.getString("cityInfo");
		if(jsonObject.getString("rName").equals("")){
			outStr += jsonObject.getString("pName") + "附近" + Math.floor(jsonObject.getDoubleValue("rDistan")) + "米处";
		}else if(jsonObject.getString("pName").equals("")){
			outStr += jsonObject.getString("rName") + "附近";
		}else if(jsonObject.getString("pName").equals("") && jsonObject.getString("rName").equals("")){
			
		}else{
			outStr += jsonObject.getString("rName") + "的" + jsonObject.getString("pName") + "附近" + Math.floor(jsonObject.getDoubleValue("pDistan")) + "米处";
		}
		
		return outStr;
	}
	
	public static void main(String[] args) {
		//System.out.println(getPositionInfo( 117.25986492, 29.25739503));
		
		
		double lon =new BigDecimal( MapEncrpt.getMapEncrpt().getDecryptLongitude(119.3084814115)).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		double lat = new BigDecimal(MapEncrpt.getMapEncrpt().getDecryptLatitude(26.0662044785)).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(lon);
		System.out.println(lat);
		System.out.println(getPositionInfo(lon, lat));
	}
}
