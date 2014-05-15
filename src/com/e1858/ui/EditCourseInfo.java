package com.e1858.ui;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.e1858.BaseActivity;
import com.e1858.adapter.GridViewWeekAdapter;
import com.e1858.bean.WeekData;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Course;
import com.e1858.protocol.CourseTimes;
import com.e1858.protocol.http.AddCourse;
import com.e1858.protocol.http.AddCourseResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.wheel.WheelAdapter;
import com.e1858.widget.wheel.WheelView;

public class EditCourseInfo extends BaseActivity {	
	
	private EditText 				course_name;
	private EditText 				course_teacher;

		 
	private TextView 				weeks_textview;
	private TextView 				course_time_string;
	private EditText 				classroom;	 
	 
	private Button   				add_more_course_time;	
	private LinearLayout 			add_course_layout;
	 
	private WheelView  				week_picker;
	private WheelView  				start_picker;
	private WheelView  				end_picker;
	private Button    				btn_ok;
	private Button     				btn_cancle;
	 
	private String 					course_name_str;
	private String 					course_teacher_str;
	private int                     weekCnt;

	 
	
	 
	private List<CourseTimes> 		times_list=new ArrayList<CourseTimes>();
	private CourseTimes 			entity;
	private Course      			course;
	 
	 
	private AddCourse 				addCourse_protocol;
	private AddCourseResp 			addCourseResp;
	 
	private List<WeekData>	 		weekdata_list=new ArrayList<WeekData>();
	private WeekData       			weekdata_entity;
	private GridViewWeekAdapter 	adapter;
	 
