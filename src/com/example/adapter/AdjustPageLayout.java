package com.example.adapter;

import android.util.Log;

/**
 * 适配的最大分辨率是1080p
 * 如果更大的就不做处理了，现在这些机型还不多
 * 2014-10-24 10:45:10
 * @author Coder
 *
 */
public class AdjustPageLayout {
	/**
	 * 设置MainActivity底部的button的字体大小
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static int AdjustButtonTextSize(int screenWidth, int screenHeight)
	{
		return AdjustText(screenWidth, 15);
	}
	public static int AdjustListTitleTextSize(int screenWidth)
	{
		return AdjustText(screenWidth, 18);
	}
	public static int AdjustListInfoSize(int screenWidth)
	{
		return AdjustText(screenWidth, 15);
	}
	public static int AdjustImageSize(int screenWidth)
	{
		Log.e("", screenWidth+"");
		return screenWidth / 6;
		//return AdjustImage(screenWidth, 200);
	}
	public static int AdjustTextSizeInYourNeed(int screenWidth, int MaxTextSize)
	{//根据需求设定字体大小，这里不限定死
		return AdjustText(screenWidth, MaxTextSize);
	}
	private static int AdjustImage(int screenWidth, int MaxSize)
	{
		if (screenWidth <= 240) 
		 {        // 240X320 屏幕
			 return MaxSize-15;
		 }
		 else if (screenWidth <= 320)
		 {   // 320X480 屏幕
		 
			 return MaxSize-12;
		 
		 }else if (screenWidth <= 480)
		 {   // 480X800 或 480X854 屏幕
		 
			 return MaxSize-8;
		 
		 }
		 else if (screenWidth <= 540)
		 {   // 540X960 屏幕
		 
			 return MaxSize-6;    
		 }
		 else if(screenWidth <= 720)
		 {
			 return MaxSize-4;
		 }
		 else if(screenWidth <= 800)
		 {    // 800X1280 屏幕
		 
			 return MaxSize-3;
		         
		 }
		 else if(screenWidth <= 1080)
		 {                          // 大于 800X1280
			 return MaxSize-2;
		         
		 }
		 else
		 {
			 return MaxSize;
		 }
	}
	private static int AdjustText(int screenWidth, int MaxSize)
	{
		if (screenWidth <= 240) 
		 {        // 240X320 屏幕
			 return MaxSize-8;
		 }
		 else if (screenWidth <= 320)
		 {   // 320X480 屏幕
		 
			 return MaxSize-7;
		 
		 }else if (screenWidth <= 480)
		 {   // 480X800 或 480X854 屏幕
		 
			 return MaxSize-6;
		 
		 }
		 else if (screenWidth <= 540)
		 {   // 540X960 屏幕
		 
			 return MaxSize-5;
		         
		 }
		 else if(screenWidth <= 720)
		 {    // 800X1280 屏幕
		 
			 return MaxSize-4;
		         
		 }
		 else if(screenWidth <= 800)
		 {    // 800X1280 屏幕
		 
			 return MaxSize-3;
		         
		 }
		 else if(screenWidth <= 1080)
		 {   
		 
			 return MaxSize-2;
		         
		 }
		 else
		 {                          // 大于 1080p
			 return MaxSize;
		 }
	}
	
}
