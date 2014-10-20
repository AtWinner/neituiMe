package com.example.neituime;

import java.util.HashMap;

import org.w3c.dom.UserDataHandler;

import com.example.adapter.AdjustPageLayout;
import com.example.network.CheckNetwork;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.AnalyzeJson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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

public class ResumeActivity extends Activity {
	private static final int JSON_SUCCESS = 1; // 获取Json成功
	private static final int MSG_FAILED = 2; // 网络请求失败
	private static final int IMG_SUCCESS = 3 ;// 获取图片成功
	private static final int MSG_REFRESH = 4;// 刷新
	
	private int Width;
	private int Height;
	private String Token;
	private String UID;
	private String ResumeURL;
	
	private ImageButton ResumeGetBack;
	private Button ResumebtnGetBack;
	private LinearLayout ResumeBottomLinear;
	private PullToRefreshScrollView ResumeBodyScrollView;
	private ImageView UserPhoto;
    private LinearLayout UserDetailInfo;
    private LinearLayout UserMainInfo;
    private LinearLayout ResumeLinearTop;
    
    private ProgressDialog progressDialog = null;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_activity);
		progressDialog = ProgressDialog.show(this, "请稍等...", "拼命数据获取中...", true);
		init();
		SetParams();
		BindEvent();
		mThread myThread = new mThread(JSON_SUCCESS, ResumeURL);
		myThread.start();
		
	}
	private void init()
	{
		Intent beforeIntent = getIntent();
		Token = beforeIntent.getStringExtra("Token");
		UID = beforeIntent.getStringExtra("UID");
		ResumeURL = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=resume&token="
			+ Token;
		
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		
		ResumeBottomLinear = (LinearLayout)findViewById(R.id.ResumeBottomLinear);
		ResumebtnGetBack = (Button)findViewById(R.id.ResumebtnGetBack);
		ResumeGetBack = (ImageButton)findViewById(R.id.ResumeGetBack);
		ResumeBodyScrollView = (PullToRefreshScrollView)findViewById(R.id.ResumeBodyScrollView);
		ResumeBodyScrollView.setMode(Mode.PULL_FROM_START);
		UserPhoto = (ImageView)findViewById(R.id.UserPhoto);
		UserDetailInfo = (LinearLayout)findViewById(R.id.UserDetailInfo);
		UserMainInfo = (LinearLayout)findViewById(R.id.UserMainInfo);
		ResumeLinearTop = (LinearLayout)findViewById(R.id.ResumeLinearTop);
	}
	private void SetParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		ResumeGetBack.setLayoutParams(params);
		
		LinearLayout.LayoutParams photoParams = (LinearLayout.LayoutParams)UserPhoto.getLayoutParams();
		photoParams.width = photoParams.height = Width / 4;
		UserPhoto.setLayoutParams(photoParams);
		
		int scrollViewHeight = (int)(height * 11.5);
		RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams)ResumeLinearTop.getLayoutParams();
		scrollViewParams.height = scrollViewHeight;
		ResumeLinearTop.setLayoutParams(scrollViewParams);
		
		
		
	}
	private void BindEvent()
	{
		ResumebtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ResumeActivity.this.finish();				
			}
		});
		ResumeGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ResumeActivity.this.finish();
				
			}
		});
		ResumeBodyScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {


			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
				
			}
		});
	}
	 private class GetDataTask extends AsyncTask<Void, Void, String[]> {  
		  
	        @Override  
	        protected String[] doInBackground(Void... params) {  
	            return null;  
	        }  
	  
	        @Override  
	        protected void onPostExecute(String[] result) {  
	        	mThread myThread = new mThread(MSG_REFRESH, ResumeURL);
	    		myThread.start();
	    		
	            super.onPostExecute(result);  
	        }  
	    }  
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) 
			{
			case MSG_REFRESH:
				UserMainInfo.removeAllViews();
	    		UserDetailInfo.removeAllViews();
			case JSON_SUCCESS:
				String JsonStr = (String)msg.obj;
				AnalyzeJson myJson = new AnalyzeJson(JsonStr);
				HashMap<String,String> ResumeMap = myJson.GetUserResume();
				SetData(ResumeMap);
				//Toast.makeText(ResumeActivity.this, ResumeMap.toString(), Toast.LENGTH_LONG).show();
				String PhotoUrl = ResumeMap.get("photo");
				mThread imageThread = new mThread(IMG_SUCCESS, PhotoUrl);
				imageThread.start();
				break;

			case IMG_SUCCESS:
				Bitmap bitmap = (Bitmap)msg.obj;
				UserPhoto.setImageBitmap(bitmap);
				break;
			case MSG_FAILED:
				
				break;
			}
			ResumeBodyScrollView.onRefreshComplete();  
			progressDialog.dismiss();
			super.handleMessage(msg);
		}
		
	};
	private class mThread extends Thread 
	{
		private int KIND;
		private String URL;
		public mThread(int kind, String Url)
		{
			KIND = kind;
			URL = Url;
		}
		@Override
		public void run() {
			CheckNetwork check = new CheckNetwork();
			switch (KIND) 
			{
			case MSG_REFRESH:
			case JSON_SUCCESS:
				if(check.isNetworkConnected(ResumeActivity.this) || check.OpenNetwork(ResumeActivity.this))
				{
					GetHtml GH = new GetHtml();
					String JsonStr = GH.GetJsonByUrl(URL);
					mHandler.obtainMessage(KIND, JsonStr).sendToTarget();
				}
				break;

			case IMG_SUCCESS:
				if(check.isNetworkConnected(ResumeActivity.this) || check.OpenNetwork(ResumeActivity.this))
				{
					Bitmap bitmap = GetImage.returnBitMap(URL);
					mHandler.obtainMessage(KIND, bitmap).sendToTarget();
				}
				break;
			}
			super.run();
		}
		
	}
	private void SetData(HashMap<String,String> ResumeMap)
	{
		((LinearLayout)findViewById(R.id.ReumueTop)).setBackgroundColor(Color.WHITE);
		UserDetailInfo.setBackgroundColor(Color.WHITE);
		TextView tt = new TextView(ResumeActivity.this);
		tt.setText(ResumeMap.toString());
		String Name = ResumeMap.get("realname") 
				+ "   " + ResumeMap.get("city");
		AddTextView(Name, AdjustPageLayout.AdjustListTitleTextSize(Width), UserMainInfo);
		
		String Title = ResumeMap.get("currentcompany")
				+ "  " + ResumeMap.get("currentjob")
				+ "\n工作年限：" + ResumeMap.get("workage")
				+ "   年龄：" + ResumeMap.get("age")
				+ "\n手机：" + ResumeMap.get("mobile")
				+ "\n邮箱：" + ResumeMap.get("email");
		AddTextView(Title, AdjustPageLayout.AdjustListInfoSize(Width), UserMainInfo);
		
		AddTextView("简历标签", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("experiencetags"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		
		AddTextView("工作经验", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("experience"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		AddTextView("教育经历", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("education"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		TextView line = new TextView(ResumeActivity.this);
		line.setText("当你给内推人发简历时，会展示以上内容\n");
		line.setTextSize(AdjustPageLayout.AdjustListInfoSize(Width));
		line.setTextColor(Color.RED);
		UserDetailInfo.addView(line);
		
		AddTextView("目标职位", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("interestjobs"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		AddTextView("目标城市", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("interestcitys"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		AddTextView("期望月薪", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("salary") + "k", AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		AddTextView("简历隐私状态", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		int privateState =  Integer.parseInt(ResumeMap.get("privatestatus"));
		String State;
		if(privateState == 0)
			State = "观望中";
		else if(privateState == 1)
			State = "求内推";
		else
			State = "勿扰我";
		AddTextView(State, AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		

		AddTextView("简历创建时间", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("updatetime"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);

		AddTextView("最后一次修改时间", AdjustPageLayout.AdjustListTitleTextSize(Width), UserDetailInfo);
		AddTextView(ResumeMap.get("createtime"), AdjustPageLayout.AdjustListInfoSize(Width), UserDetailInfo);
		
		//UserDetailInfo.addView(tt);
	}
	private void AddTextView(String innerText, int TextSize, LinearLayout myLinear)
	{
		TextView myTextView = new TextView(ResumeActivity.this);
		myTextView.setText(innerText);
		myTextView.setTextSize(TextSize); 
		if(TextSize == AdjustPageLayout.AdjustListInfoSize(Width))
		{
			myTextView.setTextColor(Color.GRAY);
		}
		myLinear.addView(myTextView);
	}
	@Override
	public void finish() {
		
		super.finish();
	}

}