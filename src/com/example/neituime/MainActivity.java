package com.example.neituime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;








import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.GridViewAdapter;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.model.neituiValue;
import com.example.network.CheckNetwork;
import com.example.network.GetHtml;
import com.example.tencent.CheckOnlineState;
import com.example.view.AddLayoutView;
import com.example.view.AnalyzeJson;
import com.example.view.GridViewItem;
import com.exmple.data.SetCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {
	static final int MENU_SET_MODE = 0;
	private static final int MSG_SUCCESS = 0;// 获取成功的标识
	private static final int MSG_FAILURE = 1;// 获取失败的标识
	private static final int REFRESH = 2;
	private static final int LOADMORE = 3;
	private static final int IMG_SUCCESS = 4;
	private static final int MSG_GETUID = 5;// 通过第三方登录之后获取uid
	
	private static final int ResponseNumber = 1;//跳转到UserCenter或Login
	private Tencent mTencent;
	private static final String AppID = "101016468";
	
	private ImageButton MainQuit;
	private ImageButton imageMainAcitvityLogo;
	private ImageButton MainActivityUserCenter;
	private ImageButton CityArrow;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private SimpleAdapter sa;
	private HorizontalScrollView scrollView;
	private Button btnBity;
	private TabHost cityTabHost;
	private Button SelectCommon;
	private Button SelectAll;
	
	//private Spinner CitySpinner;
	private int Width;//屏幕宽
	private int Height;//屏幕高
	private SharedPreferences ButtonInfo;
	private ImageButton setAlways;
	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	private myProgressDialog progressDialog = null;
	private int point = 0;
	private String MainURL;
	private String Keyword;
	private String Kcity;
	private int Page;
	private long mExitTime;
	
	private Boolean IsFirst = false;
	private RelativeLayout bodyCity;	
	private LinearLayout commonCity;
	private LinearLayout commonCityInner;
	private LinearLayout AllCity;
	private LinearLayout AllCityInner;
	private RelativeLayout.LayoutParams commonCityParams;
	private RelativeLayout.LayoutParams AllCityParams;
	private Boolean isCommonCity;
	
	/**
	 * 记录显示在左侧的city
	 */
	private int cityLeft;
	/**
	 * 记录隐藏在右侧的city
	 */
	private int cityRight;
	
	/**
	 * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
	 */
	public static final int SNAP_VELOCITY = 200;
	/**
	 * 屏幕宽度值。
	 */
	private int screenWidth;
	/**
	 * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
	 */
	private int leftEdge;
	/**
	 * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	 */
	private int rightEdge = 0;
	/**
	 * menu完全显示时，留给content的宽度值。
	 */
	private int menuPadding = 200;
	/**
	 * 主内容的布局。
	 */
	private View content;
	/**
	 * menu的布局。
	 */
	private View menu;
	/**
	 * menu布局的参数，通过此参数来更改leftMargin的值。
	 */
	private LinearLayout.LayoutParams menuParams;
	/**
	 * content布局的参数，通过此参数来更改params。
	 */
	private LinearLayout.LayoutParams contentParams;
	/**
	 * 记录手指按下时的横坐标。
	 */
	private float xDown;
	/**
	 * 记录手指移动时的横坐标。
	 */
	private float xMove;
	/**
	 * 记录手机抬起时的横坐标。
	 */
	private float xUp;
	/**
	 * city当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
	 */
	private boolean isCityVisible;
	/**
	 * 用于计算手指滑动的速度。
	 */
	private VelocityTracker mVelocityTracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		//结束
		init();
		initMenuValues();
		SetParams();
		CreateButton();
		CreateCityButton();
		BindEvent();
		ForGridView();
		Toast.makeText(MainActivity.this, GetScreenSize.getStatusBarHeight(MainActivity.this) +"", Toast.LENGTH_SHORT).show();
	}
	
	private void init()
	{
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(MainActivity.this, Height);
		imageMainAcitvityLogo = (ImageButton)findViewById(R.id.imageMainAcitvityLogo);
		scrollView = (HorizontalScrollView)findViewById(R.id.scrollView);
		//CitySpinner = (Spinner)findViewById(R.id.city);
		ButtonInfo = getSharedPreferences("ButtonInfo", MODE_PRIVATE);
		setAlways = (ImageButton)findViewById(R.id.setAlways);
		MainQuit = (ImageButton)findViewById(R.id.MainQuit);
		CityArrow = (ImageButton)findViewById(R.id.CityArrow);
		MainActivityUserCenter = (ImageButton)findViewById(R.id.MainActivityUserCenter);
		btnBity = (Button)findViewById(R.id.btnBity);
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		mListItems = new LinkedList<String>();
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		MainURL = "http://www.neitui.me/?name=devapi&handle=jobs&jsonp=1&version=1.0.4&itemnum=20";//主页的URL
		Keyword = "";
		Kcity = "全国";
		Page = 1;
		isCityVisible = false;
		SelectCommon = (Button)findViewById(R.id.SelectCommon);
		SelectAll = (Button)findViewById(R.id.SelectAll);
		commonCity = (LinearLayout)findViewById(R.id.commonCity);
		commonCityInner = (LinearLayout)findViewById(R.id.commonCityInner);
		AllCity = (LinearLayout)findViewById(R.id.AllCity);
		AllCityInner = (LinearLayout)findViewById(R.id.AllCityInner);
		bodyCity = (RelativeLayout)findViewById(R.id.bodyCity);
		isCommonCity = true;
	}
	/**
	 * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
	 */
	private void initMenuValues()
	{
		menuPadding = Width * 3 / 10;
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menuCity);
		menuParams = (LinearLayout.LayoutParams)menu.getLayoutParams();
		// 将menu的宽度设置为屏幕宽度减去menuPadding
		menuParams.width = Width - menuPadding;
		leftEdge =- menuPadding;
		rightEdge = 0;  
		contentParams = (LinearLayout.LayoutParams)content.getLayoutParams();
		contentParams.leftMargin = 0;
		contentParams.width = Width;
		commonCityParams = (RelativeLayout.LayoutParams)commonCity.getLayoutParams();
		AllCityParams = (RelativeLayout.LayoutParams)AllCity.getLayoutParams();
		//默认显示commonCity，因此初始化为commonCity的leftMargin为0，AllCity的为menuPadding
		AllCityParams.leftMargin = menuPadding;
		commonCityParams.leftMargin = 0;
		cityLeft = 0;
		cityRight = menuPadding;
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
		//CitySpinner.setAdapter(adapter);
		int GridViewHeight = (int)(Height * 11) / 13;
		RelativeLayout.LayoutParams GridViewParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, GridViewHeight);
		mPullRefreshGridView.setLayoutParams(GridViewParams);
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams QuitParams = new LayoutParams(width, height);
		MainQuit.setLayoutParams(QuitParams);
		CityArrow.setLayoutParams(QuitParams);
		LinearLayout.LayoutParams userCenterParams = (LinearLayout.LayoutParams) MainActivityUserCenter.getLayoutParams();
		userCenterParams.width=ViewHeight;
		userCenterParams.height=ViewHeight;
		MainActivityUserCenter.setLayoutParams(userCenterParams);
		btnBity.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
		
		RelativeLayout.LayoutParams cityScrollParams = (RelativeLayout.LayoutParams)bodyCity.getLayoutParams();
		cityScrollParams.height = (int)(Height * 12 / 13);
		bodyCity.setLayoutParams(cityScrollParams);
		
		LinearLayout.LayoutParams cityBottomBtnParams = (LinearLayout.LayoutParams)SelectCommon.getLayoutParams();
		cityBottomBtnParams.height = Height / 13;
		cityBottomBtnParams.width = Width * 3 / 20;
		SelectCommon.setLayoutParams(cityBottomBtnParams);
		SelectCommon.setTextColor(Color.BLACK);
		SelectAll.setLayoutParams(cityBottomBtnParams);
	}
	private void BindEvent()
	{
		CityArrow.setOnClickListener(new MyListener());
		btnBity.setOnClickListener(new MyListener());
		setAlways.setOnClickListener(new MyListener());
		imageMainAcitvityLogo.setOnClickListener(new  MyListener());
		MainActivityUserCenter.setOnClickListener(new MyListener());
		SelectCommon.setOnClickListener(new MyListener());
		SelectAll.setOnClickListener(new MyListener());
	}
	
	private class MyListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.CityArrow:
			case R.id.btnBity:
				ClickCityArrow();
				break;
			case R.id.setAlways:
				ClicksetAlways();
				break;
			case R.id.imageMainAcitvityLogo:
				ClickimageMainAcitvityLogo();
				break;
			case R.id.MainActivityUserCenter:
				ClickMainActivityUserCenter();
				break;
			case R.id.SelectCommon:
				ClickSelectCommonCity();
				SelectCommon.setTextColor(Color.BLACK);
				SelectAll.setTextColor(Color.WHITE);
				break;
			case R.id.SelectAll:
				ClickSelectAllCity();
				SelectAll.setTextColor(Color.BLACK);
				SelectCommon.setTextColor(Color.WHITE);
				break;
			}
		}
	}

	/**
	 * 点击CityArrow触发的操作
	 */
	private void ClickCityArrow()
	{
		if(isCityVisible)
		{
			Log.e("", isCityVisible+"");
			scrollToContent();
		}
		else
		{
			Log.e("", isCityVisible+"");
			scrollToMenu();
		}
	}
	/**
	 * 点击setAlways触发的操作
	 */
	private void ClicksetAlways()
	{
		Intent intent = new Intent(MainActivity.this, ChooseBtnActivity.class);
		// TODO 加入切换模式
		startActivityForResult(intent, 100);
		overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
	}
	/**
	 * 点击imageMainAcitvityLogo触发的操作
	 */
	private void ClickimageMainAcitvityLogo()
	{
		if ((System.currentTimeMillis() - mExitTime) > 2000) 
		{
			Object mHelperUtils;
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
		} 
		else 
		{
            finish();
            System.exit(0);//会将进程完全杀死
		}
	}
	/**
	 * 点击MainActivityUserCenter触发的操作
	 */
	private void ClickMainActivityUserCenter()
	{
		SharedPreferences OnlineInfo = getSharedPreferences("OnlineInfo", 0);
		if(CheckOnlineState.IsOnline(OnlineInfo))
		{
			Toast.makeText(MainActivity.this, OnlineInfo.getString("LoginStyle", ""), Toast.LENGTH_SHORT).show();
			Intent userIntent = new Intent(MainActivity.this, UserCenterActivity.class);
			userIntent.putExtra("LoginStyle", OnlineInfo.getString("LoginStyle",""));
			userIntent.putExtra("Token", OnlineInfo.getString("Token",""));
			startActivity(userIntent);
			overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		}
//		mTencent = Tencent.createInstance(AppID, MainActivity.this);
//		if(mTencent.isSessionValid() && mTencent.getOpenId() != null) 
//		{//已使用qq登录
//			String PostStr = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getauth&type=qq&authkey=dc94e7adc147d381e26e74b63434b132&";
//			String OpenId = mTencent.getOpenId();
//			PostStr += ("otherid=" + OpenId);
//			MThread myThread = new MThread(PostStr, MSG_GETUID);
//			myThread.start();
//			showDialog();
//		}
//		else if(false)
//		{//这里放新浪的
//			
//		}
		else
		{
			Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
			loginIntent.putExtra("ResponseNumber", ResponseNumber);
			startActivityForResult(loginIntent, ResponseNumber);
			overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		}
	}
	/**
	 * 点击SelectCommon触发的操作
	 */
	private void ClickSelectCommonCity()
	{
		if(!isCommonCity)
		{//标记选择的是常见city
			new CityScrollTask().execute(-4);
		}
	}
	
	private void ClickSelectAllCity()
	{
		if(isCommonCity)
		{
			new CityScrollTask().execute(4);
		}
	}
	
	/**
	 * 处理关于GridView的相关事务
	 */
	private void ForGridView()
	{
		CheckNetwork check = new CheckNetwork();
		if(check.isNetworkConnected(this) || check.OpenNetwork(this))
		{
			showDialog();
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
				String DetailUrl = "http://www.neitui.me/index.php?name=mobile&handle=detail&id=" + mymap.get("id").toString() + "&code=" + SetCode.weChatCode();//链接
				Intent intent = new Intent(MainActivity.this, JobDetailActivity.class);
				intent.putExtra("URL", DetailUrl);
				intent.putExtra("id", mymap.get("id").toString());
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
					}
				}
				Button thisBtn = (Button)arg0;
				thisBtn.setTextColor(Color.rgb(31, 102, 146));
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
					showDialog();
					Page = 1;
					MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
					m.start();
				}
			}
		});
		ButtonLinear.addView(btnAndroid);
	} 
	/**
	 * 添加城市选择layout的标签
	 * @param linearLayout 要添加的layout
	 * @param innerText 显示的文字
	 */
	private void addCityLabel(LinearLayout linearLayout, String innerText)
	{
		TextView cityLabel = new TextView(MainActivity.this);
		LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(menuPadding, (int)(menuPadding / 2.4));
		cityLabel.setLayoutParams(labelParams);
		cityLabel.setText(innerText);
		cityLabel.setGravity(Gravity.CENTER);
		cityLabel.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));
		cityLabel.setTextColor(Color.rgb(249, 100, 10));//橘黄色
		linearLayout.addView(cityLabel);
	}
	/**
	 * 添加城市选择layout中的城市button
	 * @param linearLayout 要添加的布局
	 * @param context 添加的页面Activity
	 * @param innerText Button要显示文字
	 */
	private void addBtnCity(final LinearLayout linearLayout, final Context context, final String innerText)
	{
		Button btnCity = new Button(context);
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(menuPadding, (int)(menuPadding / 2.4));
		btnCity.setLayoutParams(buttonParams);
		btnCity.setText(AddLayoutView.GetInnerText(innerText));
		btnCity.setWidth(menuPadding);
		btnCity.setHeight((int)(menuPadding / 2.4));
		btnCity.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));
		btnCity.setTextColor(Color.WHITE);
		btnCity.setBackgroundResource(R.drawable.city_item_background);
