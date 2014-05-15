package com.e1858.ui;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.RoleType;
import com.e1858.common.UIHelper;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Student;
import com.e1858.protocol.http.GetStudentInfo;
import com.e1858.protocol.http.GetStudentInfoResp;
import com.e1858.protocol.http.GetTeacherInfo;
import com.e1858.protocol.http.GetTeacherInfoResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.Login;
import com.e1858.protocol.http.LoginResp;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Pass.ceappPass;
import com.e1858.widget.ClearEditText;

public class LoginActivity extends BaseActivity{
	
	private	 	ClearEditText  		account;
	private 	ClearEditText 		password;
	private 	Button    			btn_login;
	private     Button      		btn_forgetpass;
	private     Button              btn_about;
	
	private 	Login     			loginprotocol;
	private 	LoginResp 			loginResp;
	
	
	private     GetStudentInfo 		getStudentInfo;
	private     GetStudentInfoResp 	getStudentInfoResp;
	private     Student         	student;
	
	private     GetTeacherInfo 		getTeacherInfo;
	private     GetTeacherInfoResp 	getTeacherInfoResp;
	
	
	private 	String 				account_str;
	private 	String 				password_str;
	
	private 	SharedPreferences 	spPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		spPreferences = getSharedPreferences(Constant.USER_INFO, 0);
		respBackButton=true;
		initView();
		if (!cEappApp.isNetworkAvailable())
		{
			ToastUtil.showShort(LoginActivity.this,getResources().getString(R.string.network_fail));
		}
		else
		{
			if (NetUtil.isMobileActive(this) && !NetUtil.isWifiActive(this))
			{
				ToastUtil.showShort(LoginActivity.this, getResources().getString(R.string.network_mobile));
			}
		}		
		if(isAutoLogin()){
			loginRequest();
		}
	}
	
	public boolean isAutoLogin(){
		boolean flag=false;
		boolean isAutoLogin=spPreferences.getBoolean(Constant.IS_AUTO_LOGIN, false);
		account_str=spPreferences.getString(Constant.USERNAME, "");
		password_str=spPreferences.getString(Constant.PASSWORD, "");
		account.setText(account_str);
		password.setText(password_str);	
		if(isAutoLogin){				
			flag=true;
		}
		return flag;
	}
	
	private void initView(){
		account=(ClearEditText)findViewById(R.id.account);
		password=(ClearEditText)findViewById(R.id.password);
		btn_login=(Button)findViewById(R.id.btn_login);
		btn_forgetpass=(Button)findViewById(R.id.btn_forgetpass);
		btn_about=(Button)findViewById(R.id.btn_about);

		account.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!account.getText().toString().trim().equals(account_str)){
					password.setText("");
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_forgetpass.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				account_str=account.getText().toString().trim();
				if(account_str.equals("")){
					Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
					findViewById(R.id.account).startAnimation(shake); 
					ToastUtil.showShort(LoginActivity.this, "账号为空");
				}else{
					
					Intent intent=new Intent(LoginActivity.this,ResetPassActivity.class);
					intent.putExtra(Constant.USERNAME, account_str);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);	
					
				}
			}
		});
		
		btn_about.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
				
		btn_login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				judgeLogin();
				
			}
		});
	}
	public void judgeLogin(){
		account_str=account.getText().toString().trim();
		password_str=password.getText().toString().trim();
		if(account_str.equals("")){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
			findViewById(R.id.account).startAnimation(shake); 
			ToastUtil.showShort(this, "账号为空");
		}else if(account_str!=null&password_str.equals("")){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
			findViewById(R.id.password).startAnimation(shake); 
			ToastUtil.showShort(this, "密码为空");
		}else if(account_str!=null&password_str!=null){		
			loginRequest();
		}
	}
	public boolean judgeVersion(){
		try{
			PackageInfo packageInfo =getPackageManager().getPackageInfo(getPackageName(), 0);
			int currentVer=packageInfo.versionCode;
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			int lastVer=prefs.getInt(Constant.VERSION_KEY,0);
			if(currentVer>lastVer){
				prefs.edit().putInt(Constant.VERSION_KEY,currentVer).commit();  
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		return false;
	}
	
	
	public void loginRequest(){
		
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("正在登录...");
			loginprotocol=new Login();
			loginprotocol.setUserName(account_str);
//			loginprotocol.setPass(password_str);
			//对密码进行加密
			loginprotocol.setPass(ceappPass.EncodePass(account_str, password_str));
			
			Log.v("encodepass", "=="+ceappPass.EncodePass(account_str, password_str));
			
			loginprotocol.setSchool(cEappApp.getSchoolID());	

			
			NetUtil.post(Constant.BASE_URL,loginprotocol, handler, MessageWhat.LOGIN_RESP);
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}

	}
	@Override
	public boolean handleMessage(Message msg) {
        if (null == msg.obj)
        {
            closeProgressDialog();
            return true;
        }
        switch (msg.what)
        {
            case MessageWhat.LOGIN_RESP:
            	
                if (null != (String) msg.obj)
                {
                    closeProgressDialog();
                    loginResp = JsonUtil.fromJson((String) msg.obj, LoginResp.class);
                    Log.v("loginResp", (String)msg.obj);
                    if(null==loginResp)
                    {
                    	break;
                    }
                    if (HttpDefine.RESPONSE_SUCCESS == loginResp.getStatus())
                    {
                    	cEappApp.setLoginOK(true);         
                    
                    	Editor editor = spPreferences.edit();                      
                        editor.putString(Constant.USERNAME, account_str);
                        editor.putString(Constant.PASSWORD, password_str);
                        editor.putBoolean(Constant.IS_AUTO_LOGIN, true);
                        editor.commit();
                        
                        cEappApp.setUser(loginResp.getUser());
                        cEappApp.setUserName(account_str);
                        cEappApp.setPass(ceappPass.EncodePass(account_str, password_str));
                        cEappApp.setRole(loginResp.getLevel());
                        cEappApp.setServer(loginResp.getServer());              
                        cEappApp.setSchool(loginResp.getSchool());
                        cEappApp.setPersonID(loginResp.getPersonID()); 
                        cEappApp.setTermcnf(loginResp.getSchoolCnf().getTermCnf());
                        cEappApp.setModules(loginResp.getSchoolCnf().getModules());
                        cEappApp.setMaxSession(loginResp.getSchoolCnf().getTermCnf().getMaxSession());
                        	
                        if(loginResp.getLevel()==RoleType.STUDENT){
                        	getStudentInfo=new GetStudentInfo();
                    		getStudentInfo.setUser(cEappApp.getUser());
                    		getStudentInfo.setPass(cEappApp.getPass());
                    		getStudentInfo.setStudent(loginResp.getPersonID());  
                    		NetUtil.post(Constant.BASE_URL, getStudentInfo, handler, MessageWhat.GET_STUDENTINFO_RESP);
                    
                        }else if(loginResp.getLevel()==RoleType.TEACHER){
                    		getTeacherInfo=new GetTeacherInfo();
                    		getTeacherInfo.setUser(cEappApp.getUser());
                    		getTeacherInfo.setPass(cEappApp.getPass());

                    		getTeacherInfo.setTeacher(loginResp.getPersonID());
                    		NetUtil.post(Constant.BASE_URL, getTeacherInfo,handler,MessageWhat.GET_TEACHERINFO_RESP);
                    	} 
                        
                      
                        
                        Constant.SOCKET_ACTIVETEST_INTERVAL=loginResp.getServer().getActiveInterval();
                       
                        cEappApp.getSocketSession().setIP(loginResp.getServer().getIp());
                        cEappApp.getSocketSession().setPort(loginResp.getServer().getPort());     
                        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
                       //测试版本中可以存在，发布版本时一定要关闭掉strictmode 
                        try{
                        	 cEappApp.getSocketSession().open();      
                        }catch (Exception e) {
							 //TODO: handle exception
                        	e.printStackTrace();
						}
                           
                        
                        if(judgeVersion()){                   	
                        	  if(loginResp.getSchool().getPics()==null||loginResp.getSchool().getPics().size()==0){
                        		  Intent intent=new Intent(); 
                        		  try{
                              		Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.MODULE +"  where home!=?", new String[]{-1+""});
                              		if(c.getCount()==0){
                              			cEappApp.setHomeID(0);
                              			cEappApp.setHomeName("消息中心");
                              			intent.setClassName(this, UIHelper.doIntent(0));
                                      	startActivity(intent);
                                      	this.finish();
                              		}else{
                              			int homeid=c.getInt(c.getColumnIndex("id"));
                              			cEappApp.setHomeID(homeid);
                              			cEappApp.setHomeName(c.getString(c.getColumnIndex("name")));
                              			intent.setClassName(this, UIHelper.doIntent(homeid));
                                      	startActivity(intent);
                                      	this.finish();
                              		}
                              		c.close();
                              	}catch (Exception e) {
      								// TODO: handle exception
                              		e.printStackTrace();
      							}
                        	  }else{
                        		  Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
                                  startActivity(intent);
                                  this.finish(); 
                        	  }
                        	  
                        }else{
                        	//判断那一个是第一屏	
                        	Intent intent=new Intent();
                        	try{
                        		Cursor c=cEappApp.dbManager.queryData2Cursor("select id,name from "+TableName.MODULE +" where home!=?", new String[]{-1+""});
                        		if(c.getCount()==0){
                        			cEappApp.setHomeID(0);
                        			cEappApp.setHomeName("消息中心");
                        			intent.setClassName(this, UIHelper.doIntent(0));
                                	startActivity(intent);
                                	this.finish();
                        		}else{
                        			int homeid=c.getInt(c.getColumnIndex("id"));
                        			cEappApp.setHomeID(homeid);
                        			cEappApp.setHomeName(c.getString(c.getColumnIndex("name")));
                        			intent.setClassName(this, UIHelper.doIntent(homeid));
                                	startActivity(intent);
                                	this.finish();
                        		}
                        		c.close();
                        	}catch (Exception e) {
								// TODO: handle exception
                        		e.printStackTrace();
							}
                        	
                        }
                        
                      
                    }else{
                    		
                    	  if(loginResp.getStatus()==10010001){
                    		  account.setText("");
                              password.setText("");
                              account.requestFocus();
                    	  }else{		  
                              password.setText("");
                              password.requestFocus();
                          }
                          ToastUtil.showAtCenterShort(LoginActivity.this, loginResp.getErr());
                    }
                    msg.obj = null;
                }
                break;
           case MessageWhat.GET_STUDENTINFO_RESP:
        	   if (null != (String) msg.obj)
               {  		
        		   Log.v("getStudentInfoResp", (String)msg.obj);
                   
        		   getStudentInfoResp = JsonUtil.fromJson((String) msg.obj, GetStudentInfoResp.class);
                   
                   if(null==getStudentInfoResp)
                   {
                   	break;
                   }
                   if (HttpDefine.RESPONSE_SUCCESS == getStudentInfoResp.getStatus()){
                	   student=getStudentInfoResp.getStudent();
                	   cEappApp.setStudent(student);
                	   
                   }
                   msg.obj=null; 
               }     	
            	break;
           case MessageWhat.GET_TEACHERINFO_RESP:
        	   if(null!=(String)msg.obj){
        		   getTeacherInfoResp=JsonUtil.fromJson((String)msg.obj, GetTeacherInfoResp.class);
        		   Log.v("getTeacherInfoResp", (String)msg.obj);
        		   if(null==getTeacherInfoResp){
        			   break;
        		   }
        		   if(HttpDefine.RESPONSE_SUCCESS==getTeacherInfoResp.getStatus()){
        			   cEappApp.setTeacher(getTeacherInfoResp.getTeacher());
        		   }
        		   msg.obj=null;
        	   }
        	   break;
        	
        }
        return false;
    }
}
