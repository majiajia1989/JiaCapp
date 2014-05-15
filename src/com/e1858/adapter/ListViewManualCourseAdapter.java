package com.e1858.adapter;

import java.util.List;

import com.e1858.ceapp.R;
import com.e1858.protocol.CourseTimes;
import com.e1858.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ListViewManualCourseAdapter extends BaseAdapter {

	private List<CourseTimes> list;
	private int itemResource;
	private LayoutInflater layoutInflater;
	private Context context;
	public ListViewManualCourseAdapter(Context context,List<CourseTimes> list,int itemResource){
		this.context=context;
		this.list=list;
		this.layoutInflater=LayoutInflater.from(context);
		this.itemResource=itemResource;
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
			convertView=layoutInflater.inflate(itemResource, null);
			vh.week_text=(TextView)convertView.findViewById(R.id.weeks_textview);
			vh.course_time=(TextView)convertView.findViewById(R.id.course_time_string);
			vh.classroom=(EditText)convertView.findViewById(R.id.classroom);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		vh.week_text.setText(StringUtils.ConvertDataType(list.get(position).getWeeks()).toString());
		vh.course_time.setText(list.get(position).getWeekday());
		vh.classroom.setText(vh.classroom.getText().toString());	
		vh.week_text.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}

	static class ViewHolder{
		private TextView week_text;
		private TextView course_time;
		private EditText classroom;
	}
}
