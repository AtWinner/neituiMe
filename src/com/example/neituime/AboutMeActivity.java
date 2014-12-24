package com.example.neituime;

import com.example.adapter.GetScreenSize;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AboutMeActivity extends Activity {
	
	private int Width;
	private int Height;
	
	private ScrollView AboutMeSctollView;
	private ImageButton AboutMeGetBack;
	private Button UserCenterbtnGetBack;
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
		UserCenterbtnGetBack = (Button)findViewById(R.id.UserCenterbtnGetBack);
		AboutMeGetBack = (ImageButton)findViewById(R.id.AboutMeGetBack);
		
	}
	private void setParams()
	{
		LinearLayout.LayoutParams AboutMeGetBackParmas = (LinearLayout.LayoutParams)AboutMeGetBack.getLayoutParams();
		AboutMeGetBackParmas.height = Height / 13;
		AboutMeGetBackParmas.width = Height / 26;
		AboutMeGetBack.setLayoutParams(AboutMeGetBackParmas);
	}
	private void bindEvent()
	{
		AboutMeGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FinishActivity();
			}
		});
		UserCenterbtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FinishActivity();
			}
		});
	}
	private void FinishActivity()
	{
		AboutMeActivity.this.finish();
	}
}
