package com.e1858;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.ui.MessageActivity;
import com.e1858.ui.NoticeActivity;
import com.e1858.utils.AppUtil;
import com.e1858.utils.Exit;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements Handler.Callback{

	protected TextView   		title;
	protected Button  		 	btn_back;
	protected Button   			btn_right;
	protected Button            btn_right_before;
	
	protected CEappApp 			cEappApp;
	protected Handler  			handler;
	protected NotificationInfo  notificationInfo;	
	public    StringBuffer 		stringBuffer = new StringBuffer();
	private   ProgressDialog 	progressDialog;
	protected boolean			respBackButton	= false;
	private   Exit 				exit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(null==cEappApp){
			cEappApp = (CEappApp) this.getApplication();
		}
		if (null == handler)
		{
			handler = new Handler(this);
			cEappApp.setCurrentHandler(handler);
		}
		cEappApp.setCurrentActivity(BaseActivity.this);
		exit=new Exit();
		AppManager.getAppManager().addActivity(this);
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onStart"));
		cEappApp.setCurrentHandler(handler);
		cEappApp.setCurrentActivity(this);
		if (null != btn_back)
		{
			btn_back.setOnClickListener(this.button_backOnClickListener);
		}
	}
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onRestart"));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onResume"));
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onPause"));
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onStop"));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
		Log.d(Constant.TAG_ACTIVITY_LIFE, this.getClass().getName().concat(" onDestroy"));
	}
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			return true;
		}
		switch (msg.what)
		{
			case MessageWhat.NETWORK_OPENED:
				break;
			case MessageWhat.NETWORK_CLOSED:
				break;
			case MessageWhat.NETWORK_MOBILE_OPENED:
				break;
			case MessageWhat.NETWORK_MOBILE_CLOSED:
				break;
			case MessageWhat.NETWORK_WIFI_OPENED:
				break;
			case MessageWhat.NETWORK_WIFI_CLOSED:
				break;
			case MessageWhat.OPEN_PROGRESS_DIALOG:
				if (null == progressDialog)
				{
					progressDialog = new ProgressDialog(this);
					progressDialog.setCancelable(false);
				}
				progressDialog.setMessage((String) msg.obj);
				progressDialog.show();
				break;
			case MessageWhat.CLOSE_PROGRESS_DIALOG:
				if (null != progressDialog)
				{
					progressDialog.dismiss();
				}
				break;
			case MessageWhat.DISCONNECT_RESP:
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
		return true;
	}
	 public void openProgressDialog(final String message)
	    {
	        if (null == progressDialog)
	        {
	            progressDialog = new ProgressDialog(this);
	            progressDialog.setCancelable(false);
	            progressDialog.setCanceledOnTouchOutside(false);
	        }
	        progressDialog.setMessage(message);
	        progressDialog.show();
	    }

	 public void closeProgressDialog()
	 {
		 if (null != progressDialog)
		 {
			 progressDialog.dismiss();
		 }
	 }
	 private View.OnClickListener button_backOnClickListener	= new View.OnClickListener()
	 {
		 public void onClick(View v)
		 {
			 AppUtil.sendKey(KeyEvent.KEYCODE_BACK);
			 
		 }
	 };
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)
	 {
		 // TODO Auto-generated method stub
		 if (respBackButton &KeyEvent.KEYCODE_BACK == keyCode & cEappApp.isCEApp())
		 {
			 /**
			 AlertDialog.Builder builder = new AlertDialog.Builder(cEappApp.getCurrentActivity());
			 builder.setMessage("确认退出吗？");
			 builder.setTitle("提示");
			 builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
			 {
				 public void onClick(DialogInterface dialog, int which)
				 {
					 Disconnect disconnect = new Disconnect();
					 cEappApp.getSocketSession().write(disconnect);
					 System.exit(0);
				 }
			 });
			 builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			 {
				 public void onClick(DialogInterface dialog, int which)
				 {
					 dialog.dismiss();
				 }
			 });
			 builder.create().show();
			 return true;
			 */
			 
			 if(exit.isExit()){
				 	if(cEappApp.getSocketSession().isOpened()){
				 		Disconnect disconnect = new Disconnect();
					 	cEappApp.getSocketSession().write(disconnect);
					 	AppManager.getAppManager().AppExit(getApplicationContext());
//					 	System.exit(0);
				 	}else{
				 		AppManager.getAppManager().AppExit(getApplicationContext());
//				 		System.exit(0);
				 	}
				
				}else{
					ToastUtil.showShort(getApplicationContext(), "再按一次返回键回到桌面");
					//退出
					exit.doExitInOneSecond();
				
				}    
				return true;   
		 }
		 else
		 {
			 return super.onKeyDown(keyCode, event);
		 }
	 }
}
