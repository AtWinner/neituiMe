package com.example.view;

import com.example.adapter.AdjustPageLayout;
import com.example.neituime.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AddLayoutView {
	public static String GetInnerText(String originalStr)
	{
		String returnStr = originalStr;
		int length = originalStr.length();
		if(length == 2)
		{
			returnStr = originalStr.substring(0,1) + "    "+ originalStr.substring(1,2);
		}
		else if(length == 3)
		{
			returnStr = originalStr.substring(0,1) + "  "+ originalStr.substring(1,2)+ "  "+ originalStr.substring(2,3);
		}
		else if(length == 4)
		{
			returnStr = originalStr.substring(0,1) + " "+ originalStr.substring(1,2)+ " "+ originalStr.substring(2,3) + " " +originalStr.substring(3,4);
		}
		return returnStr;
	}

}
