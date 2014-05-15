package com.e1858.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.SendModeType;
import com.e1858.database.TableName;
import com.e1858.utils.DateUtil;

public class SendMsgInfo extends BaseActivity{

	private long 			sendID;
	private TextView 		sendusers;
	private TextView 		content;
	private TextView 		time;
	private TextView        send_mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_msginfo);
		Intent intent=getIntent();
		sendID=intent.getLongExtra("sendID", -1);
		initView();	
		loadlocalData();
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("发送明细");
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		
		sendusers=(TextView)findViewById(R.id.users);
		content=(TextView)findViewById(R.id.content);
		send_mode=(TextView)findViewById(R.id.mode);
		time=(TextView)findViewById(R.id.time);
	}
	
	private void loadlocalData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.SUBMIT_MSG +" where id=?", new String[]{""+sendID});
			//设置text值
			String receiver=c.getString(c.getColumnIndex("receiver"));
			String content_str=c.getString(c.getColumnIndex("content"));
			String time_str=c.getString(c.getColumnIndex("time"));	
			int mode=c.getInt(c.getColumnIndex("mode"));			
			sendusers.setText(receiver);
			content.setText(content_str);
			time.setText(DateUtil.dateToZh(DateUtil.strToDatestrLong(time_str)));
			if(mode==SendModeType.MOBILESMS){
				send_mode.setText("发送方式：短信");
			}else if(mode==SendModeType.PUSHMSG){
				send_mode.setText("发送方式：推送");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
