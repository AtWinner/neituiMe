package com.example.neituime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetDataSource;
import com.example.adapter.GridViewAdapter;
import com.example.neituime.R.string;
import com.example.network.CheckNetwork;
import com.example.network.DocumentsSelect;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.AnalyzeJson;
import com.example.view.GridViewItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tencent.tauth.Tencent;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.Image;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	static final int MENU_SET_MODE = 0;
	private static final int MSG_SUCCESS = 0;// 获取成功的标识
	private static final int MSG_FAILURE = 1;// 获取失败的标识
	private static final int REFRESH = 2;
	private static final int LOADMORE = 3;
	private static final int IMG_SUCCESS = 4;
	private static final int MSG_GETUID = 5;// 通过第三方登录之后获取uid
	
	private Tencent mTencent;
	private static final String AppID = "101016468";
	
	private ImageButton MainQuit;
	private ImageButton imageMainAcitvityLogo;
	private ImageButton MainActivityUserCenter;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private SimpleAdapter sa;
	private HorizontalScrollView scrollView;
	private Spinner CitySpinner;
	private int Width;//屏幕宽
	private int Height;//屏幕高
	private SharedPreferences ButtonInfo;
	private ImageButton setAlways;
	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	private ProgressDialog progressDialog = null;
	private int point = 0;
	private String MainURL;
	private String Keyword;
	private String Kcity;
	private int Page;
	private long mExitTime;
	
	private Boolean IsFirst = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		//结束
		init();
		SetParams();
		CreateButton();
		BindEvent();
		ForGridView();

	}
	
	private void init()
	{
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		imageMainAcitvityLogo = (ImageButton)findViewById(R.id.imageMainAcitvityLogo);
		scrollView = (HorizontalScrollView)findViewById(R.id.scrollView);
		CitySpinner = (Spinner)findViewById(R.id.city);
		ButtonInfo = getSharedPreferences("ButtonInfo", MODE_PRIVATE);
		setAlways = (ImageButton)findViewById(R.id.setAlways);
		MainQuit = (ImageButton)findViewById(R.id.MainQuit);
		MainActivityUserCenter = (ImageButton)findViewById(R.id.MainActivityUserCenter);
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		mListItems = new LinkedList<String>();
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		MainURL = "http://www.neitui.me/?name=devapi&handle=jobs&jsonp=1&version=1.0.4&itemnum=20";//主页的URL
		Keyword = "";
		Kcity = "全国";
		Page = 1;
	}
	private void SetParams()
	{
		int ViewHeight = Height / 13;
		int ViewWidth = ViewHeight * 13 / 9;
		LinearLayout.LayoutParams LogoParams = new LinearLayout.LayoutParams(ViewWidth, ViewHeight);//Set the height and width for Logo.13:9
		imageMainAcitvityLogo.setLayoutParams(LogoParams);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(ViewHeight));
		scrollView.setLayoutParams(scrollParams);
		LinearLayout.LayoutParams setAlwaysparams = new LayoutParams(ViewHeight , ViewHeight);
		setAlwaysparams.setMargins(5, 0, 0, 5);
		setAlways.setLayoutParams(setAlwaysparams);
		String[] items = getResources().getStringArray(R.array.cities);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
		CitySpinner.setAdapter(adapter);
		int GridViewHeight = (int)(Height * 10.5) / 13;
		RelativeLayout.LayoutParams GridViewParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, GridViewHeight);
		mPullRefreshGridView.setLayoutParams(GridViewParams);
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams QuitParams = new LayoutParams(width, height);
		MainQuit.setLayoutParams(QuitParams);
		LinearLayout.LayoutParams userCenterParams = (LinearLayout.LayoutParams) MainActivityUserCenter.getLayoutParams();
		userCenterParams.width=ViewHeight;
		userCenterParams.height=ViewHeight;
		MainActivityUserCenter.setLayoutParams(userCenterParams);
	}
	private void BindEvent()
	{
		CitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Kcity = GetDataSource.GetCity(arg2);
				if(IsFirst)
				{
					Page = 1;
					CheckNetwork check = new CheckNetwork();
					if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
					{
						progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命数据获取中...", true);
						MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
						m.start();
					}
				}	
				IsFirst = true;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		setAlways.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, ChooseBtnActivity.class);
				// TODO 加入切换模式
				startActivityForResult(intent, 100);
				overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
			}
		});
		imageMainAcitvityLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				progressDialog.dismiss();
				 if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Object mHelperUtils;
                     Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
                     finish();
                     System.exit(0);//会将进程完全杀死
             }
			}
		});
		MainActivityUserCenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTencent = Tencent.createInstance(AppID, MainActivity.this);
				if(mTencent.isSessionValid() && mTencent.getOpenId() != null) 
				{//已使用qq登录
					String PostStr = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getauth&type=qq&authkey=dc94e7adc147d381e26e74b63434b132&";
					String OpenId = mTencent.getOpenId();
					PostStr += ("otherid=" + OpenId);
					MThread myThread = new MThread(PostStr, MSG_GETUID);
					myThread.start();
				}
				else if(false)
				{//这里放新浪的
					
				}
				else
				{
					Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
					startActivityForResult(loginIntent, 0);
					overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
				}
			}
		});
	}
	/**
	 * 处理关于GridView的相关事务
	 */
	private void ForGridView()
	{
		CheckNetwork check = new CheckNetwork();
		if(check.isNetworkConnected(this) || check.OpenNetwork(this))
		{
			progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命获取数据中...", true);
			Kcity = "全国";
			Keyword = "";
			Page = 1;
			MThread mythread = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
			mythread.start();

		}
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			//去工作详细页面
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> mymap = (HashMap<String,Object>)mGridView.getItemAtPosition(arg2);
				String DetailUrl = "http://www.neitui.me/mobile/detail/id=" + mymap.get("id").toString() + ".html";//链接
				Intent intent = new Intent(MainActivity.this, JobDetailActivity.class);
				intent.putExtra("URL", DetailUrl);
				intent.putExtra("position", mymap.get("positionfull").toString());
				intent.putExtra("department", mymap.get("departmentfull").toString());
				intent.putExtra("cmail", mymap.get("cmail").toString());
				intent.putExtra("createdate", mymap.get("createdate").toString());
				intent.putExtra("realname", mymap.get("realname").toString());
				intent.putExtra("avatar", mymap.get("avatar").toString());
				intent.putExtra("salary", mymap.get("salary").toString());
				intent.putExtra("city", mymap.get("city").toString());
				startActivity(intent);
				overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
			}
		});
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) 
			{
				CheckNetwork check = new CheckNetwork();
				if(check.isNetworkConnected(getApplicationContext()) || check.OpenNetwork(MainActivity.this))
				{
					new GetRefreshDataTask().execute();
				}
				else
					mPullRefreshGridView.onRefreshComplete();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				CheckNetwork check = new CheckNetwork();
				if(check.isNetworkConnected(getApplicationContext()) || check.OpenNetwork(MainActivity.this))
				{
					new GetMoreDataTask().execute();
				}
				else
					mPullRefreshGridView.onRefreshComplete();
				
				
			}		
		});
	}
	private class GetMoreDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected void onPostExecute(String[] result) {
			//Kcity = GetDataSource.GetCity(CitySpinner.getSelectedItemId());
			CheckNetwork check = new CheckNetwork();
			if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
			{
				Page++;
				MThread m = new MThread(GetUrl(Kcity, Keyword, Page), LOADMORE);
				m.start();
			}
			super.onPostExecute(result);
		}
		
	}
	private class GetRefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			//Kcity = GetDataSource.GetCity(CitySpinner.getSelectedItemId());
			CheckNetwork check = new CheckNetwork();
			if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
			{
				Page = 1;
				MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
				m.start();
			}
			super.onPostExecute(result);
		}
	}
	/**
	 * 在MainActivity创建底部Button
	 * @param ButtonLinear
	 * @param context
	 */
	private void myButton(final LinearLayout ButtonLinear, final Context context, String btnText)
	{
		Button btnAndroid = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.setMargins(5, 0, 0, 5);
		btnAndroid.setText(btnText);
		btnAndroid.setTextSize(AdjustPageLayout.AdjustButtonTextSize(Width, Height));//设置字体大小，是试出来的
		btnAndroid.setBackgroundResource(R.color.white);//背景默认为白色
		btnAndroid.setTextColor(Color.rgb(0, 0, 0));//字体默认为黑色
		if(btnText.equals("全部职位"))
		{//初始化显示全部为已选择
			btnAndroid.setTextColor(Color.rgb(31, 102, 146));
		}
		btnAndroid.setLayoutParams(params);
		btnAndroid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for(int i=0; i<ButtonLinear.getChildCount(); i++)
				{
					View v=ButtonLinear.getChildAt(i);
					if(v instanceof Button)
					{
						((Button)v).setTextColor(Color.BLACK);
						//((Button)v).setBackground(null);//去掉边框
					}
				}
				Button thisBtn = (Button)arg0;
				thisBtn.setTextColor(Color.rgb(31, 102, 146));
				//thisBtn.setBackgroundResource(R.drawable.button_border);//加边框
				CheckNetwork check = new CheckNetwork();
				if(thisBtn.getText().toString().equals("全部职位"))
				{
					Keyword = "";
				}
				else
				{
					Keyword = thisBtn.getText().toString();
				}
				if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
				{
					progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命获取数据中...", true);
					Page = 1;
					MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
					m.start();
				}
			}
		});
		ButtonLinear.addView(btnAndroid);
	}
	/**
	 * 在底部ScrollView中添加Button
	 */
	private void CreateButton()
	{
		LinearLayout buttonLinear = (LinearLayout)findViewById(R.id.ButtonLinearInside);
		buttonLinear.removeAllViews();
		Map<String, ?> buttonALL = ButtonInfo.getAll();
		myButton(buttonLinear, MainActivity.this, "全部职位");
		for(String name : buttonALL.keySet())
		{
			myButton(buttonLinear, MainActivity.this, name);
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 2)
		{//2表示点击的搜索按钮
			Toast.makeText(MainActivity.this, data.getStringExtra("Value"), Toast.LENGTH_SHORT).show();
			Keyword = data.getStringExtra("Value");
			progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命获取数据中...", true);
			Kcity = "全国";
			Page = 1;
			MThread mythread = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
			mythread.start();
			
			
		}
		if(resultCode == 1)
		{//监听到修改过设置信息
			ButtonInfo = getSharedPreferences("ButtonInfo", MODE_PRIVATE);//需要重新获取一次设置信息
			CreateButton();
		}
//		else
//		{
//			Toast.makeText(getApplicationContext(), "没改过", Toast.LENGTH_SHORT).show();
//		}
	}
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem setModeItem = menu.findItem(MENU_SET_MODE);
		setModeItem.setTitle(mPullRefreshGridView.getMode() == Mode.BOTH ? "Change to MODE_PULL_FROM_START"
				: "Change to MODE_PULL_BOTH");

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SET_MODE:
				mPullRefreshGridView
						.setMode(mPullRefreshGridView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START
								: Mode.BOTH);
				break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 progressDialog.dismiss();
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Object mHelperUtils;
                     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
                     finish();
                     System.exit(0);//会将进程完全杀死
             }
             return true;
     }
		return super.onKeyDown(keyCode, event);
	}

	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
	private Handler mHandler = new Handler()
	{//主要负责的是对反馈回来的数据进行处理，并展现在GridView中
		ArrayList<HashMap<String, Object>> al;
		@Override
		public void handleMessage(Message msg) 
		{
			
			String JsonStr = (String)msg.obj;
			GridViewItem item = new GridViewItem(JsonStr); 
			//Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
			//DocumentsSelect DS = new DocumentsSelect((Document) msg.obj);
			//Toast.makeText(getApplicationContext(), DS.GetList().toString(), Toast.LENGTH_SHORT).show();
			switch(msg.what)
			{
			case REFRESH:
				al = item.GetGridViewItemsByJson();
				Toast.makeText(getApplicationContext(), al.size()+"", Toast.LENGTH_SHORT).show();
				mGridView.setAdapter(new GridViewAdapter(MainActivity.this, al, R.layout.gridlist, Width));
				mGridView.setStackFromBottom(false);//不要定位到GridView底部
				break;
			case LOADMORE:
				point = al.size() - 1;//保存刷新之前的地步
				al.addAll(item.GetGridViewItemsByJson());
				Toast.makeText(getApplicationContext(), al.size()+"", Toast.LENGTH_SHORT).show();
				mGridView.setAdapter(new GridViewAdapter(MainActivity.this, al, R.layout.gridlist, Width));
				mGridView.setStackFromBottom(true);
				mGridView.setSelection(point);//将Selection定位到GridView底部
				break;
			case MSG_GETUID:
				String userInfo = (String)msg.obj;
				AnalyzeJson aj = new AnalyzeJson(userInfo);
				HashMap<String, String> userMap = aj.GetUserInfoByJson();
				if(userMap.get("message").equals("ok") && userMap.get("className").equals("success") && !userMap.get("uid").equals("0"))
				{
						Intent userIntent = new Intent(MainActivity.this, UserCenterActivity.class);
						userIntent.putExtra("LoginStyle", "Tencent");
						userIntent.putExtra("Token", userMap.get("token"));
						startActivity(userIntent);
				}
				else if(userMap.get("uid").equals("0"))
				{
					Toast.makeText(MainActivity.this, "没有找到您的简历，请到网页版完善", Toast.LENGTH_SHORT).show();
				}
				break;
		
			}
			progressDialog.dismiss();
			mPullRefreshGridView.onRefreshComplete();
		}
	};
	private String GetUrl(String kcity, String keyword, int page)
	{
		return MainURL + "&city=" + kcity + "&keyword=" +keyword + "&page=" + page; 
	}
	private class MThread extends Thread
	{
		private String URL;
		private int MISSION;
		public MThread(String Url, int Mission)
		{
			URL = Url;
			MISSION = Mission;
			Log.e("1",MISSION+"");
		}
		@Override
		public void run() {
			if(MISSION == MSG_GETUID)
			{
				GetHtml gh = new GetHtml();
				String jsonStr = gh.GetJsonByUrl(URL);
				mHandler.obtainMessage(MISSION, gh.GetJsonByUrl(URL)).sendToTarget();
			}
			else
			{
				try
				{
					GetHtml gh = new GetHtml();
					mHandler.obtainMessage(MISSION, gh.GetJsonByUrl(URL)).sendToTarget();//如果成功需要返回依着在GridView中显示的结果集
				}
				catch(Exception e)
				{
					mHandler.obtainMessage(MSG_FAILURE, e.getMessage()).sendToTarget();
				}
			}
			super.run();
		}
	}
}
