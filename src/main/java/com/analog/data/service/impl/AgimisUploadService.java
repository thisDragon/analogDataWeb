package com.analog.data.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.analog.data.cache.JedisUtil;
import com.analog.data.entity.DataConfig;
import com.analog.data.service.IAgimisUploadService;
import com.analog.data.util.HttpRequest;
import com.analog.data.util.MapUtil;
import com.analog.data.util.UrlUtils;

/**
 * 
* @ClassName: AgimisUploadService
* @Description: 事件上报
* @author yangjianlong
* @date 2019年9月10日
*
 */
@Service
public class AgimisUploadService implements IAgimisUploadService{

	private static final String fileSuffix = "mp4";
	@Autowired
	private DataConfig dataConfig;
	@Autowired
	private JedisUtil.Hash jedisHash;
	
	@Override
	public void sendEvent(String terminal, double lon, double lat, String centerUrl, String userName,Long time,String remark) throws IOException {
		String maketime ="";
		if (time == null) {
			maketime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // 文件上传时间
		}else{
			maketime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(time)); // 文件上传时间
		}
		
		String requestUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.agimisUploadUrl);
		String dictIds = "";
		
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("imsi", "");
		reqParams.put("maketime", maketime);// 录像时间
		reqParams.put("gpstime", maketime);//
		reqParams.put("suffix", fileSuffix);
		reqParams.put("description", remark);
		reqParams.put("lon", String.valueOf(lon));
		reqParams.put("lat", String.valueOf(lat));
		List<Map<String, String>> dataDicTree = getDataDicTree(dataConfig, jedisHash, centerUrl, userName);
		if (dataDicTree.size()>0) {
			Random ra =new Random();
			dictIds =dataDicTree.get(ra.nextInt(dataDicTree.size())).get("id");
		}
		reqParams.put("dictid", dictIds);
		//位置描述
		reqParams.put("locinfo", MapUtil.getPositionInfo(lon, lat));
		sendData(requestUrl, reqParams, terminal);
	}

	/**
	 * 
	* @Title: getDataDicTree
	* @Description: 获取业务字典树
	* @param @param dataConfig
	* @param @param jedisHash
	* @param @param centerUrl
	* @param @param userName
	* @param @return
	* @param @throws ClientProtocolException
	* @param @throws IOException    参数
	* @return List<Map<String,String>>    返回类型
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> getDataDicTree(DataConfig dataConfig,JedisUtil.Hash jedisHash,String centerUrl,String userName) throws ClientProtocolException, IOException{
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Map<String, String> requestMap =new HashMap<String, String>();
		String requestUrl = UrlUtils.getRequestUrl(centerUrl, userName, dataConfig, jedisHash, UrlUtils.getDataDicTreeUrl);
		
		requestMap.put("run", UrlUtils.getDataDicTreeRun);
		requestMap.put("modulename", UrlUtils.getDataDicTreeModuleName);
		requestMap.put("name", "");
		requestMap.put("pid", "");
		requestMap.put("type", "0");
		
		Map<String, String> requestParamsBySid = UrlUtils.getRequestParamsBySid(requestMap, centerUrl, userName, dataConfig, jedisHash);
		
		String httpClientGet = HttpRequest.httpClientGetOfPartition(requestUrl, requestParamsBySid);
		Map<String, Object> parseObject = JSON.parseObject(httpClientGet, JSONObject.class);
		if (parseObject.get("code").toString().equals("1")) {
			resultList = (List<Map<String, String>>) parseObject.get("dataDictList");
		}
		return resultList;
	}
	
	private void sendData(String path, Map<String, String> params, String tel) throws IOException {
		InputStream inputS = null;
		BufferedReader in = null;
		String line = "";
		try {
			URL url = new URL(path);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-type", "application/stream");
			urlConn.setRequestMethod("POST");
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(urlConn.getOutputStream()));
			urlConn.connect();
			dos.writeUTF(tel);
			// 其他数据
			Set<String> keys = params.keySet();
			Iterator<String> iter = keys.iterator();
			// 上传其他数据
			while (iter.hasNext()) {
				String key = iter.next();
				dos.writeUTF(key);
				dos.writeUTF(params.get(key) == null ? "" : params.get(key));
			}
			// 必需字段
			dos.writeUTF("curseg");
			dos.writeShort(1);
			dos.writeUTF("segcount");
			dos.writeShort(1);
			// 内容
			dos.writeUTF("iscompress");
			dos.writeUTF("0");
			dos.writeUTF("content");
			dos.write(0);
			dos.flush();
			dos.close();
			inputS = urlConn.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputS, "UTF-8"));
			StringBuffer buffer = new StringBuffer();

			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			line = buffer.toString();

		} catch (ProtocolException e) {
			e.printStackTrace();
		}finally {
			if (inputS!=null) {
				inputS.close();
			}
			if (in!=null) {
				in.close();
			}
		}
	}
	
}
