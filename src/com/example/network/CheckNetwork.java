package com.example.network;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class CheckNetwork {

	public boolean isNetworkConnected(Context context) 
	{
		if (context != null) 
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	} 
	public boolean OpenNetwork(final Context context)
	{
		Dialog dialog = new AlertDialog.Builder(context).setTitle("温馨提示！")
				.setMessage("需不需要小弟帮您打开网络捏？").setNegativeButton("开Wifi", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								OpenWifi(context);
								
							}
						}
				).setNeutralButton("开流量",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Open3G(context);
										
					}
				}).setPositiveButton("一边去", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).create();
		dialog.show();
		return isNetworkConnected(context);
	}
	private void OpenWifi(Context context)
	{//打开wifi
		if (context != null) 
		{
			WifiManager wifimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			wifimanager.setWifiEnabled(true);
		}
	}
	private void Open3G(Context context)
	{//打开流量
		if(context != null)
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			//ConnectivityManager类    
			Class<?> connectivityManagerClass = null;
			//ConnectivityManager类中的字段    
	        Field connectivityManagerField = null;  
	        //IConnectivityManager接口  
	        Class<?> iConnectivityManagerClass = null;  
	        //IConnectivityManager接口的对象  
	        Object iConnectivityManagerObject = null;  
	        //IConnectivityManager接口的对象的方法  
	        Method setMobileDataEnabledMethod = null; 
	        try {  
	            //取得ConnectivityManager类  
	            connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());  
	            //取得ConnectivityManager类中的字段mService  
	            connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");  
	            //取消访问私有字段的合法性检查   
	            //该方法来自java.lang.reflect.AccessibleObject  
	            connectivityManagerField.setAccessible(true);  
	              
	              
	            //实例化mService  
	            //该get()方法来自java.lang.reflect.Field  
	            //一定要注意该get()方法的参数:  
	            //它是mService所属类的对象  
	            //完整例子请参见:  
	            //http://blog.csdn.net/lfdfhl/article/details/13509839  
	            iConnectivityManagerObject = connectivityManagerField.get(connectivityManager);  
	            //得到mService所属接口的Class  
	            iConnectivityManagerClass = Class.forName(iConnectivityManagerObject.getClass().getName());  
	            //取得IConnectivityManager接口中的setMobileDataEnabled(boolean)方法  
	            //该方法来自java.lang.Class.getDeclaredMethod  
	            setMobileDataEnabledMethod =   
	            iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);  
	            //取消访问私有方法的合法性检查   
	            //该方法来自java.lang.reflect.AccessibleObject  
	            setMobileDataEnabledMethod.setAccessible(true);  
	            //调用setMobileDataEnabled方法  
	            setMobileDataEnabledMethod.invoke(iConnectivityManagerObject,true);  
	        } catch (ClassNotFoundException e) {     
	            e.printStackTrace();    
	        } catch (NoSuchFieldException e) {     
	            e.printStackTrace();    
	        } catch (SecurityException e) {     
	            e.printStackTrace();    
	        } catch (NoSuchMethodException e) {     
	            e.printStackTrace();    
	        } catch (IllegalArgumentException e) {     
	            e.printStackTrace();    
	        } catch (IllegalAccessException e) {     
	            e.printStackTrace();    
	        } catch (InvocationTargetException e) {     
	            e.printStackTrace();    
	        }   
	    }  
	  
		
	}
	
}
