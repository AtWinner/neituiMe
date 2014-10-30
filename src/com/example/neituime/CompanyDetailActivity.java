package com.example.neituime;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Document;

import com.example.adapter.AdjustPageLayout;
import com.example.adapter.GetScreenSize;
import com.example.adapter.GridViewAdapter;
import com.example.adapter.myProgressDialog;
import com.example.event.myOnKeyListener;
import com.example.network.CheckNetwork;
import com.example.network.DocumentsSelect;
import com.example.network.GetHtml;
import com.example.network.GetImage;
import com.example.view.GridViewItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CompanyDetailActivity extends Activity {
	private static final int MSG_SUCCESS = 0;// 获取成功的标识
	private static final int MSG_FAILURE = 1;// 获取失败的标识
	private static final int IMG_SUCCESS = 2;// 图片获取成功
	private static final int JSON_SUCCESS = 3;//Json获取成功
	private static final int LOADE_MORE = 4;// 上拉获取更多
	
	private Intent beforeIntent;
	private String PageUrl;// 保存本页面的Url
	private String CompanyLogoUrl;// 保存公司logo的Url
	private String JsonUrl;
	private int PageNumber;
	private int Width;
	private int Height;
	private int PageItemCount;
	private int PageItemSize = 10;
	
	private ImageButton CompanyGetBack;
	private LinearLayout CompanyBottomLinear;
	private Button CompanybtnGetBack;
	private ImageView MyCompanyLogo;
	private TextView CompanyName;
	private TextView CompanyDetail;
	private LinearLayout CompanyLabel; //2010.8.25 260 
	private LinearLayout CompanyDetailBodyId;
	private PullToRefreshGridView pull_refresh_grid_company_detail;
	private GridView mGridView;
	private myProgressDialog progressDialog = null;
	
	private ArrayList<HashMap<String, Object>> al;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_detail_activity);
		init();
		SetParams();
		BindEvent();
		CheckNetwork check = new CheckNetwork();
		if(check.isNetworkConnected(CompanyDetailActivity.this) || check.OpenNetwork(CompanyDetailActivity.this))
		{
			showDialog();
			(new mThread(PageUrl, MSG_SUCCESS)).start();
			(new mThread(CompanyLogoUrl, IMG_SUCCESS)).start();
			(new mThread(GetJsonUrl(), JSON_SUCCESS)).start();
		}
		
		
	}
	private void init()
	{
		PageNumber = 1;
		PageItemCount = 0;
		Width = getWindowManager().getDefaultDisplay().getWidth();
		Height = getWindowManager().getDefaultDisplay().getHeight();
		Height = GetScreenSize.getUsefulScreenHeight(CompanyDetailActivity.this, Height);
		beforeIntent = getIntent();
		PageUrl = beforeIntent.getStringExtra("CompanyUrl");
		CompanyLogoUrl = beforeIntent.getStringExtra("CompanyLogo");
		CompanyGetBack = (ImageButton)findViewById(R.id.CompanyGetBack);
		CompanyBottomLinear = (LinearLayout)findViewById(R.id.CompanyBottomLinear);
		CompanybtnGetBack = (Button)findViewById(R.id.CompanybtnGetBack);
		MyCompanyLogo = (ImageView)findViewById(R.id.MyCompanyLogo);
		CompanyName = (TextView)findViewById(R.id.CompanyName);
		CompanyDetail = (TextView)findViewById(R.id.CompanyDetail);
		CompanyLabel = (LinearLayout)findViewById(R.id.CompanyLabel);
		CompanyDetailBodyId = (LinearLayout)findViewById(R.id.CompanyDetailBodyId);
		pull_refresh_grid_company_detail = (PullToRefreshGridView)findViewById(R.id.pull_refresh_grid_company_detail);
		mGridView = pull_refresh_grid_company_detail.getRefreshableView();
		GetJsonUrl();
	}
	private void SetParams()
	{
		int height = Height / 13;
		int width = height / 2;//返回图片的宽：高=7:11
		LinearLayout.LayoutParams params = new LayoutParams(width, height);
		CompanyGetBack.setLayoutParams(params);
		CompanyName.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(Width));
		RelativeLayout.LayoutParams bodyParams = (RelativeLayout.LayoutParams)CompanyDetailBodyId.getLayoutParams();//new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)(Height * 11.5 / 13));
		bodyParams.height = (int)(Height * 12 / 13);
		CompanyDetailBodyId.setLayoutParams(bodyParams);
		
		CompanybtnGetBack.setTextSize(AdjustPageLayout.AdjustTextSizeInYourNeed(Width, 30));
	}
	private void BindEvent()
	{
		CompanyGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CompanyDetailActivity.this.finish();
			}
		});
		CompanybtnGetBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CompanyDetailActivity.this.finish();
			}
		});
		pull_refresh_grid_company_detail.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) 
			{
				CheckNetwork check = new CheckNetwork();
				if(check.isNetworkConnected(CompanyDetailActivity.this) || check.OpenNetwork(CompanyDetailActivity.this))
				{
					new GetRefreshDataTask().execute();
				}
				else
					pull_refresh_grid_company_detail.onRefreshComplete();	
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) 
			{
				CheckNetwork check = new CheckNetwork();
				if(check.isNetworkConnected(CompanyDetailActivity.this) || check.OpenNetwork(CompanyDetailActivity.this))
				{
					new GetMoreDataTask().execute();
				}
				else
					pull_refresh_grid_company_detail.onRefreshComplete();
				
			}		
		});
		pull_refresh_grid_company_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> mymap = (HashMap<String,Object>)mGridView.getItemAtPosition(arg2);
				
				Toast.makeText(CompanyDetailActivity.this,
						mymap.toString(), Toast.LENGTH_LONG).show();
				String DetailUrl = "http://www.neitui.me/mobile/detail/id=" + mymap.get("id").toString() + ".html";//链接
				Intent intent = new Intent(CompanyDetailActivity.this, JobDetailActivity.class);
				intent.putExtra("URL", DetailUrl);
				intent.putExtra("position", mymap.get("positionfull").toString());
				intent.putExtra("department", mymap.get("departmentfull").toString());
				//intent.putExtra("cmail", mymap.get("cmail").toString());
				intent.putExtra("createdate", mymap.get("createdate").toString());
				intent.putExtra("avatar", mymap.get("avatar").toString());
				intent.putExtra("salary", mymap.get("salary").toString());
				intent.putExtra("city", mymap.get("city").toString());
				startActivity(intent);
				overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
			}
		});
		
		
	}
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case MSG_SUCCESS:
				Document doc = (Document)msg.obj;
				//CompanyDetail.setText(doc.toString());
				DocumentsSelect DS = new DocumentsSelect(doc);
				HashMap<String, String> map = DS.GetCompanyDetailActivityInfo();

				SetPageData(map);//设置页面数据
				break;
			case IMG_SUCCESS:
				Bitmap bitmap = (Bitmap)msg.obj;
				MyCompanyLogo.setImageBitmap(bitmap);
				break;
				
			case MSG_FAILURE:
				Toast.makeText(CompanyDetailActivity.this, "网络不给力了...", Toast.LENGTH_SHORT).show();
				break;
			case JSON_SUCCESS: 
				String JsonStr = (String)msg.obj;
				GridViewItem GVI = new GridViewItem(JsonStr);
				al = GVI.GetCompanyGridViewItemsByJson();
				PageItemCount = al.size();
				mGridView.setAdapter(new GridViewAdapter(CompanyDetailActivity.this, al, R.layout.gridlist, Width));
				if(PageItemCount < PageItemSize)
				{
					Toast.makeText(CompanyDetailActivity.this, "没有更多职位了", Toast.LENGTH_SHORT).show();
				}
				mGridView.setStackFromBottom(false);//不要定位到GridView底部
				break;
			case LOADE_MORE:
				String JsonStrMore = (String)msg.obj;
				GridViewItem GVIMore = new GridViewItem(JsonStrMore);
				al.addAll(GVIMore.GetCompanyGridViewItemsByJson());
				mGridView.setAdapter(new GridViewAdapter(CompanyDetailActivity.this, al, R.layout.gridlist, Width));
				mGridView.setStackFromBottom(true);
				mGridView.setSelection(PageItemCount-1);
				PageItemCount = al.size();
				break;
			}
			progressDialog.dismiss();
			pull_refresh_grid_company_detail.onRefreshComplete();
		}
		
	};
	private class mThread extends Thread
	{
		private String URL;
		private int KIND;
		public mThread(String Url, int kind)
		{
			URL = Url;
			KIND = kind;
		}
		@Override
		public void run() {
			try
			{
				switch (KIND) {
				case MSG_SUCCESS:
					//获取Document
					GetHtml GH = new GetHtml();
					Document doc = GH.GetDocByUrl(URL);
					mHandler.obtainMessage(KIND, doc).sendToTarget();
					break;
				case IMG_SUCCESS:
					//获取图片
					Bitmap bitmap = GetImage.returnBitMap(URL);
					mHandler.obtainMessage(KIND, bitmap).sendToTarget();
					break;
				case JSON_SUCCESS:
					//获取列表中显示数据的Json
					GetHtml gh = new GetHtml();
					mHandler.obtainMessage(KIND, gh.GetJsonByUrl(JsonUrl)).sendToTarget();//如果成功需要返回依着在GridView中显示的结果集
					
					break;
				case LOADE_MORE:
					//上拉获取更多
					GetHtml ghMore = new GetHtml();
					mHandler.obtainMessage(KIND, ghMore.GetJsonByUrl(JsonUrl)).sendToTarget();//如果成功需要返回依着在GridView中显示的结果集
					
					break;
				}
				
			}
			catch(Exception e)
			{
				mHandler.obtainMessage(MSG_FAILURE, "网络不给力...").sendToTarget();
			}
			
			super.run();
		}
	}
	private void SetPageData(HashMap<String, String> map)
	{
		CompanyName.setText(map.get("CompanyName"));
		CompanyDetail.setText(map.get("Area")+"\n"+map.get("Scale")+"\n"+map.get("Financing"));
		
		double StringLength = 0;
		int j=10;//保存的是LinearLayout的ID
		LinearLayout innerLinear = CreateLinearLayout(j);
		for(int i=0; map.get("CompanyTag"+i) != null; i++)
		{
			String LabelText = map.get("CompanyTag"+i);
			StringLength += LabelText.length();
			StringLength += 1.5;
			if(StringLength >= 30.5)//每行显示28字，超过强制换行
			{
				j++;
				CreateLinearLayout(j);
				StringLength = 0;
				StringLength += LabelText.length();
			}
			CreateTextViewLabel(map.get("CompanyTag"+i), (LinearLayout)findViewById(j));
		}
	}
	private void CreateTextViewLabel(String InnerText, LinearLayout layoutbody)
	{//添加公司标签
		TextView myView = new TextView(CompanyDetailActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 5, 0);
		myView.setLayoutParams(params);
		myView.setText(InnerText);
		myView.setPadding(5, 0, 5, 5);
		myView.setTextSize(15);
		myView.setBackgroundResource(R.drawable.border);
		layoutbody.addView(myView);
	}
	private LinearLayout CreateLinearLayout(int j)
	{
		LinearLayout innerLinear = new LinearLayout(CompanyDetailActivity.this);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 5, 0, 0);
		innerLinear.setLayoutParams(params);
		innerLinear.setOrientation(LinearLayout.HORIZONTAL);
		innerLinear.setId(j);
		CompanyLabel.addView(innerLinear);
		return innerLinear;
	}
	
	private class GetRefreshDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... arg0) {
			return null;
		}
		@Override
		protected void onPostExecute(String[] result) {
			CheckNetwork check = new CheckNetwork();
			if(check.isNetworkConnected(CompanyDetailActivity.this) || check.OpenNetwork(CompanyDetailActivity.this))
			{
				PageItemSize = 10;
				PageNumber = 1;
				(new mThread(GetJsonUrl(), JSON_SUCCESS)).start();
				
			}
			super.onPostExecute(result);
		}
		
	}
	private class GetMoreDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... arg0) {
			return null;
		}
		@Override
		protected void onPostExecute(String[] result) {
			CheckNetwork check = new CheckNetwork();
			if(check.isNetworkConnected(CompanyDetailActivity.this) || check.OpenNetwork(CompanyDetailActivity.this))
			{
				if(PageItemCount >= PageItemSize)
				{
					PageItemSize += 10;
					PageNumber++;
					(new mThread(GetJsonUrl(), LOADE_MORE)).start();
				}
				else 
				{
					pull_refresh_grid_company_detail.onRefreshComplete();
					Toast.makeText(CompanyDetailActivity.this, "没有更多职位了", Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
		
	}
	private String GetJsonUrl()
	{
		int end;
		if(PageUrl.indexOf(".cn") > 0)
		{
			end = PageUrl.indexOf(".cn") + 3;
		}
		
		else if(PageUrl.indexOf(".com") > 0)
		{
			end = PageUrl.indexOf(".com") + 4;
		}
		else
		{
			end = PageUrl.indexOf(".net") + 4;
		}
		JsonUrl = PageUrl.substring(PageUrl.indexOf("domain"), end);
		JsonUrl = "http://www.neitui.me/index.php?name=company&handle=detail&type=job&jsonp=1&version=1.0.4&itemnum=10&" 
				+ JsonUrl + "&page=" + PageNumber;
		return JsonUrl;
	}
	
	private void showDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = myProgressDialog.createDialog(CompanyDetailActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setOnKeyListener(new myOnKeyListener());
			progressDialog.setMessage("拼命获取数据中...");
		}
		progressDialog.show();		
	}
}
