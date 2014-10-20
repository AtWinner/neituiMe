package com.example.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class GridViewItem {
	private Document document;
	private JSONObject Json;
	private JSONArray Jsonarry;
	public GridViewItem(Document doc)
	{
		document = doc;
	}
	public GridViewItem(String JsonStr)
	{
		try 
		{   
			Json = new JSONObject(JsonStr);
			Jsonarry = Json.getJSONArray("jobs");
		} 
		catch (JSONException e) 
		{
			Log.e("", e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<HashMap<String, Object>> GetGridViewItemsByJson()
	{
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		int a = Jsonarry.length();
		Log.e("asa",a+"");
		for(int i=0; i<a; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			try 
			{
				JSONObject JsonItem = Jsonarry.getJSONObject(i);
				map.put("position", subString(JsonItem.get("position").toString(), 33));
				map.put("positionfull", JsonItem.get("position").toString());
				map.put("uid", JsonItem.get("uid"));
				map.put("id", JsonItem.get("id"));
				map.put("department", subString(JsonItem.get("department").toString(), 25));
				map.put("departmentfull", JsonItem.get("department").toString());
				map.put("cmail", JsonItem.get("cmail"));
				map.put("createdate", JsonItem.get("createdate"));
				map.put("realname", JsonItem.get("realname"));
				map.put("avatar", JsonItem.get("avatar"));
				map.put("salary", JsonItem.get("beginsalary") + "k-" + JsonItem.get("endsalary") + "k");
				map.put("city", CutCityString(JsonItem.get("city").toString()));
				Log.e("", map.get("salary").toString());
				al.add(map);
			} 
			catch (JSONException e) 
			{
				Log.e("JsonItemError", e.getMessage());
				e.printStackTrace();
			}
		}
		return al;
	}
	public ArrayList<HashMap<String, Object>> GetCompanyGridViewItemsByJson()
	{
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		int a = Jsonarry.length();
		Log.e("asa",a+"");
		for(int i=0; i<a; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			try 
			{
				JSONObject JsonItem = Jsonarry.getJSONObject(i);
				map.put("position", subString(JsonItem.get("position").toString(), 33));
				map.put("positionfull", JsonItem.get("position").toString());
				map.put("id", JsonItem.get("id").toString());
				map.put("department", subString(JsonItem.get("cpname").toString(), 25));
				map.put("departmentfull", JsonItem.get("cpname").toString());
//				map.put("cmail", JsonItem.get("cmail"));
				map.put("createdate", JsonItem.get("dateinfo").toString());
				map.put("avatar", JsonItem.get("avatar").toString());
				map.put("salary", JsonItem.get("beginsalary") + "k~" + JsonItem.get("endsalary") + "k");
				map.put("city", CutCityString(JsonItem.get("cityorig").toString()));
				Log.e("", map.get("salary").toString());
				al.add(map);
			} 
			catch (JSONException e) 
			{
				Log.e("JsonItemError", e.getMessage());
				e.printStackTrace();
			}
		}
		return al;
	}
	private String CutCityString(String YourStr)
	{
		if(!YourStr.equals("") && YourStr.length() > 2)
		{
			return YourStr.substring(0, 3);
		}
		return YourStr;
	}
	private String subString(String str, int bytelength) {
        if (str == null) {
            return null;
        }
        if (bytelength <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= bytelength) {
                return str;
            }
        } catch (Exception ex) {
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        while (bytelength > 0) {
            c = str.charAt(index);
            if (c < 128) {
                bytelength--;
            } else {//GBK编码汉字占2字节，其他编码的不一样，要修改下面几句
                bytelength--;
                if (bytelength < 1)//被截断了
                    break;
                bytelength--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }
}
