package com.e1858.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.RoleType;

public class AddSendMsgUsersRole extends BaseActivity {

	private RelativeLayout 	teacher_resp;
	private RelativeLayout 	student_resp;
	private int            	roleType;
	private Button          button_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact_role);
		initView();
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("选择收件人");
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		button_back.setVisibility(View.VISIBLE);
	
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setText("完成");
		btn_right.setVisibility(View.VISIBLE);
		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddSendMsgUsersRole.this,AddNewMessage.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			}
		});
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddSendMsgUsersRole.this,AddNewMessage.class);
				startActivity(intent);
				finish();
			}
		});
		
		teacher_resp=(RelativeLayout)findViewById(R.id.teacher_resp);
		student_resp=(RelativeLayout)findViewById(R.id.student_resp);
		teacher_resp.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				roleType=RoleType.TEACHER;
				Intent intent=new Intent(AddSendMsgUsersRole.this,AddSendMsgUsersDep.class);
				intent.putExtra("roleType", roleType);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		student_resp.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				roleType=RoleType.STUDENT;
				Intent intent=new Intent(AddSendMsgUsersRole.this,AddSendMsgUsersDep.class);
				intent.putExtra("roleType", roleType);
				startActivity(intent);
				finish();
				
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
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
