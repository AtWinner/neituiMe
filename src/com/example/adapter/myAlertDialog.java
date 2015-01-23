package com.example.adapter;

import com.example.event.myOnTouchListenerChangeCityBackground;
import com.example.neituime.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class myAlertDialog 
{
	private Context context;
	private Dialog dialog;
	private LinearLayout DialogLinear;
	private LinearLayout.LayoutParams DialogLinearParams;
	private TextView DialogUserCenter;
	private TextView DialogAbout;
	private TextView DialogInfoCenter;
	private LinearLayout HaveNo;
	public myAlertDialog(Context mcontext, int Width, int Height)
	{
		context = mcontext;
		//dialog = new AlertDialog.Builder(mcontext).create();
		dialog=new Dialog(mcontext, R.style.DialogStyle);
		dialog.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = dialog.getWindow();
		window.setContentView(R.layout.main_activity_alertdialog);
		DialogLinear = (LinearLayout)window.findViewById(R.id.DialogLinear);
		DialogUserCenter = (TextView)window.findViewById(R.id.DialogUserCenter);
		DialogInfoCenter = (TextView)window.findViewById(R.id.DialogInfoCenter);
		DialogAbout = (TextView)window.findViewById(R.id.DialogAbout);
		dialog.getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = Width / 2;
		DialogLinearParams = (LinearLayout.LayoutParams)DialogLinear.getLayoutParams();
		DialogLinearParams.setMargins(0, 0, 0, 0);//原来用来隔离下半部的，现在不用了，先留着
		DialogLinear.setLayoutParams(DialogLinearParams);
		HaveNo = (LinearLayout)window.findViewById(R.id.HaveNo);
		LinearLayout.LayoutParams noParams = (LinearLayout.LayoutParams)HaveNo.getLayoutParams();
		noParams.height = Height / 13;
		HaveNo.setLayoutParams(noParams);
		LinearLayout.LayoutParams DialogUserCenterParams = (LinearLayout.LayoutParams)DialogUserCenter.getLayoutParams();
		DialogUserCenterParams.height = Height / 13;
		DialogUserCenter.setLayoutParams(DialogUserCenterParams);
		LinearLayout.LayoutParams DialogAboutParams = (LinearLayout.LayoutParams)DialogAbout.getLayoutParams();
		DialogAboutParams.height = Height / 13;
		DialogAbout.setLayoutParams(DialogAboutParams);
		DialogInfoCenter.setLayoutParams(DialogAboutParams);
		bindEvent();
	}
	private void bindEvent()
	{
		HaveNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
	}
	public void setItem(String text)
	{
//		TextView item = new TextView(context);
//		LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		item.setText(text);
//		item.setLayoutParams(params);
//		DialogLinear.addView(item);
	}
	public void setTextSize(int size, int id)
	{
		switch(id)
		{
		case R.id.DialogUserCenter:
			DialogUserCenter.setTextSize(size);
			break;
		case R.id.DialogAbout:
			DialogAbout.setTextSize(size);
			break;
		case R.id.DialogInfoCenter:
			DialogInfoCenter.setTextSize(size);
			break;
		}
	}
	public void setGravity(int gravity)
	{
		dialog.getWindow().setGravity(gravity | Gravity.CENTER );
	}
	public void setOnclickListener(OnClickListener click, int id)
	{
		switch(id)
		{
		case R.id.DialogUserCenter:
			DialogUserCenter.setOnClickListener(click);
			break;
		case R.id.DialogAbout:
			DialogAbout.setOnClickListener(click);
			break;
		case R.id.DialogInfoCenter:
			DialogInfoCenter.setOnClickListener(click);
			break;
		}
	}
	/**
	 * 设置点击颜色
	 */
	public void setOntouchColor()
	{
		DialogAbout.setOnTouchListener(new myOnTouchListenerChangeCityBackground());
		DialogUserCenter.setOnTouchListener(new myOnTouchListenerChangeCityBackground());
		DialogInfoCenter.setOnTouchListener(new myOnTouchListenerChangeCityBackground());
	}
	public void dismiss()
	{
		this.dialog.dismiss();
	}
}
