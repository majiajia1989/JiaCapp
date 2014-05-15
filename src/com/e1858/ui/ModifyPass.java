package com.e1858.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.RoleType;
import com.e1858.network.NetUtil;
import com.e1858.protocol.http.ChangePass;
import com.e1858.protocol.http.ChangePassResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Pass.ceappPass;
import com.e1858.widget.ClearEditText;

public class ModifyPass extends BaseActivity {

	private   ClearEditText    	old_pass;
	private   ClearEditText    	new_pass;
	private   ClearEditText    	confirm_new;
	private   EditText          account;
	private   Button            btn_modify_btn;
	private   ChangePass  		changePass;
	private   ChangePassResp 	changePassResp;
	private   Button            button_back;
	private   String            old_pass_str,new_pass_str,new_pass_confirmstr;
	private   SharedPreferences sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_pass);
		sp=getSharedPreferences(Constant.USER_INFO, 0);
		initView();
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("修改密码");
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);
		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ModifyPass.this,Setting.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		old_pass=(ClearEditText)findViewById(R.id.old_pass);
		new_pass=(ClearEditText)findViewById(R.id.new_pass);
		confirm_new=(ClearEditText)findViewById(R.id.new_pass_confirm);
		account=(EditText)findViewById(R.id.account);
		if(cEappApp.getRole()==RoleType.STUDENT){
			account.setText("账号:"+cEappApp.getStudent().getCode());
		}else if(cEappApp.getRole()==RoleType.TEACHER){
			account.setText("账号:"+cEappApp.getTeacher().getCode());
		}
		
		
		btn_modify_btn=(Button)findViewById(R.id.btn_modifypass);
		btn_modify_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				old_pass_str=old_pass.getText().toString().trim();
				new_pass_str=new_pass.getText().toString().trim();
				new_pass_confirmstr=confirm_new.getText().toString().trim();
				
				if(old_pass_str.equals("")){
					Animation shake = AnimationUtils.loadAnimation(ModifyPass.this, R.anim.shake);
					findViewById(R.id.old_pass).startAnimation(shake); 
					ToastUtil.showShort(ModifyPass.this, "旧密码为空");
				}else if(new_pass_str.equals("")){
					Animation shake = AnimationUtils.loadAnimation(ModifyPass.this, R.anim.shake);
					findViewById(R.id.new_pass).startAnimation(shake); 
					ToastUtil.showShort(ModifyPass.this, "新密码为空");
				}else if(new_pass_confirmstr.equals("")){
					Animation shake = AnimationUtils.loadAnimation(ModifyPass.this, R.anim.shake);
					findViewById(R.id.new_pass_confirm).startAnimation(shake); 
					ToastUtil.showShort(ModifyPass.this, "新密码确认为空");
				}else if(!(new_pass_confirmstr.equals(new_pass_str))){
					ToastUtil.showShort(ModifyPass.this, "两次密码输入不一致");
				}else{
					requestModify();
				}
				
				
			}
		});
	}

	private void requestModify(){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("提交中...");
			changePass=new ChangePass();
			changePass.setUser(cEappApp.getUser());
			changePass.setPass(cEappApp.getPass());
			if(cEappApp.getRole()==RoleType.STUDENT){
				changePass.setNewPass(ceappPass.EncodePass(cEappApp.getStudent().getCode(), new_pass_str));
			}else if(cEappApp.getRole()==RoleType.TEACHER){
				changePass.setNewPass(ceappPass.EncodePass(cEappApp.getTeacher().getCode(), new_pass_str));
			}			
			NetUtil.post(Constant.BASE_URL, changePass, handler, MessageWhat.CHANGE_PASS_RESP);
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
			case MessageWhat.CHANGE_PASS_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					changePassResp=JsonUtil.fromJson((String)msg.obj, ChangePassResp.class);
				     Log.v("loginResp", (String)msg.obj);
					if(null==changePassResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==changePassResp.getStatus()){	
						ToastUtil.showShort(this, "密码修改成功！");
						
						Editor editor = sp.edit();  
						editor.putString(Constant.PASSWORD, new_pass_str);
						editor.putBoolean(Constant.IS_AUTO_LOGIN, true);
						editor.commit();
						 
						Disconnect disconnect = new Disconnect();
						cEappApp.getSocketSession().write(disconnect);
						cEappApp.setFirstScroll(true);
						 
						Intent intent=new Intent(ModifyPass.this,LoginActivity.class);
						startActivity(intent);
						this.finish();
						
						
					}else{
						ToastUtil.showShort(this,changePassResp.getErr());
					}
				}
				break;
		}
		
		return false;
	}
	
	
	
}
