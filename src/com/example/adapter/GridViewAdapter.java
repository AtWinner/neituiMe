package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.neituime.R;
import com.example.network.GetImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewAdapter extends BaseAdapter {
	private LayoutInflater inFlater;
	private static int flag = 0;
	private ArrayList<HashMap<String, Object>> list;
	private int resource;// 绑定的一个条目界面的id，此例中即为item.xml
	private int ScreenWidth;
	private Context context;
	ViewHolder holder = null;
	public GridViewAdapter(Context context, ArrayList<HashMap<String, Object>> al, int res, int Width)
	{
		inFlater = LayoutInflater.from(context);
		list = al;
		resource = res;
		ScreenWidth = Width;
		this.context = context;
	}
	@Override
	public int getCount() {
		if(list!=null)  
            return list.size();  
        else 
            return 0;  
		
	}

	@Override
	public Object getItem(int position) {
		if(list!=null)  
            return list.get(position);  
        else 
            return null;  
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		
		//"position", "createdate", "department", "city", "salary"
		if (contentView == null)
		{// 显示第一页的时候convertView为空
			contentView = inFlater.inflate(resource, null);// 生成条目对象
			holder = new ViewHolder();
			holder.city = (TextView)contentView.findViewById(R.id.city);
			holder.createdate = (TextView)contentView.findViewById(R.id.createdate);
			holder.department = (TextView)contentView.findViewById(R.id.department);
			holder.position = (TextView)contentView.findViewById(R.id.position);
			holder.salary = (TextView)contentView.findViewById(R.id.salary);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)contentView.getTag();
		}
		holder.city.setText((String)list.get(position).get("city"));
		holder.city.setTextSize(AdjustPageLayout.AdjustListInfoSize(ScreenWidth));
		LinearLayout.LayoutParams CityParams = (LayoutParams)holder.city.getLayoutParams();
		CityParams.height = AdjustPageLayout.AdjustImageSize(ScreenWidth) / 2;
		holder.city.setLayoutParams(CityParams);
		holder.createdate.setText((String)list.get(position).get("createdate"));
		holder.createdate.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(ScreenWidth));
		holder.department.setText((String)list.get(position).get("department"));
		holder.department.setTextSize(AdjustPageLayout.AdjustListInfoSize(ScreenWidth));
		holder.position.setText((String)list.get(position).get("position"));
		holder.position.setTextSize(AdjustPageLayout.AdjustListTitleTextSize(ScreenWidth));
		holder.salary.setText((String)list.get(position).get("salary"));
		final int aa = position;
		return contentView;
	}
	
	private class ViewHolder
	{
		public TextView position;
		public TextView createdate;
		public TextView department;
		public TextView city;
		public TextView salary;
	}
}
