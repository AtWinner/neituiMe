package com.example.adapter;

import com.example.neituime.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JobDetailAlertDialog 
{
	private Context context;
	private AlertDialog dialog;
	private LinearLayout JobDetailDialogLinear;
	private LinearLayout.LayoutParams DialogLinearParams;
	private TextView JobDetailDialogShare;
	private TextView JobDetailDialogSend;
	private LinearLayout JobDetailHaveNo;
	public JobDetailAlertDialog(Context context, int Width, int Height)
	{
		this.context = context;
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = dialog.getWindow();
		window.setContentView(R.layout.job_detail_activity_alertdialog);
		JobDetailDialogLinear = (LinearLayout)window.findViewById(R.id.JobDetailDialogLinear);
		JobDetailDialogShare = (TextView)window.findViewById(R.id.JobDetailDialogShare);
		JobDetailDialogSend = (TextView)window.findViewById(R.id.JobDetailDialogSend);
		JobDetailHaveNo = (LinearLayout)window.findViewById(R.id.JobDetailHaveNo);
		
		LinearLayout.LayoutParams noParams = (LinearLayout.LayoutParams)JobDetailHaveNo.getLayoutParams();
		noParams.height = Height / 13;
		JobDetailHaveNo.setLayoutParams(noParams);
		dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.RIGHT);
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = Width / 3;
		
		DialogLinearParams = (LinearLayout.LayoutParams)JobDetailDialogLinear.getLayoutParams();
		DialogLinearParams.setMargins(0, 0, 0, 0);//原来用来隔离下半部的，现在不用了，先留着
		JobDetailDialogLinear.setLayoutParams(DialogLinearParams);
		
		bindEvent();
	}
	private void bindEvent()
	{
		JobDetailHaveNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
	}
}
