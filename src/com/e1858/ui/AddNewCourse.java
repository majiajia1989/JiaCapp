package com.e1858.ui;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class AddNewCourse extends BaseActivity{	
	
	private EditText 		course_name;
	private EditText 		course_teacher;
	private TextView 		p_weeks_textview;
	private TextView 		p_course_time_string;
	private EditText 		p_classroom;
		 
	private TextView 		weeks_textview;
	private TextView 		course_time_string;
	private EditText 		classroom;	 
	 
	private Button   		button_back;
	
	private Button   		add_more_course_time;	
	private LinearLayout 	add_course_layout;
	 
	private WheelView  		week_picker;
	private WheelView  		start_picker;
	private WheelView  		end_picker;
	private Button    		btn_ok;
	private Button    		btn_cancle;
	 
	private String 			course_name_str;
	private String 			course_teacher_str;
	private String 			p_weeks_textview_str;
	private String 			p_course_time_str;
	private String 			p_classroom_str;
	 
//	private  StringBuilder 		weeksb=new StringBuilder();	 
	private List<CourseTimes> 	times_list=new ArrayList<CourseTimes>();
	private CourseTimes 		entity;
	private Course      		course;
	 
	 
	private AddCourse 			addCourse_protocol;
	private AddCourseResp 		addCourseResp;
	 
	private List<WeekData> 		weekdata_list=new ArrayList<WeekData>();
	private WeekData       		weekdata_entity;
	private GridViewWeekAdapter adapter;
	private int                 weekCnt;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_course);
		weekCnt=cEappApp.getTermcnf().getWeekCnt();
		initView();
		initData();	
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
			p_weeks_textview_str=p_weeks_textview.getText().toString().trim();
			p_course_time_str=p_course_time_string.getText().toString().trim();
			p_classroom_str=p_classroom.getText().toString();			
			if(course_name_str.equals("")){
				ToastUtil.showShort(this, "课程名空");
			}else if(p_course_time_str.equals("")){
				ToastUtil.showShort(this,"课程时间为空");
			}else{
				StringBuilder sb=getCourseTime(p_course_time_str);
				
				
				String[] aa=sb.toString().split(",");
			
				entity=new CourseTimes();
			
				entity.setId(-1);
				
				entity.setWeeks(StringUtils.ConvetDataType(getWeeksTime(p_weeks_textview_str.substring(0, p_weeks_textview_str.length()-1)).toString()));
				
				entity.setClassroom(p_classroom_str);
				
				entity.setStart(Integer.valueOf(aa[1]));
				
				if(p_course_time_str.contains("第")){
					entity.setEnd(Integer.valueOf(aa[1]));	
				}else{
					entity.setEnd(Integer.valueOf(aa[2]));
				}		
				entity.setWeekday(Integer.valueOf(aa[0]));
				
			    times_list.add(entity);
			    
				int linCount=add_course_layout.getChildCount();
				
				for(int i=1;i<linCount;i++){
					View lin=(View) add_course_layout.getChildAt(i);
					course_time_string=(TextView)lin.findViewById(R.id.course_time_string);
					classroom=(EditText)lin.findViewById(R.id.classroom);
					weeks_textview=(TextView)lin.findViewById(R.id.weeks_textview);			
					
					if(course_time_string.getText().toString().trim().equals("")){
						ToastUtil.showShort(this, "课程时间"+(i+1)+"时间为空");
					}else{
					
						StringBuilder sbitem=getCourseTime(course_time_string.getText().toString().trim());
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

				
				
				openProgressDialog("提交中...");
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
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);	
		button_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundColor(getResources().getColor(R.color.title_bar_btn_bg));
		btn_right.setText("完成");
		btn_right.setVisibility(View.VISIBLE);		
		title=(TextView)findViewById(R.id.title);
		title.setText("添加课程");
		
		course_name=(EditText)findViewById(R.id.course_name);
		course_teacher=(EditText)findViewById(R.id.teacher);
		p_weeks_textview=(TextView)findViewById(R.id.weeks_textview);
		p_course_time_string=(TextView)findViewById(R.id.course_time_string);
		p_classroom=(EditText)findViewById(R.id.classroom);		
		
		p_weeks_textview.setText("1-17周");	
		p_course_time_string.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				addFoucs(p_course_time_string);
				
				final Dialog dialog = new Dialog(AddNewCourse.this,android.R.style.Theme_Translucent_NoTitleBar);
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
			    start_picker.setCurrentItem(0);
			    
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
			    end_picker.setCurrentItem(start_picker.getCurrentItem());
						    
			    btn_ok.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(start_picker.getCurrentItem()>end_picker.getCurrentItem()){
							ToastUtil.showShort(AddNewCourse.this, "对不起，结束时间不能小于开始时间");
						}else{
						
							if(start_picker.getCurrentItem()==end_picker.getCurrentItem()){
								p_course_time_string.setText(weeks[week_picker.getCurrentItem()]+"第"+(start_picker.getCurrentItem()+1)+"节");
							}else{
								p_course_time_string.setText(weeks[week_picker.getCurrentItem()]+(start_picker.getCurrentItem()+1)+"-"+(end_picker.getCurrentItem()+1)+"节");
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
		
		
		p_weeks_textview.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFoucs(p_weeks_textview);
				final Dialog dialog=new Dialog(AddNewCourse.this,android.R.style.Theme_Translucent_NoTitleBar);
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
				adapter=new GridViewWeekAdapter(AddNewCourse.this,weekdata_list,R.layout.course_week_picker_item);
				
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
						StringBuilder weeksb=new StringBuilder();	
						for(int i=0;i<weekCnt;i++){
							if(weekdata_list.get(i).isSelected()){
								weeksb.append(""+weekdata_list.get(i).getId()).append(",");						
							}
						}		
						weeksb.deleteCharAt(weeksb.length()-1);

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
						
						p_weeks_textview.setText(weeks_sb+"周");
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
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(AddNewCourse.this,Schedules.class);
				Intent intent=new Intent(AddNewCourse.this,SchoolTimeTable.class);
				startActivity(intent);
				AddNewCourse.this.finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			}
		});
		
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
					Log.v("添加课程", (String)msg.obj);
					addCourseResp=JsonUtil.fromJson((String)msg.obj,AddCourseResp.class);
					if(null==addCourseResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS == addCourseResp.getStatus()){ 
						
						
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
							course_time.put("weeks", StringUtils.ConvertDataType(times_list.get(i).getWeeks()).toString());
							course_time.put("weekday", times_list.get(i).getWeekday());
							course_time.put("start", times_list.get(i).getStart());
						
							course_time.put("end", times_list.get(i).getEnd());
							cEappApp.dbManager.insertData(TableName.COURSE_TIMES, course_time);
		
						}
						
						Intent intent=new Intent(AddNewCourse.this,SchoolTimeTable.class);
						startActivity(intent);
						AddNewCourse.this.finish();
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
				final Dialog dialog=new Dialog(AddNewCourse.this,android.R.style.Theme_Translucent_NoTitleBar);
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
				
				adapter=new GridViewWeekAdapter(AddNewCourse.this,weekdata_list,R.layout.course_week_picker_item);
				
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
						StringBuilder 		weeksb=new StringBuilder();
						for(int i=0;i<weekCnt;i++){
							if(weekdata_list.get(i).isSelected()){
								weeksb.append(""+weekdata_list.get(i).getId()).append(",");						
							}
						}		
						weeksb.deleteCharAt(weeksb.length()-1);
					
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
				final Dialog dialog = new Dialog(AddNewCourse.this,android.R.style.Theme_Translucent_NoTitleBar);
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
							ToastUtil.showShort(AddNewCourse.this, "对不起，结束时间不能小于开始时间");
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			return true;
		}		
		return super.onKeyDown(keyCode, event);
	}
	public void addFoucs(TextView view){
		view.setFocusable(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
	}
}
