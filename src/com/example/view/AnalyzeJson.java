package com.example.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AnalyzeJson {
	private String JsonStr;
	
	public AnalyzeJson(String json)
	{
		JsonStr = json;
	}
	public HashMap<String, String> GetTencentInfoByJson()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		try 
		{
			JSONObject myJson = new JSONObject(JsonStr);
			map.put("message", myJson.getString("message"));
			map.put("className", myJson.getString("className"));
			map.put("code", myJson.getString("code"));
			if(myJson.getString("className").toString().trim().equals("success"))
			{
				JSONObject bodyJson =  new JSONObject(myJson.getString("online"));
				map.put("id", bodyJson.getString("id"));
				map.put("avatar", bodyJson.getString("avatar"));
				map.put("realname", bodyJson.getString("realname"));
				map.put("cmailvalidate", bodyJson.getString("cmailvalidate"));
				map.put("descrip", bodyJson.getString("descrip"));
				map.put("canfindresum", bodyJson.getString("canfindresum"));
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return map;
		
	}
	public HashMap<String, String> GetUserInfoByJson()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		try 
		{
			JSONObject myJson = new JSONObject(JsonStr);
//			JSONArray Jsonarry = myJson.getJSONArray("user");
//			JSONObject UserJson = Jsonarry.getJSONObject(0);
			map.put("message", myJson.getString("message"));
			map.put("className", myJson.getString("className"));
			if(myJson.getString("message").equals("ok") && myJson.getString("className").equals("success"))
			{
				map.put("code", myJson.getString("code"));
				map.put("uid", myJson.getString("uid"));
				map.put("token", myJson.getString("token"));
				map.put("cmailvalidate", myJson.getString("cmailvalidate"));
				map.put("canfindresume", myJson.getString("canfindresume"));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return map;
	}
	public HashMap<String, String> GetUserResume()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		try 
		{
			JSONObject myJson = new JSONObject(JsonStr);
//			JSONArray Jsonarry = myJson.getJSONArray("user");
//			JSONObject UserJson = Jsonarry.getJSONObject(0);
			map.put("message", myJson.getString("message"));
			map.put("className", myJson.getString("className"));
			map.put("code", myJson.getString("code"));
			map.put("attach", myJson.getString("attach"));
			if(myJson.getString("className").toString().trim().equals("success"))
			{
				JSONObject bodyJson =  new JSONObject(myJson.getString("online"));
				map.put("uid", bodyJson.getString("uid"));
				map.put("photo", bodyJson.getString("photo"));
				map.put("realname", bodyJson.getString("realname"));
				map.put("email", bodyJson.getString("email"));
				map.put("mobile", bodyJson.getString("mobile"));
				map.put("age", bodyJson.getString("age"));
				map.put("workage", bodyJson.getString("workage"));
				map.put("experience", bodyJson.getString("experience"));
				map.put("experiencetags", bodyJson.getString("experiencetags"));//经历标签
				map.put("education", bodyJson.getString("education"));
				map.put("currentjob", bodyJson.getString("currentjob"));
				map.put("currentcompany", bodyJson.getString("currentcompany"));
				map.put("city", bodyJson.getString("city"));
				map.put("interestjobs", bodyJson.getString("interestjobs"));
				map.put("interestcitys", bodyJson.getString("interestcitys"));
				map.put("privatestatus", bodyJson.getString("privatestatus"));
				map.put("forbiddomains", bodyJson.getString("forbiddomains"));
				map.put("createtime", bodyJson.getString("createtime"));
				map.put("updatetime", bodyJson.getString("updatetime"));
				map.put("issubscribe", bodyJson.getString("issubscribe"));
				map.put("portfolio", bodyJson.getString("portfolio"));
				map.put("social", bodyJson.getString("social"));
				map.put("salary", bodyJson.getString("salary"));
				map.put("status", bodyJson.getString("status"));
				map.put("md5key", bodyJson.getString("md5key"));
				map.put("viewnums", bodyJson.getString("viewnums"));
				map.put("resume_type", bodyJson.getString("resume_type"));
				
			}
//			map.put("cmailvalidate", myJson.getString("cmailvalidate"));
//			map.put("canfindresume", myJson.getString("canfindresume"));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return map;
	}
	public HashMap<String, String> GetUserCenterInfo()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		try 
		{
			JSONObject myJson = new JSONObject(JsonStr);
			if(myJson.getString("className").toString().trim().equals("success"))
			{
				JSONObject bodyJson =  new JSONObject(myJson.getString("user"));
				if(!myJson.getString("user").endsWith("0"))
				{
					map.put("realname", bodyJson.getString("realname"));
					map.put("avatar", bodyJson.getString("avatar"));
					map.put("cmailvalidate", bodyJson.getString("cmailvalidate"));
					map.put("descrip", bodyJson.getString("descrip"));
					map.put("canfindresume", bodyJson.getString("canfindresume"));
				}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return map;
	}

	public HashMap<String, String> GetSendResumeInfo()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		try 
		{
			JSONObject myJson = new JSONObject(JsonStr);
			map.put("message", myJson.getString("message"));
			map.put("className", myJson.getString("className"));
			map.put("code", myJson.getString("code"));
			map.put("jumptext", myJson.getString("jumptext"));
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return map;
	}
	/**
	 * 解析消息中心的json数据
	 * @return
	 */
	public List<HashMap<String, String>> GetMessageCenter()
	{
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try
		{
			JSONObject myJson = new JSONObject(JsonStr);
			if(myJson.getString("message").equals("ok") && myJson.getString("className").equals("success"))
			{
				JSONArray array = myJson.getJSONArray("messages");
				int arrayLength = array.length();
				for(int i = 0; i < arrayLength; i++)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					try 
					{
						JSONObject JsonItem = array.getJSONObject(i);
						map.put("type", JsonItem.get("type").toString());
						map.put("lastcontent", JsonItem.get("lastcontent").toString());
						map.put("createdate", JsonItem.get("createdate").toString());
						map.put("realname", JsonItem.get("realname").toString());
						map.put("touid", JsonItem.get("touid").toString());
						map.put("avatar", JsonItem.get("avatar").toString());
						map.put("noreadnum", JsonItem.get("noreadnum").toString());
						list.add(map);
					} 
					catch (JSONException e) 
					{
						Log.e("JsonItemError", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("AnalyzeJsonError", e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * 解析消息中心的json
	 * @return
	 */
	public List<HashMap<String, String>> GetSystemInfoMessage()
	{
		List<HashMap<String, String>> list  = new ArrayList<HashMap<String, String>>();
		try
		{
			JSONObject myJson = new JSONObject(JsonStr);
			if(myJson.getString("message").equals("ok") && myJson.getString("className").equals("success"))
			{
				JSONArray array = myJson.getJSONArray("notices");
				int arrayLength = array.length();
				for(int i = 0; i < arrayLength; i++)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					try 
					{
						JSONObject JsonItem = array.getJSONObject(i);
						map.put("id", JsonItem.get("id").toString());
						map.put("type", JsonItem.get("type").toString());
						map.put("fuid", JsonItem.get("fuid").toString());
						map.put("uid", JsonItem.get("uid").toString());
						map.put("message", JsonItem.get("message").toString());
						map.put("createtime", JsonItem.get("createtime").toString());
						map.put("status", JsonItem.get("status").toString());
						map.put("oid", JsonItem.get("oid").toString());
						map.put("frealname", JsonItem.get("frealname").toString());
						map.put("trealname", JsonItem.get("trealname").toString());
						map.put("avatar", JsonItem.get("avatar").toString());
						map.put("rate", JsonItem.get("rate").toString());
						map.put("attachurl", JsonItem.get("attachurl").toString());
						map.put("file", JsonItem.get("file").toString());

						list.add(map);
					} 
					catch (JSONException e) 
					{
						Log.e("JsonItemError", e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Log.e("AnalyzeJsonError", e.getMessage());
		}
		return list;
	}
}
