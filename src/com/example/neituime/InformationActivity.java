package com.example.neituime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class InformationActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_activity);
	}

}
