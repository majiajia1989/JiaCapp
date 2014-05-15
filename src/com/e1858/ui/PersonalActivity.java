package com.e1858.ui;

import java.util.regex.Pattern;

import com.e1858.BaseMenuActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.RoleType;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Student;
import com.e1858.protocol.Teacher;
import com.e1858.protocol.http.EditStudentInfo;
import com.e1858.protocol.http.EditStudentInfoResp;
import com.e1858.protocol.http.EditTeacherInfo;
import com.e1858.protocol.http.EditTeacherInfoResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.ToastUtil;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PersonalActivity extends BaseMenuActivity {
	private EditText  		mobile;
	private EditText 	 	email;
		
	private TextView  		name;
	private TextView  		sex;
	
	private LinearLayout 	job_lay;
	private TextView  		professional;
	
	
	private TextView  		schoolname;
	private TextView  		depname;
	private TextView  		number;
	private LinearLayout 	class_lay;
	private TextView  		classname;
	
	private EditStudentInfo 	editStudentInfo;
	private EditStudentInfoResp editStudentInfoResp;
	
	private EditTeacherInfo     editTeacherInfo;
	private EditTeacherInfoResp editTeacherInfoResp;
	
	private Button    			modify_btn;
	
	private Teacher   			teacher;
	private Student   			student;
	private String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	private Pattern regex;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.personnal);
		doScroll(ModuleInteger.PERSONAL);	
		initView();
		regex = Pattern.compile(format);
		loadData();
	}

	private void initView(){
		
		title_name.setText("个人中心");
		mobile=(EditText)findViewById(R.id.mobile);
		email=(EditText)findViewById(R.id.email);
		mobile.setEnabled(false);
		email.setEnabled(false);
		name=(TextView)findViewById(R.id.name);		
		sex=(TextView)findViewById(R.id.sex);
		number=(TextView)findViewById(R.id.number);
		job_lay=(LinearLayout)findViewById(R.id.job_lay);
		professional=(TextView)findViewById(R.id.professional);
		
		schoolname=(TextView)findViewById(R.id.schoolname);
		depname=(TextView)findViewById(R.id.depname);
		class_lay=(LinearLayout)findViewById(R.id.class_lay);
		classname=(TextView)findViewById(R.id.classname);	
		
		modify_btn=(Button)findViewById(R.id.modify_btn);
		
		modify_btn.setText("修改基本信息");
		
		modify_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(modify_btn.getText().equals("修改基本信息")){
					mobile.setEnabled(true);
					email.setEnabled(true);	
					modify_btn.setText("保存");
					modify_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_yellow));
				}else if(modify_btn.getText().equals("保存")){
					if(mobile.getText().toString().trim().equals("")){
						ToastUtil.showShort(PersonalActivity.this, "手机号为空");
					}else if(email.getText().toString().trim().equals("")){
						ToastUtil.showShort(PersonalActivity.this, "邮箱为空");
					}else if(!(regex.matcher(email.getText().toString().trim()).matches())){
						ToastUtil.showShort(PersonalActivity.this, "邮箱格式不正确");
					}else{
						if(cEappApp.getRole()==RoleType.STUDENT){
							studentRequest();
						}else if(cEappApp.getRole()==RoleType.TEACHER){
							teacherRequest();
						}	
					}
				}
				
			}
		});
		/**
		btn_top_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mobile.getText().toString().trim().equals("")){
					ToastUtil.showShort(PersonalActivity.this, "手机号为空");
				}else if(email.getText().toString().trim().equals("")){
					ToastUtil.showShort(PersonalActivity.this, "邮箱为空");
				}else{
					if(cEappApp.getRole()==RoleType.STUDENT){
						studentRequest();
					}else if(cEappApp.getRole()==RoleType.TEACHER){
						teacherRequest();
					}	
				}
			}
		});
		*/
	}
	
	private void loadData(){
		if(cEappApp.getRole()==RoleType.STUDENT){
			mobile.setText(cEappApp.getStudent().getMobile());
			email.setText(cEappApp.getStudent().getEmail());
			name.setText(cEappApp.getStudent().getName());
			sex.setText(cEappApp.getStudent().getGender());
			number.setText(cEappApp.getStudent().getCode());
			schoolname.setText(cEappApp.getStudent().getSchoolName());
			depname.setText(cEappApp.getStudent().getDepName());
			class_lay.setVisibility(View.VISIBLE);
			classname.setText(cEappApp.getStudent().getClassName());
		}else if(cEappApp.getRole()==RoleType.TEACHER){
			mobile.setText(cEappApp.getTeacher().getMobile());
			email.setText(cEappApp.getTeacher().getEmail());
			name.setText(cEappApp.getTeacher().getName());
			sex.setText(cEappApp.getTeacher().getGender());
			job_lay.setVisibility(View.VISIBLE);
			professional.setText(cEappApp.getTeacher().getJobTitle());
			number.setText(cEappApp.getTeacher().getCode());
			schoolname.setText(cEappApp.getTeacher().getSchoolName());
			depname.setText(cEappApp.getTeacher().getDepName());
		}
	}

	private void teacherRequest(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("提交中...");
			teacher=cEappApp.getTeacher();
			teacher.setMobile(mobile.getText().toString().trim());
			teacher.setEmail(email.getText().toString().trim());
			editTeacherInfo=new EditTeacherInfo();
			editTeacherInfo.setUser(cEappApp.getUser());
			editTeacherInfo.setPass(cEappApp.getPass());
			editTeacherInfo.setTeacher(teacher);
			NetUtil.post(Constant.BASE_URL, editTeacherInfo, handler, MessageWhat.EDIT_TEACHERINFO_RESP);
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
		
	}
	private void studentRequest(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("提交中...");
			student=cEappApp.getStudent();
			student.setMobile(mobile.getText().toString().trim());
			student.setEmail(email.getText().toString().trim());
			editStudentInfo=new EditStudentInfo();
			editStudentInfo.setUser(cEappApp.getUser());
			editStudentInfo.setPass(cEappApp.getPass());
			editStudentInfo.setStudent(student);
			NetUtil.post(Constant.BASE_URL, editStudentInfo, handler, MessageWhat.EDIT_STUDENTINFO_RESP);
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
			case MessageWhat.EDIT_TEACHERINFO_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					editTeacherInfoResp=JsonUtil.fromJson((String)msg.obj, EditTeacherInfoResp.class);
					if(null==editTeacherInfoResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==editTeacherInfoResp.getStatus()){
						ToastUtil.showShort(this, "修改成功");
						mobile.setEnabled(false);
						email.setEnabled(false);
						modify_btn.setText("修改基本信息");
						modify_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_blue));
					}
					msg.obj=null;
				}
				break;
			case MessageWhat.EDIT_STUDENTINFO_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					editStudentInfoResp=JsonUtil.fromJson((String)msg.obj, EditStudentInfoResp.class);
					if(null==editStudentInfoResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==editStudentInfoResp.getStatus()){
						ToastUtil.showShort(this, "修改成功");
						mobile.setEnabled(false);
						email.setEnabled(false);
					}
					msg.obj=null;
				}
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
	
	
	
}
