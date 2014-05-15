package com.e1858.ui;

import java.util.Calendar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.SendStatusTyp;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Notice;
import com.e1858.protocol.http.AddNotice;
import com.e1858.protocol.http.AddNoticeResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.wheel.NumericWheelAdapter;
import com.e1858.widget.wheel.OnWheelChangedListener;
import com.e1858.widget.wheel.OnWheelScrollListener;
import com.e1858.widget.wheel.WheelView;

public class AddNewNotice extends BaseActivity {

	private EditText 						notice_title;
	private EditText 						notice_content;
	private TextView  						start_time;
	private TextView  						end_time;
	
	private Notice   						notice;
	
	private AddNotice 						addNotice;
	private AddNoticeResp 					addNoticeResp;
	
	private int 							curyear ;
	private int 							curMinutes ;
	private int 							curDays ;
	private int 							poor;
	private int    							dayNum;
	private WheelView 						yeardialog;
	private WheelView						monthdialog;
	private WheelView 						daydialog;
	private Button 							okBtn,cancelBtn;
	boolean   appnotice=false;
	boolean   mobilenotice=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_newnotice);
		initView();
	}

	private void initView(){
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		btn_back.setVisibility(View.VISIBLE);
		title=(TextView)findViewById(R.id.title);
		title.setText("编辑公告");
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setText("发送");
		btn_right.setVisibility(View.VISIBLE);
		notice_title=(EditText)findViewById(R.id.notice_title);
		notice_content=(EditText)findViewById(R.id.content);
		start_time=(TextView)findViewById(R.id.start_time);
		end_time=(TextView)findViewById(R.id.end_time);
		
		start_time.setText(DateUtil.getStringDateShort().replace("-", "."));
		
		end_time.setText(DateUtil.getStringDateShort().replace("-", "."));
		
		start_time.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog(start_time);
			}
		});
		
		end_time.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog(end_time);
			}
		});
		
		
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(notice_title.getText().toString().trim().equals("")){
					ToastUtil.showShort(AddNewNotice.this, "公告标题不能为空");
				}else if(start_time.getText().toString().trim().equals("")){
					ToastUtil.showShort(AddNewNotice.this, "有效期为空");
				}else if(!(start_time.getText().toString().trim().equals(""))&&!(end_time.getText().toString().trim().equals(""))){
					if((Long.parseLong(end_time.getText().toString().trim().replace(".", "")+"235959")-(Long.parseLong(start_time.getText().toString().trim().replace(".", "")+"000000")))<0){
						ToastUtil.showShort(AddNewNotice.this, "结束日期小于开始日期");
					}else if(notice_content.getText().toString().trim().equals("")){
						ToastUtil.showShort(AddNewNotice.this, "公告内容不能为空");
					}else{
						sendNoticeRequest();
					}
				}else if(notice_content.getText().toString().trim().equals("")){
					ToastUtil.showShort(AddNewNotice.this, "公告内容不能为空");
				}else{
					sendNoticeRequest();
				}
				
			}
		});
	}
	
	private void createDialog(final TextView editText){
		
		final Dialog dialog = new Dialog(AddNewNotice.this,android.R.style.Theme_Translucent_NoTitleBar);
	   
		dialog.setContentView(R.layout.data_picker_dialog);

		Calendar c = Calendar.getInstance();
		
		curyear = c.get(Calendar.YEAR);
		curMinutes = c.get(Calendar.MONTH);
		curDays = c.get(Calendar.DAY_OF_MONTH);
		
		poor = curyear + 100; 

	    yeardialog=(WheelView)dialog.findViewById(R.id.yeardialog);
	 
	    yeardialog.setAdapter(new NumericWheelAdapter(c.get(Calendar.YEAR),2113));
	   
	    yeardialog.setLabel("年");
	   
//	    yeardialog.setCyclic(true);
	   
	    yeardialog.setCurrentItem(curyear);
	   
	    
	    monthdialog=(WheelView)dialog.findViewById(R.id.monthdialog);
	
	    monthdialog.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
	    monthdialog.setLabel("月");
	    monthdialog.setCyclic(true);
		curMinutes += 1;
		
		
		daydialog = (WheelView)dialog.findViewById(R.id.daydialog);
		dayNum = setwheelDay(curyear, curMinutes);
		daydialog.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
		daydialog.setLabel("日");
		daydialog.setCyclic(true);

		
		
		yeardialog.setCurrentItem(curyear-94);
		monthdialog.setCurrentItem(curMinutes-1);
		daydialog.setCurrentItem(curDays-1);
	
	
		addChangingListener(monthdialog, "月");
		addChangingListener(yeardialog, "年");
		addChangingListener(daydialog, "日");
		
		OnWheelScrollListener scrollListener = new OnWheelScrollListener()
		{
			public void onScrollingStarted(WheelView wheel)
			{
			}

			public void onScrollingFinished(WheelView wheel)
			{
				if (wheel.getId() == R.id.yeardialog)
				{				
					
					dayNum = setwheelDay(yeardialog.getCurrentItem()-poor, monthdialog.getCurrentItem()+1);
					daydialog.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
				}
				else if (wheel.getId() == R.id.monthdialog)
				{			
					dayNum = setwheelDay(yeardialog.getCurrentItem()-poor, monthdialog.getCurrentItem()+1);
					daydialog.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
				}
				else if (wheel.getId() == R.id.daydialog)
				{
					curDays = daydialog.getCurrentItem();				
				}
				
			}
		};
		
		yeardialog.addScrollingListener(scrollListener);
		monthdialog.addScrollingListener(scrollListener);
		daydialog.addScrollingListener(scrollListener);
        okBtn=(Button)dialog.findViewById(R.id.button_ok);
        okBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText.setText((yeardialog.getCurrentItem()+2013)+"."+String.format("%02d", (monthdialog.getCurrentItem()+1))+"."+String.format("%02d",(daydialog.getCurrentItem()+1)));
				dialog.dismiss();
			}
		});
        cancelBtn=(Button)dialog.findViewById(R.id.button_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		dialog.show();
		
	
	}
	
	private void sendNoticeRequest(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("正在发送....");
			
			addNotice=new AddNotice();
			
			notice=new Notice();
			
			notice.setId(-1);
			notice.setTitle(notice_title.getText().toString().trim());
			notice.setUrl(String.valueOf(-1));
			
			notice.setContent(notice_content.getText().toString().trim());

			String beginTime=DateUtil.strToServerDate(start_time.getText().toString().trim().replace(".", "")+"000000");
			
			notice.setBegin(beginTime);
			
			if(end_time.getText().toString().equals("")){
				notice.setEnd(null);

			}else{
				String endTime=DateUtil.strToServerDate(end_time.getText().toString().trim().replace(".", "")+"235959");
				notice.setEnd(endTime);	
		
			}
			
			
			addNotice.setUser(cEappApp.getUser());
			addNotice.setPass(cEappApp.getPass());
			addNotice.setNotice(notice);
	
			NetUtil.post(Constant.BASE_URL, addNotice, handler, MessageWhat.ADD_NOTICE_RESP);
		
			
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
			case MessageWhat.ADD_NOTICE_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					Log.v("sendNotice", "==="+(String)msg.obj);
					addNoticeResp=JsonUtil.fromJson((String)msg.obj, AddNoticeResp.class);
					if(null==addNoticeResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==addNoticeResp.getStatus()){
						long newID=addNoticeResp.getNotice();
						ContentValues values=new ContentValues();
						values.put("id", newID);
						values.put("title", notice_title.getText().toString().trim());
						values.put("content", notice_content.getText().toString().trim());
						values.put("url", "-1");
						String beginTime=DateUtil.strToServerDate(start_time.getText().toString().trim().replace(".", "")+"000000");
						
						values.put("begin",beginTime);
						
						if(end_time.getText().toString().equals("")){
							values.put("end", "");
						}else{
							String endTime=DateUtil.strToServerDate(end_time.getText().toString().trim().replace(".", ""+"235959"));
							values.put("end", endTime);
						}
						values.put("time", DateUtil.getStringToday());
						
						values.put("status", SendStatusTyp.SUCCESS);
						
						
						cEappApp.dbManager.insertData(TableName.NOTICE, values);		
						//弹出是否发送短消息或者直接跳转
						Intent intent=new Intent(AddNewNotice.this,NoticeActivity.class);
						startActivity(intent);
						this.finish();
				
					}else{
						ToastUtil.showShort(this, "公告发布失败！");
						btn_right.requestFocus();
					}
					msg.obj=null;
				}
				break;
		}
		return false;
	}
	
	
	private int setwheelDay(int year, int month)
	{
		int day = 31;
		if (month == 2)
		{
			if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0)))
			{
				day = 29;
			}
			else
			{
				day = 28;
			}
		}
		if (month == 4 || month == 6 || month == 9 || month == 11)
		{
			day = 30;
		}
		return day;
	}

	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel.setLabel(newValue != 1 ? label  : label);
			}
		});
	}
}
