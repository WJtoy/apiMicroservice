/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.utils;

import com.kkl.kklplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Http请求工具
 */
@Slf4j
public class HttpClientUtils {

	private static PoolingHttpClientConnectionManager cm = null;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 10000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 50;
	/**
	 * 连接超时时间,单位毫秒
	 */
	public final static int CONNECT_TIMEOUT = 20000;
	/**
	 * 从connect Manager(连接池)获取Connection 超时时间，单位毫秒
	 */
	public final static int CONNECT_REQUEST_TIMEOUT = 10000;
	/**
	 * 请求获取数据的超时时间(即响应时间)，单位毫秒
	 */
	public final static int READ_TIMEOUT = 60000;

	static {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			//new ConnectionSocketFactory()
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			log.error("创建SSL连接失败",e);
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf)
				.register("http", new PlainConnectionSocketFactory())
				.build();
		cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
	}

	private static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(cm)
				.build();
		return httpClient;
	}

	//region post

	/**
	 * post请求
	 *
	 * @param url 		地址
	 * @param params 	参数
	 * @return
	 */
	public static String post(String url, Map<String, String> params,String cookies) {
		return post(url,params,cookies,CONNECT_TIMEOUT,READ_TIMEOUT);
	}

	/**
	 * post请求
	 *
	 * @param url 		地址
	 * @param params 	参数
	 * @param cookies   cookies
	 * @param connectTimeout 连接建立时间，三次握手完成时间
	 *                       http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。
	 *                       超时后会ConnectionTimeOutException
	 * @param socketTimeout	 客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 * @return
	 */
	public static String post(String url, Map<String, String> params,String cookies,int connectTimeout,int socketTimeout) {

		CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
		CloseableHttpResponse httpResponse = null;
		HttpPost httpPost = null;
		try {
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectTimeout<=0?CONNECT_TIMEOUT:connectTimeout)
					.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
					.setSocketTimeout(socketTimeout<=0?READ_TIMEOUT:socketTimeout)
					.build();
			httpPost = new HttpPost(url);
			if(StringUtils.isNotBlank(cookies)) {
				httpPost.setHeader("cookie", cookies);
			}
			httpPost.setConfig(requestConfig);
			if (null != params && params.size()>0) {
				List<NameValuePair> ps = new ArrayList<NameValuePair>();
				for (String pKey : params.keySet()) {
					ps.add(new BasicNameValuePair(pKey, params.get(pKey)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(ps,"utf-8"));
			}
			httpResponse = httpClient.execute(httpPost);
			// response实体
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				//请求发送成功，并得到响应
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					//读取服务器返回过来的json字符串数据
					String response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
					int statusCode = httpResponse.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						// 成功
						return response;
					} else {
						return null;
					}
				}
				return null;
			}
			return null;
		}
		catch (ClientProtocolException e) {
			log.error("[HttpClientUtils.post] url:{},params:{}",url,GsonUtils.toGsonString(params),e);
			return null;
		}
		catch (IOException e) {
			log.error("[HttpClientUtils.post] url:{},params:{}",url,GsonUtils.toGsonString(params),e);
			return null;
		} finally {
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
					httpResponse.close();
				} catch (IOException e) {
					log.error("[HttpClientUtils.post] close response error,url:{},params:{}",url,GsonUtils.toGsonString(params),e);
				}
			}
		}
	}

	public static String post(String url, String body,String cookies){
		return post(url,body,cookies,"application/x-www-form-urlencoded");
	}

	public static String post(String url, String body,String cookies,String contentType){
		return post(url,body,cookies,contentType);
	}

	/**
	 *
	 * @param url
	 * @param body 如：name=Jack&sex=1&type=2
	 *
	 */
	public static String post(String url, String body,String cookies,String contentType,int connectTimeout,int socketTimeout){
		CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
		CloseableHttpResponse httpResponse = null;
		HttpPost httpPost = null;
		try {
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectTimeout<=0?CONNECT_TIMEOUT:connectTimeout)
					.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
					.setSocketTimeout(socketTimeout<=0?READ_TIMEOUT:socketTimeout)
					.build();
			httpPost = new HttpPost(url);
			if(StringUtils.isNotBlank(cookies)) {
				httpPost.setHeader("cookie", cookies);
			}
			httpPost.setConfig(requestConfig);
			// 解决中文乱码问题
			StringEntity entity = new StringEntity(body,"utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType(StringUtils.isBlank(contentType)?"application/x-www-form-urlencoded":contentType);
			httpPost.setEntity(entity);
			// response实体
			httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (null != entity) {
				//请求发送成功，并得到响应
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					//读取服务器返回过来的json字符串数据
					String response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
					int statusCode = httpResponse.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						// 成功
						return response;
					} else {
						return null;
					}
				}
				return null;
			}
			return null;
		} catch (ClientProtocolException e) {
			log.error("[HttpClientUtils.post] url:{},body:{}",url,body,e);
			return null;
		} catch (IOException e) {
			log.error("[HttpClientUtils.post] url:{},body:{}",url,body,e);
			return null;
		}finally{
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
					httpResponse.close();
				} catch (IOException e) {
					log.error("[HttpClientUtils.post] close response error,url:{},body:{}",url,body,e);
				}
			}
		}
	}

	//endregion post

	//region get

	/**
	 * get请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, String> params){
		return get(url,params,CONNECT_TIMEOUT,READ_TIMEOUT);
	}

	/**
	 * get请求
	 *
	 * @param url 		地址
	 * @param params 	参数
	 * @param connectTimeout 连接建立时间，三次握手完成时间
	 *                       http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。
	 *                       超时后会ConnectionTimeOutException
	 * @param socketTimeout	 客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 */
	public static String get(String url, Map<String, String> params,int connectTimeout,int socketTimeout) {
		CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
		CloseableHttpResponse httpResponse = null;
		HttpGet httpGet = null;
		try {
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectTimeout<=0?CONNECT_TIMEOUT:connectTimeout)
					.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
					.setSocketTimeout(socketTimeout<=0?READ_TIMEOUT:socketTimeout)
					.build();
			StringBuffer ps = new StringBuffer();
			for (String pKey : params.keySet()) {
				if(!"".equals(ps)){
					ps.append("&");
				}
				ps.append(pKey).append("=").append(params.get(pKey));
			}
			if(ps.length()>0){
				url = url + "?" + ps;
			}
			httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			httpResponse = httpClient.execute(httpGet);
			// response实体
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				//请求发送成功，并得到响应
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					//读取服务器返回过来的json字符串数据
					String response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
					int statusCode = httpResponse.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						// 成功
						return response;
					} else {
						return null;
					}
				}
				return null;
			}
			return null;
		}
		catch (ClientProtocolException e) {
			log.error("[HttpClientUtils.get] url:{},params:{}",url,GsonUtils.toGsonString(params),e);
			return null;
		}
		catch (IOException e) {
			log.error("[HttpClientUtils.get] url:{},params:{}",url,GsonUtils.toGsonString(params),e);
			return null;
		} finally {
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
					httpResponse.close();
				} catch (IOException e) {
					log.error("[HttpClientUtils.get] close response error,url:{},params:{}",url,GsonUtils.toGsonString(params),e);
				}
			}
		}
	}

	/**
	 * get请求
	 */
	public static String get(String url,String param){
		return get(url,param,CONNECT_TIMEOUT,2000);
	}

	/**
	 * get请求
	 * @param url 地址
	 * @param param 参数 如：id=123123&name=abc
	 * @param connectTimeout 连接建立时间，三次握手完成时间
	 *                       http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。
	 *                       超时后会ConnectionTimeOutException
	 * @param socketTimeout	 客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 */
	public static String get(String url,String param,int connectTimeout,int socketTimeout) {
		CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
		CloseableHttpResponse httpResponse = null;
		HttpGet httpGet = null;
		try {
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectTimeout<=0?CONNECT_TIMEOUT:connectTimeout)
					.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
					.setSocketTimeout(socketTimeout<=0?READ_TIMEOUT:socketTimeout)
					.build();
			httpGet = new HttpGet(url + (param.startsWith("?")?"":"?") + URLEncoder.encode(param, "UTF-8"));
			httpGet.setConfig(requestConfig);
			httpResponse = httpClient.execute(httpGet);
			// response实体
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				//请求发送成功，并得到响应
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					//读取服务器返回过来的json字符串数据
					String response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
					int statusCode = httpResponse.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						// 成功
						return response;
					} else {
						return null;
					}
				}
				return null;
			}
			return null;
		}
		catch (ClientProtocolException e) {
			log.error("[HttpClientUtils.get] close response error,url:{},params:{}",url,param,e);
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			log.error("[HttpClientUtils.get] close response error,url:{},params:{}",url,param,e);
			e.printStackTrace();
			return null;
		} finally {
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
					httpResponse.close();
				} catch (IOException e) {
					log.error("[HttpClientUtils.get] close response error,url:{},params:{}",url,param,e);
				}
			}
		}
	}

	//endregion get

}
