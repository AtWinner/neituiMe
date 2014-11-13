package com.example.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class httpPost {
	public String DoHttpPostByUrl(List<NameValuePair> params, String Url)
	{
		String result = "";
		try
		{
			Log.e("step", "postBegin");
			// 获取HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			Log.e("step", "1");
			HttpPost httpPost = new HttpPost(Url);Log.e("step", "2");
			//设置字符集
			HttpEntity entity = new UrlEncodedFormEntity(params, Url);Log.e("step", "3");
			//设置参数实体
			httpPost.setEntity(entity);Log.e("step", "4");
			//设置连接超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);Log.e("step", "5");
			//设置请求超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			Log.e("statusCode", "");
			HttpResponse httpResp = httpClient.execute(httpPost);
			// 判断是够请求成功
			int statusCode = httpResp.getStatusLine().getStatusCode();
			Log.e("statusCode", statusCode+"");
	        if (statusCode == 200) 
	        {
	        	result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
	        	Log.i("HttpPost", "HttpPost方式请求成功，返回数据如下：");
	        	Log.i("result", result);
	        }
	        else 
	        {
	        	Log.e("HttpPost", "HttpPost方式请求失败");
	        }
		}
		catch(Exception e)
		{
			Log.e("", e.getMessage());
			result = "请求超时";
		}
		return result;
	}
	
	
	public String sendhttpclient_postrequest(String path,Map<String, String> params) 
	{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) 
		{
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
        try 
        {
            UrlEncodedFormEntity entity=new  UrlEncodedFormEntity(pairs,"UTF-8");
            HttpPost httpost=new HttpPost(path);
            httpost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse response= client.execute(httpost);
            if(response.getStatusLine().getStatusCode()==200)
            {
	            return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } 
        catch (Exception e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        return null;
    }
}
