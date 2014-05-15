package com.e1858.ui;

import java.util.ArrayList;
import java.util.List;

import com.e1858.BaseActivity;
import com.e1858.adapter.ViewPagerAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.DownloadType;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.UIHelper;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Course;
import com.e1858.protocol.CourseTimes;
import com.e1858.protocol.http.GetSchoolTimeTable;
import com.e1858.protocol.http.GetSchoolTimeTableResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.AsyncImageLoader;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.StringUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout; 


public class LogoActivity extends BaseActivity implements OnClickListener,OnTouchListener,OnPageChangeListener{
	
	private ViewPager 			viewPager;
	private ViewPagerAdapter 	viewPagerAdapter;
	private List<View> 			views;
	private ImageView[] 		dots;	
	private int 				currentIndex;	
	private int 				lastX = 0;	
	private AsyncImageLoader	asyncImageLoader;
	
	private String 				logopics;
	
	private String 				logopic[];
	private GetSchoolTimeTable  getSchoolTimeTable;
	private GetSchoolTimeTableResp getSchoolTimeTableResp;
	private List<Course> 				server_courses=new ArrayList<Course>();
	private List<CourseTimes> 			server_coursetimes=new ArrayList<CourseTimes>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		
		asyncImageLoader=new AsyncImageLoader(cEappApp, getResources().getDrawable(R.drawable.start_background));
		
		views = new ArrayList<View>();
		
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
	
		
		logopics=StringUtils.ConvertDataType(cEappApp.getSchool().getPics()).toString();
		
		logopic=logopics.split(",");
		
		for (int i = 0; i < logopic.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setScaleType(ScaleType.CENTER_CROP);
			asyncImageLoader.loadDrawable(iv, DownloadType.LOGO_PICTURE, Integer.valueOf(logopic[i]),true,false,true);
			views.add(iv);
		}
		viewPager = (ViewPager) findViewById(R.id.guide_pager);
		viewPagerAdapter = new ViewPagerAdapter(views);
		viewPager.setOnTouchListener(this);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
	
		initBottomDots();
		
		judgeTerm();
	
	}
	private void judgeTerm(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select termID  from "+TableName.CLASS_SHEDULE, null);
			if(c.getCount()!=0){	
				c.moveToPosition(c.getCount()-1);
				int termID=c.getInt(c.getColumnIndex("termID"));
				if(termID==cEappApp.getTermcnf().getId()){
					return;
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
	
	private void loadData(){
		getSchoolTimeTable=new GetSchoolTimeTable();
		getSchoolTimeTable.setUser(cEappApp.getUser());
		getSchoolTimeTable.setPass(cEappApp.getPass());
		getSchoolTimeTable.setClasse(cEappApp.getStudent().getClasse());
		NetUtil.post(Constant.BASE_URL, getSchoolTimeTable, handler, MessageWhat.GET_SCHOOLTIMETABLE_RESP);	
	}
	
	private void initBottomDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		dots = new ImageView[logopic.length];
		LinearLayout.LayoutParams mParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		
		for (int i = 0; i < logopic.length; i++) {
			dots[i]=new ImageView(this);
			dots[i].setLayoutParams(mParams);
			dots[i].setClickable(true);
			dots[i].setPadding(5, 5, 5, 5);
			dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot_selector));
			dots[i].setEnabled(true);
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);	
			ll.addView(dots[i]);
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);
	}
	
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}
	
	public void onPageScrollStateChanged(int state) {
	}
	
	public void onPageScrolled(int index, float arg1, int dis) {
	}
	
	public void onPageSelected(int index) {
		setCurDot(index);
	}
	
	private void setCurDot(int positon) {
		if (positon < 0 || positon > logopic.length - 1 || currentIndex == positon) {
			return;
		}
		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = positon;
	}
	
	private void setCurView(int position) {
		if (position < 0 || position >= logopic.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int)event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if((lastX - event.getX()) >100 && (currentIndex == views.size() -1)){
				Intent intent=new Intent();
				intent.setClassName(this, UIHelper.doIntent(0));
				startActivity(intent);
				this.finish();
			}
			break;
		default:
			break;
		}
		return false;
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			return true;
		}
		switch (msg.what) {
		case MessageWhat.GET_SCHOOLTIMETABLE_RESP:
			if(null!=(String)msg.obj){
				getSchoolTimeTableResp=JsonUtil.fromJson((String)msg.obj, GetSchoolTimeTableResp.class);
				Log.v("schedule", "=="+(String)msg.obj);
				if(null==getSchoolTimeTableResp){
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == getSchoolTimeTableResp.getStatus()){	
					server_courses=getSchoolTimeTableResp.getCourses();	
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
				}
				
				msg.obj=null;
			}
			break;
		}
		
		return false;
	}
	
}
