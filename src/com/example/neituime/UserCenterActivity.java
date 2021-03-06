package com.example.neituime;

import java.util.HashMap;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.event.myOnTouchListenerChangeBackground;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.AnalyzeJson;
import com.tencent.tauth.Tencent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
	
	private static final int ResponseNumber = 1;
	
	private myProgressDialog progressDialog = null;
	
	private static final String AppID = "101016468";
	private Tencent mTencent;
	
	private int Width;
	private int Height;
	private String LoginStyle;
	private String Token;
	
	private ImageButton UserCenterGetBack;
	private Button UserCenterbtnGetBack;
	private ScrollView  UserCenterSctollView;
	private LinearLayout  UserCenterTop;
	private LinearLayout userCenterBottom;
	
	private ImageView UserCenterImageView;
	private TextView UserCenterName;
	private TextView UserCenterDescription;
	
	private RelativeLayout RelativeHaveSend;
	private ImageView arrowHaveSend;
	private Button txHaveSend;
	
	private RelativeLayout RelativeSearch;
	private ImageView arrowSearch;
	private Button txSearch;
	
	private RelativeLayout RelativeResumeCollection;
	private ImageView arrowResumeCollection;
	private Button txResumeCollection;
	
	private RelativeLayout RelativeMyResume;
	private ImageView arrowMyResume;
	private Button txMyResume;
	
	private RelativeLayout RelativeJobCollection;
	private ImageView arrowJobCollection;
	private Button txJobCollection;
	
	private RelativeLayout RelativeSuggestion;
	private ImageView arrowSuggestion;
	private Button txSuggestion;
	
	private RelativeLayout RelativeQuit;
	private ImageView arrowQuit;
	private Button txQuit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_activity);
		Intent beforeIntent = getIntent();
		LoginStyle = beforeIntent.getStringExtra("LoginStyle");
		Token = beforeIntent.getStringExtra("Token");
		if(Token.equals(""))
		{
			this.finish();
			return;
		}
		Log.e(Token, LoginStyle);
		init();
		setParams();
		bindEvent();
		if(LoginStyle.equals("Tencent"))
		{
			String JsonUrl = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getuser&token=" + Token;
			mThread UserInfoThread = new mThread(JSON_SUCCESS, JsonUrl);
			UserInfoThread.start();
		}
		showDialog();
	}
	private void init()
	{
		mTencent = Tencent.createInstance(AppID, UserCenterActivity.this);
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(UserCenterActivity.this, Height);
		userCenterBottom = (LinearLayout)findViewById(R.id.userCenterBottom);
		UserCenterGetBack = (ImageButton)findViewById(R.id.UserCenterGetBack);
		UserCenterbtnGetBack = (Button)findViewById(R.id.UserCenterbtnGetBack);
		UserCenterSctollView = (ScrollView)findViewById(R.id.UserCenterSctollView);
		UserCenterTop = (LinearLayout)findViewById(R.id.UserCenterTop);
		UserCenterImageView = (ImageView)findViewById(R.id.UserCenterImageView);
		UserCenterName = (TextView)findViewById(R.id.UserCenterName);
		UserCenterDescription =(TextView)findViewById(R.id.UserCenterDescription);
		RelativeHaveSend = (RelativeLayout)findViewById(R.id.RelativeHaveSend);
		arrowHaveSend = (ImageView)findViewById(R.id.arrowHaveSend);
		txHaveSend = (Button)findViewById(R.id.txHaveSend);
		RelativeJobCollection = (RelativeLayout)findViewById(R.id.RelativeJobCollection);
		arrowJobCollection = (ImageView)findViewById(R.id.arrowJobCollection);
		txJobCollection = (Button)findViewById(R.id.txJobCollection);
		RelativeMyResume = (RelativeLayout)findViewById(R.id.RelativeMyResume);
		arrowMyResume = (ImageView)findViewById(R.id.arrowMyResume);
		txMyResume = (Button)findViewById(R.id.txMyResume);
		RelativeResumeCollection = (RelativeLayout)findViewById(R.id.RelativeResumeCollection);
		arrowResumeCollection = (ImageView)findViewById(R.id.arrowResumeCollection);
		txResumeCollection = (Button)findViewById(R.id.txResumeCollection);
		RelativeSearch = (RelativeLayout)findViewById(R.id.RelativeSearch);
		arrowSearch = (ImageView)findViewById(R.id.arrowSearch);
		txSearch = (Button)findViewById(R.id.txSearch);
		RelativeSuggestion = (RelativeLayout)findViewById(R.id.RelativeSuggestion);
		arrowSuggestion = (ImageView)findViewById(R.id.arrowSuggestion);
		txSuggestion = (Button)findViewById(R.id.txSuggestion);
		RelativeQuit = (RelativeLayout)findViewById(R.id.RelativeQuit);
		arrowQuit = (ImageView)findViewById(R.id.arrowQuit);
		txQuit = (Button)findViewById(R.id.txQuit);
		setBackgroundWhite(UserCenterTop);
	}
	private void setParams()
	{//在这只测试Github
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		UserCenterGetBack.setLayoutParams(params);
		
		RelativeLayout.LayoutParams scrollViewParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, height * 12);
		UserCenterSctollView.setLayoutParams(scrollViewParams);
		
		UserCenterbtnGetBack.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
		
		setParamsHelp(arrowHaveSend, txHaveSend);
		setParamsHelp(arrowJobCollection, txJobCollection);
		setParamsHelp(arrowMyResume, txMyResume);
		setParamsHelp(arrowResumeCollection, txResumeCollection);
		setParamsHelp(arrowSearch, txSearch);
		setParamsHelp(arrowSuggestion, txSuggestion);
		setParamsHelp(arrowQuit, txQuit);
	}
	private void setParamsHelp(ImageView imageView, Button button)
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		imageView.setLayoutParams(params);
		button.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));
	}
	private void bindEvent()
	{
		RelativeJobCollection.setOnClickListener(new mOnClickListener());
		RelativeJobCollection.setOnTouchListener(new myOnTouchListenerChangeBackground());
		RelativeResumeCollection.setOnClickListener(new mOnClickListener());
		RelativeResumeCollection.setOnTouchListener(new myOnTouchListenerChangeBackground());
		RelativeSearch.setOnClickListener(new mOnClickListener());
		RelativeSearch.setOnTouchListener(new myOnTouchListenerChangeBackground());
		RelativeSuggestion.setOnClickListener(new mOnClickListener());
		RelativeSuggestion.setOnTouchListener(new myOnTouchListenerChangeBackground());
		RelativeHaveSend.setOnClickListener(new mOnClickListener());
		RelativeHaveSend.setOnTouchListener(new myOnTouchListenerChangeBackground());
		txHaveSend.setOnClickListener(new mOnClickListener());
		UserCenterbtnGetBack.setOnClickListener(new mOnClickListener());
		UserCenterGetBack.setOnClickListener(new mOnClickListener());
		RelativeQuit.setOnClickListener(new mOnClickListener());
		RelativeQuit.setOnTouchListener(new myOnTouchListenerChangeBackground());
		RelativeMyResume.setOnClickListener(new mOnClickListener());
		RelativeMyResume.setOnTouchListener(new myOnTouchListenerChangeBackground());
		txSuggestion.setOnClickListener(new mOnClickListener());
		RelativeSuggestion.setOnClickListener(new mOnClickListener());
	}
	
	private class mOnClickListener implements OnClickListener 
	{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.UserCenterbtnGetBack:
			case R.id.UserCenterGetBack:
				UserCenterActivity.this.finish();
				break;
			case R.id.RelativeHaveSend:
			case R.id.txHaveSend:
				//Toast.makeText(UserCenterActivity.this, "text", Toast.LENGTH_SHORT).show();
				break;
			case R.id.txSuggestion:
			case R.id.RelativeSuggestion:
				ClickSuggestion();
				break;
			case R.id.RelativeQuit:
				ClickQuit();
				break;
			case R.id.RelativeMyResume:
				ClickMyResume();
				break;
			}
			
		}
	}
	private void ClickSuggestion()
	{
		Intent intent = new Intent(UserCenterActivity.this, AboutMeActivity.class);
		startActivity(intent);
	}
	private void  ClickMyResume()
	{
		if(!Token.equals(""))
		{
			Intent intent = new Intent(UserCenterActivity.this, ResumeActivity.class);
			intent.putExtra("ResponseNumber", ResponseNumber);
			intent.putExtra("Token",Token);
			intent.putExtra("UID", "");//UID暂时在Resume中用不到
			startActivity(intent);
			overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		}
		else
		{
			Toast.makeText(UserCenterActivity.this, "登录信息不存在，请重新登录", Toast.LENGTH_SHORT).show();
		}
	}
	private void ClickQuit()
	{
		new AlertDialog.Builder(UserCenterActivity.this).setTitle("提示").setMessage("确定要退出吗？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//需要添加一个提示
				if(mTencent.isSessionValid() && mTencent.getOpenId() != null) 
				{//已使用qq登录
					mTencent.logout(UserCenterActivity.this);
				}
				else if(false)
				{//放sina
					
				}
				SharedPreferences OnlineInfo = getSharedPreferences("OnlineInfo", Context.MODE_PRIVATE);
				Editor editor =OnlineInfo.edit();
				editor.clear();
				editor.commit();
				UserCenterActivity.this.finish();
			}
		}).setNegativeButton("取消", null).show();
		
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
				//Toast.makeText(UserCenterActivity.this, UserInfoMap.toString(), Toast.LENGTH_SHORT).show();
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
			progressDialog.dismiss();
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
	@SuppressLint("ResourceAsColor")
	private void BindData(HashMap<String, String> map)
	{
		UserCenterName.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width) + 1);
		UserCenterName.setText(map.get("realname"));
		UserCenterDescription.setTextSize(AdjustPageLayout.AdjustListInfoSize(Width));
		UserCenterDescription.setText(map.get("descrip"));
		UserCenterDescription.setTextColor(R.color.myGrary);
	}
	private void showDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = myProgressDialog.createDialog(UserCenterActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new myOnKeyListener());
			progressDialog.setMessage("拼命获取数据中...");
		}
		progressDialog.show();		
	}
}
