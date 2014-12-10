package com.example.neituime;

import com.example.adapter.GetScreenSize;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ScrollView;

public class AboutMeActivity extends Activity {
	
	private int Width;
	private int Height;
	
	private ScrollView AboutMeSctollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		init();
		setParams();
		bindEvent();
	}
	private void init()
	{
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(AboutMeActivity.this, Height);
		
		AboutMeSctollView = (ScrollView)findViewById(R.id.AboutMeSctollView);
		
	}
	private void setParams()
	{
		
	}
	private void bindEvent()
	{
		
	}
}
