package com.example.tencent;

import android.app.Activity;
import android.content.SharedPreferences;

public class CheckOnlineState{
	/**
	 * 判断登录信息是否存在
	 * @param OnlineInfo
	 * @return
	 */
	public static Boolean IsOnline(SharedPreferences OnlineInfo)
	{
		if(OnlineInfo == null)
		{
			return false;
		}
		String uid = OnlineInfo.getString("UID", "");
		String Token = OnlineInfo.getString("Token", "");
		if(uid.equals("") || Token.equals(""))
		{
			return false;
		}
		return true;
	}
}
