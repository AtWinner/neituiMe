package com.example.neituime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SendNotesActivity extends Activity {
	
	private int Width;
	private int Height;
	
	private ImageButton SendNotesGetBack;
	private Button SendNotesbtnGetBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendnotes_activity);
		init();
		setParams();
		bindEvent();
	}
	private void init()
	{
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		
		SendNotesGetBack = (ImageButton)findViewById(R.id.SendNotesGetBack);
		SendNotesbtnGetBack = (Button)findViewById(R.id.SendNotesbtnGetBack);
	}
	private void setParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		SendNotesGetBack.setLayoutParams(params);
	}
	private void bindEvent()
	{
		
	}
}
