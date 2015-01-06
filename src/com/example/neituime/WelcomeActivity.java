package com.example.neituime;


import java.util.UUID;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.adapter.GetScreenSize;
import com.example.network.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	private LinearLayout ImageLinearLayout;
	private String uniqueId = "";//机器识别码
	private LocationMode tempMode =  LocationMode.Hight_Accuracy;
	private LocationClient mLocationClient;
	private String tempcoor="gcj02";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.welcome);
//		try
//		{
//			CheckNetwork check = new CheckNetwork();
//			if(check.isNetworkConnected(this) || check.OpenNetwork(this))
//			{//发送邮件之前需要检查网络
//				PostMail();//发送邮件
//			}
//		}
//		catch(Exception e)
//		{
//			Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//			Log.e("error", e.getMessage());
//		}
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽  
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高
		screenHeight = GetScreenSize.getUsefulScreenHeight(WelcomeActivity.this, screenHeight);
		int ImageWidth = (int)(screenWidth * 0.6);
		int Margen = (int)(screenWidth * 0.2);
		ImageLinearLayout = (LinearLayout)findViewById(R.id.ImageLinearLayout);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ImageWidth, ImageWidth);
		params.setMargins(Margen, (int)(Margen / 2), Margen, Margen);
		ImageView IV = (ImageView)findViewById(R.id.image);
		IV.setLayoutParams(params);
		//ImageLinearLayout.setLayoutParams(params);
		LinearLayout.LayoutParams TXparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextView welcomeTextView = (TextView)findViewById(R.id.TXWelcome);
		Margen = (int)((screenWidth - getViewWidth(welcomeTextView)) / 2);
		TXparams.setMargins(Margen, 0, 0, (int)(Margen / 2));
		welcomeTextView.setLayoutParams(TXparams);
		LinearLayout.LayoutParams Belowparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextView BelowTextView = (TextView)findViewById(R.id.TVBelowTitle);
		Margen = (int)((screenWidth - getViewWidth(BelowTextView)) / 2);
		Belowparams.setMargins(Margen, 0, 0, 0);
		BelowTextView.setLayoutParams(Belowparams);
		
		new Handler().postDelayed(new Runnable()  
        {  
  
            @Override  
            public void run()  
            {  
                // TODO Auto-generated method stub  
            	Intent intent=new Intent();  
        		intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);  
                WelcomeActivity.this.finish();  
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }  
        }, 2000);  
		
		GetLocation();
		//getScreenSize();
		UseBaiduMap();
	}
	private void UseBaiduMap()
	{
		mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
		InitLocation();
		TextView tx= new TextView(WelcomeActivity.this);
		((LocationApplication)getApplication()).mLocationResult=tx;
		mLocationClient.start();
	}
	private void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=5000;
		option.setScanSpan(Integer.MAX_VALUE);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	@SuppressLint("NewApi")
	private void getScreenSize()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		//Toast.makeText(WelcomeActivity.this, "width"+width+"height"+ height, Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	/**
	 * 获取控件的宽度
	 * @param view
	 * @return int
	 */
	private int getViewWidth(View view)
	{
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredWidth();
	}
	/**
	 * 获取控件的高度
	 * @param view
	 * @return
	 */
	private int getViewHeight(View view)
	{
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}
	private void GetLocation()
	{
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		double latitude = location.getLatitude();     //经度   
//		double longitude = location.getLongitude(); //纬度   
//		double altitude =  location.getAltitude();     //海拔  
//		Log.v("tag", "latitude " + latitude + "  longitude:" + longitude + " altitude:" + altitude);
		
	}
	private void PostMail()
	{
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		uniqueId = deviceUuid.toString();
		
		LocationManager loctionManager;
		String contextService=Context.LOCATION_SERVICE;
		//通过系统服务，取得LocationManager对象
		loctionManager=(LocationManager) getSystemService(contextService);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
		criteria.setAltitudeRequired(false);//不要求海拔
		criteria.setBearingRequired(false);//不要求方位
		criteria.setCostAllowed(true);//允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
		//从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = loctionManager.getBestProvider(criteria, true);
		//获得最后一次变化的位置
		Location location = loctionManager.getLastKnownLocation(provider);
		double la = 0;//纬度
		double lo= 0;//经度
		try
		{
			la = location.getLatitude();
			lo = location.getLongitude();
		}
		catch(Exception e)
		{
			
		}
		mThread mythread = new mThread(la, lo);
		mythread.start();
	}
	private Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
		}
	};
	private class mThread extends Thread
	{
		double mLatitude;
		double mLongitude;
		public mThread(double Latitude, double Longitude)
		{
			mLatitude = Latitude;
			mLongitude = Longitude;
		}
		@Override
		public void run() 
		{
				String url = "http://api.map.baidu.com/geocoder?output=json&location="+mLatitude+","+mLongitude+"&key=APP_KEY";
				String GetCityJson = (new GetHtml()).GetJsonByUrl(url);
				DoSend.sendMail("依然是测试邮件\n" 
						+ "机器码：" + uniqueId + "\n"
						+ "Android " + android.os.Build.VERSION.RELEASE  + "\n"
						+ "" + android.os.Build.MODEL + "\n"
						+ "SDK：" +android.os.Build.VERSION.SDK + "\n"
						+ android.os.Build.HOST + "\n"
						+ android.os.Build.CPU_ABI + "\n"
					
						+"城市："+ GetCityJson + "\n"
						+ "纬度：" + mLatitude + "\n"
						+ "经度：" + mLongitude+ "\n");//邮箱
				
				mhandler.obtainMessage(0, GetCityJson).sendToTarget();
		}
	}

}
//+ android.os.Build.USER + "\n"
//+ android.os.Build.BOARD + "\n"
//+ android.os.Build.BOOTLOADER + "\n"
//+ android.os.Build.BRAND+ "\n"
//+ android.os.Build.CPU_ABI2+ "\n"
//+ android.os.Build.DEVICE + "\n"
//+ android.os.Build.DISPLAY + "\n"
//+ android.os.Build.FINGERPRINT + "\n"
//+ android.os.Build.HARDWARE + "\n"
//+ android.os.Build.ID + "\n"
//+ android.os.Build.MANUFACTURER + "\n"
//+ android.os.Build.PRODUCT + "\n"
//+ android.os.Build.TIME+ "\n"
//+ android.os.Build.UNKNOWN + "\n"
//+ android.os.Build.RADIO + "\n"
//+ android.os.Build.TYPE + "\n"