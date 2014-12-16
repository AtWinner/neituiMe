package com.example.neituime;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.JobDetailAlertDialog;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.event.myOnTouchListenerChangeBackground;
import com.example.neituime.R.string;
import com.example.network.DocumentsSelect;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.tencent.CheckOnlineState;
import com.example.tencent.MyIUiListener;
import com.example.view.AnalyzeJson;
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class JobDetailActivity extends Activity{
	private static final int MSG_SUCCESS = 0;// 获取成功的标识
	private static final int MSG_FAILURE = 1;// 获取失败的标识
	private static final int IMG_SUCCESS = 2;// 图片获取成功
	private static final int IMG_CREATOR = 3;// 头像获取成功
	private static final int IMG_COMPANY = 4;// 公司logo获取成功
	private static final int MSG_GETUID = 5;// 通过第三方登录之后获取uid
	
	private String JobID;
	
	private static final int ResponseNumber = 2; //
	private String UID;
	private String Token;
	private static final String AppID = "101016468";
	private int Width;
	private int Height;
	private String URL;
	private Intent beforeIntent;
	private ImageButton JobGetBack;
	private ImageButton Creater;
	private ScrollView JobScrollview;
	private Button JobbtnGetBack;
	private TextView CreaterInfo;
	private TextView JobInfo;
	private TextView JobTitle;
	private TextView JobTitleDetail;
	private ImageButton CompanyLogo;
	//private TextView JobMain;
	private LinearLayout JobMainLinear;
	private LinearLayout CompanyBody;
	private LinearLayout DetailTop;
	private LinearLayout DetailBottom;
	private RelativeLayout JobBottomRelative;
	private  myProgressDialog progressDialog = null;
	private Button Login;
	private Tencent mTencent;
	private SharedPreferences OnlineInfo;
	
	private int LabelHeight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_detail_acitvity);
		init();
		SetParams();
		SetData();
		BindEvent();
		mThread m = new mThread();
		m.start();
	}
	private void init()
	{
		OnlineInfo = getSharedPreferences("OnlineInfo", Context.MODE_PRIVATE);
		UID = OnlineInfo.getString("UID", "");
		Token = OnlineInfo.getString("Token", "");
		beforeIntent = getIntent();
		URL = beforeIntent.getStringExtra("URL");
		JobID = beforeIntent.getStringExtra("id");
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(JobDetailActivity.this, Height);
		JobGetBack = (ImageButton)findViewById(R.id.JobGetBack);
		JobScrollview = (ScrollView)findViewById(R.id.JobScrollview);
		CreaterInfo = (TextView)findViewById(R.id.CreaterInfo);
		CreaterInfo.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));
		JobInfo = (TextView)findViewById(R.id.JobInfo);
		JobInfo.setTextSize(AdjustPageLayout.AdjustListInfoSize(Width));
		JobTitle = (TextView)findViewById(R.id.JobTitle);
		JobTitle.setTextSize(AdjustPageLayout.AdjustListInfoSize(Width));
		JobTitleDetail = (TextView)findViewById(R.id.JobTitleDetail);
		JobTitleDetail.setTextSize(AdjustPageLayout.AdjustListInfoSize(Width));
		JobMainLinear = (LinearLayout)findViewById(R.id.JobMainLinear);
		JobbtnGetBack = (Button)findViewById(R.id.JobbtnGetBack);
		CompanyBody = (LinearLayout)findViewById(R.id.CompanyBody);
		Creater = (ImageButton)findViewById(R.id.Creater);
		CompanyLogo = (ImageButton)findViewById(R.id.CompanyLogo);
		DetailBottom = (LinearLayout)findViewById(R.id.DetailBottom);
		DetailTop = (LinearLayout)findViewById(R.id.DetailTop);
		Login = (Button)findViewById(R.id.QQ);
		JobBottomRelative = (RelativeLayout)findViewById(R.id.JobBottomRelative);
		mTencent = Tencent.createInstance(AppID, JobDetailActivity.this);
		showDialog();
	}
	private void SetParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		JobGetBack.setLayoutParams(params);
		LinearLayout.LayoutParams scrollParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(height * 12));
		JobScrollview.setLayoutParams(scrollParams);
		//JobMainLinear.setBackgroundResource(R.drawable.boder_detail);
		JobbtnGetBack.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
		
		RelativeLayout.LayoutParams JobBottomRelativeParams = (RelativeLayout.LayoutParams)JobBottomRelative.getLayoutParams();
		JobBottomRelativeParams.height = height;
		JobBottomRelative.setLayoutParams(JobBottomRelativeParams);
		
		Login.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
	}
	private void BindEvent()
	{
		DetailBottom.setOnTouchListener(new myOnTouchListenerChangeBackground());
		JobGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				JobDetailActivity.this.finish();
			}
		});
		JobbtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				JobDetailActivity.this.finish();
			}
		});
		Login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ShowAlertDialog();
			}
		});
	}
	/**
	 * 显示自定义AlertDialog
	 */
	private void ShowAlertDialog()
	{
		JobDetailAlertDialog dialog = new JobDetailAlertDialog(JobDetailActivity.this, Width, Height);
	}
	/**
	 * 投递简历
	 */
	private void GoToSend()
	{
		if (CheckOnlineState.IsOnline(OnlineInfo)) 
		{//去简历界面
			Intent resumeIntent = new Intent(JobDetailActivity.this, ResumeActivity.class);
			if(UID != null && Token != null)
			{
				resumeIntent.putExtra("UID", UID);
				resumeIntent.putExtra("Token", Token);
				resumeIntent.putExtra("ResponseNumber", ResponseNumber);
				resumeIntent.putExtra("JobID", JobID);
				startActivity(resumeIntent);
				overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
			}
			else 
			{
				String PostStr = "http://www.neitui.me/?dev=android&version=1.0.4&name=devapi&json=1&handle=getauth&type=qq&authkey=dc94e7adc147d381e26e74b63434b132&";
				String OpenId = mTencent.getOpenId();
				PostStr += ("otherid=" + OpenId);
				mThread uidThread = new mThread(MSG_GETUID, PostStr);
				uidThread.start();
				//在请求一次服务器，如果依旧为null就真没有了
			}
		}
		else
		{//如果没登录就去登录界面
			Intent loginIntent = new Intent(JobDetailActivity.this, LoginActivity.class);
			loginIntent.putExtra("ResponseNumber", ResponseNumber);
			startActivityForResult(loginIntent, 1);
			overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		}
	}
	private void SetData()
	{
		if(CheckOnlineState.IsOnline(OnlineInfo))
		{
			Login.setText("投递简历");
		}
		else
		{
			Login.setText("投递简历[请登录]");
		}
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) 
		{
			switch(msg.what)
			{
			case MSG_SUCCESS:
				JobMainLinear.setBackgroundColor(Color.WHITE);
				DetailBottom.setBackgroundColor(Color.WHITE);
				DetailTop.setBackgroundColor(Color.WHITE);
				CreateJobDetailTitle();//设置职位细节部分的标题
				DocumentsSelect DS = new DocumentsSelect((Document)msg.obj);
				HashMap<String, String> map = DS.GetList();
				Log.e("", map.get("CompanyCont"));
				Log.e("", map.get("CompanyHref"));
				
				ImageThread creatorImage = new ImageThread(beforeIntent.getStringExtra("avatar"), IMG_CREATOR);
				creatorImage.start();//获取创建者的头像
				ImageThread companyImage = new ImageThread(map.get("CompanyImageSrc"), IMG_COMPANY);
				companyImage.start();
				JobInfo.setText(Html.fromHtml(map.get("JobInfo")));
				//JobTitle.setText(Html.fromHtml(map.get("JobTitle")));
				//JobMain.setText((map.get("Detail") + "\n" + map.get("ImageInfo1")));
				HashMap<String, String> TextMap = DS.GetText();
				HashMap<String, String> ImageMap = DS.GetImage();
				DisplayData(TextMap, ImageMap);
				//处理完文本之后再加载更加费时 的图片，使用新的线程
				ImageThread imageThread = new ImageThread(ImageMap);
				imageThread.start();
				JobTitle.setText(map.get("CompanyCont"));
				JobTitleDetail.setText(map.get("CompanyDetail"));
				CreaterInfo.setText(map.get("RealName"));
				for(int CompanyTag = 1; map.containsKey("CompanyTag" + CompanyTag) ; CompanyTag++)
				{
					CreateTextViewLabel(map.get("CompanyTag"+ CompanyTag) );
				}
				LinearLayout.LayoutParams labelParams = (LinearLayout.LayoutParams)CompanyBody.getLayoutParams();
				labelParams.height = LabelHeight;
				CompanyBody.setLayoutParams(labelParams);
				
				final String CompanyUrl = map.get("CompanyHref");
				final String CompanyLogoo = map.get("CompanyImageSrc");
				DetailBottom.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent companyIntent = new Intent(JobDetailActivity.this, CompanyDetailActivity.class);
						companyIntent.putExtra("CompanyUrl", CompanyUrl);
						companyIntent.putExtra("CompanyLogo", CompanyLogoo);
						startActivity(companyIntent);
						overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
					}
				});
				break;
			case MSG_FAILURE:
				Toast.makeText(JobDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case IMG_SUCCESS:
				HashMap<String, Bitmap> myMap = (HashMap<String, Bitmap>)msg.obj;
				if(myMap != null && myMap.size() > 0)
				{
					for(int i=1; i<=myMap.size(); i++)
					{
						if(myMap.get("Image"+i) != null)
						{
							((ImageView)findViewById(i)).setImageBitmap((Bitmap)myMap.get("Image"+i));
						}
					}
				}
				break;
			case IMG_CREATOR:
				Bitmap mybitmap = (Bitmap)msg.obj;
				if(mybitmap != null)
				{
					Creater.setImageBitmap(mybitmap);
				}
				break;
			case IMG_COMPANY:
				Bitmap companybitmap = (Bitmap)msg.obj;
				if(companybitmap != null)
				{
					CompanyLogo.setImageBitmap(companybitmap);
				}
				break;
			case MSG_GETUID:
				String JsonStr = (String)msg.obj;
				AnalyzeJson analyze = new AnalyzeJson(JsonStr);
				HashMap<String, String> userMap = analyze.GetUserInfoByJson();
				//Toast.makeText(LoginActivity.this, JsonStr, Toast.LENGTH_LONG).show();
				UID = userMap.get("uid");
				if(!UID.equals("0"))
				{
					Token = userMap.get("token");
					Intent resumeIntent = new Intent(JobDetailActivity.this, ResumeActivity.class);
					resumeIntent.putExtra("UID", UID);
					resumeIntent.putExtra("Token", Token);
					startActivity(resumeIntent);
					overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
				}
				else
				{
					Toast.makeText(JobDetailActivity.this, "没有找到您的简历，请到网页版完善", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			progressDialog.dismiss();
		}
	};
	private class mThread extends Thread
	{
		private int KIND;
		private String JsonUrl;
		public mThread()
		{
			KIND = MSG_SUCCESS;
		}
		public mThread(int Kind, String Url)
		{
			KIND = Kind;
			JsonUrl = Url;
		}
		@Override
		public void run() {
			try
			{
				switch(KIND)
				{
				case MSG_SUCCESS:
					GetHtml GH = new GetHtml();
					Document doc = GH.GetDocByUrl(URL);
					mHandler.obtainMessage(KIND, doc).sendToTarget();
					break;
				case MSG_GETUID:
					GetHtml GHJson = new GetHtml();
					mHandler.obtainMessage(KIND, GHJson.GetJsonByUrl(JsonUrl)).sendToTarget();
					break;
				}
			}
			catch(Exception e)
			{
				mHandler.obtainMessage(MSG_FAILURE, "不好意思，网络不给力了...").sendToTarget();
			}
		}
	}
	private class ImageThread extends Thread
	{
		HashMap<String, String> MyImageMap;
		String CreatorImageUrl = null;
		int Point;//表示图片的位置，IMG_CREATOR代表HR的图片，IMG_COMPANY代表公司的
		public ImageThread(HashMap<String, String> ImageMap)
		{
			MyImageMap = ImageMap;
		}
		public ImageThread(String Url, int ImagePoint)
		{
			CreatorImageUrl = Url;
			Point = ImagePoint;
		}
		@Override
		public void run() {
			if(CreatorImageUrl == null)
			{
				try
				{
					if(MyImageMap.size() <= 0)
					{
						return;
					}
					else
					{
						HashMap<String, Bitmap> myHashmap = new HashMap<String, Bitmap>();
						for(int i=1; i<=MyImageMap.size(); i++)
						{
							String ImageUrl = MyImageMap.get("ImageInfo"+i);
							if(ImageUrl != null && (ImageUrl.indexOf("png") > 0 || ImageUrl.indexOf("jpg") > 0 || ImageUrl.indexOf("jpeg") > 0 || ImageUrl.indexOf("bmp") > 0))
							{
								Bitmap myBitmap = GetImage.returnBitMap(ImageUrl);
								myHashmap.put("Image"+i, myBitmap);
							}
							else
							{
								continue;
							}
						}
						mHandler.obtainMessage(IMG_SUCCESS, myHashmap).sendToTarget();
					}
				}
				catch(Exception e)
				{
					mHandler.obtainMessage(MSG_FAILURE, "不好意思，网络不给力了...").sendToTarget();
				}
			}
			else
			{
				try
				{
					Bitmap bitmap = GetImage.returnBitMap(CreatorImageUrl);
					mHandler.obtainMessage(Point, bitmap).sendToTarget();
				}
				catch(Exception e)
				{
					mHandler.obtainMessage(MSG_FAILURE, "不好意思，网络不给力了...").sendToTarget();
				}
			}
		}
	}
	private void DisplayData(HashMap<String, String> TextMap, HashMap<String, String> ImageMap)
	{
		int TextNum = TextMap.size();
		CreateTextView(TextMap.get("Text1"));
		for(int i=2; i<=TextNum; i++)
		{
			int j = i-1;
			CreateImageView(ImageMap.get("ImageInfo"+j), j);
			CreateTextView(TextMap.get("Text"+i));
		}
		
	}
	private void CreateTextView(String InnerText)
	{
		TextView myView = new TextView(JobDetailActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		myView.setText(Html.fromHtml(InnerText));
		myView.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 17));
		//myView.setLineSpacing(add, mult);
		myView.setLayoutParams(params);
		JobMainLinear.addView(myView);
	}
	private void CreateJobDetailTitle()
	{//在工作细节的LinearLayout中加入标题
		LinearLayout JobMainInnerLinear = (LinearLayout)findViewById(R.id.JobMainInnerLinear);
		ImageView JobDeatailTitleFront = new ImageView(JobDetailActivity.this);
		LinearLayout.LayoutParams imageParams = new LayoutParams(Height / 60, Height / 30);
		JobDeatailTitleFront.setLayoutParams(imageParams);
		JobDeatailTitleFront.setImageResource(R.drawable.title_front);
		JobMainInnerLinear.addView(JobDeatailTitleFront);
		
		TextView JobDetailTitle = new TextView(JobDetailActivity.this);
		LinearLayout.LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, Height / 30);
		textParams.setMargins(5, 0, 0, 0);
		JobDetailTitle.setLayoutParams(textParams);
		JobDetailTitle.setText(string.JobDetail);
		JobDetailTitle.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));

		JobMainInnerLinear.addView(JobDetailTitle);
	}
	private void CreateTextViewLabel(String InnerText)
	{
		TextView myView = new TextView(JobDetailActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 5, 0);
		myView.setLayoutParams(params);
		myView.setText(InnerText);
		myView.setPadding(5, 5, 5, 5);
		myView.setTextSize(15);
		myView.setBackgroundResource(R.drawable.border);
		CompanyBody.addView(myView);
		LabelHeight = GetViewHeight(myView);
		
	}
	private int GetViewHeight(View view)
	{
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);//获取控件的宽度和高度
		return view.getMeasuredHeight();
	}
	private void CreateImageView(String ImageUrl, int foot)
	{
		if(ImageUrl == null)
		{
			return;
		}
		if(ImageUrl != null && (ImageUrl.indexOf("png") > 0 || ImageUrl.indexOf("jpg") > 0 || ImageUrl.indexOf("jpeg") > 0 || ImageUrl.indexOf("bmp") > 0))
		{
			ImageView myView = new ImageView(JobDetailActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			myView.setLayoutParams(params);
			myView.setAdjustViewBounds(true);
			myView.setMaxHeight(Height * 9 / 10);
			myView.setMaxWidth(Width * 9 / 10); 
			//myView.setImageResource(R.drawable.main_logo);
			myView.setId(foot);
			JobMainLinear.addView(myView);
		}
		else
		{
			 TextView myTextView = new TextView(JobDetailActivity.this);  
			 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 myTextView.setLayoutParams(params);
			 SpannableString sp = new SpannableString(ImageUrl);             
			 sp.setSpan(new URLSpan(ImageUrl), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);          
//			 sp.setSpan(new BackgroundColorSpan(R.color.myRed), 17 ,19,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        
//			 sp.setSpan(new ForegroundColorSpan(R.color.myRed),0,sp.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);      
			 sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0,sp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);           
			 //SpannableString对象设置给TextView
			 myTextView.setTextSize(17);
			 myTextView.setText(sp);          
			 //设置TextView可点击          
			 myTextView.setMovementMethod(LinkMovementMethod.getInstance());
			 JobMainLinear.addView(myTextView);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		UID = data.getStringExtra("UID");
		Token = data.getStringExtra("Token");
		//Toast.makeText(JobDetailActivity.this, data.getStringExtra("UID")+"，"+ data.getStringExtra("Token"), Toast.LENGTH_SHORT).show();
		SetData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void showDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = myProgressDialog.createDialog(JobDetailActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new myOnKeyListener());
			progressDialog.setMessage("拼命获取数据中...");
		}
		progressDialog.show();		
	}
	
}
