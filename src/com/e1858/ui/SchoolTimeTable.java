package com.e1858.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.e1858.BaseMenuActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Course;
import com.e1858.protocol.CourseTimes;
import com.e1858.protocol.http.GetSchoolTimeTable;
import com.e1858.protocol.http.GetSchoolTimeTableResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.CustomScrollView;
import com.e1858.widget.FormListener;
import com.e1858.widget.WeekView;


public class SchoolTimeTable extends BaseMenuActivity implements FormListener{

	 private CustomScrollView 		  	customScrollView;
	    	
	 private HorizontalScrollView     	topHorizontalScrollView;
	 private ScrollView               	leftScrollView;
	 private WeekView                 	week_view;
	 private TableLayout              	left_table;

	 private GetSchoolTimeTable 		getSchoolTimeTable_protocal;
	 private GetSchoolTimeTableResp 	getSchoolTimeTable_Resp;

	 private List<Course> 				server_courses=new ArrayList<Course>();
	 private List<CourseTimes> 			server_coursetimes=new ArrayList<CourseTimes>();
	
	
	 private List<Course> 				courses=new ArrayList<Course>();
	 private List<CourseTimes> 			courseTimes=new ArrayList<CourseTimes>();
	
	 private Course      				course_entity;
	 private CourseTimes 				coursetime_entity;
	 private int                    	rowCount;
	 private String     				date;
	 
