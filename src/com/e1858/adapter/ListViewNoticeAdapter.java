package com.e1858.adapter;

import java.util.List;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.protocol.NoticeBase;
import com.e1858.ui.NoticeActivity;
import com.e1858.utils.DateUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewNoticeAdapter extends BaseAdapter {

	private NoticeActivity context;
	private List<NoticeBase> list;
	private int itemResource;
	private LayoutInflater layiInflater;
	private boolean        inNotice;
	private long[]         noticeids;
	
	private int selectedPosition = -1;// 选中的位置
	private int first=-1;//是否第一次点击按钮设置item背景
	private int type=-1;//是否第二次点击长按item 可控制所有item
	
	private boolean allSelect=false;
	
	
	public ListViewNoticeAdapter(NoticeActivity context,List<NoticeBase> list,int itemResource,boolean inNotice){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;
		this.inNotice=inNotice;
		this.layiInflater=LayoutInflater.from(context);
		noticeids=new long[list.size()];
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
	public void setSelectedPosition(int position,int first) {
		
		this.selectedPosition = position;
		this.first=first;
	}
	
	public void setType(int type){
		this.type=type;
	}

	
	
	public boolean isAllSelect() {
		return allSelect;
	}

	public void setAllSelect(boolean allSelect) {
		this.allSelect = allSelect;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=layiInflater.inflate(itemResource, null);
			vh.notice_title=(TextView)convertView.findViewById(R.id.notice_title);
			vh.notice_time=(TextView)convertView.findViewById(R.id.notice_time);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}

		
		vh.notice_title.setText(list.get(position).getTitle());
		try{
			vh.notice_time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(list.get(position).getIssuedDate()))));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(allSelect){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.main_darkblue));
		}else{
			if(type==0){
				if(selectedPosition==position){
					if(first==0){	
						convertView.setBackgroundColor(context.getResources().getColor(R.color.main_darkblue));
					}else{
						convertView.setBackgroundColor(context.getResources().getColor(R.color.listitem_transparent));
					}	
				}	
			}else{
				convertView.setBackgroundColor(context.getResources().getColor(R.color.listitem_transparent));
			}
			
		}	
		return convertView;
		
	}
	static class  ViewHolder{
		TextView notice_title;
		TextView notice_time;
	}

}
