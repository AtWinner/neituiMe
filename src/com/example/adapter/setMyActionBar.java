package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;

public class setMyActionBar 
{
	private int Layout;
	private Context myActivity;
	private ActionBar Bar;
	public setMyActionBar(int layout, Context context)
	{
		Layout = layout;
		myActivity = context;
	}
	public setMyActionBar(ActionBar bar)
	{
		Bar = bar;
	}
	/**
	 * 
	 * @param setDisplayHomeAsUpEnabled 点击home是否有效
	 * @param setDisplayUseLogoEnabled 是否显示logo
	 * @param setTitle 设置标题
	 */
	@SuppressLint("NewApi")
	public void setBarAttribute(Boolean setDisplayHomeAsUpEnabled, Boolean setDisplayUseLogoEnabled, String setTitle)
	{
		Bar.setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled);
		Bar.setDisplayUseLogoEnabled(setDisplayUseLogoEnabled);
		Bar.setTitle(setTitle);
	}
}
