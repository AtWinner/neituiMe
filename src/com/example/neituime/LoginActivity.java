﻿package com.example.neituime;

import java.util.HashMap;

import com.example.network.GetHtml;
import com.example.tencent.MyIUiListener;
import com.example.view.AnalyzeJson;
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.IntToString;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class LoginActivity extends Activity {
	private static final int JSON_SUCCESS = 1; // 获取Json成功
	private static final int MSG_FAILED = 2; // 网络请求失败
	private static final String AppID = "101016468";
	
	
	private int Width;
	private int Height;

	private ImageButton LoginGetBack;
	private LinearLayout LoginBottomLinear;
	private Button LoginbtnGetBack;
	private ImageButton TencentLogin;
	private ImageButton SinaLogin;
	
	private ProgressDialog progressDialog = null;
	private Tencent mTencent;
	private String UID;
	private String Token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		init();
		setParams();
		bindEvent();
	}
	/**
	 * 各种初始化
	 */
	private void init()
	{
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		
		LoginBottomLinear = (LinearLayout)findViewById(R.id.LoginBottomLinear);
		LoginbtnGetBack = (Button)findViewById(R.id.LoginbtnGetBack);
		LoginGetBack = (ImageButton)findViewById(R.id.LoginGetBack);
		TencentLogin = (ImageButton)findViewById(R.id.TencentLogin);
		SinaLogin = (ImageButton)findViewById(R.id.SinaLogin);
		
		mTencent = Tencent.createInstance(AppID, LoginActivity.this);
	}
	/**
	 * 设置页面布局
	 */
	private void setParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		LoginGetBack.setLayoutParams(params);
		
		LinearLayout.LayoutParams tencentParams = (LinearLayout.LayoutParams)TencentLogin.getLayoutParams();
		tencentParams.setMargins((int)(Width-(Width / 3.5)) / 2 , Height / 4, width / 4, Height / 10);
		tencentParams.width = tencentParams.height =  (int)(Width / 3.5);
		TencentLogin.setLayoutParams(tencentParams);
		
		LinearLayout.LayoutParams sinaParams = (LinearLayout.LayoutParams)SinaLogin.getLayoutParams();
		sinaParams.setMargins((int)(Width-(Width / 3.5)) / 2, 0, width / 4, 0);
		sinaParams.width = sinaParams.height = (int)(Width / 3.5);
		SinaLogin.setLayoutParams(sinaParams);
	}
	/**
	 * 绑定页面上的事件
	 */
	private void bindEvent()
	{
		LoginGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
		LoginbtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
				
			}
		});
		TencentLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (mTencent.isSessionValid() && mTencent.getOpenId() != null) 
				{
					Toast.makeText(LoginActivity.this, "已登录", Toast.LENGTH_LONG).show();
				}
				else
				{//执行登录
					progressDialog = ProgressDialog.show(LoginActivity.this, "请稍等...", "拼命数据获取中...", true);
					String SCOPE = "get_info,get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,check_page_fans,add_t,del_t,add_pic_t,get_repost_list,"
							+ "get_other_info,get_fanslist,get_idollist,add_idol,del_idol";
					
					mTencent.login(LoginActivity.this, SCOPE, new MyIUiListener(LoginActivity.this));          
				}
			}
		});
		SinaLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void finish() {
		
		Intent intent = new Intent();
		intent.putExtra("UID", UID);
		intent.putExtra("Token", Token);
		//Toast.makeText(LoginActivity.this, intent.getStringExtra("UID"), Toast.LENGTH_LONG).show();
		setResult(1, intent);
		super.finish();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (mTencent.isSessionValid() && mTencent.getOpenId() != null) 
		{
			String PostStr = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getauth&type=qq&authkey=dc94e7adc147d381e26e74b63434b132&";
			String OpenId = mTencent.getOpenId();
			PostStr += ("otherid=" + OpenId);
			//Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
			mThread mythread = new mThread(JSON_SUCCESS, PostStr);
			mythread.start();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JSON_SUCCESS:
				String JsonStr = (String)msg.obj;
				AnalyzeJson analyze = new AnalyzeJson(JsonStr);
				HashMap<String, String> userMap = analyze.GetUserInfoByJson();
				//Toast.makeText(LoginActivity.this, JsonStr, Toast.LENGTH_LONG).show();
				UID = userMap.get("uid");
				Token = userMap.get("token");
				
				break;

			case MSG_FAILED:
				
				break;
			}
			progressDialog.dismiss();
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 进行网络请求的线程
	 * @author Coder
	 *
	 */
	private class mThread extends Thread
	{
		private int KIND;
		private String URL;
		public mThread(int Kind, String Url)
		{
			this.KIND = Kind;
			this.URL = Url;
		}

		@Override
		public void run() {
			switch (KIND) {
			case JSON_SUCCESS:
				GetHtml GH = new GetHtml();
				String JsonString = GH.GetJsonByUrl(URL);
				mHandler.obtainMessage(KIND, JsonString).sendToTarget();
				break;

			case MSG_FAILED:
				
				break;
			}
			super.run();
		}
		
	}
}