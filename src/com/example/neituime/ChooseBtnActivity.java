package com.example.neituime;

import java.util.Map;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.neituime.R.string;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ChooseBtnActivity extends Activity {
	private int Width;
	private int Height;
	private ImageButton GetBack;
	private Button btnGetBack;
	private LinearLayout BottomLinear;
	private ScrollView selectButtonScrollview;
	private SharedPreferences ButtonInfo;
	private Editor editor;
	private Boolean IsChange;
	private Boolean IsSearch = false;
	private EditText SearchEditText;
	private ImageButton SearchButton;
	private Intent SearchData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_button);
		init();
		SetParams();
		CreateView();
		BindEvent();
		
	}
	/**
	 * 初始化操作
	 */
	private void init()
	{
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(ChooseBtnActivity.this, Height);
		GetBack = (ImageButton)findViewById(R.id.GetBack);
		btnGetBack = (Button)findViewById(R.id.btnGetBack);
		BottomLinear = (LinearLayout)findViewById(R.id.BottomLinear);
		selectButtonScrollview = (ScrollView)findViewById(R.id.selectButtonScrollview);
		ButtonInfo = getSharedPreferences("ButtonInfo", Context.MODE_PRIVATE);
		editor = ButtonInfo.edit();
		IsChange = false;//初始化为没有修改过
		SearchEditText = (EditText)findViewById(R.id.SearchEditText);
		SearchButton = (ImageButton)findViewById(R.id.SearchButton);
	}
	private void SetParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		GetBack.setLayoutParams(params);
		LinearLayout.LayoutParams scrollParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(height * 12));
		selectButtonScrollview.setLayoutParams(scrollParams);
		
		RelativeLayout.LayoutParams EditParams = (RelativeLayout.LayoutParams)SearchEditText.getLayoutParams();
		EditParams.width = Width * 5 / 6;
		EditParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
		SearchEditText.setLayoutParams(EditParams);
		
		RelativeLayout.LayoutParams ButtonParams = (RelativeLayout.LayoutParams)SearchButton.getLayoutParams();
		ButtonParams.width = ButtonParams.height = (int)(height * 0.8);
		SearchButton.setLayoutParams(ButtonParams);
		
		btnGetBack.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
	}
	private void CreateView()
	{
		TextView TXGetBack = new TextView(this);
		TXGetBack.setText("返回");
		TXGetBack.setTextColor(color.white);
		TXGetBack.setTextSize(20);
		BottomLinear.addView(TXGetBack);
		LinearLayout TechnologyLinear1 = (LinearLayout)findViewById(R.id.TechnologyLinear1);
		myButton(TechnologyLinear1, ChooseBtnActivity.this, string.Java);
		myButton(TechnologyLinear1, ChooseBtnActivity.this, string.PHP);
		myButton(TechnologyLinear1, ChooseBtnActivity.this, string.Android);
		myButton(TechnologyLinear1, ChooseBtnActivity.this, string.iOS);
		LinearLayout TechnologyLinear2 = (LinearLayout)findViewById(R.id.TechnologyLinear2);
		myButton(TechnologyLinear2, ChooseBtnActivity.this, string.Operation);
		myButton(TechnologyLinear2, ChooseBtnActivity.this, string.Framework);
		myButton(TechnologyLinear2, ChooseBtnActivity.this, string.Python);
		myButton(TechnologyLinear2, ChooseBtnActivity.this, string.BigData);
		LinearLayout TechnologyLinear3 = (LinearLayout)findViewById(R.id.TechnologyLinear3);
		myButton(TechnologyLinear3, ChooseBtnActivity.this, string.CSharp);
		myButton(TechnologyLinear3, ChooseBtnActivity.this, string.RearEnd);
		myButton(TechnologyLinear3, ChooseBtnActivity.this, string.Testing);
		myButton(TechnologyLinear3, ChooseBtnActivity.this, string.CPP);
		LinearLayout TechnologyLinear4 = (LinearLayout)findViewById(R.id.TechnologyLinear4);
		myButton(TechnologyLinear4, ChooseBtnActivity.this, string.Algorithm);
		myButton(TechnologyLinear4, ChooseBtnActivity.this, string.FrontEnd);
		LinearLayout ProductLinear = (LinearLayout)findViewById(R.id.ProductLinear);
		myButton(ProductLinear, ChooseBtnActivity.this, string.ProductManager);
		myButton(ProductLinear, ChooseBtnActivity.this, string.ProductSpecialist);
		myButton(ProductLinear, ChooseBtnActivity.this, string.ProductPlanning);
		myButton(ProductLinear, ChooseBtnActivity.this, string.ProductAssistant);
		LinearLayout ProductLinear2 = (LinearLayout)findViewById(R.id.ProductLinear2);
		myButton(ProductLinear2, ChooseBtnActivity.this, string.ProductDirector);
		LinearLayout StyleLinear1 = (LinearLayout)findViewById(R.id.StyleLinear1);
		myButton(StyleLinear1, ChooseBtnActivity.this, string.View);
		myButton(StyleLinear1, ChooseBtnActivity.this, string.UI);
		myButton(StyleLinear1, ChooseBtnActivity.this, string.Interactive);
		myButton(StyleLinear1, ChooseBtnActivity.this, string.UE);
		LinearLayout StyleLinear2 = (LinearLayout)findViewById(R.id.StyleLinear2);
		myButton(StyleLinear2, ChooseBtnActivity.this, string.Painting);
		myButton(StyleLinear2, ChooseBtnActivity.this, string.Designer);
		myButton(StyleLinear2, ChooseBtnActivity.this, string.DesignDirector);
		LinearLayout Motion_BusinessLinear = (LinearLayout)findViewById(R.id.Motion_BusinessLinear);
		myButton(Motion_BusinessLinear, ChooseBtnActivity.this, string.Motion_Business);
		myButton(Motion_BusinessLinear, ChooseBtnActivity.this, string.Editor);
		myButton(Motion_BusinessLinear, ChooseBtnActivity.this, string.Plan);
		myButton(Motion_BusinessLinear, ChooseBtnActivity.this, string.OperatingDirector);
		LinearLayout MarketLinear = (LinearLayout)findViewById(R.id.MarketLinear);
		myButton(MarketLinear, ChooseBtnActivity.this, string.Sale);
		myButton(MarketLinear, ChooseBtnActivity.this, string.BD);
		myButton(MarketLinear, ChooseBtnActivity.this, string.Business);
		myButton(MarketLinear, ChooseBtnActivity.this, string.Market);
		LinearLayout FunctionLinear = (LinearLayout)findViewById(R.id.FunctionLinear);
		myButton(FunctionLinear, ChooseBtnActivity.this, string.HR);
		myButton(FunctionLinear, ChooseBtnActivity.this, string.Administration);
		myButton(FunctionLinear, ChooseBtnActivity.this, string.Accounting);																												
	}
	private void BindEvent()
	{
		OnClickListener eventGetBack = new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ChooseBtnActivity.this.finish();
				
			}
		};
		GetBack.setOnClickListener(eventGetBack);
		btnGetBack.setOnClickListener(eventGetBack);
		SearchButton.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				final String SearchText = SearchEditText.getEditableText().toString();
				if(SearchText.trim().isEmpty() || SearchText.trim().equals(""))
				{
					Toast.makeText(ChooseBtnActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
				}
				else
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //隐藏在SearchEditText使用的键盘
					imm.hideSoftInputFromWindow(SearchEditText.getWindowToken(), 0); //强制隐藏键盘  
					SearchData = new Intent();
					IsSearch = true;
					SearchData.putExtra("Value", SearchText.trim());
					ChooseBtnActivity.this.finish();
				}
				
			}
		});
	}
	/**
	 * 创建Button
	 * @param ButtonLinear
	 * @param context
	 */
	private void myButton(LinearLayout ButtonLinear, final Context context, int btnText)
	{
		Button btnAndroid = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams((int)(Width / 4.5), (int)(Height / 15));
		params.setMargins(Width/100, 10, Width/100, 10);
		btnAndroid.setText(btnText);
		btnAndroid.setTextSize(AdjustPageLayout.AdjustButtonTextSize(Width, Height));//设置字体大小，是试出来的
		btnAndroid.setBackgroundResource(R.color.myGrary);
		//btnAndroid.setTextColor(Color.rgb(31, 102, 146));//这是内推的主题色
		btnAndroid.setTextColor(Color.rgb(255, 255, 255));
		btnAndroid.setLayoutParams(params);
		Map<String, ?> AllButton = ButtonInfo.getAll();
		String str = getString(btnText);
		for(String name : AllButton.keySet())
		{//遍历ButtonInfo中保存的全部标签中是否有当前btnText
			if(name.equals(str))
				btnAndroid.setTextColor(Color.rgb(31, 102, 146));//这是内推的主题色
		}
		btnAndroid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				IsChange = true;//只要点过任何一个Button就要修改为true
				Button thisButton = (Button)arg0;
				String text = (String) thisButton.getText();
				Map<String, ?> AllButton = ButtonInfo.getAll();
				if(AllButton.toString().equals("{}") || AllButton.toString().equals(""))
				{//AllButton为空
					editor.putString(text, text);
					editor.commit();
					thisButton.setTextColor(Color.rgb(31, 102, 146));//这是内推的主题色
				}
				for(String name : AllButton.keySet())
				{//遍历ButtonInfo中保存的全部标签中是否有当前btnText
					if(name.equals(text))//已经选择过这个button时，需要删除这个标记
					{
						Log.e(name, text);
						editor.remove(text);
						editor.commit();
						thisButton.setTextColor(Color.rgb(255, 255, 255));//这是内推的主题色
						break;
					}
					else//否则需要将这个标签添加
					{
						editor.putString(text, text);
						editor.commit();
						thisButton.setTextColor(Color.rgb(31, 102, 146));//这是内推的主题色
					}
				}
				
			}
		});
		ButtonLinear.addView(btnAndroid);
	}
	@Override
	public void finish() {
		if(IsSearch)
		{
			setResult(2, SearchData);
		}
		else
		{
			if(IsChange)
			{
				Log.e("", IsChange+"");
				setResult(1);
			}
			else
			{
				setResult(0);
			}
		}
		super.finish();
	}

}
