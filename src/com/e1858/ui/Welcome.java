package com.e1858.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.MobileOSType;
import com.e1858.network.NetUtil;
import com.e1858.protocol.http.CheckVersion;
import com.e1858.protocol.http.CheckVersionResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.AppUtil;
import com.e1858.utils.FileUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ThreadPool;
import com.e1858.widget.RoundProgressBar;
public class Welcome extends BaseActivity {
	private AssetManager  		assetManager;
	private CheckVersion  		checkVersion;
	private CheckVersionResp 	checkVersionResp;
	private ProgressBar         progressBar;
//	private RoundProgressBar    progressBar;
	private int                 progressValue;
	private boolean 			interceptFlag = false;
	private Dialog 				downloadDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		View view=View.inflate(this, R.layout.welcome, null);
		assetManager=getAssets();
		readAppConfig();
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener()
		{
			public void onAnimationEnd(Animation arg0) {
				
				checkVersion();
			}
			public void onAnimationRepeat(Animation animation) {}
			
			public void onAnimationStart(Animation animation) {
				
			}
					
		});
	}
	//读取配置
	private void readAppConfig(){	
		try {  
			InputStream inputStream = assetManager.open("replaceable/Config.xml");
			
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();  
			 
			XmlPullParser parser = pullFactory.newPullParser();  
			 
			parser.setInput(inputStream, "utf-8");
			 
			int eventType = parser.getEventType();    
			 
			while(eventType!=XmlPullParser.END_DOCUMENT)    
			{    
				switch (eventType)     
				{
				 case XmlPullParser.START_DOCUMENT:  
					 break;
				 case XmlPullParser.START_TAG:
					 String name=parser.getName();
					 if("schoolid".equals(name)){
						 //id 在ceapp里面时
						 //cEappApp.setSchoolID(Integer.valueOf(parser.getAttributeValue(0)));
						 cEappApp.setSchoolID(Integer.valueOf(parser.nextText()));
					 }
					 break;
				 case XmlPullParser.END_TAG:
					 break;
				 case XmlPullParser.END_DOCUMENT:
					 break;
				 default:
					 break;
				}
				eventType = parser.next();
			 }			 
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//跳转
	private void redirectTo(){
		Intent intent=new Intent();
		intent.setClass(Welcome.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void checkVersion(){
		if (NetUtil.checkNetWorkStatus(this))
		{
			checkVersion = new CheckVersion();
			checkVersion.setUser(-1);
			checkVersion.setPass("");
			checkVersion.setMobileOS(MobileOSType.ANDROID);
			int vercode = 0;
			try{
				PackageManager manager = this.getPackageManager();
				PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
				vercode=info.versionCode;
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
			checkVersion.setCurrentVer(vercode);
			checkVersion.setSchool(cEappApp.getSchoolID());
	            
			NetUtil.post(Constant.BASE_URL, checkVersion, handler, MessageWhat.CHECK_VERSON_RESP);
		}else{
			redirectTo();
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			Intent intent = new Intent(Welcome.this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		}
		switch(msg.what){
		case MessageWhat.CHECK_VERSON_RESP:		
			
			if(((String)msg.obj)!=null){
				Log.v("(String)msg.obj","===="+(String)msg.obj);
				checkVersionResp=JsonUtil.fromJson((String)msg.obj,CheckVersionResp.class);
				if(null==checkVersionResp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==checkVersionResp.getStatus()){
					boolean exists=checkVersionResp.isExists();
					String url=checkVersionResp.getUrl();
					if(exists){
						checkUpdate(url);
					}else{
						Intent intent = new Intent(Welcome.this, LoginActivity.class);
				        startActivity(intent);
				        this.finish();
					}
				}
				msg.obj=null;
			}else{
				  Intent intent = new Intent(Welcome.this, LoginActivity.class);
		          startActivity(intent);
		          this.finish();
			}	
			break;
		case MessageWhat.DOWN_UPDATE:
			progressBar.setProgress(progressValue);
			break;
		case MessageWhat.DOWN_OVER:
			downloadDialog.dismiss();
			AppUtil.installApk(this, FileUtil.getCachDirectory() + "/" + Constant.APKNAME);
			break;
		}	
		return false;
	}
	
	private void checkUpdate(String url){
		StringBuffer updateMsg = new StringBuffer();
		updateMsg.append("发现新版本,");
		try{
			updateMsg.append("版本号:"+this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode);
		}catch(Exception e){
			e.printStackTrace();
		}
		updateMsg.append(Constant.LINE_SEPARATOR);
		updateMsg.append("是否升级?");
		showNoticeDialog(url,updateMsg);
	}
	
	private void showNoticeDialog(final String url,final StringBuffer updateMsg){
		AlertDialog.Builder builder = new Builder(cEappApp.getCurrentActivity());
		builder.setTitle("软件版本升级");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("现在升级", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				showDownloadDialog(url,updateMsg);
			}
		});
		builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
				
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				dialog.dismiss();
				Intent intent = new Intent(Welcome.this, LoginActivity.class);
				startActivity(intent);
				Welcome.this.finish();
			}
		});
		builder.create().show();
	}
	private void showDownloadDialog(final String url,StringBuffer updateMsg){

		AlertDialog.Builder builder = new Builder(cEappApp.getCurrentActivity());
		builder.setTitle("软件版本升级");
		
		final LayoutInflater inflater = LayoutInflater.from(this);
		View progressView = inflater.inflate(R.layout.progress, null);
		progressBar = (ProgressBar) progressView.findViewById(R.id.progress);  
//		View progressView=inflater.inflate(R.layout.down_dialog, null);
//        progressBar=(RoundProgressBar)progressView.findViewById(R.id.roundProgressBar);
       
        builder.setView(progressView);
        
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
        	public void onClick(DialogInterface dialog, int which)
            {
        		dialog.dismiss();
                interceptFlag = true;
                Intent intent = new Intent(Welcome.this, LoginActivity.class);
                startActivity(intent);
                Welcome.this.finish();
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        // 下载线程
        ThreadPool.execute(new Runnable()
        {
        	public void run()
            {
        		try
                {            
        			HttpURLConnection httpURLConnection = null;
                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    File file = new File(FileUtil.getCachDirectory() + "/" + Constant.APKNAME);
                    fileOutputStream = new FileOutputStream(file);
                    int length = httpURLConnection.getContentLength();
                    Log.v("http_header", "=="+httpURLConnection.getHeaderFields());
                    byte buffer[] = new byte[1024];
                    int count = 0;              
                    do
                    {
                        int numread = inputStream.read(buffer);
                        count += numread;                      
                        progressValue = (int) (((float) count / length) * 100);                 
                        Message message = handler.obtainMessage(MessageWhat.DOWN_UPDATE, "");
                        handler.sendMessage(message);
                        if (numread <= 0)
                        {
                            Message message1 = handler.obtainMessage(MessageWhat.DOWN_OVER, "");
                            handler.sendMessage(message1);
                            break;
                        }
                        fileOutputStream.write(buffer, 0, numread);
                       
                    }while (!interceptFlag);
            
                    fileOutputStream.close();
                    inputStream.close();
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    
	}
}
