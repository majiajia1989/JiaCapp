package com.e1858.ui;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.adapter.ListViewAddCourseAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.NewDataToast;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Course;
import com.e1858.protocol.CourseTimes;
import com.e1858.protocol.http.GetCourseList;
import com.e1858.protocol.http.GetCourseListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.ClearEditText;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.widget.PullToRefreshListView.OnRefreshListener;

public class AddCourseActivity extends BaseActivity{
	
	private PullToRefreshListView       exist_course_list;
	private Button            			button_back;
	private ClearEditText    	 		search_edit;
	
	private GetCourseList     			get_course_list_protocol;
	private GetCourseListResp 			get_course_list_resp;
	
	private ListViewAddCourseAdapter 	adapter;

	private StringBuilder               sb=new StringBuilder();
	
	private List<Course> 				courses=new ArrayList<Course>();
	private List<Course> 				server_courses=new ArrayList<Course>();
	private List<Course> 				new_courses=new ArrayList<Course>();
	private List<CourseTimes> 			server_coursetimes=new ArrayList<CourseTimes>();
	
	private Course       				course_entity;
	private CourseTimes  				courestimes_entity;
	
	private String 						date;
	private boolean             		isRefresh=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_course);
		initView();
		date=cEappApp.getTermcnf().getFirstDate();
		date=DateUtil.strToDatestr(date);
		initData();
		judgeisExistsData();
	}
	public void initData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select courseID from "+ TableName.CLASS_SHEDULE, null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				int id=c.getInt(c.getColumnIndex("courseID"));
				sb.append(id).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			Log.v("sb", "==="+sb.toString());
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {  
	
		ListAdapter listAdapter = listView.getAdapter();   
	   
	    if (listAdapter == null) {  
	        return;  
	    }  

	    int totalHeight = 0;  
	    for (int i = 0; i < listAdapter.getCount(); i++) {  
	        View listItem = listAdapter.getView(i, null, listView);  
	        listItem.measure(0, 0);  
	        totalHeight += listItem.getMeasuredHeight();  
	    }  
	    ViewGroup.LayoutParams params = listView.getLayoutParams();  
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));  
	    listView.setLayoutParams(params);  
	} 

	
	
	public void initView(){
		title=(TextView)findViewById(R.id.title);
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);
		button_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setText("手动添课");   
		
		title=(TextView)findViewById(R.id.title);
		title.setText("课程列表");
		exist_course_list=(PullToRefreshListView)findViewById(R.id.exist_course_list);

		search_edit=(ClearEditText)findViewById(R.id.seach_edit);
		

		
		exist_course_list.setOnRefreshListener(new OnRefreshListener() {
			
			public void onRefresh() {
				// TODO Auto-generated method stub
				isRefresh=true;
				loadData();
			}
		});
		
		
		search_edit.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				new_courses.clear();
				if (search_edit.getText() != null) {
					String input_info = search_edit.getText().toString();
					new_courses = getNewData(input_info);
					adapter = new ListViewAddCourseAdapter(AddCourseActivity.this, cEappApp,new_courses, R.layout.add_course_item,sb.toString());
					exist_course_list.setAdapter(adapter);
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddCourseActivity.this,AddNewCourse.class);
				startActivity(intent);
				AddCourseActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(AddCourseActivity.this,Schedules.class);
				Intent intent=new Intent(AddCourseActivity.this,SchoolTimeTable.class);
				startActivity(intent);
				AddCourseActivity.this.finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			}
		});
		
		exist_course_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				long[] ids=new long[courses.size()];
				for(int i=0;i<courses.size();i++){
					ids[i]=courses.get(i).getId();
				}
				long courseID=courses.get(position-1).getId();
				Intent intent=new Intent(AddCourseActivity.this,CourseInfo.class);
				intent.putExtra("courseID", courseID);
				intent.putExtra("schedule", false);
				intent.putExtra("index", position-1);
				intent.putExtra("ids", ids);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				
			}
			
		});
	}
	
	private  List<Course> getNewData(String input_info){
		for (int i = 0; i < courses.size(); i++) {
			course_entity=courses.get(i);
			//如果遍历到的名字包含所输入字符串
			if (course_entity.getName().contains(input_info)) {
				//将遍历到的元素重新组成一个list
				Course newData = new Course();
				newData.setId(course_entity.getId());
				newData.setName(course_entity.getName());
				newData.setTeacher(course_entity.getTeacher());
				newData.setCourseTimes(course_entity.getCourseTimes());
				new_courses.add(newData);
			}
		 }
		return new_courses;
	}

	public void loadlocalData(){		
		try{
			Cursor course_c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.COURSE+" order by id desc", null);
			for(int j=0;j<course_c.getCount();j++){
				course_c.moveToPosition(j);
				int courseID=course_c.getInt(course_c.getColumnIndex("id"));						
				course_entity=new Course();
				
				course_entity.setId(course_c.getInt(course_c.getColumnIndex("id")));
				
				course_entity.setName(course_c.getString(course_c.getColumnIndex("name")));
				
				course_entity.setTeacher(course_c.getString(course_c.getColumnIndex("teacher")));
				
				List<CourseTimes>  server_coursetimes=new ArrayList<CourseTimes>();
				
				Cursor courses_cursor=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.COURSE_TIMES+" where id=?", new String[]{""+courseID});
				
				StringBuilder weeks_sb=new StringBuilder();
				
				for(int k=0;k<courses_cursor.getCount();k++){
				
					courses_cursor.moveToPosition(k);
					courestimes_entity=new CourseTimes();
					courestimes_entity.setId(courses_cursor.getInt(courses_cursor.getColumnIndex("id")));
					courestimes_entity.setClassroom(courses_cursor.getString(courses_cursor.getColumnIndex("classroom")));
					courestimes_entity.setWeeks(StringUtils.ConvetDataType(courses_cursor.getString(courses_cursor.getColumnIndex("weeks"))));
					courestimes_entity.setWeekday(courses_cursor.getInt(courses_cursor.getColumnIndex("weekday")));
					courestimes_entity.setStart(courses_cursor.getInt(courses_cursor.getColumnIndex("start")));
					courestimes_entity.setEnd(courses_cursor.getInt(courses_cursor.getColumnIndex("end")));								
					if(courestimes_entity.getStart()==courestimes_entity.getEnd()){	
						weeks_sb.append(getTextWeek(StringUtils.ConvertDataType(courestimes_entity.getWeeks()).toString()).append("周").append(getWeekday(courestimes_entity.getWeekday())).append("第").append(courestimes_entity.getStart()).append("节").append(System.getProperty( "line.separator")));
					}else{
						weeks_sb.append(getTextWeek(StringUtils.ConvertDataType(courestimes_entity.getWeeks()).toString()).append("周").append(getWeekday(courestimes_entity.getWeekday())).append(courestimes_entity.getStart()).append("-").append(courestimes_entity.getEnd()).append("节").append(System.getProperty( "line.separator")));
					}
					server_coursetimes.add(courestimes_entity);			
				}				
				course_entity.setCourseTimes(server_coursetimes);
				course_entity.setTimes(weeks_sb.toString());
				courses.add(course_entity);
				courses_cursor.close();
			}
			course_c.close();
			
			adapter=new ListViewAddCourseAdapter(AddCourseActivity.this,cEappApp, courses,R.layout.add_course_item,sb.toString());
		
			exist_course_list.setAdapter(adapter);
		
		}catch(Exception e){
			e.printStackTrace();
		}	
	
	}
	
	private void judgeisExistsData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.COURSE, null);
			if(c.getCount()==0){
				loadData();
			}else{
				loadlocalData();
			}
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
		
	public void loadData(){
		
		if(NetUtil.checkNetWorkStatus(this)){
//			openProgressDialog("加载已存在课程....");
			get_course_list_protocol=new GetCourseList();
			get_course_list_protocol.setUser(cEappApp.getUser());
			get_course_list_protocol.setPass(cEappApp.getPass());
			long id=-1;
			if(courses.size()==0){
				get_course_list_protocol.setId(id);
			}else{
				try{
					Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.COURSE+" where comefrom="+IntentValue.SERVER_COURSE +" order by id desc", null);
					if(c.getCount()==0){
						id=-1;
					}else{
						c.moveToPosition(0);
						id=c.getInt(c.getColumnIndex("id"));
					}
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				get_course_list_protocol.setId(id);
			}
		
			NetUtil.post(Constant.BASE_URL, get_course_list_protocol, handler, MessageWhat.GET_COURSE_LIST_RESP);
		
			
		}else{
			loadlocalData();
			ToastUtil.showShort(AddCourseActivity.this,getResources().getString(R.string.network_fail));
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
//			closeProgressDialog();
			return true;
		}
		switch(msg.what){
			case MessageWhat.GET_COURSE_LIST_RESP:
				if(null!=(String)msg.obj){
//					closeProgressDialog();				
					Log.v("courses", "==="+(String)msg.obj);
					get_course_list_resp=JsonUtil.fromJson((String)msg.obj,GetCourseListResp.class);				
					if(null==get_course_list_resp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS == get_course_list_resp.getStatus()){
						//数据处理
						server_courses=get_course_list_resp.getCourses();
						
						if(isRefresh){
							exist_course_list.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							if(server_courses==null||server_courses.size()==0){
								NewDataToast.makeText(AddCourseActivity.this, getString(R.string.new_data_toast_none), false).show();
								isRefresh=false;
								break;
							}else{
								isRefresh=false;
							 	NewDataToast.makeText(this, getString(R.string.new_data_toast_message,server_courses.size()), false).show();
							 	StringBuilder cursor_sb=new StringBuilder();
								try{
									Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.COURSE, null);
									for(int i=0;i<c.getCount();i++){
										c.moveToPosition(i);
										int id=c.getInt(c.getColumnIndex("id"));
										cursor_sb.append(id).append(",");
									}
									
									
								}catch(Exception e){
									
								}	
							 	
							 	
							 	for(int i=0;i<server_courses.size();i++){
							 		if(!(cursor_sb.toString().contains(""+server_courses.get(i).getId()))){
							 		
							 			course_entity=server_courses.get(i);
										ContentValues content_course=new ContentValues();
										
										content_course.put("id", server_courses.get(i).getId());
										content_course.put("name", server_courses.get(i).getName());
										content_course.put("teacher", server_courses.get(i).getTeacher());
										content_course.put("comefrom", IntentValue.SERVER_COURSE);
										
										cEappApp.dbManager.insertData(TableName.COURSE, content_course);	
					
										server_coursetimes=server_courses.get(i).getCourseTimes();
										
									
										if(server_coursetimes!=null||server_coursetimes.size()>0){	
											
											for(int j=0;j<server_coursetimes.size();j++){
												ContentValues content_coursetimes=new ContentValues();
												content_coursetimes.put("id", server_courses.get(i).getId());
												content_coursetimes.put("classroom", server_coursetimes.get(j).getClassroom());
												content_coursetimes.put("weeks", StringUtils.ConvertDataType(server_coursetimes.get(j).getWeeks()).toString());
												content_coursetimes.put("weekday", server_coursetimes.get(j).getWeekday());
												content_coursetimes.put("start", server_coursetimes.get(j).getStart());
												content_coursetimes.put("end",server_coursetimes.get(j).getEnd());	
												cEappApp.dbManager.insertData(TableName.COURSE_TIMES, content_coursetimes);
											}	
										}
								 		
							 			
								 		courses.add(0, course_entity);
								 		adapter.notifyDataSetChanged();
							 	}
							 }
								
								
							}
						}
						
						
						
						StringBuilder cursor_sb=new StringBuilder();
						try{
							Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.COURSE, null);
							for(int i=0;i<c.getCount();i++){
								c.moveToPosition(i);
								int id=c.getInt(c.getColumnIndex("id"));
								cursor_sb.append(id).append(",");
							}
							
							
						}catch(Exception e){
							
						}	
						for(int i=0;i<server_courses.size();i++){	
							if(!(cursor_sb.toString().contains(""+server_courses.get(i).getId()))){	
								
								ContentValues content_course=new ContentValues();
								
								content_course.put("id", server_courses.get(i).getId());
								content_course.put("name", server_courses.get(i).getName());
								content_course.put("teacher", server_courses.get(i).getTeacher());
								content_course.put("comefrom", IntentValue.SERVER_COURSE);
								cEappApp.dbManager.insertData(TableName.COURSE, content_course);	
			
								server_coursetimes=server_courses.get(i).getCourseTimes();
								
							
								if(server_coursetimes!=null||server_coursetimes.size()>0){	
									
									for(int j=0;j<server_coursetimes.size();j++){
										ContentValues content_coursetimes=new ContentValues();
										content_coursetimes.put("id", server_courses.get(i).getId());
										content_coursetimes.put("classroom", server_coursetimes.get(j).getClassroom());
										content_coursetimes.put("weeks", StringUtils.ConvertDataType(server_coursetimes.get(j).getWeeks()).toString());
										content_coursetimes.put("weekday", server_coursetimes.get(j).getWeekday());
										content_coursetimes.put("start", server_coursetimes.get(j).getStart());
										content_coursetimes.put("end",server_coursetimes.get(j).getEnd());	
										cEappApp.dbManager.insertData(TableName.COURSE_TIMES, content_coursetimes);
									}	
								}
							}
						}
						loadlocalData();	
					}
					msg.obj=null;
				}
				break;
			
		}
		return false;
	}
	
	public StringBuilder getTextWeek(String weeksb){
		String[]  weeks=weeksb.toString().split(",");			
		StringBuilder weeks_sb=new StringBuilder();
	
		boolean isFirsttime=true;
		for(int j=0;j<weeks.length-1;j++){ 	
			if((Integer.valueOf(weeks[j])+1)!=Integer.valueOf(weeks[j+1])){
				isFirsttime=true;
				weeks_sb.append(weeks[j]).append(",");
			}else{	
				if(isFirsttime){
					weeks_sb.append(weeks[j]).append("-");
					isFirsttime=false;
				}	
			}	
		}
		weeks_sb.append(weeks[weeks.length-1]);
		
		return weeks_sb;
	}
	public String  getWeekday(int weekday){
		String day="";
		switch(weekday){
		case 1:
			day="周一";
			break;
		case 2:
			day="周二";
			break;
		case 3:
			day="周三";
			break;
		case 4:
			day="周四";
			break;
		case 5:
			day="周五";
			break;
		case 6:
			day="周六";
			break;
		case 7:
			day="周日";
			break;
		}
		return day;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			return true;
		}		
		return super.onKeyDown(keyCode, event);
	}
}
