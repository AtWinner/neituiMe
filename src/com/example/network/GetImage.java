package com.example.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GetImage {
	private String myUrl;
	public GetImage()
	{
		
	}
	public GetImage(String Url)
	{
		myUrl = Url;
	}
	public static ArrayList<HashMap<String, Object>> GetBitmapList(ArrayList<HashMap<String, Object>> al)
	{
		for(HashMap<String, Object> map : al)
		{
			String url = map.get("avatar").toString();
			Bitmap bitmap = returnBitMap(url);
			map.put("bitmap", bitmap);
		}
		return al;
	}
	public static Bitmap returnBitMap(String url) 
	{   
		URL myFileUrl = null;   
		Bitmap bitmap = null;   
		try 
		{   
			myFileUrl = new URL(url);   
		} 
		catch (MalformedURLException e) 
		{   
			e.printStackTrace();   
		}   
		try 
		{   
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
		    conn.setDoInput(true);   
		    conn.connect();   
		    InputStream is = conn.getInputStream();   
		    bitmap = BitmapFactory.decodeStream(is);   
		    is.close();   
		} 
		catch (IOException e) 
		{   
			e.printStackTrace();
		}   
		return bitmap;   
	} 
	public Bitmap GetBitMap(String url) 
	{   
		URL myFileUrl = null;   
		Bitmap bitmap = null;   
		try 
		{   
			myFileUrl = new URL(url);   
		} 
		catch (MalformedURLException e) 
		{   
			e.printStackTrace();   
		}   
		try 
		{   
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
		    conn.setDoInput(true);   
		    conn.connect();   
		    InputStream is = conn.getInputStream();   
		    bitmap = BitmapFactory.decodeStream(is);   
		    is.close();   
		} 
		catch (IOException e) 
		{   
			e.printStackTrace();
		}   
		return bitmap;   
	} 
}