	 private Handler                    drawHander;
	 private AlertDialog                refreshAlert;
	 
	 
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.week_graduation);
		doScroll(ModuleInteger.SHEDULE);	
		drawHander=new Handler();
		respBackButton=true;
		
		rowCount=cEappApp.getTermcnf().getMaxSession();
		
		initView();	
		
		judgeTerm();
		
	}
	     
	public void initView(){  
	
		week_view=(WeekView)findViewById(R.id.week_view);
        week_view.setFormListener(SchoolTimeTable.this); 
		left_table=(TableLayout)findViewById(R.id.left_table);
		
		date=cEappApp.getTermcnf().getFirstDate();

		date=DateUtil.strToDatestr(date);
		
		title_name.setText("第"+DateUtil.getNowWeek(date.toString().trim())+"周");
		
		btn_top_right.setVisibility(View.VISIBLE);
		
//		btn_top_right.setText("添课");
		
		btn_top_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_course_bg));
		btn_top_right_before.setVisibility(View.VISIBLE);
		btn_top_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.refresh_course_bg));
		
		btn_top_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SchoolTimeTable.this,AddCourseActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		btn_top_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshTimeTable();
			}
		});
		
		topHorizontalScrollView=(HorizontalScrollView)findViewById(R.id.top_title_parent);
		leftScrollView=(ScrollView)findViewById(R.id.left_title_parent);
		customScrollView=(CustomScrollView)findViewById(R.id.scroll1);
		customScrollView.setFlexible(false); 
		customScrollView.setScrollView(topHorizontalScrollView,leftScrollView);   
		topHorizontalScrollView.addView(getHead());
		for(int i=0;i<rowCount;i++){
			left_table.addView(getIdRow(i+1));			
		}
		
	}
	public void loadlocalData(){	
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select courseID from  "+TableName.CLASS_SHEDULE, null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				int courseID=c.getInt(c.getColumnIndex("courseID"));		

				Cursor course_c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.COURSE+" where id=?", new String[]{""+courseID});	
	
				course_entity=new Course();
				
				course_entity.setId(courseID);
				
				course_entity.setName(course_c.getString(course_c.getColumnIndex("name")));
				
				course_entity.setTeacher(course_c.getString(course_c.getColumnIndex("teacher")));	

				List<CourseTimes> courseTimes=new ArrayList<CourseTimes>();
			
				Cursor courses_cursor=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.COURSE_TIMES+" where id=?", new String[]{""+courseID});	
				boolean isHas=false;
				
				for(int k=0;k<courses_cursor.getCount();k++){
					courses_cursor.moveToPosition(k);
					coursetime_entity=new CourseTimes();
					coursetime_entity.setId(courses_cursor.getInt(courses_cursor.getColumnIndex("id")));
					
					coursetime_entity.setClassroom(courses_cursor.getString(courses_cursor.getColumnIndex("classroom")));
					coursetime_entity.setWeeks(StringUtils.ConvetDataType(courses_cursor.getString(courses_cursor.getColumnIndex("weeks"))));
					coursetime_entity.setWeekday(courses_cursor.getInt(courses_cursor.getColumnIndex("weekday")));
					coursetime_entity.setStart(courses_cursor.getInt(courses_cursor.getColumnIndex("start")));
					coursetime_entity.setEnd(courses_cursor.getInt(courses_cursor.getColumnIndex("end")));
				
					
					if((StringUtils.ConvertDataType(coursetime_entity.getWeeks()).toString()).contains(DateUtil.getNowWeek(date))){
						isHas=true;
				
					}	 
					courseTimes.add(coursetime_entity);	
				}
				if(isHas){
					course_entity.setCourseTimes(courseTimes);	
					courses.add(course_entity);	
					isHas=false;
				}
				
				courses_cursor.close();
				course_c.close();
			}
			
			c.close();	
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	//添加刷新
	
	private void refreshTimeTable(){
		
		refreshAlert=new AlertDialog.Builder(this).setTitle("提示").setMessage("确认刷新最新课程表吗?")
				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						cEappApp.dbManager.deleteData(TableName.CLASS_SHEDULE, null, null);
						cEappApp.dbManager.deleteData(TableName.COURSE, null, null);
						cEappApp.dbManager.deleteData(TableName.COURSE_TIMES, null, null);				
						courses.clear();
			
						loadData();
						
					
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						refreshAlert.dismiss();
					}
				}).create();
		
			refreshAlert.show();
	}
	
	
	private void judgeTerm(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select termID  from "+TableName.CLASS_SHEDULE, null);
			if(c.getCount()!=0){	
				c.moveToPosition(c.getCount()-1);
				int termID=c.getInt(c.getColumnIndex("termID"));
				if(termID==cEappApp.getTermcnf().getId()){
					loadlocalData();
					parseData();
				}else{
					
					cEappApp.dbManager.deleteData(TableName.CLASS_SHEDULE, null, null);
					
					cEappApp.dbManager.deleteData(TableName.COURSE, null, null);
					
					cEappApp.dbManager.deleteData(TableName.COURSE_TIMES, null, null);
					
					
					loadData();
				
				}
			}else{
				loadData();
				
			}
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadData(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("加载中...");
			getSchoolTimeTable_protocal=new GetSchoolTimeTable();
			getSchoolTimeTable_protocal.setUser(cEappApp.getUser());
			getSchoolTimeTable_protocal.setPass(cEappApp.getPass());
			getSchoolTimeTable_protocal.setClasse(cEappApp.getStudent().getClasse());
			NetUtil.post(Constant.BASE_URL, getSchoolTimeTable_protocal, handler, MessageWhat.GET_SCHOOLTIMETABLE_RESP);	
		}else{	
			loadlocalData();
			parseData();
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}		
	}
	
	public void parseData(){		
		week_view.setDate(date);	
		week_view.setCol(rowCount);
		week_view.setList(courses);
		week_view.invalidate();
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case MessageWhat.GET_SCHOOLTIMETABLE_RESP:
			if(null!=(String)msg.obj){
				closeProgressDialog();
				getSchoolTimeTable_Resp=JsonUtil.fromJson((String)msg.obj, GetSchoolTimeTableResp.class);
				Log.v("schedule", "=="+(String)msg.obj);
				if(null==getSchoolTimeTable_Resp){
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == getSchoolTimeTable_Resp.getStatus()){	
				
					server_courses=getSchoolTimeTable_Resp.getCourses();	
					
					for(int i=0;i<server_courses.size();i++){					
						
						ContentValues content=new ContentValues();
						
						content.put("courseID", server_courses.get(i).getId());
						
						content.put("classID", cEappApp.getStudent().getClasse());
										
						content.put("termID", cEappApp.getTermcnf().getId());
						
						cEappApp.dbManager.insertData(TableName.CLASS_SHEDULE, content);
										
							
						ContentValues content_course=new ContentValues();
										
						content_course.put("id", server_courses.get(i).getId());
						content_course.put("name", server_courses.get(i).getName());
						content_course.put("teacher", server_courses.get(i).getTeacher());
						content_course.put("comefrom", IntentValue.LOCAL_COURSE);
						
						cEappApp.dbManager.insertData(TableName.COURSE, content_course);
										
									
						server_coursetimes=server_courses.get(i).getCourseTimes();
										
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
				
					for(int i=0;i<server_courses.size();i++){
						course_entity=new Course();
						course_entity.setId(server_courses.get(i).getId());
						course_entity.setName(server_courses.get(i).getName());
						course_entity.setTeacher(server_courses.get(i).getTeacher());
						boolean isHas=false;
						
						server_coursetimes=server_courses.get(i).getCourseTimes();

						for(int j=0;j<server_coursetimes.size();j++){
							
							if(StringUtils.ConvertDataType(server_coursetimes.get(j).getWeeks()).toString().contains(DateUtil.getNowWeek(date.toString().trim()))){
								isHas=true;
							}
						}	
						if(isHas){
							course_entity.setCourseTimes(server_coursetimes);
							courses.add(course_entity);
							isHas=false;
						}	
					}
					parseData();
				
				}
				
				msg.obj=null;
			}
			break;
		case MessageWhat.UPDATE_SHEDULES:
			courses.clear();
			loadlocalData();
			parseData();
			break;
		case MessageWhat.LOGOUT_RESP:
			 if(null!=(String)msg.obj){
				 closeProgressDialog();
				LoginOutResp loginOutResp=JsonUtil.fromJson((String)msg.obj, LoginOutResp.class);
				
				 Log.v("loginout", (String)msg.obj);
				 
				 if(null==loginOutResp){
					 break;
				 }
				 if(HttpDefine.RESPONSE_SUCCESS==loginOutResp.getStatus()){
					 Disconnect disconnect = new Disconnect();
					 cEappApp.getSocketSession().write(disconnect);
					 cEappApp.setFirstScroll(true);
					 Editor editor = sp.edit();  
					 editor.putBoolean(Constant.IS_AUTO_LOGIN, false);
					 editor.commit();
					 Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
					 startActivity(intent);			
					 this.finish();
					
					 
				 }
				 msg.obj=null;
			 } 
			 break;
		case MessageWhat.PUSH_MSG:
			
			PushMsg pushMsg=(PushMsg)msg.obj;
			//判断发送对象
			if(null==pushMsg.getMsg().getSender()){
				break;
			}
			NotificationUtil.cancel(this, SocketDefine.PUSH_MSG);
           
			notificationInfo = new NotificationInfo();
            
			notificationInfo.setNotificationID(SocketDefine.PUSH_MSG);
            
			if (pushMsg.getMsg().getContentType() == ContentType.TEXT)
            {
                stringBuffer.append(pushMsg.getMsg().getSender().getRealName()).append(" 发送了一条消息：").append(pushMsg.getMsg().getContent());
            }
            
			if(pushMsg.getMsg().getModule()==ModuleInteger.MESSAGE){
				 NotificationUtil.create(this, R.drawable.icon, "消息中心", "您有新的消息", stringBuffer.toString(), MessageActivity.class, notificationInfo);
			}else if(pushMsg.getMsg().getModule()==ModuleInteger.NOTICE){
				 NotificationUtil.create(this, R.drawable.icon, "通知公告", "您有新的消息", stringBuffer.toString(), NoticeActivity.class, notificationInfo);
			}
			
            break;
		
		}	
	return false;
}
		
	public View getHead(){   
    	View v = this.getLayoutInflater().inflate(R.layout.week_item, null);
  	
    	TextView week_one = (TextView) v.findViewById(R.id.week_one);   	
    	week_one.setText(getResources().getString(R.string.weekday_one));	
    	week_one.setTextColor(Color.BLACK);
	
    	TextView week_two = (TextView) v.findViewById(R.id.week_two); 	
    	week_two.setText(getResources().getString(R.string.weekday_two));   	   	
    	week_two.setTextColor(Color.BLACK);
     	
    	TextView week_three = (TextView) v.findViewById(R.id.week_three);   	
    	week_three.setText(getResources().getString(R.string.weekday_three));
    	week_three.setTextColor(Color.BLACK);
    	
    	TextView week_four = (TextView) v.findViewById(R.id.week_four);
    	week_four.setText(getResources().getString(R.string.weekday_four));	
    	week_four.setTextColor(Color.BLACK);  	
    	
    	TextView week_five = (TextView) v.findViewById(R.id.week_five);   	
    	week_five.setText(getResources().getString(R.string.weekday_five));
    	week_five.setTextColor(Color.BLACK);
    	
    	TextView week_six=(TextView)v.findViewById(R.id.week_six);  	
    	week_six.setText(getResources().getString(R.string.weekday_six));
    	week_six.setTextColor(Color.BLACK);
    	
    	TextView week_seven=(TextView)v.findViewById(R.id.week_seven);  	
    	week_seven.setText(getResources().getString(R.string.weekday_seven));
    	week_seven.setTextColor(Color.BLACK);
    	
    	return v;
    
	}
	public TableRow getIdRow(int n) {   	
    	TableRow row = new TableRow(this);
    	View v = this.getLayoutInflater().inflate(R.layout.left_row_item, null);  	
    	TextView row_text = (TextView) v.findViewById(R.id.row_text);  	
    	row_text.setText(""+n);
    	View divier=(View)v.findViewById(R.id.divier);
    	if(n%2==0){
    		divier.setBackgroundColor(getResources().getColor(R.color.left_row_dark));
    	}else{
    		divier.setBackgroundColor(getResources().getColor(R.color.left_row_hight));
    	}
    	row.addView(v);	   	
    	return row;
    }
	public void ShowCourseInfo(long courseID) {
		// TODO Auto-generated method stub
		if(courseID==-1){
			return;
		}else{
			Intent intent=new Intent(SchoolTimeTable.this,CourseInfo.class);
			intent.putExtra("courseID",courseID);
			intent.putExtra("schedule", true);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}	
}
