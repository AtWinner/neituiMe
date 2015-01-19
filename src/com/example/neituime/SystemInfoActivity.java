package com.example.neituime;

import com.example.adapter.GetScreenSize;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SystemInfoActivity extends Activity {
	private static final int JSON_SUCCESS = 1; // 获取Json成功
	private static final int MSG_FAILED = 2; // 网络请求失败
	private static final int IMG_SUCCESS = 3 ;// 获取图片成功
	private static final int MSG_REFRESH = 4;// 下拉刷新
	private static final int LOAD_MORE = 5;//上拉加载更多
	
	private int Page;
	private int Width;//屏幕宽
	private int Height;//屏幕高
	
	private String Token;
	
	private ImageButton SystemInfoGetBack;
	private Button SystemInfobtnGetBack;
	private LinearLayout SystemInfoLinearTop;
	private LinearLayout SystemInfo_sub_root_lin;
	private PullToRefreshScrollView SystemInfoBodyScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_information_activity);
		init();
		setParams();
//		bindEvent();
//		DoRequest();
		
	}
	private void init()
	{
		Intent intent = getIntent();
		Token = intent.getStringExtra("Token");
		Page = 1;
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(SystemInfoActivity.this, Height);
		SystemInfoGetBack = (ImageButton)findViewById(R.id.SystemInfoGetBack);
		SystemInfobtnGetBack = (Button)findViewById(R.id.SystemInfobtnGetBack);
		SystemInfoLinearTop = (LinearLayout)findViewById(R.id.SystemInfoLinearTop);
		SystemInfo_sub_root_lin = (LinearLayout)findViewById(R.id.System_Info_sub_root_lin);
		SystemInfoBodyScrollView = (PullToRefreshScrollView)findViewById(R.id.SystemInfoBodyScrollView);
		SystemInfoBodyScrollView.setMode(Mode.BOTH);
	}
	private void setParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		SystemInfoGetBack.setLayoutParams(params);
		LinearLayout.LayoutParams scrollViewParams = (LinearLayout.LayoutParams)SystemInfoBodyScrollView.getLayoutParams();
		scrollViewParams.height = height*12;
		SystemInfoBodyScrollView.setLayoutParams(scrollViewParams);
	}
	

}
