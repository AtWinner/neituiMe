package com.example.neituime;

import com.example.adapter.GetScreenSize;
import com.example.network.CheckNetwork;
import com.example.network.GetHtml;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class InformationActivity extends Activity {
	private static final int JSON_SUCCESS = 1; // 获取Json成功
	private static final int MSG_FAILED = 2; // 网络请求失败
	private static final int IMG_SUCCESS = 3 ;// 获取图片成功
	private static final int MSG_REFRESH = 4;// 刷新
	private static final int MSG_RESUME= 5;//下载
	
	private int Page;
	private int Width;//屏幕宽
	private int Height;//屏幕高
	
	private ImageButton InfoGetBack;
	private Button InfobtnGetBack;
	private LinearLayout InfoLinearTop;
	private LinearLayout Info_sub_root_lin;
	private PullToRefreshScrollView InfoBodyScrollView;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_activity);
		init();
		setParams();
		bindEvent();
	}
	private void init()
	{
		Page = 1;
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(InformationActivity.this, Height);
		InfoGetBack = (ImageButton)findViewById(R.id.InfoGetBack);
		InfobtnGetBack = (Button)findViewById(R.id.InfobtnGetBack);
		InfoLinearTop = (LinearLayout)findViewById(R.id.InfoLinearTop);
		Info_sub_root_lin = (LinearLayout)findViewById(R.id.Info_sub_root_lin);
		InfoBodyScrollView = (PullToRefreshScrollView)findViewById(R.id.InfoBodyScrollView);
		InfoBodyScrollView.setMode(Mode.BOTH);
	}
	private void setParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		InfoGetBack.setLayoutParams(params);
		LinearLayout.LayoutParams scrollViewParams = (LinearLayout.LayoutParams)InfoBodyScrollView.getLayoutParams();
		scrollViewParams.height = height*12;
		InfoBodyScrollView.setLayoutParams(scrollViewParams);
	}
	private void bindEvent()
	{
		InfobtnGetBack.setOnClickListener(new myOnClickListener());
		InfoGetBack.setOnClickListener(new myOnClickListener());
		String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=messages&page=1&token=e00de96065df77805bf260f01f842161&nowtime=1420785672858";
		mThread onloadThread = new mThread(JSON_SUCCESS, url);
		onloadThread.start();
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case MSG_REFRESH:
			case JSON_SUCCESS:
				addView(msg.obj.toString() + msg.obj.toString());
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private class mThread extends Thread
	{
		private int MSG;
		private String URL;
		public mThread(int message, String url)
		{
			MSG = message;
			URL = url;
		}
		@Override
		public void run()
		{
			CheckNetwork check = new CheckNetwork();
			// TODO Auto-generated method stub
			switch(MSG)
			{
			case MSG_REFRESH:
			case JSON_SUCCESS:
				if(check.isNetworkConnected(InformationActivity.this) || check.OpenNetwork(InformationActivity.this))
				{
					GetHtml GH = new GetHtml();
					String JsonStr = GH.GetJsonByUrl(URL);
					mHandler.obtainMessage(MSG, JsonStr).sendToTarget();
				}
				break;
			}
		}
	}
	class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			switch(arg0.getId())
			{
			case R.id.InfoGetBack:
			case R.id.InfobtnGetBack:
				InformationActivity.this.finish();
				break;
			}
		}
	}
	private void addView(String innerText)
	{
		TextView tx = new TextView(InformationActivity.this);
		tx.setText(innerText);
		Info_sub_root_lin.addView(tx);
	}
	

}
