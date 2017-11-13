/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import constants.ServerConstants;
import exception.ApiRuntimeException;

/**   
 * 此类描述的是：http 工具类
 * @author: wangjian  
 * @date: 2017年11月13日 下午2:06:15    
 */
public final class HttpUtils {
    
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static final String CHARSET = ServerConstants.CHARSET;

    private HttpClient httpClient;

    private HttpClientConnectionManager httpClientConnectionManager;

    private final Timer connectionManagerTimer = new Timer("HttpUtil.connectionManagerTimer", true);

    public HttpUtils(HttpClient httpClient, HttpClientConnectionManager connectionManager) {
        super();
        this.httpClient = httpClient;
        this.httpClientConnectionManager = connectionManager;
    }

    @PostConstruct
    private void initialize() {
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (httpClientConnectionManager == null) {
                    return;
                }
                httpClientConnectionManager.closeExpiredConnections();
                // Optionally, close connections
                // that have been idle longer than 30 sec
                httpClientConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
            }
        }, 30000, 5000);
    }

    @PreDestroy
    public void stop() {
        this.connectionManagerTimer.cancel();
    }

    /**
     * get请求(不带参数)
     *
     * @param url 请求url
     * @return html 页面数据
     * @throws IOException
     */
    public String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        return execute(httpGet);
    }

    /**
     * HTTP GET with custom RequestConfig
     *
     * @param url request url
     * @param config self-custom RequestConfig
     * @return html or json
     * @throws IOException
     */
    public String get(String url, RequestConfig config) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        return execute(httpGet);
    }

    /**
     * get请求(不带参数)
     *
     * @param url 请求url
     * @param headers 请求头
     * @return html 页面数据
     * @throws IOException
     */
    public String get(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(httpGet);
    }

    private String execute(HttpRequestBase httpMethod) {
        String result = null;
        try {
            HttpResponse response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, CHARSET);
        } catch (IOException io) {
            log.error("execute HTTP request:{} error", httpMethod.getURI(), io);
            throw new ApiRuntimeException(io.getCause());
        } finally {
            httpMethod.releaseConnection();
        }
        return result;
    }

    /**
     * get请求(带参数)
     *
     * @param url 请求url
     * @param paramsMap get请求参数
     * @return html 页面数据
     * @throws IOException IO
     */
    public String getWithParams(String url, Map<String, String> paramsMap) {
        String finalUrl = url + "?" + parseParams(paramsMap);
        HttpGet httpGet = new HttpGet(finalUrl);
        return execute(httpGet);
    }

    /**
     * post请求
     *
     * @param url 请求url
     * @param paramsMap post报文参数
     * @return html 页面数据
     * @throws IOException
     */
    public String post(String url, Map<String, String> paramsMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<>();
        if (paramsMap != null) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return execute(httpPost);
    }

    /**
     * post请求
     *
     * @param url 请求url
     * @param headers request Header
     * @return html 页面数据
     * @throws IOException
     */
    public String postWithHeader(String url, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(httpPost);
    }

    /**
     * post请求
     *
     * @param url 请求url @param requestEntity Post请求实体 @param headers request Header @return html
     *        页面数据 @throws IOException @throws
     */
    public String post(String url, StringEntity requestEntity, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        String html = null;
        try {
            httpPost.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, CHARSET);
        } finally {
            httpPost.releaseConnection();
        }
        return html;
    }

    /**
     * 转换参数列表用于get请求
     *
     * @param paramsMap
     * @return
     */
    private static String parseParams(Map<String, String> paramsMap) {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return params.substring(0, params.length() - 1);
    }

    /**
     * 转义成&#xhhhh;形式
     *
     * @param strInput
     * @return result
     */
    public static String parseStr(String strInput) {
        StringBuilder strOutput = new StringBuilder();
        for (int i = 0; i < strInput.length(); i++) {
            strOutput.append("&#x" + Integer.toString(strInput.charAt(i), 16));
        }
        return strOutput.toString();
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static boolean isRequestSuccess(int httpStatusCode) {
        HttpStatus status = HttpStatus.valueOf(httpStatusCode);
        return !(status.is4xxClientError() || status.is5xxServerError());
    }
}
