package com.example.neituime;

import java.util.HashMap;
import java.util.List;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.event.myOnTouchListenerChangeBackground;
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
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewDebug.IntToString;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class InformationActivity extends Activity {
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
	
	private ImageButton InfoGetBack;
	private Button InfobtnGetBack;
	private LinearLayout InfoLinearTop;
	private LinearLayout Info_sub_root_lin;
	private PullToRefreshScrollView InfoBodyScrollView;
	
	private mThread imageThread;
	private mThread onloadThread;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_activity);
		init();
		setParams();
		bindEvent();
		DoRequest();
	}
	private void init()
	{
		Intent intent = getIntent();
		Token = intent.getStringExtra("Token");
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
		InfoBodyScrollView.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) 
			{
				Page = 1;
				String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=messages&page=" + Page + "&token=" + Token + "&nowtime=1420785672858";
				onloadThread = new mThread(MSG_REFRESH, url);
				onloadThread.start();
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) 
			{
				Page++;
				String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=messages&page=" + Page + "&token=" + Token + "&nowtime=1420785672858";
				onloadThread = new mThread(LOAD_MORE, url);
				onloadThread.start();
			}
		});
	}
	private void DoRequest()
	{
		if(Token == null || Token.equals(""))
		{
			Toast.makeText(InformationActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
			this.finish();
			return;
		}
		String url = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=messages&page=" + Page + "&token=" + Token + "&nowtime=1420785672858";
		
		onloadThread = new mThread(JSON_SUCCESS, url);
		onloadThread.start();
		showDialog();//显示加载框
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
				Info_sub_root_lin.removeAllViews();
			case LOAD_MORE:
			case JSON_SUCCESS:
				AnalyzeJson analyzeJson = new AnalyzeJson(msg.obj.toString());
				List<HashMap<String, String>> list = analyzeJson.GetMessageCenter();
				if(list == null || list.size() <= 0)
				{
					Toast.makeText(InformationActivity.this, "没有更多消息了！", Toast.LENGTH_SHORT).show();
					break;
				}
				addList(list);
				loadImage(list);
				break;
			
			}
			super.handleMessage(msg);
			progressDialog.dismiss();
			InfoBodyScrollView.onRefreshComplete();
			if(msg.what == MSG_REFRESH)
			{
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				InfoBodyScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			}
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
				if(check.isNetworkConnected(InformationActivity.this) || check.OpenNetwork(InformationActivity.this))
				{
					GetHtml GH = new GetHtml();
					String JsonStr = GH.GetJsonByUrl(URL);
					mHandler.obtainMessage(MSG, JsonStr).sendToTarget();
				}
			case IMG_SUCCESS:
				if(check.isNetworkConnected(InformationActivity.this) || check.OpenNetwork(InformationActivity.this))
				{
					Bitmap bitmap = GetImage.returnBitMap(URL);
					imageHandler.obtainMessage(MSG, Item, Item, bitmap).sendToTarget();
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
	 * 根据HashMap创建列表项数据
	 * @param map 哈希表
	 * @param Item 根据在list中的位置确定，要作为每个列表的id
	 */
	private void addView(HashMap<String, String> map, int Item)
	{
		int itemNum = 7;
		//外侧LinearLayout
		LinearLayout out = new LinearLayout(InformationActivity.this);
		out.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams outParams = new LayoutParams(LayoutParams.MATCH_PARENT, Height / itemNum);
		out.setLayoutParams(outParams);
		Info_sub_root_lin.addView(out);
		//ImageView
		ImageView logo = new ImageView(InformationActivity.this);
		logo.setId(Item);
		LinearLayout.LayoutParams logoparams = new LayoutParams((int)(Height / itemNum * 0.8), (int)(Height / itemNum * 0.8));
		logoparams.gravity = Gravity.CENTER_VERTICAL;
		logoparams.leftMargin = (int)(Height / itemNum * 0.1);
//		logoparams.topMargin = (int)(Height / itemNum * 0.1);
		logoparams.rightMargin = (int)(Height / itemNum * 0.1);
		logo.setLayoutParams(logoparams);
		logo.setImageResource(R.drawable.main_logo);
	
		out.addView(logo);
		
		
		//内侧LinearLayout
		int innerHeight = (int)(Height / itemNum * 0.8);
		int innerWidth = Width - 20 - (int)(Height / itemNum);
		LinearLayout inner = new LinearLayout(InformationActivity.this);
		inner.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams innerParams = new LayoutParams(innerWidth, LayoutParams.WRAP_CONTENT);
		inner.setLayoutParams(innerParams);
		//内侧左侧LinearLayout
		LinearLayout innerLeft = new LinearLayout(InformationActivity.this);
		innerLeft.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams innerLeftParams = new LayoutParams((int)(innerWidth * 0.8), LayoutParams.WRAP_CONTENT);
		innerLeft.setLayoutParams(innerLeftParams);
		
		
		//上面的TextView
		TextView TXTop = new TextView(InformationActivity.this);
		LinearLayout.LayoutParams TXTopParams = new LayoutParams(LayoutParams.MATCH_PARENT, innerHeight / 2);
		TXTop.setLayoutParams(TXTopParams);
		TXTop.setText(map.get("realname"));
		TXTop.setPadding(5, 0, 0, 0);
		TXTop.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 20));
		TXTop.setGravity(Gravity.CENTER_VERTICAL);
		innerLeft.addView(TXTop);

		
		//下面的TextView
		TextView TXBottom = new TextView(InformationActivity.this);
		LinearLayout.LayoutParams TXBottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, innerHeight / 2);
		TXBottom.setLayoutParams(TXBottomParams);
		TXBottom.setText(map.get("lastcontent"));
		TXBottom.setPadding(5, 0, 0, 0);
		TXBottom.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width) - 3);
		TXBottom.setGravity(Gravity.CENTER_VERTICAL);
		innerLeft.addView(TXBottom);
		inner.addView(innerLeft);
		
		
		//右面的TextView
		TextView TXMiddle = new TextView(InformationActivity.this);
		LinearLayout.LayoutParams TXMiddleParams = new LayoutParams((int)(innerWidth * 0.2), LayoutParams.MATCH_PARENT);
		TXMiddle.setGravity(Gravity.CENTER_VERTICAL);
		TXMiddle.setText(map.get("createdate"));
		TXMiddle.setLayoutParams(TXMiddleParams);
		inner.addView(TXMiddle);
		
		out.addView(inner);
		
		//下面设置事件
		out.setOnTouchListener(new myOnTouchListenerChangeBackground());
		final String type = map.get("type");
		//这是系统消息，需要跳转到系统消息
		out.setOnClickListener(new OnClickListener() {
				
				@Override
			public void onClick(View arg0) {
				if(type.equals("9"))
				{
					Intent intent = new Intent(InformationActivity.this, SystemInfoActivity.class);
					intent.putExtra("Token", Token);
					startActivity(intent);
				}
				//Toast.makeText(InformationActivity.this, type, Toast.LENGTH_SHORT).show();
			}
		});
		
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
			loadImageItem(avatar, Item);
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
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(imageThread);
		mHandler.removeCallbacks(onloadThread);
		super.onStop();
	}
	private void showDialog()
	{
		progressDialog = myProgressDialog.createDialog(InformationActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setOnKeyListener(new myOnKeyListener());
		progressDialog.setMessage("拼命获取数据中...");
		progressDialog.show();
	}

}
