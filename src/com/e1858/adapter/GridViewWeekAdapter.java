package com.e1858.adapter;

import java.util.List;

import com.e1858.bean.WeekData;
import com.e1858.ceapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewWeekAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layInflater;
	private int itemResource;
	private List<WeekData> list;
	public GridViewWeekAdapter(Context context,List<WeekData> list,int itemResource){
		this.context=context;
		this.itemResource=itemResource;
		this.list=list;
		this.layInflater=LayoutInflater.from(context);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=layInflater.inflate(itemResource, null);
			viewHolder.week_title=(TextView)convertView.findViewById(R.id.week_title);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}		
		viewHolder.week_title.setText(""+list.get(position).getId());
		
		if(list.get(position).isSelected()){
			viewHolder.week_title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kecheng_on));
		}else{
			viewHolder.week_title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.week_gridview_item_bg));
		}
		return convertView;
	}

	static class ViewHolder{
		TextView week_title;
	}
}
