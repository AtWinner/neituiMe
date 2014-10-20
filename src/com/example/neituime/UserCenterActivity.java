package com.example.neituime;

import java.util.HashMap;

import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.AnalyzeJson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class UserCenterActivity extends Activity {
	private static final int JSON_SUCCESS = 0; //json获取成功
	private static final int IMG_SUCCESS = 1; //图片获取成功
	private static final int MSG_FAILED = 2; //获取失败
	
	
	private int Width;
	private int Height;
	private String LoginStyle;
	private String Token;
	
	private ImageButton UserCenterGetBack;
	private Button UserCenterbtnGetBack;
	private ScrollView  UserCenterSctollView;
	private LinearLayout  UserCenterTop;
	private ImageView UserCenterImageView;
	private TextView UserCenterName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_activity);
		init();
		setParams();
		bindEvent();
		if(LoginStyle.equals("Tencent"))
		{
			String JsonUrl = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getuser&token=" + Token;
			mThread UserInfoThread = new mThread(JSON_SUCCESS, JsonUrl);
			UserInfoThread.start();
		}
	}
	private void init()
	{
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		
		Intent beforeIntent = getIntent();
		LoginStyle = beforeIntent.getStringExtra("LoginStyle");
		Token = beforeIntent.getStringExtra("Token");
		
		UserCenterGetBack = (ImageButton)findViewById(R.id.UserCenterGetBack);
		UserCenterbtnGetBack = (Button)findViewById(R.id.UserCenterbtnGetBack);
		UserCenterSctollView = (ScrollView)findViewById(R.id.UserCenterSctollView);
		UserCenterTop = (LinearLayout)findViewById(R.id.UserCenterTop);
		UserCenterImageView = (ImageView)findViewById(R.id.UserCenterImageView);
		UserCenterName = (TextView)findViewById(R.id.UserCenterName);
		setBackgroundWhite(UserCenterTop);
	}
	private void setParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		UserCenterGetBack.setLayoutParams(params);
		
		RelativeLayout.LayoutParams scrollViewParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, height * 12);
		UserCenterSctollView.setLayoutParams(scrollViewParams);
	}
	private void bindEvent()
	{
		UserCenterbtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				UserCenterActivity.this.finish();
			}
		});
		UserCenterGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserCenterActivity.this.finish();
			}
		});
	}
	private void setBackgroundWhite(View myView)
	{
		myView.setBackgroundColor(Color.WHITE);
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JSON_SUCCESS:
				String UserInfo = (String)msg.obj;
				AnalyzeJson AJ = new AnalyzeJson(UserInfo);
				HashMap<String, String> UserInfoMap = AJ.GetUserCenterInfo();
				Toast.makeText(UserCenterActivity.this, UserInfoMap.toString(), Toast.LENGTH_SHORT).show();
				mThread ImageThread = new mThread(IMG_SUCCESS, UserInfoMap.get("avatar"));
				ImageThread.start();
				BindData(UserInfoMap);
				break;
			case IMG_SUCCESS:
				Bitmap bitmap = (Bitmap)msg.obj;
				UserCenterImageView.setImageBitmap(bitmap);
				break;
			case MSG_FAILED:
				
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private class mThread extends Thread
	{
		private int KIND;
		private String URL;
		public mThread(int Kind, String Url)
		{
			KIND = Kind;
			URL = Url;
		}
		@Override
		public void run() {
			switch (KIND) {
			case JSON_SUCCESS:
				GetHtml GH = new GetHtml();
				String JsonStr = GH.GetJsonByUrl(URL);
				mHandler.obtainMessage(KIND, JsonStr).sendToTarget();
				break;

			case IMG_SUCCESS:
				Bitmap bitmap = GetImage.returnBitMap(URL);
				mHandler.obtainMessage(KIND, bitmap).sendToTarget();
				break;
			
			}
			super.run();
		}
		
	}
	private void BindData(HashMap<String, String> map)
	{
		
	}
}
