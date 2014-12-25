package com.example.neituime;

import java.util.HashMap;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.network.GetHtml;
import com.example.tencent.MyIUiListener;
import com.example.view.AnalyzeJson;
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private static final String SinaAppKey = "";//新浪微博登录的AppKey
	private static final String AppID = "101016468";//腾讯登录的ID
	
	private int ResponseNumber;
	private String JobID;
	
	private int Width;
	private int Height;

	private ImageButton LoginGetBack;
	private LinearLayout LoginBottomLinear;
	private Button LoginbtnGetBack;
	private ImageButton TencentLogin;
	private ImageButton SinaLogin;
	
	private myProgressDialog progressDialog = null;
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
		Intent intent = getIntent();
		ResponseNumber = intent.getIntExtra("ResponseNumber", 0);
		if(ResponseNumber == 2)
		{
			JobID = intent.getStringExtra("JobID");
		}
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(LoginActivity.this, Height);
		
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
		
		LoginbtnGetBack.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
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
				
				if (mTencent.isSessionValid() && mTencent.getOpenId() != null && !UID.equals("0")) 
				{
					Toast.makeText(LoginActivity.this, "已登录", Toast.LENGTH_LONG).show();
				}
				else
				{//执行登录
					showDialog();
					String SCOPE = "get_info,get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,check_page_fans,add_t,del_t,add_pic_t,get_repost_list,"
							+ "get_other_info,get_fanslist,get_idollist,add_idol,del_idol";
					mTencent.login(LoginActivity.this, SCOPE, new MyIUiListener(LoginActivity.this));          
				}
			}
		});
		SinaLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "无法获取APP的AppKey和AppSecrect，因此无法实现", Toast.LENGTH_SHORT).show();
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
			Log.e(PostStr, PostStr);
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
				try
				{
					HashMap<String, String> userMap = analyze.GetUserInfoByJson();
					//Toast.makeText(LoginActivity.this, JsonStr, Toast.LENGTH_LONG).show();
					UID = userMap.get("uid");
					if(UID.equals("0"))
					{
						Toast.makeText(LoginActivity.this, "找不到您的信息，请您到内推网充实您的简历", Toast.LENGTH_SHORT).show();
						break;
					}
					Token = userMap.get("token");
					SharedPreferences OnlineInfo = getSharedPreferences("OnlineInfo", Context.MODE_PRIVATE);
					Editor editor = OnlineInfo.edit();
					editor.putString("UID", UID);
					editor.putString("Token", Token);
					editor.putString("LoginStyle", "Tencent");
					editor.commit();//将用户信息保存到本地，知道点击退出时删除
					if(ResponseNumber == 1)
					{
						Intent userIntent = new Intent(LoginActivity.this, UserCenterActivity.class);
						userIntent.putExtra("LoginStyle", "Tencent");
						userIntent.putExtra("Token", userMap.get("token"));
						userIntent.putExtra("ResponseNumber", ResponseNumber);
						startActivity(userIntent);
						overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
						LoginActivity.this.finish();
					}
					else if(ResponseNumber == 2)
					{
						Intent resumeIntent = new Intent(LoginActivity.this, ResumeActivity.class);
						resumeIntent.putExtra("Token", userMap.get("token"));
						resumeIntent.putExtra("ResponseNumber", ResponseNumber);
						resumeIntent.putExtra("JobID", JobID);
						startActivity(resumeIntent);
						overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
						LoginActivity.this.finish();
					}
				}
				catch(Exception e)
				{
					Toast.makeText(LoginActivity.this, "找不到您的信息，请您到", Toast.LENGTH_SHORT).show();
				}
				
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
	private void showDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = myProgressDialog.createDialog(LoginActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new myOnKeyListener());
			progressDialog.setMessage("拼命获取数据中...");
		}
		progressDialog.show();		
	}
}
