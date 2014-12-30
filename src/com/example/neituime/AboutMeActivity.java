package com.example.neituime;

import com.example.adapter.GetScreenSize;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutMeActivity extends Activity {
	
	private int Width;
	private int Height;
	
	private myProgressDialog progressDialog = null;
	
	private ScrollView AboutMeSctollView;
	private ImageButton AboutMeGetBack;
	private Button UserCenterbtnGetBack;
	private ImageView NeituiMeLogo;
	private ImageView mail163;
	private ImageView qqq;
	private TextView mail163Text;
	private TextView qqNumberText;
	private EditText EditTitle;
	private EditText EditContent;
	private Button btnSendMail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//打开软键盘时调整页面布局
		setContentView(R.layout.about_me);
		init();
		setParams();
		bindEvent();
	}
	private void init()
	{
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(AboutMeActivity.this, Height);
		
		AboutMeSctollView = (ScrollView)findViewById(R.id.AboutMeSctollView);
		UserCenterbtnGetBack = (Button)findViewById(R.id.UserCenterbtnGetBack);
		AboutMeGetBack = (ImageButton)findViewById(R.id.AboutMeGetBack);
		NeituiMeLogo = (ImageView)findViewById(R.id.NeituiMeLogo);
		mail163 = (ImageView)findViewById(R.id.mail163);
		qqq = (ImageView)findViewById(R.id.qqq);
		mail163Text = (TextView)findViewById(R.id.mail163Text);
		qqNumberText = (TextView)findViewById(R.id.qqNumberText);
		EditTitle = (EditText)findViewById(R.id.EditTitle);
		EditContent = (EditText)findViewById(R.id.EditContent);
		btnSendMail = (Button)findViewById(R.id.btnSendMail);
	}
	private void setParams()
	{
		LayoutParams AboutMeSctollViewParams = (LayoutParams)AboutMeSctollView.getLayoutParams();
		AboutMeSctollViewParams.height = Height * 12 / 13;
		AboutMeSctollView.setLayoutParams(AboutMeSctollViewParams);
		LinearLayout.LayoutParams AboutMeGetBackParmas = (LinearLayout.LayoutParams)AboutMeGetBack.getLayoutParams();
		AboutMeGetBackParmas.height = Height / 13;
		AboutMeGetBackParmas.width = Height / 26;
		AboutMeGetBack.setLayoutParams(AboutMeGetBackParmas);
		LinearLayout.LayoutParams NeituiMeLogoParams = (LinearLayout.LayoutParams)NeituiMeLogo.getLayoutParams();
		NeituiMeLogoParams.width = Width / 2;
		NeituiMeLogoParams.height = Width / 2;
		NeituiMeLogoParams.leftMargin = NeituiMeLogoParams.topMargin = NeituiMeLogoParams.rightMargin = Width / 4;
		NeituiMeLogo.setLayoutParams(NeituiMeLogoParams);
		LinearLayout.LayoutParams logoParams = new LayoutParams(Width / 11, Width / 11);
		mail163.setLayoutParams(logoParams);
		qqq.setLayoutParams(logoParams);
		LayoutParams EditTitleParams = new LayoutParams(Width * 3 / 4, LayoutParams.WRAP_CONTENT);
		EditTitle.setLayoutParams(EditTitleParams);
		LayoutParams EditContentParams = (LayoutParams)EditContent.getLayoutParams();
		EditContentParams.width = Width * 3 / 4;
		EditContent.setLayoutParams(EditContentParams);
	}
	private void bindEvent()
	{
		AboutMeGetBack.setOnClickListener(new myOnClickListener());
		UserCenterbtnGetBack.setOnClickListener(new myOnClickListener());
		btnSendMail.setOnClickListener(new myOnClickListener());
	}
	private class myOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId())
			{
			case R.id.AboutMeGetBack:
			case R.id.UserCenterbtnGetBack:
				FinishActivity();
				break;
			case R.id.btnSendMail:
				DoSend();
				break;
			}
		}
	}
	private void DoSend()
	{
		String Title = EditTitle.getEditableText().toString();
		String Content = EditContent.getEditableText().toString();
		showDialog();
		mThread m= new mThread(Title, Content);
		m.start();
	}
	private class mThread extends Thread
	{
		private String Title;
		private String Content;
		public mThread(String title, String content)
		{
			Title = title;
			Content = content;
		}
		@Override
		public void run() 
		{
			com.example.network.DoSend.sendSuggestion(Title, Content);
			mHandler.obtainMessage().sendToTarget();;
		}
	}
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			progressDialog.dismiss();
			Toast.makeText(AboutMeActivity.this, "邮件发送成功，感谢您的反馈", Toast.LENGTH_SHORT).show();
		}
	};
	private void FinishActivity()
	{
		AboutMeActivity.this.finish();
	}
	private void showDialog()
	{
		progressDialog = myProgressDialog.createDialog(AboutMeActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setOnKeyListener(new myOnKeyListener());
		progressDialog.setMessage("拼命发送中...");
		progressDialog.show();
	}
}
