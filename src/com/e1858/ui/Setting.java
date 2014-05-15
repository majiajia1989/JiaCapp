package com.e1858.ui;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CacheManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.e1858.BaseMenuActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.MessageWhat;
import com.e1858.common.MobileOSType;
import com.e1858.common.ModuleInteger;
import com.e1858.common.SelectorType;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Module;
import com.e1858.protocol.http.CheckVersion;
import com.e1858.protocol.http.CheckVersionResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.utils.AppUtil;
import com.e1858.utils.FileUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.ThreadPool;
import com.e1858.utils.ToastUtil;
public class Setting extends BaseMenuActivity {

	private RelativeLayout   		btn_modify_pass;
	private RelativeLayout   		btn_select_home;
	private RelativeLayout          check_update;
	private RelativeLayout          clearcache;
	
	private TextView                home_text;
	private ListView 				module_list;
	private ModuleAapter 			adapter;
	private List<Module>   			modules=new ArrayList<Module>();
	private Module       			module;	
	private CheckVersion            checkVersion;
	private CheckVersionResp        checkVersionResp;
	private TextView                update_text;
	private ProgressBar         	progressBar;
	private int                 	progressValue;
	private boolean 				interceptFlag = false;
	private Dialog 					downloadDialog;
	
	private AlertDialog             clearCachedialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.setting);
		doScroll(ModuleInteger.SETTING);	
		initView();
		loadData();
	}

	private void initView(){
		
		title_name.setText("设置 ");
		btn_modify_pass=(RelativeLayout)findViewById(R.id.modify_pass_btn);
		btn_select_home=(RelativeLayout)findViewById(R.id.home_select_btn);
		check_update=(RelativeLayout)findViewById(R.id.checkUpdate);		
		clearcache=(RelativeLayout)findViewById(R.id.clearcache);		
		
		module_list=(ListView)findViewById(R.id.module_select_list);
		update_text=(TextView)findViewById(R.id.update_text);
		home_text=(TextView)findViewById(R.id.home_text);
		home_text.setText("当前首页:"+cEappApp.getHomeName());
		btn_modify_pass.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Setting.this,ModifyPass.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		btn_select_home.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Setting.this,ModuleHomeSelect.class);
				startActivityForResult(intent, 10);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		check_update.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkVersion();
			}
		});
		clearcache.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				clearCachedialog=new AlertDialog.Builder(Setting.this).setTitle("提示")
						.setMessage("确认要清除缓存吗？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								clearCachedialog.dismiss();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								clearCachedialog.dismiss();
								
								File file=CacheManager.getCacheFileBaseDir();
								
								if (file != null && file.exists() && file.isDirectory()) { 

									for (File item : file.listFiles()) { 
											item.delete(); 
										} 

									file.delete();
								} 

								Setting.this.deleteDatabase("WebView.db"); 

								Setting.this.deleteDatabase("WebViewCache.db");
								ToastUtil.showShort(Setting.this, "缓存清除成功");
								
							}
						}).create();
					clearCachedialog.show();
				
			}
		});
	}
	
	private void loadData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.MODULE+" where id not in(100,0,1,2)", null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				module=new Module();
				module.setId(c.getInt(c.getColumnIndex("id")));
				module.setName(c.getString(c.getColumnIndex("name")));
				module.setIcon(c.getInt(c.getColumnIndex("icon")));
				module.setHome(c.getInt(c.getColumnIndex("home")));
				module.setSelect(c.getInt(c.getColumnIndex("isselect")));
				modules.add(module);
			}
			adapter=new ModuleAapter(this, modules, R.layout.setting_module_item);
			module_list.setAdapter(adapter);
			setListViewHeightBasedOnChildren(module_list);
			
			c.close();
		}catch(Exception e){
			
		}
		
		
	}
	public void setListViewHeightBasedOnChildren(ListView listView) {  
		
		ListAdapter listAdapter = listView.getAdapter();    
	    if (listAdapter == null) {  
	        return;  
	    }  
	    int totalHeight = 0;  
	    for (int i = 0; i < listAdapter.getCount(); i++) {  
	    	View listItem = listAdapter.getView(i, null, listView);  
	        listItem.measure(0, 0);  
	        totalHeight += listItem.getMeasuredHeight();  
	    }  
	    ViewGroup.LayoutParams params = listView.getLayoutParams();  
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));  
	    listView.setLayoutParams(params);  
	} 
	class ModuleAapter extends BaseAdapter{
		
		private Context 		context;
		private List<Module> 	list;
		private int          	itemResource;
		private LayoutInflater 	layoutInflater;
		
		public ModuleAapter(Context context,List<Module> list,int itemResource){
			this.context=context;
			this.list=list;
			this.itemResource=itemResource;
			this.layoutInflater=LayoutInflater.from(context);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh=null;
			if(convertView==null){
				vh=new ViewHolder();
				convertView=layoutInflater.inflate(itemResource, null);
				vh.module_name=(TextView)convertView.findViewById(R.id.module_title);
				vh.module_select=(ToggleButton)convertView.findViewById(R.id.module_select);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder)convertView.getTag();
			}
			vh.module_name.setText(list.get(position).getName());
			
			
			if(list.get(position).getSelect()==SelectorType.SELETED){
				
				vh.module_select.setChecked(true);
			}else{
				vh.module_select.setChecked(false);
			}
			vh.module_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						modules.get(position).setSelect(SelectorType.SELETED);
						
						try{
							cEappApp.dbManager.updateDataBySql("update  "+TableName.MODULE +" set isselect=1 where id=?", new String[]{""+list.get(position).getId()});
						}catch(Exception e){
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
					}else{
						modules.get(position).setSelect(SelectorType.UNSELECTED);
						try{
							cEappApp.dbManager.updateDataBySql("update "+TableName.MODULE +" set isselect=0 where id=?", new String[]{""+list.get(position).getId()});
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
					}
					localmodules.clear();
					initData();
				}
			});
			return convertView;
		}
		
	}
	class ViewHolder{
		public TextView module_name;
		public ToggleButton module_select;
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
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
		case MessageWhat.CHECK_VERSON_RESP:
			if((String)msg.obj!=null){
				closeProgressDialog();
				Log.v("(String)msg.obj", (String)msg.obj);
				checkVersionResp=JsonUtil.fromJson((String)msg.obj, CheckVersionResp.class);
				if(null==checkVersionResp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==checkVersionResp.getStatus()){
					boolean exists=checkVersionResp.isExists();
					String url=checkVersionResp.getUrl();
					if(exists){
						checkUpdate(url);
					}else{
						update_text.setText("已是最新版本");
					}
				}
				
			}else{
				update_text.setText("已是最新版本");
			}
			break;
		case MessageWhat.DOWN_UPDATE:
			progressBar.setProgress(progressValue);
			break;
		case MessageWhat.DOWN_OVER:
			downloadDialog.dismiss();
			AppUtil.installApk(this, FileUtil.getCachDirectory()+"/"+Constant.APKNAME);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null){
			return;
		}else{
			home_text.setText("当前首页："+data.getStringExtra("homename"));
		}
	}
	
	private void  checkVersion(){
		if (NetUtil.checkNetWorkStatus(this))
		{
			openProgressDialog("版本检测...");
	          
			checkVersion = new CheckVersion();
			
			checkVersion.setUser(cEappApp.getUser());
	           
			checkVersion.setPass(cEappApp.getPass());
	           
			checkVersion.setMobileOS(MobileOSType.ANDROID);
	           
			checkVersion.setSchool(cEappApp.getSchoolID());
				
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
				
				
			NetUtil.post(Constant.BASE_URL, checkVersion, handler, MessageWhat.CHECK_VERSON_RESP);
		}else{
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
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

       builder.setView(progressView);
       builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
       {
           public void onClick(DialogInterface dialog, int which)
           {
               dialog.dismiss();
               interceptFlag = true;
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
                   }
                   while (!interceptFlag);
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

