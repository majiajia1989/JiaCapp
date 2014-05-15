package com.e1858.ui;

import java.util.regex.Pattern;

import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.network.NetUtil;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.ResetPass;
import com.e1858.protocol.http.ResetPassResp;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.ClearEditText;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.e1858.BaseActivity;


public class ResetPassActivity extends BaseActivity {

    private 	Button         	btn_resetpass;
    private     ClearEditText   email;
    private     EditText	    account;
    		
    private 	ResetPass    	resetPass;
    private 	ResetPassResp  	resetPassResp;
    
    private     String 			emailStr;
    private     String          accountStr;
    private     String 			format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	private     Pattern 		regex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_pass);
		regex=Pattern.compile(format);
		Intent intent=getIntent();
		accountStr=intent.getStringExtra(Constant.USERNAME);
		initView();
	}

	public void resetPassRequest(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("重置密码到邮箱,请稍后...");
			resetPass=new ResetPass();
			resetPass.setSchool(cEappApp.getSchoolID());
			resetPass.setUserName(accountStr);
			resetPass.setEmail(emailStr);
			NetUtil.post(Constant.BASE_URL, resetPass, handler, MessageWhat.RESET_PASS_RESP);
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
	}
	
	 public static boolean isMobileNO(String mobile) { 
		 	if(mobile.startsWith("13")||mobile.startsWith("15")||mobile.startsWith("18")){
		 		return true;
		 	}
		 	return false;
	    } 
	public void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText(getResources().getString(R.string.reset_pass));
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_resetpass=(Button)findViewById(R.id.btn_resetpass);
		email=(ClearEditText)findViewById(R.id.email);
		account=(EditText)findViewById(R.id.account);
		account.setText("账号："+accountStr);
		btn_resetpass.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailStr=email.getText().toString().trim();
				
				if(emailStr.equals("")){
					Animation shake = AnimationUtils.loadAnimation(ResetPassActivity.this, R.anim.shake);
					findViewById(R.id.email).startAnimation(shake); 
					ToastUtil.showShort(ResetPassActivity.this, "邮箱为空");
				}else if(!(regex.matcher(emailStr).matches())){
					ToastUtil.showShort(ResetPassActivity.this, "邮箱格式不正确");
				}else{
					resetPassRequest();	
				}
				
				/**
				if(emailStr.equals("")){
					Animation shake = AnimationUtils.loadAnimation(ResetPassActivity.this, R.anim.shake);
					findViewById(R.id.mobile).startAnimation(shake); 
					ToastUtil.showShort(ResetPassActivity.this, "手机号为空");
				}else if(emailStr.length()!=11){
					ToastUtil.showShort(ResetPassActivity.this, "请输入正确的手机号");
				}else if(!isMobileNO(emailStr)){
					ToastUtil.showShort(ResetPassActivity.this, "请输入已注册的手机号");
				}else{
					resetPassRequest();
				}
				*/
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
			case MessageWhat.RESET_PASS_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					resetPassResp=JsonUtil.fromJson((String)msg.obj, ResetPassResp.class);
				     Log.v("loginResp", (String)msg.obj);
					if(null==resetPassResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==resetPassResp.getStatus()){
						ToastUtil.showShort(this, "重置密码成功，请到邮箱查收!");		
						/**
						Intent intent=new Intent(ResetPassActivity.this,LoginActivity.class);
						startActivity(intent);
						this.finish();
						*/
					}else{
						ToastUtil.showShort(this, resetPassResp.getErr());
					}
				}
				break;
		}
		
		return super.handleMessage(msg);
	}
	
	
	
}