	private long 					courseID;  
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editcourse);
		weekCnt=cEappApp.getTermcnf().getWeekCnt();
		Intent intent=getIntent();
		courseID=intent.getLongExtra("courseID", 0);	
		initView();	
		initData();		
		loadlocalData();
	}
	
	public void loadlocalData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.COURSE+" where id=?", new String[]{""+courseID});
			course_name.setText(c.getString(c.getColumnIndex("name")));
			course_teacher.setText(c.getString(c.getColumnIndex("teacher")));
			Cursor times=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.COURSE_TIMES+" where id=?", new String[]{""+courseID});
			CourseTimes time_entity;
			List<CourseTimes> times_list=new ArrayList<CourseTimes>();
			for(int i=0;i<times.getCount();i++){
				times.moveToPosition(i);
				time_entity=new CourseTimes();
				time_entity.setId(times.getInt(times.getColumnIndex("id")));
				time_entity.setClassroom(times.getString(times.getColumnIndex("classroom")));
				time_entity.setWeeks(StringUtils.ConvetDataType(times.getString(times.getColumnIndex("weeks"))));
				time_entity.setWeekday(times.getInt(times.getColumnIndex("weekday")));
				time_entity.setStart(times.getInt(times.getColumnIndex("start")));
				time_entity.setEnd(times.getInt(times.getColumnIndex("end")));
				times_list.add(time_entity);
			}
			times.close();
			for(int i=0;i<times_list.size();i++){
				add_course_layout.addView(createView());
				LinearLayout lin=(LinearLayout) add_course_layout.getChildAt(i);
				course_time_string=(TextView)lin.findViewById(R.id.course_time_string);
				classroom=(EditText)lin.findViewById(R.id.classroom);
				weeks_textview=(TextView)lin.findViewById(R.id.weeks_textview);			
				weeks_textview.setText(getTextWeek(StringUtils.ConvertDataType(times_list.get(i).getWeeks()).toString()).append("周"));		
				if(times_list.get(i).getEnd()==times_list.get(i).getStart()){
					course_time_string.setText(getWeekday(times_list.get(i).getWeekday())+"第"+times_list.get(i).getStart()+"节");
				}else{
					course_time_string.setText(getWeekday(times_list.get(i).getWeekday())+times_list.get(i).getStart()+"-"+times_list.get(i).getEnd()+"节");
				}
			    classroom.setText(times_list.get(0).getClassroom());
			}
			c.close();
			
			
			
		}catch(Exception e){
			
		}	
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
	
	
	public void initData(){
		weekdata_list.clear();		
		for(int i=0;i<17;i++){
			weekdata_entity=new WeekData();
			weekdata_entity.setId(i+1);
			weekdata_entity.setSelected(true);
			weekdata_list.add(weekdata_entity);
		}
		for(int i=0;i<weekCnt-17;i++){
			weekdata_entity=new WeekData();
			weekdata_entity.setId(17+i+1);
			weekdata_entity.setSelected(false);
			weekdata_list.add(weekdata_entity);
		}
	}
	//周一1-8
	public StringBuilder getCourseTime(String coursetime){
		StringBuilder sb=new StringBuilder();
		if(coursetime.contains("一")){
			sb.append("1").append(",");
		}else if(coursetime.contains("二")){
			sb.append("2").append(",");
		}else if(coursetime.contains("三")){
			sb.append("3").append(",");
		}else if(coursetime.contains("四")){
			sb.append("4").append(",");
		}else if(coursetime.contains("五")){
			sb.append("5").append(",");
		}else if(coursetime.contains("六")){
			sb.append("6").append(",");
		}else if(coursetime.contains("日")){
			sb.append("7").append(",");
		}
		if(coursetime.contains("第")){
			if(coursetime.length()==6){
				sb.append(coursetime.substring(3,5));
			}else if(coursetime.length()==5){
				sb.append(coursetime.substring(3,4));
			}
			
		}else if(coursetime.contains("-")){
			if(coursetime.length()==6){
				sb.append(coursetime.substring(2, 3)).append(",").append(coursetime.subSequence(4, 5));
			}else if(coursetime.length()==7){
				sb.append(coursetime.substring(2, 3)).append(",").append(coursetime.subSequence(4, 6));
			}else if(coursetime.length()==8){
				sb.append(coursetime.substring(2, 4)).append(",").append(coursetime.subSequence(5, 7));
			}
			
		}		
		return sb;
	}
	
	
	private void  addCourseRequest(){
		if(NetUtil.checkNetWorkStatus(this)){
			
			
			course_name_str=course_name.getText().toString();
			course_teacher_str=course_teacher.getText().toString();		    	
			int linCount=add_course_layout.getChildCount();
			
			if(linCount==0){
				ToastUtil.showShort(this, "上课时间不能为空");
				return;
			}else{
				openProgressDialog("提交中...");
				for(int i=0;i<linCount;i++){
					LinearLayout lin=(LinearLayout) add_course_layout.getChildAt(i);
					course_time_string=(TextView)lin.findViewById(R.id.course_time_string);
					classroom=(EditText)lin.findViewById(R.id.classroom);
					weeks_textview=(TextView)lin.findViewById(R.id.weeks_textview);			
					if(course_time_string.getText().toString().equals("")){
						ToastUtil.showShort(this, "课程时间"+(i+1)+"时间为空");
					}else{
					
						StringBuilder sbitem=getCourseTime(course_time_string.getText().toString().trim());
						Log.v("sbitem", "=="+sbitem.toString());
						String[] aaitem=sbitem.toString().split(",");
						
						entity=new CourseTimes();
						entity.setId(-1);
						entity.setWeeks(StringUtils.ConvetDataType(getWeeksTime(weeks_textview.getText().toString().trim().substring(0, weeks_textview.getText().toString().trim().length()-1)).toString()));
						entity.setClassroom(classroom.getText().toString());
						
						entity.setStart(Integer.valueOf(aaitem[1]));
						
						if(course_time_string.getText().toString().trim().contains("第")){
							entity.setEnd(Integer.valueOf(aaitem[1]));	
						}else{
							entity.setEnd(Integer.valueOf(aaitem[2]));
						}
						entity.setWeekday(Integer.valueOf(aaitem[0]));	
					    times_list.add(entity);
					}
				}		
				course=new Course();
				course.setId(-1);
				course.setName(course_name_str);
				course.setTeacher(course_teacher_str);
				course.setCourseTimes(times_list);
				addCourse_protocol=new AddCourse();
				addCourse_protocol.setUser(cEappApp.getUser());
				addCourse_protocol.setPass(cEappApp.getPass());
				addCourse_protocol.setCourse(course);
				NetUtil.post(Constant.BASE_URL, addCourse_protocol, handler, MessageWhat.ADD_COURSE_RESP);
			}
	
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}	
	}
	
	
	
	private void initView(){
	
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);	
		
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundColor(getResources().getColor(R.color.title_bar_btn_bg));
		btn_right.setText("保存");
		
		btn_right.setVisibility(View.VISIBLE);		
		title=(TextView)findViewById(R.id.title);
		title.setText("添加课程");
		
		course_name=(EditText)findViewById(R.id.course_name);
		course_teacher=(EditText)findViewById(R.id.teacher);
		
		
		add_more_course_time=(Button)findViewById(R.id.add_more_course_time);			
		
		add_course_layout=(LinearLayout)findViewById(R.id.add_course_layout);				
		
		add_more_course_time.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				add_course_layout.addView(createView());
				
			}
		});
			
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				addCourseRequest();
			}
		});		
		
	}
	public StringBuilder getWeeksTime(String weeksTime){
		StringBuilder sb=new StringBuilder();
		if(weeksTime.contains(",")){
			String[] total=weeksTime.split(",");
			for(int i=0;i<total.length;i++){
				String aa=total[i];
				if(aa.contains("-")){
					String[] child=aa.split("-");
					for(int j=Integer.parseInt(child[0]);j<=Integer.parseInt(child[1]);j++){
						sb.append(j).append(",");
					}
				}else{
					sb.append(aa).append(",");
				}	
			}
		}else if(!weeksTime.contains(",")&&weeksTime.contains("-")){
			String[] child=weeksTime.split("-");
			for(int j=Integer.parseInt(child[0]);j<=Integer.parseInt(child[1]);j++){
				sb.append(j).append(",");
			}
		}else{
			sb.append(weeksTime).append(",");
		}	

		sb.deleteCharAt(sb.length()-1);
		return sb;
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		  case MessageWhat.ADD_COURSE_RESP:
			  if(null!=(String)msg.obj){
					closeProgressDialog();
					addCourseResp=JsonUtil.fromJson((String)msg.obj,AddCourseResp.class);
					if(null==addCourseResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS == addCourseResp.getStatus()){ 
						try{
							cEappApp.dbManager.deleteDataBySql("delete from "+TableName.CLASS_SHEDULE +" where courseID=? and termID=?", new String[]{""+courseID,""+cEappApp.getTermcnf().getId()});
							cEappApp.dbManager.deleteDataBySql("delete from "+TableName.COURSE+" where id=?", new String[]{""+courseID});
							cEappApp.dbManager.deleteDataBySql("delete from "+TableName.COURSE_TIMES+" where id=?", new String[]{""+courseID});
						}catch(Exception e){
							e.printStackTrace();
						}
						
						
						long newID=addCourseResp.getNewID();
						
						ContentValues content=new ContentValues();
						content.put("courseID", newID);
						content.put("termID", cEappApp.getTermcnf().getId());
						content.put("classID", -1);			
						cEappApp.dbManager.insertData(TableName.CLASS_SHEDULE, content);
						
						ContentValues coursevalue=new ContentValues();
						coursevalue.put("id", newID);
						coursevalue.put("name",course.getName());
						coursevalue.put("teacher", course.getTeacher());
						coursevalue.put("comefrom", IntentValue.LOCAL_COURSE);
						cEappApp.dbManager.insertData(TableName.COURSE, coursevalue);
						
						for(int i=0;i<times_list.size();i++){
							ContentValues course_time=new ContentValues();
							course_time.put("id", newID);
							course_time.put("classroom", times_list.get(i).getClassroom());
							course_time.put("weeks",StringUtils.ConvertDataType(times_list.get(i).getWeeks()).toString());
							course_time.put("weekday", times_list.get(i).getWeekday());
							course_time.put("start", times_list.get(i).getStart());
							course_time.put("end", times_list.get(i).getEnd());
						    cEappApp.dbManager.insertData(TableName.COURSE_TIMES, course_time);
						}
						
						Intent intent=new Intent(EditCourseInfo.this,CourseInfo.class);
						intent.putExtra("courseID", newID);
						intent.putExtra("schedule", true);
						startActivity(intent);
						this.finish();
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						
					}
			  }
			  
			  
			  break;
		}
		return false;
	}
	private View createView(){
	
		
		final View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.new_course_item, null);			
	
		final TextView weeks_textview=(TextView)view.findViewById(R.id.weeks_textview);
	
		final TextView course_time_string=(TextView)view.findViewById(R.id.course_time_string);
		
		final EditText classroom=(EditText)view.findViewById(R.id.classroom);
	   
		final Button delete_course_time=(Button)view.findViewById(R.id.delete_course_time);
		
	    final ImageView iv_delete=(ImageView)view.findViewById(R.id.iv_delete);
		
	    weeks_textview.setText("1-17周");
	    
		iv_delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delete_course_time.setVisibility(View.VISIBLE);
			}
		});
		delete_course_time.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_course_layout.removeView(view);
			}
		});
		weeks_textview.setOnClickListener(new OnClickListener() {				
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//addFoucs(weeks_textview);
				final Dialog dialog=new Dialog(EditCourseInfo.this,android.R.style.Theme_Translucent_NoTitleBar);
				
				dialog.setContentView(R.layout.coure_week_picker);
				
				GridView week_grid;
			
				Button   button_ok;
				Button   button_cancle;
				Button   single_week;
				Button   double_week;
				Button   all_week;		
				
				initData();	
				
				week_grid=(GridView)dialog.findViewById(R.id.week_gridview);
				button_ok=(Button)dialog.findViewById(R.id.button_ok);
				button_cancle=(Button)dialog.findViewById(R.id.button_cancel);
				single_week=(Button)dialog.findViewById(R.id.single_week);
				double_week=(Button)dialog.findViewById(R.id.double_week);
				all_week=(Button)dialog.findViewById(R.id.all_week);
				
				adapter=new GridViewWeekAdapter(EditCourseInfo.this,weekdata_list,R.layout.course_week_picker_item);
				
				week_grid.setAdapter(adapter);
				
				week_grid.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						if(weekdata_list.get(position).isSelected()){
							weekdata_list.get(position).setSelected(false);
						}else{
							weekdata_list.get(position).setSelected(true);
						}
						adapter.notifyDataSetChanged();
					}
				});
				
				single_week.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						for(int i=0;i<weekCnt;i++){
							if(weekdata_list.get(i).getId()%2==0){
								weekdata_list.get(i).setSelected(false);
							}else{
								weekdata_list.get(i).setSelected(true);
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
				double_week.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						for(int i=0;i<weekCnt;i++){
							if(weekdata_list.get(i).getId()%2==0){
								weekdata_list.get(i).setSelected(true);
							}else{
								weekdata_list.get(i).setSelected(false);
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
				all_week.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						for(int i=0;i<weekCnt;i++){
							weekdata_list.get(i).setSelected(true);
						}
						adapter.notifyDataSetChanged();
					}
				});
				button_ok.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						StringBuilder 	weeksb=new StringBuilder();
						for(int i=0;i<weekCnt;i++){
							if(weekdata_list.get(i).isSelected()){
								weeksb.append(""+weekdata_list.get(i).getId()).append(",");						
							}
						}		
						weeksb.deleteCharAt(weeksb.length()-1);
						Log.v("weeksb", "=="+weeksb);
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
						
						weeks_textview.setText(weeks_sb+"周");
						dialog.dismiss();
					}
				});
				button_cancle.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		
		});
		
		
		course_time_string.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//addFoucs(course_time_string);
				final Dialog dialog = new Dialog(EditCourseInfo.this,android.R.style.Theme_Translucent_NoTitleBar);
			    dialog.setContentView(R.layout.course_time_picker);
			    week_picker=(WheelView)dialog.findViewById(R.id.week_picker);
			    start_picker=(WheelView)dialog.findViewById(R.id.start_picker);
			    end_picker=(WheelView)dialog.findViewById(R.id.end_picker);
			    btn_ok=(Button)dialog.findViewById(R.id.button_ok);
			    btn_cancle=(Button)dialog.findViewById(R.id.button_cancel);
			    
			   final String[] weeks={"周一","周二","周三","周四","周五","周六","周日"};
			   
			    week_picker.setAdapter(new WheelAdapter() {
					
					public int getMaximumLength() {
						// TODO Auto-generated method stub
						return 7;
					}
					
					public int getItemsCount() {
						// TODO Auto-generated method stub
						return 7;
					}
					
					public String getItem(int index) {
						// TODO Auto-generated method stub
						
						return weeks[index];
					}
				});

			    week_picker.setCurrentItem(0);
			    
			    
			    start_picker.setAdapter(new WheelAdapter() {
					
					public int getMaximumLength() {
						// TODO Auto-generated method stub
						return cEappApp.getMaxSession();
					}
					
					public int getItemsCount() {
						// TODO Auto-generated method stub
						return cEappApp.getMaxSession();
					}
					
					public String getItem(int index) {
						// TODO Auto-generated method stub
						return "第"+(index+1)+"节";
					}
				});
			    
			    
			    end_picker.setAdapter(new WheelAdapter() {
					
					public int getMaximumLength() {
						// TODO Auto-generated method stub
						return cEappApp.getMaxSession();
					}
					
					public int getItemsCount() {
						// TODO Auto-generated method stub
						return cEappApp.getMaxSession();
					}
					
					public String getItem(int index) {
						// TODO Auto-generated method stub
						return "到"+(index+1)+"节";
					}
				});
			  
			    end_picker.setCurrentItem(0);
			    			    
			    
			    btn_ok.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(start_picker.getCurrentItem()>end_picker.getCurrentItem()){
							ToastUtil.showShort(EditCourseInfo.this, "对不起，结束时间不能小于开始时间");
						}else{
							if(start_picker.getCurrentItem()==end_picker.getCurrentItem()){
								course_time_string.setText(weeks[week_picker.getCurrentItem()]+"第"+(start_picker.getCurrentItem()+1)+"节");
							}else{
								course_time_string.setText(weeks[week_picker.getCurrentItem()]+(start_picker.getCurrentItem()+1)+"-"+(end_picker.getCurrentItem()+1)+"节");
							}					
							dialog.dismiss();
						}
						
					}
				});
			    btn_cancle.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			    dialog.show();
			
			}
		});			
		return view;
	}
	public void addFoucs(View view){
		view.setFocusable(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
	}
}
