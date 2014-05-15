package com.e1858.adapter;

import java.util.List;

import com.e1858.ceapp.R;
import com.e1858.protocol.LibGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewGroupAdapter extends BaseAdapter {

	private Context context;
	private List<LibGroup> list;
	private int resourceItem;
	private LayoutInflater layiInflater;
	public ListViewGroupAdapter(Context context,List<LibGroup> list,int resourceItem){
		this.context=context;
		this.list=list;
		this.resourceItem=resourceItem;
		this.layiInflater=LayoutInflater.from(context);
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
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=layiInflater.inflate(resourceItem, null);
			vh.group_name=(TextView)convertView.findViewById(R.id.group_name);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.group_name.setText(list.get(position).getName());
		return convertView;
	}

	static class ViewHolder{
		TextView group_name;
	}
}
