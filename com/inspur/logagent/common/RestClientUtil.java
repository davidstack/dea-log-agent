package com.inspur.logagent.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;



/**
 * http 请求
 * @author wang
 *
 */
public class RestClientUtil {
	private Log logger = LogFactory.getLog(RestClientUtil.class);
	
	public RestClientUtil() {
	}
	
	public Object invokeGet(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		
		//TODO 设置超时时间
		HttpResponse response;
		try {
			response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() == 200) {
				return "ok";
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			getRequest.releaseConnection();
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}
	
	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public <T> T invokePost(String url, Map<String,Object> messages,
			TypeReference<T> reference) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost method = new HttpPost(url);
			String body = null;
			JSONArray jsonObject =null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator<Entry<String,Object>> iter=messages.entrySet().iterator();
	
			while(iter.hasNext())
			{
				Entry<String,Object> entry=iter.next();
				jsonObject = JSONArray.fromObject(entry.getValue());
				params.add(new BasicNameValuePair(entry.getKey(), jsonObject.toString()));
			}
			method.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			HttpResponse response = httpClient.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("RestClientUtil post failed statusCode="
						+ statusCode);
				return null;
			}

			body = EntityUtils.toString(response.getEntity());
			
			logger.info("RestClientUtil post result body="+body);
			ObjectMapper objMappper = new ObjectMapper();
			return (T) objMappper.readValue(body, reference);

		} catch (Exception e) {
			logger.error("RestClientUtil post failed", e);
			return null;
		} finally {
			// 释放
			httpClient.getConnectionManager().shutdown();
		}

	}
	
	/**
	 * 发送post请求 ,只返回是否发送成功
	 * 
	 * @param url
	 * @param message
	 * @return
	 */
	public  <T> T  invokePost1(String url, Object messages,TypeReference<T> reference) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);  

			JSONArray jsonObject = JSONArray.fromObject(messages);
			StringEntity params =new StringEntity(jsonObject.toString(),"UTF-8");  
			request.addHeader("content-type", "application/json");  
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);  
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("RestClientUtil post failed statusCode="
						+ statusCode);
				return null;
			}
			String body = null;
			body = EntityUtils.toString(response.getEntity());
			
			logger.info("RestClientUtil post result body="+body);
			ObjectMapper objMappper = new ObjectMapper();
			return (T) objMappper.readValue(body, reference);

		} catch (Exception e) {
			logger.error("RestClientUtil post failed", e);
			return null;
		} finally {
			// 释放
			httpClient.getConnectionManager().shutdown();
		}

	}
}
