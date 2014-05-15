package com.e1858.adapter;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.protocol.SubmitMsg;
import com.e1858.ui.MessageActivity;
import com.e1858.utils.DateUtil;

public class ListViewSendMsgAdapter extends BaseAdapter {

	private List<SubmitMsg> list;
	private LayoutInflater layInflater;
	private MessageActivity context;
	private int itemResource;
	private int selectedPosition = -1;// 选中的位置
	private int first=-1;//是否第一次点击按钮设置item背景
	private int type=-1;//是否第二次点击长按item 可控制所有item
	private boolean allSelect=false;;
	
	public ListViewSendMsgAdapter(MessageActivity context,List<SubmitMsg> list,int itemResource){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;
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
		ListItemView itemView=null;
		if(convertView==null){
			itemView=new ListItemView();
			convertView=layInflater.inflate(itemResource, null);
			itemView.title=(TextView)convertView.findViewById(R.id.title);
			itemView.time=(TextView)convertView.findViewById(R.id.time);
			itemView.content=(TextView)convertView.findViewById(R.id.content);
			convertView.setTag(itemView);
		}else{
			itemView=(ListItemView)convertView.getTag();
		}
		itemView.title.setText(list.get(position).getReceiver());
		try{
			itemView.time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(list.get(position).getTime()))));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		itemView.content.setText(list.get(position).getContent());
		
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

	static class ListItemView{
		TextView title;
		TextView time;
		TextView content;
	}
}
