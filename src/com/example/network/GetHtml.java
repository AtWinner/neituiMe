package com.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.protocol.HTTP; 
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;


public class GetHtml {

	private Document myDoc;
	
	public String GetJsonByUrl(String Url)
	{
		return HttpPost(Url);
	}
	private String HttpPost(String Url)
	{
		String url = Url;
		HttpPost httpPost = new HttpPost(url); 
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		
		
		HttpResponse httpResponse = null;
		try 
		{ 
			// 设置httpPost请求参数 
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
            httpResponse = new DefaultHttpClient().execute(httpPost); 
            //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
            if (httpResponse.getStatusLine().getStatusCode() == 200) 
            { 
                // 第三步，使用getEntity方法获得返回结果 
                result = EntityUtils.toString(httpResponse.getEntity()); 
                Log.e("result", result);
            } 
        } 
		catch (ClientProtocolException e) 
		{ 
            e.printStackTrace(); 
        } 
		catch (IOException e) 
        { 
            e.printStackTrace(); 
        } 
		return result;
	}
	public String GetHtmlByUrl(String Url)
	{
		try
		{
			Document doc = Jsoup.connect(Url).timeout(1000).get();
			myDoc = doc;
		}
		catch(IOException e)
		{
			Log.e("GetHtml"+Url, e.getMessage());
		}
		return myDoc.toString();
	}

	/**
	 * 使用时建议对返回值判断，非空之后再进行操作
	 * @param Url
	 * @return
	 */
	public Document GetDocByUrl(String Url)
	{
		try
		{
			Log.e("", Url);
			Document doc = Jsoup.connect(Url).timeout(10000).get();//10秒超时
			myDoc = doc;
		}
		catch(IOException e)
		{
			
			Log.e("GetHtml"+Url, e.getMessage());
		}
		return myDoc;
	}
}
