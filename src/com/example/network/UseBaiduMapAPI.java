package com.example.network;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class UseBaiduMapAPI {
	private LocationMode tempMode =  LocationMode.Hight_Accuracy;
	private LocationClient mLocationClient;
	private String tempcoor="gcj02";
	public UseBaiduMapAPI()
	{
		
	}
	public void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

}
