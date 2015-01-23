package com.example.neituime;

import java.util.HashMap;
import java.util.List;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.network.CheckNetwork;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.AnalyzeJson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SystemInfoActivity extends Activity {
	private static final int JSON_SUCCESS = 1; // 获取Json成功
	private static final int MSG_FAILED = 2; // 网络请求失败
	private static final int IMG_SUCCESS = 3 ;// 获取图片成功
	private static final int MSG_REFRESH = 4;// 下拉刷新
	private static final int LOAD_MORE = 5;//上拉加载更多
	
	private myProgressDialog progressDialog = null;
	
	private int Page;
	private int Width;//屏幕宽
	private int Height;//屏幕高
	
	private String Token;
	
	private ImageButton SystemInfoGetBack;
	private Button SystemInfobtnGetBack;
	private LinearLayout SystemInfoLinearTop;
	private LinearLayout SystemInfo_sub_root_lin;
	private PullToRefreshScrollView SystemInfoBodyScrollView;
	
	private mThread imageThread;
	private mThread onloadThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_information_activity);
		init();
		setParams();
		bindEvent();
		DoRequest();
//		addView(new HashMap<String, String>() , 1);
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
	private void bindEvent()
	{
		SystemInfoGetBack.setOnClickListener(new myOnClickListener());
		SystemInfobtnGetBack.setOnClickListener(new myOnClickListener());
		SystemInfoBodyScrollView.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) 
			{
				Page = 1;
				String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=notices&page=" + Page + "&token=" + Token + "&nowtime=1421022804919&typein=9";
				onloadThread = new mThread(MSG_REFRESH, url);
				onloadThread.start();
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) 
			{
				Page++;
				String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=notices&page=" + Page + "&token=" + Token + "&nowtime=1421022804919&typein=9";
				onloadThread = new mThread(LOAD_MORE, url);
				onloadThread.start();
			}
		});
	}
	private void DoRequest()
	{
		if(Token == null || Token.equals(""))
		{
			Toast.makeText(SystemInfoActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
			this.finish();
			return;
		}
		String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=notices&page=" + Page + "&token=" + Token + "&nowtime=1421022804919&typein=9";
		Log.e("", url);
		onloadThread = new mThread(JSON_SUCCESS, url);
		onloadThread.start();
		showDialog();
	}
	/**
	 * 加载图片的线程
	 */
	private Handler imageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case IMG_SUCCESS:
				int Item = msg.arg1;
				ImageView imageView = (ImageView)findViewById(Item);
				imageView.setImageBitmap((Bitmap)msg.obj);
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 加载json的线程
	 */
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case MSG_REFRESH:
				SystemInfo_sub_root_lin.removeAllViews();
			case LOAD_MORE:
			case JSON_SUCCESS:
				AnalyzeJson analyzeJson = new AnalyzeJson(msg.obj.toString());
				List<HashMap<String, String>> list = analyzeJson.GetSystemInfoMessage();
				if(list == null || list.size() <= 0)
				{
					Toast.makeText(SystemInfoActivity.this, "没有更多消息了！", Toast.LENGTH_SHORT).show();
					break;
				}
				addList(list);
				loadImage(list);
				break;
			
			}
			super.handleMessage(msg);
			
			SystemInfoBodyScrollView.onRefreshComplete();
			progressDialog.dismiss();
		}
		
	};
	private class mThread extends Thread
	{
		private int MSG;
		private String URL;
		private int Item;
		/**
		 * 不加载图片时使用
		 * @param message 消息类型
		 * @param url 加载路径
		 */
		public mThread(int message, String url)
		{
			MSG = message;
			URL = url;
		}
		/**
		 * 加载图片的时候使用
		 * @param message 消息类型
		 * @param url 加载路径
		 * @param Item Item
		 */
		public mThread(int message, String url, int Item)
		{
			MSG = message;
			URL = url;
			this.Item = Item;
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
			case LOAD_MORE:
				if(check.isNetworkConnected(SystemInfoActivity.this) || check.OpenNetwork(SystemInfoActivity.this))
				{
					GetHtml GH = new GetHtml();
					String JsonStr = GH.GetJsonByUrl(URL);
					mHandler.obtainMessage(MSG, JsonStr).sendToTarget();
				}
			case IMG_SUCCESS:
				if(check.isNetworkConnected(SystemInfoActivity.this) || check.OpenNetwork(SystemInfoActivity.this))
				{
					Bitmap bitmap = GetImage.returnBitMap(URL);
					imageHandler.obtainMessage(MSG, Item, Item, bitmap).sendToTarget();
				}
				break;
			}
		}
	}
	class myOnClickListener implements OnClickListener 
	{

		@Override
		public void onClick(View arg0) {
			switch(arg0.getId())
			{
			case R.id.SystemInfoGetBack:
			case R.id.SystemInfobtnGetBack:
				SystemInfoActivity.this.finish();
				break;
			}
		}
	}
	private void addList(List<HashMap<String, String>> list)
	{
		int Item = 0;
		for(HashMap<String, String> map : list)
		{
			addView(map, Item);
			Item++;
		}
	}
	/**
	 * 获取logo图片
	 * @param list
	 */
	private void loadImage(List<HashMap<String, String>> list)
	{
		int Item = 0;
		for(HashMap<String, String> map : list)
		{
			String avatar = map.get("avatar");
			if(!avatar.equals("") && !avatar.equals("null"))
			{//没有图片链接就直接跳过
				loadImageItem(avatar, Item);
			}
			Item++;
		}
	}
	/**
	 * 加载每张图片
	 * @param avatar 图片链接
	 * @param Item ImageView的ID
	 */
	private void loadImageItem(String avatar, int Item)
	{
		imageThread= new mThread(IMG_SUCCESS, avatar, Item);
		imageThread.start();
		
	}
	/**
	 * 根据HashMap创建列表项数据
	 * @param map 哈希表
	 * @param Item 根据在list中的位置确定，要作为每个列表的id
	 */
	private void addView(HashMap<String, String> map, int Item)
	{
		int itemNum = 5;
		//外侧LinearLayout
		LinearLayout out = new LinearLayout(SystemInfoActivity.this);
		out.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams outParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		out.setLayoutParams(outParams);
		out.setPadding(5, 5, 5, 5);
		SystemInfo_sub_root_lin.addView(out);
		//ImageView
		ImageView logo = new ImageView(SystemInfoActivity.this);
		logo.setId(Item);
		LinearLayout.LayoutParams logoparams = new LayoutParams((int)(Width / itemNum), (int)(Width / itemNum));
		logoparams.gravity = Gravity.CENTER_VERTICAL;
//		logoparams.leftMargin = (int)(Height / itemNum * 0.1);
//		logoparams.topMargin = (int)(Height / itemNum * 0.1);
//		logoparams.rightMargin = (int)(Height / itemNum * 0.1);
		logo.setLayoutParams(logoparams);
		logo.setImageResource(R.drawable.main_logo);
		out.addView(logo);
		//内侧LinearLayout
		int innerWidth = Width - 20 - (int)(Width / itemNum);
		LinearLayout inner = new LinearLayout(SystemInfoActivity.this);
		inner.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams innerParams = new LayoutParams(innerWidth, LayoutParams.WRAP_CONTENT);
		inner.setLayoutParams(innerParams);
		inner.setPadding(5, 2, 2, 2);
		out.addView(inner);
		//日期的TextView
		TextView TXDate = new TextView(SystemInfoActivity.this);
		LinearLayout.LayoutParams TXTopParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TXDate.setLayoutParams(TXTopParams);
		TXDate.setText(map.get("createtime"));
		TXDate.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 18));
		TXDate.setGravity(Gravity.CENTER_VERTICAL);
		inner.addView(TXDate);
		//消息正文的TextView
		TextView TXMessage = new TextView(SystemInfoActivity.this);
		LinearLayout.LayoutParams TXMessageParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TXMessage.setLayoutParams(TXMessageParams);
		TXMessage.setText(map.get("message"));
		TXMessage.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 16));
		TXMessage.setGravity(Gravity.CENTER_VERTICAL);
		TXMessage.setPadding(5, 5, 5, 0);
		inner.addView(TXMessage);
	}
	private void showDialog()
	{
		progressDialog = myProgressDialog.createDialog(SystemInfoActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setOnKeyListener(new myOnKeyListener());
		progressDialog.setMessage("拼命获取数据中...");
		progressDialog.show();
	}
}