//		CitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1,
//				int arg2, long arg3) {
//			Kcity = GetDataSource.GetCity(arg2);
//			if(IsFirst)
//			{
//				Page = 1;
//				CheckNetwork check = new CheckNetwork();
//				if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
//				{
//					progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命数据获取中...", true);
//					MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
//					m.start();
//				}
//			}	
//			IsFirst = true;
//		}
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			
//			
//		}
//	});
		btnCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Button thisBtn = (Button)arg0;
				View parent = (View) thisBtn.getParent();//找到这个button的父节点
				for(int i=0; i<((ViewGroup) parent).getChildCount(); i++)
				{
					View v=((ViewGroup) parent).getChildAt(i);
					if(v instanceof Button)
					{
						((Button)v).setTextColor(Color.WHITE);//全部变白
					}
				}
				thisBtn.setTextColor(Color.rgb(31, 102, 146));//再变蓝
				btnBity.setText(innerText);
				Page = 1;
				Kcity = innerText;
				CheckNetwork check = new CheckNetwork();
				if(check.isNetworkConnected(MainActivity.this) || check.OpenNetwork(MainActivity.this))
				{
					showDialog();
					MThread m = new MThread(GetUrl(Kcity, Keyword, Page), REFRESH);
					m.start();
				}
				scrollToContent();
			}
		});
		
		linearLayout.addView(btnCity);
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
	/**
	 * 在城市侧栏中添加button
	 */
	private void CreateCityButton()
	{
		for(String city : neituiValue.commonCity)
		{
			addBtnCity(commonCityInner, MainActivity.this, city);
		}
		addCityLabel(AllCityInner, "华北东北");
		for(String city : neituiValue.NorthCity)
		{//华北东北
			addBtnCity(AllCityInner, MainActivity.this, city);
		}
		addCityLabel(AllCityInner, "华东地区");
		for(String city : neituiValue.EastCity)
		{//华东
			addBtnCity(AllCityInner, MainActivity.this, city);
		}
		addCityLabel(AllCityInner, "华南地区");
		for(String city : neituiValue.SouthCity)
		{//华南
			addBtnCity(AllCityInner, MainActivity.this, city);
		}
		addCityLabel(AllCityInner, "中部西部");
		for(String city : neituiValue.MiddleAndWestCity)
		{//中西部
			addBtnCity(AllCityInner, MainActivity.this, city);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 2)
		{//2表示点击的搜索按钮
			Toast.makeText(MainActivity.this, data.getStringExtra("Value"), Toast.LENGTH_SHORT).show();
			Keyword = data.getStringExtra("Value");
			showDialog();
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
			if(msg.obj == null || msg.obj.equals(""))
			{//先判断请求得到的数据是否为空
				Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
				mPullRefreshGridView.onRefreshComplete();
				return;
			}
			String JsonStr = (String)msg.obj;
			GridViewItem item = new GridViewItem(JsonStr); 
			switch(msg.what)
			{
			case REFRESH:
				al = item.GetGridViewItemsByJson();
				Toast.makeText(getApplicationContext(), al.size()+"", Toast.LENGTH_SHORT).show();
				mGridView.setAdapter(new GridViewAdapter(MainActivity.this, al, R.layout.gridlist, Width));
				mGridView.setStackFromBottom(false);//不要定位到GridView底部
				break;
			case LOADMORE:
				point = al.size() - 1;//保存刷新之前的位置
				al.addAll(item.GetGridViewItemsByJson());
				Toast.makeText(getApplicationContext(), al.size()+"", Toast.LENGTH_SHORT).show();
				mGridView.setAdapter(new GridViewAdapter(MainActivity.this, al, R.layout.gridlist, Width));
				mGridView.setStackFromBottom(true);
				mGridView.setSelection(point);//将Selection定位到GridView底部
				break;
//			case MSG_GETUID:
//				String userInfo = (String)msg.obj;
//				AnalyzeJson aj = new AnalyzeJson(userInfo);
//				HashMap<String, String> userMap = aj.GetUserInfoByJson();
//				if(userMap.get("message").equals("ok") && userMap.get("className").equals("success") && !userMap.get("uid").equals("0"))
//				{
//						Intent userIntent = new Intent(MainActivity.this, UserCenterActivity.class);
//						userIntent.putExtra("LoginStyle", "Tencent");
//						userIntent.putExtra("Token", userMap.get("token"));
//						startActivity(userIntent);
//						overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
//				}
//				else if(userMap.get("uid").equals("0"))
//				{
//					Toast.makeText(MainActivity.this, "没有找到您的简历，请到网页版完善", Toast.LENGTH_SHORT).show();
//				}
//				break;
		
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
	
	//华丽丽的分割线************************************************************************************************************************************************************************
	//下面是控制MainActivity的滑动效果
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时，记录按下时的横坐标
			Log.e("ss", "sss");
			break;
		case MotionEvent.ACTION_MOVE:
			// 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
			
			break;
			
		case MotionEvent.ACTION_UP:
			// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
			Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
			break;
		}
		return false;
	}
	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 *            content界面的滑动事件
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	/**
	 * 将屏幕滚动到menu界面，滚动速度设定为5.原本是40,但是不细腻
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(-5);
	}

	/**
	 * 将屏幕滚动到content界面，滚动速度设定为-5.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(5);
	}
	/**
	 * 由于切换content和menu的操作
	 * @author Coder
	 *
	 */
	class ScrollTask extends AsyncTask<Integer, Integer, Integer> 
	{
		@Override
		protected Integer doInBackground(Integer... speed) 
		{
			int leftMargin = contentParams.leftMargin;
			Log.e("", leftMargin+"");
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
			while (true) 
			{
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) 
				{
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) 
				{
					leftMargin = leftEdge;
					break;
				}
				Log.e("", leftMargin+"");
				publishProgress(leftMargin);
				// 为了要有滚动效果产生，每次循环使线程睡眠2毫秒，这样肉眼才能够看到滚动动画。
				sleep(2);
			}
			if (speed[0] < 0) 
			{
				isCityVisible = true;
			} 
			else 
			{
				isCityVisible = false;
			}
			return leftMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			contentParams.leftMargin = leftMargin[0];
			content.setLayoutParams(contentParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			contentParams.leftMargin = leftMargin;
			content.setLayoutParams(contentParams);
		}
	}
	class CityScrollTask extends AsyncTask<Integer, Integer, Integer> 
	{
		protected Integer doInBackground(Integer... speed) 
		{
			int  leftMargin = commonCityParams.leftMargin;
			Log.e("", leftMargin+"");
			while(true)
			{
				leftMargin = leftMargin + speed[0];
				if (leftMargin < cityLeft) 
				{
					leftMargin = cityLeft;
					break;
				}
				if (leftMargin > cityRight) 
				{
					leftMargin = cityRight;
					break;
				}
				Log.e("", leftMargin+"");
				publishProgress(leftMargin);
				// 为了要有滚动效果产生，每次循环使线程睡眠2毫秒，这样肉眼才能够看到滚动动画。
				sleep(2);
			}
			if (speed[0] > 0) 
			{
				isCommonCity = false;
			} 
			else 
			{
				isCommonCity = true;
			}
			return leftMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			commonCityParams.leftMargin = leftMargin[0];
			AllCityParams.leftMargin = cityRight - leftMargin[0];
			commonCity.setLayoutParams(commonCityParams);
			AllCity.setLayoutParams(AllCityParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			commonCityParams.leftMargin = leftMargin;
			AllCityParams.leftMargin = cityRight - leftMargin;
			commonCity.setLayoutParams(commonCityParams);
			AllCity.setLayoutParams(AllCityParams);
		}
	}
	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void showDialog()
	{
//		progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "拼命获取数据中...", true);
//		progressDialog.setCancelable(false);
//		progressDialog.setOnKeyListener(new myOnKeyListener());
		if(progressDialog == null)
		{
			progressDialog = myProgressDialog.createDialog(MainActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new myOnKeyListener());
			progressDialog.setMessage("拼命获取数据中...");
		}
		progressDialog.show();		
		
	}
}
