package com.e1858.adapter;

import java.util.List;

import com.e1858.bean.MsgInfo;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.SendStatusTyp;
import com.e1858.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ListViewMsgInfoAdapter extends BaseAdapter {

	private Context context;
	private List<MsgInfo> list;
	private int leftitemResource;
	private int rightitemResource;
	private LayoutInflater layinInflater;
	
	public ListViewMsgInfoAdapter(Context context,List<MsgInfo> list,int leftitemResource,int rightitemResource){
		this.context=context;
		this.list=list;
		this.leftitemResource=leftitemResource;
		this.rightitemResource=rightitemResource;
		this.layinInflater=LayoutInflater.from(context);
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
			if(list.get(position).isComeMsg()){
				convertView=layinInflater.inflate(leftitemResource, null);
			}else{
				convertView=layinInflater.inflate(rightitemResource, null);
			}
			vh.content=(TextView)convertView.findViewById(R.id.content);
			vh.msg_time=(TextView)convertView.findViewById(R.id.subtime);
			vh.status=(ImageView)convertView.findViewById(R.id.status);
			vh.bar=(ProgressBar)convertView.findViewById(R.id.bar);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.content.setText(list.get(position).getContent());
		try{
			vh.msg_time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(list.get(position).getTime()))));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(list.get(position).getStatus()==SendStatusTyp.WAIT_SEND){
			vh.bar.setVisibility(View.VISIBLE);
		}	
		if(list.get(position).getStatus()==SendStatusTyp.FAILED){
			vh.bar.setVisibility(View.GONE);
			vh.status.setVisibility(View.VISIBLE);
		}else{
			vh.bar.setVisibility(View.GONE);
			vh.status.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder{
		TextView content;
		TextView msg_time;
		ImageView status;
		ProgressBar bar;
	}
}
