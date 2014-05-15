package com.e1858.ui;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.IntentValue;
import com.e1858.database.TableName;
import com.e1858.utils.ToastUtil;
public class CourseInfo extends BaseActivity {

	private TextView  	course_name;
	private TextView  	course_teacher;
	private TextView  	course_info;
	
	private Button    	edit_btn;
	private Button    	delete_btn;
	
	private Button      button_back;
	
	long   				courseID;
	
	private boolean     schedule;
	
	private int         index;
	private long[]      ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_info);
		Intent intent=getIntent();
		courseID=intent.getLongExtra("courseID", -1);
		schedule=intent.getBooleanExtra("schedule", false);
		if(!schedule){
			index=intent.getIntExtra("index", -1);
			ids=intent.getLongArrayExtra("ids");
		}
		initView();
		initData();
	}

	public void initView(){
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);
		
		title=(TextView)findViewById(R.id.title);
		title.setText("课程详细");
		course_name=(TextView)findViewById(R.id.course_name);
		course_teacher=(TextView)findViewById(R.id.teacher_name);
		course_info=(TextView)findViewById(R.id.times);
		edit_btn=(Button)findViewById(R.id.eidtcourse_btn);
		delete_btn=(Button)findViewById(R.id.delete_btn);
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_right));
		
		
		btn_right_before=(Button)findViewById(R.id.btn_right_before);
		btn_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_left));
		
		if(!schedule){
			
			
			btn_right.setVisibility(View.VISIBLE);
			btn_right_before.setVisibility(View.VISIBLE);
		}else{
			btn_right.setVisibility(View.GONE);
			btn_right_before.setVisibility(View.GONE);
		}
		
	
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index<ids.length-1){
					courseID=ids[index+1];
					index++;
					initData();
					
					btn_right.setEnabled(true);
					btn_right_before.setEnabled(true);
				}else{
					btn_right.setEnabled(false);
					btn_right_before.setEnabled(true);
					ToastUtil.showShort(CourseInfo.this, "已经是最后一条");
				}
			}
		});
		btn_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index>0){
					courseID=ids[index-1];
					index=index-1;
					initData();
					btn_right_before.setEnabled(true);
					btn_right.setEnabled(true);
				}else{
					btn_right_before.setEnabled(false);
					btn_right.setEnabled(true);
					ToastUtil.showShort(CourseInfo.this, "已经是最新一条");
				}
			}
		});
		
		
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select courseID from "+TableName.CLASS_SHEDULE +" where courseID=?", new String[]{""+courseID});
			if(c.getCount()>0){
				delete_btn.setText("从课表中移除");
				delete_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_red));
			}else{
				delete_btn.setText("添加课程到课表");
				delete_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_yellow));
			}
			
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(schedule){
//					Intent intent=new Intent(CourseInfo.this,Schedules.class);
					Intent intent=new Intent(CourseInfo.this,SchoolTimeTable.class);
					startActivity(intent);
					CourseInfo.this.finish();
				}else{
					Intent intent=new Intent(CourseInfo.this,AddCourseActivity.class);
					startActivity(intent);
					CourseInfo.this.finish();
				}
			}
		});
		
		edit_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(CourseInfo.this,EditCourseInfo.class);
				intent.putExtra("courseID", courseID);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		delete_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(delete_btn.getText().equals("从课表中移除")){
					try{
						ToastUtil.showShort(CourseInfo.this, "删除成功！");
						cEappApp.dbManager.deleteData(TableName.CLASS_SHEDULE, "courseID=?", new String[]{""+courseID});	
						ContentValues update=new ContentValues();
						update.put("comefrom", IntentValue.SERVER_COURSE);
						
						cEappApp.dbManager.updataData(TableName.COURSE, update, "id=?", new String[]{""+courseID});
					}catch(Exception e){
						e.printStackTrace();
					}
					delete_btn.setText("添加到课程表");
					delete_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_yellow));
				}else{
					try{
						ToastUtil.showShort(CourseInfo.this, "添加成功！");
						ContentValues values=new ContentValues();
						values.put("courseID", courseID);
						values.put("classID", -1);
						values.put("termID", cEappApp.getTermcnf().getId());
						
						cEappApp.dbManager.insertData(TableName.CLASS_SHEDULE, values);	
						ContentValues update=new ContentValues();
						update.put("comefrom", IntentValue.LOCAL_COURSE);
						
						cEappApp.dbManager.updataData(TableName.COURSE, update, "id=?", new String[]{""+courseID});
						
					}catch(Exception e){
						e.printStackTrace();
					}
					delete_btn.setText("从课表中移除");
					delete_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_red));
				}
				
			}
		});
	}
	
	public void initData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.COURSE +" where id=?", new String[]{""+courseID});
			course_name.setText(c.getString(c.getColumnIndex("name")));
			course_teacher.setText(c.getString(c.getColumnIndex("teacher")));	
			Cursor times=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.COURSE_TIMES+ " where id=?", new String[]{""+courseID});
			StringBuilder 	times_sb=new StringBuilder();
			for(int i=0;i<times.getCount();i++){
				times.moveToPosition(i);	
				if(times.getInt(times.getColumnIndex("end"))==times.getInt(times.getColumnIndex("start")))
				{
					times_sb.append(getTextWeek(times.getString(times.getColumnIndex("weeks"))).append("周").append(getWeekday(times.getInt(times.getColumnIndex("weekday")))).append("第").append(times.getInt(times.getColumnIndex("start"))).append("节"));
				}else{
					times_sb.append(getTextWeek(times.getString(times.getColumnIndex("weeks"))).append("周").append(getWeekday(times.getInt(times.getColumnIndex("weekday")))).append(times.getInt(times.getColumnIndex("start"))).append("-").append(times.getInt(times.getColumnIndex("end"))).append("节"));
				}		
				
				times_sb.append("@"+times.getString(times.getColumnIndex("classroom"))).append(System.getProperty( "line.separator"));
			}
			times.close();
			c.close();
			course_info.setText(times_sb.toString());
			
		}catch (Exception e) {
			// TODO: handle exception
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
