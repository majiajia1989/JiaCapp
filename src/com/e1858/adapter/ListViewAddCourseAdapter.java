package com.e1858.adapter;
import java.util.List;

import com.e1858.CEappApp;
import com.e1858.ceapp.R;
import com.e1858.common.IntentValue;
import com.e1858.database.TableName;
import com.e1858.protocol.Course;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListViewAddCourseAdapter extends BaseAdapter {

	private CEappApp cEappApp;
	private List<Course> list;
	private int itemResource;
	private LayoutInflater layoutInflater;
	private Context context;
	private String sb;
	
 	public ListViewAddCourseAdapter(Context context,CEappApp ceappApp,List<Course> list,int itemResource,String sb){
		this.context=context;
		this.list=list;
		this.sb=sb;
		this.layoutInflater=LayoutInflater.from(context);
		this.itemResource=itemResource;
		this.cEappApp=ceappApp;
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
		final ListItemView listItem;
		
		if(convertView==null){
			listItem=new ListItemView();
			convertView=layoutInflater.inflate(itemResource, null);
			listItem.course_title=(TextView)convertView.findViewById(R.id.course_title);
			listItem.teacher=(TextView)convertView.findViewById(R.id.teacher);
			listItem.time=(TextView)convertView.findViewById(R.id.timeandclassroom);
			listItem.addordelete=(Button)convertView.findViewById(R.id.addordelete_btn);
			convertView.setTag(listItem);
		}else{
			listItem=(ListItemView)convertView.getTag();
		
		}

		final long courseID=list.get(position).getId();		
		Log.v("courseID", "=="+courseID);
		
		listItem.course_title.setText(list.get(position).getName());
	
		listItem.teacher.setText(list.get(position).getTeacher());
		
		if(sb.contains(""+list.get(position).getId())){
			listItem.addordelete.setText("删除");
			listItem.addordelete.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_bg_red));
		}else{
			listItem.addordelete.setText("添加");
			listItem.addordelete.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_bg_yellow));
		}

		listItem.time.setText(list.get(position).getTimes());	
		
		
		listItem.addordelete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listItem.addordelete.getText().toString().trim().equals("删除")){
					try{
						cEappApp.dbManager.deleteDataBySql("delete from "+TableName.CLASS_SHEDULE +" where courseID=?", new String[]{""+courseID});	
						ContentValues update=new ContentValues();
						update.put("comefrom", IntentValue.SERVER_COURSE);
						
						cEappApp.dbManager.updataData(TableName.COURSE, update, "id=?", new String[]{""+courseID});
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					listItem.addordelete.setText("添加");
					listItem.addordelete.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_bg_yellow));
				}else{
					
					ContentValues content=new ContentValues();
					content.put("courseID", courseID);
					content.put("classID", -1);	
					content.put("termID", cEappApp.getTermcnf().getId());
					cEappApp.dbManager.insertData(TableName.CLASS_SHEDULE, content);
					
					ContentValues update=new ContentValues();
					update.put("comefrom", IntentValue.LOCAL_COURSE);
					
					cEappApp.dbManager.updataData(TableName.COURSE, update, "id=?", new String[]{""+courseID});

					listItem.addordelete.setText("删除");
					
					
					listItem.addordelete.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_bg_red));
				}
			}
		});
		
		return convertView;
	}
	static class ListItemView{
		TextView course_title;
		TextView teacher;
		TextView time;
		Button   addordelete;
	}
}
