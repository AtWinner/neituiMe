package com.example.neituime;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class WelcomeActivity extends Activity {
	private LinearLayout ImageLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.welcome);
//		RelativeLayout WelcomePage = (RelativeLayout)findViewById(R.id.WelcomePage);
//		ImageView Logo = new ImageView(getApplicationContext());
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_page);
//		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 100, 100, 100, 100);//
//		Logo.setImageBitmap(resizedBitmap);
//		WelcomePage.addView(Logo);
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽  
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高
		int ImageWidth = (int)(screenWidth * 0.6);
		int Margen = (int)(screenWidth * 0.2);
		ImageLinearLayout = (LinearLayout)findViewById(R.id.ImageLinearLayout);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ImageWidth, ImageWidth);
		params.setMargins(Margen, (int)(Margen / 2), Margen, Margen);
		ImageView IV = (ImageView)findViewById(R.id.image);
		IV.setLayoutParams(params);
		//ImageLinearLayout.setLayoutParams(params);
		LinearLayout.LayoutParams TXparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextView welcomeTextView = (TextView)findViewById(R.id.TXWelcome);
		Margen = (int)((screenWidth - getViewWidth(welcomeTextView)) / 2);
		TXparams.setMargins(Margen, 0, 0, (int)(Margen / 2));
		welcomeTextView.setLayoutParams(TXparams);
		LinearLayout.LayoutParams Belowparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextView BelowTextView = (TextView)findViewById(R.id.TVBelowTitle);
		Margen = (int)((screenWidth - getViewWidth(BelowTextView)) / 2);
		Belowparams.setMargins(Margen, 0, 0, 0);
		BelowTextView.setLayoutParams(Belowparams);
		
		new Handler().postDelayed(new Runnable()  
        {  
  
            @Override  
            public void run()  
            {  
                // TODO Auto-generated method stub  
            	Intent intent=new Intent();  
        		intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);  
                WelcomeActivity.this.finish();  
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }  
        }, 2000);  
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	/**
	 * 获取控件的宽度
	 * @param view
	 * @return int
	 */
	private int getViewWidth(View view)
	{
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredWidth();
	}
	/**
	 * 获取控件的高度
	 * @param view
	 * @return
	 */
	private int getViewHeight(View view)
	{
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}
	

}