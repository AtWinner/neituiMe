package com.example.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Intent;
import android.util.Log;
import android.webkit.URLUtil;

public class GetResume {
	private String currentFilePath = ""; 
	private String currentTempFilePath = ""; 
	/**
	 * 下载URL文件
	 * @param strPath
	 */
	public void GetFile(final String strPath, String FileName)
	{
		try
		{
			GetDataSource(strPath, FileName);
		}
		catch(Exception e)
		{
			Log.e("ResumeException", e.getMessage());
			e.printStackTrace(); 
		}
	}
	
	private void GetDataSource(String strPath, String FileName) throws Exception
	{
		if(!URLUtil.isNetworkUrl(strPath))
		{
			Log.e("", "不是URL");
		}
		URL myURL = new URL(strPath);
		/*创建连接*/
		URLConnection conn = myURL.openConnection();
		conn.connect();
		/*InputStream 下载文件*/
		InputStream is = conn.getInputStream();
		if(is == null)
		{
			throw new RuntimeException("stream is null");
		}
		//创建临时文件
		File myTempFile = File.createTempFile("个人简历", ".pdf");
		/*取得站存盘案路径*/
		currentTempFilePath = myTempFile.getAbsolutePath();
		/*将文件写入暂存盘*/
		FileOutputStream fos = new FileOutputStream(myTempFile);
		
		Log.e("", currentTempFilePath);
		byte buf[] = new byte[128];
		while(true)
		{
			int numread = is.read(buf);
			if (numread <= 0)
			{
				break;
			}
			fos.write(buf, 0, numread);
		}
		openFile(myTempFile);
	}
	private void openFile(File f)
	{
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		/* 调用getMIMEType()来取得MimeType */
		
	}
}
