package com.e1858.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.e1858.BaseMenuActivity;
import com.e1858.adapter.ListViewGroupAdapter;
import com.e1858.adapter.ListViewNoticeAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.NewDataToast;
import com.e1858.common.RoleType;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.LibGroup;
import com.e1858.protocol.NoticeBase;
import com.e1858.protocol.http.GetUnReadNoticeList;
import com.e1858.protocol.http.GetUnReadNoticeListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;

public class NoticeActivity extends BaseMenuActivity {

	private PullToRefreshListView   noticelistview;
	private LinearLayout       		write_notice;
	private LinearLayout		 	delete;
	private TextView                empty_text;
	private boolean 				inNotice=true;
	
	private LibGroup 				libGroup;
	private List<LibGroup> 			group_lists=new ArrayList<LibGroup>();
	private ListViewGroupAdapter 	groupAdapter;
	
	
	
	private GetUnReadNoticeList      getUnReadNoticeList;
	private GetUnReadNoticeListResp  getUnReadNoticeListResp;
	
	private NoticeBase               notice_entity;
	private List<NoticeBase>         notices=new  ArrayList<NoticeBase>();
	
	
	private List<NoticeBase>        server_notices=new ArrayList<NoticeBase>();
	private List<Long>              noticeids=new ArrayList<Long>();
 
	private ListViewNoticeAdapter    adapter;
	private boolean isRefresh=false;
	private AlertDialog              notice_Del_alert;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.notice);
		doScroll(ModuleInteger.NOTICE);	
		initView();
		initlocalData();
		if(cEappApp.getRole()==RoleType.STUDENT)
		{	
			judgeisExistsData();
		}else if(cEappApp.getRole()==RoleType.MANAGER){
			judgeisExistsData();
		}else{	
			judgeisExistsData();
		}		
	}
/**
 * 身份未分
 */
	private void initView(){
		
		write_notice=(LinearLayout)findViewById(R.id.write_newnotice);
		if(cEappApp.getRole()==RoleType.MANAGER){
			title_name.setText("通知公告");
			arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open));
			arrow.setVisibility(View.VISIBLE);
			pop_menu.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					changPopState(v);
				}
			});
		}else if(cEappApp.getRole()==RoleType.STUDENT){
			title_name.setText("通知公告");
			write_notice.setVisibility(View.GONE);
		}else if(cEappApp.getRole()==RoleType.TEACHER){
			write_notice.setVisibility(View.GONE);
			title_name.setText("通知公告");
		}
	
		empty_text=(TextView)findViewById(R.id.empty_text);
		noticelistview=(PullToRefreshListView)findViewById(R.id.noticelist);
		
		
		write_notice.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NoticeActivity.this,AddNewNotice.class);
				startActivity(intent);
				overridePendingTransition(R.anim.market_slide_in_from_bottom, 0);
			
			}
		});
		
		delete=(LinearLayout)findViewById(R.id.delete);	

		noticelistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
	          public void onRefresh() {  
	        	  if(inNotice){
	        			isRefresh=true;
	            		loadData();
	        	  }else{
	        		  noticelistview.onRefreshComplete();
	        	  }
	            	
	            }
	        });	
		
		
		
		
		btn_top_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_top_right.getText().equals("编辑")){
					if(notices.size()==0){
						return;
					}else{
						delete.setVisibility(View.VISIBLE);
						btn_top_right.setText("全选");
						btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_back_bg));
					}	
				}else if(btn_top_right.getText().equals("全选")){
	
						noticeids.clear();
						
						for(int i=0;i<notices.size();i++){
							noticeids.add(notices.get(i).getId());
						}
						adapter.setAllSelect(true);
						adapter.setType(0);
						adapter.notifyDataSetChanged();
					
					
				}
			}
		});
		btn_top_left.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_top_right.getText().equals("全选")){
					
					adapter.setAllSelect(false);
					adapter.setType(1);
					adapter.notifyDataSetChanged();

					btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_index_bg));
					delete.setVisibility(View.GONE);
					btn_top_right.setText("编辑");
				
				}else{
					if(isFirstScroll){
						scrollToContent();
						isFirstScroll=false;
					}else{
						scrollToMenu();
						isFirstScroll=true;
					}		
				}
			}
		});
		
		
		delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				notice_Del_alert=new AlertDialog.Builder(NoticeActivity.this).setTitle("提示")
						.setMessage("确定删除吗？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								notice_Del_alert.dismiss();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								notice_Del_alert.dismiss();
								
								for(int i=0;i<noticeids.size();i++){
									try{
										cEappApp.dbManager.deleteDataBySql("delete from "+TableName.NOTICE +" where id=?",new String[]{""+noticeids.get(i)});		
										loadlocalData();
										adapter=new ListViewNoticeAdapter(NoticeActivity.this, notices, R.layout.notice_item,inNotice);
										noticelistview.setAdapter(adapter);
										btn_top_right.setText("编辑");
										delete.setVisibility(View.GONE);
										btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_index_bg));
									}catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}
								
							}
						}).create();
					notice_Del_alert.show();
			}
		});
	
		noticelistview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
			if(!inNotice){

				if(btn_top_right.getText().equals("编辑")){
					
					long[] noticeidss=new long[notices.size()];
					for(int i=0;i<notices.size();i++){
						noticeidss[i]=notices.get(i).getId();
					}
					long noticeID=notices.get(position-1).getId();
					Intent intent=new Intent(NoticeActivity.this,NoticeInfo.class);
					intent.putExtra("noticeID", noticeID);
					intent.putExtra("inNotice", inNotice);
					intent.putExtra("noticeids",noticeidss);
					intent.putExtra("index", position-1);
					startActivity(intent);
					

				}else{
					
					adapter.setAllSelect(false);
					
					for(int i=0;i<noticeids.size();i++){

						if(notices.get(position-1).getId()==noticeids.get(i)){
								adapter.setType(0);
								adapter.setSelectedPosition(position-1, 1);
								adapter.notifyDataSetChanged();
								noticeids.remove(i);
								return;
							}
						}
						adapter.setType(0);
						adapter.setSelectedPosition(position-1, 0);
						adapter.notifyDataSetChanged();
						noticeids.add(notices.get(position-1).getId());
					
				}
			}else{
				long[] noticeids=new long[notices.size()];
				for(int i=0;i<notices.size();i++){
					noticeids[i]=notices.get(i).getId();
				}
				
				
				long noticeID=notices.get(position-1).getId();
				Intent intent=new Intent(NoticeActivity.this,NoticeInfo.class);
				intent.putExtra("noticeID", noticeID);
				intent.putExtra("inNotice", inNotice);
				intent.putExtra("noticeids", noticeids);
				intent.putExtra("index", position-1);
				startActivity(intent);
			
			}
			}
				
		});
		
	}
	
	private void initlocalData(){
		libGroup=new LibGroup();
		libGroup.setId(0);
		libGroup.setName("通知公告");
		libGroup.setPicture(0);
		libGroup.setSrcMode(0);
		libGroup.setSrcUrl("");
		
		group_lists.add(libGroup);
	
		libGroup=new LibGroup();
		libGroup.setId(1);
		libGroup.setName("已发公告");
		libGroup.setPicture(0);
		libGroup.setSrcMode(0);
		libGroup.setSrcUrl("");
		group_lists.add(libGroup);
	}
	public void changPopState(View v) { 		
	    isOpenPop = !isOpenPop;   
	    if (isOpenPop) { 
	      arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_close)); 
	      popAwindow(v); 
	    } else { 
	      arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open     )); 
	      if (window != null) { 
	        window.dismiss(); 
	      } 
	    } 
	    
	  } 	
	 private void popAwindow(View parent) { 
		 if (window == null) { 
			 LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			 View v = lay.inflate(R.layout.pop_menu, null); 
			 group_list = (ListView) v.findViewById(R.id.group_list);
			 groupAdapter=new ListViewGroupAdapter(this, group_lists,R.layout.pop_menu_item);			
			 group_list.setAdapter(groupAdapter); 
			 group_list.setItemsCanFocus(false); 
			 group_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
			 group_list.setOnItemClickListener(new OnItemClickListener() {

				 public void onItemClick(AdapterView<?> arg0, View arg1,
						 int position, long arg3) {
					// TODO Auto-generated method stub
					 if(group_lists.get(position).getId()==0){
						 inNotice=true;
						
						 btn_top_right.setVisibility(View.GONE);
						 title_name.setText("通知公告");
						 loadlocalData();
					 }else{
						 inNotice=false;
						 btn_top_right.setText("编辑");	
						 btn_top_right.setVisibility(View.VISIBLE);
						 
						 title_name.setText("已发公告");
						 loadSendNotice();
					 }
					 if (window != null) { 
						 	window.dismiss(); 
					 } 

				 }
			 }); 
			 int x = (int) getResources().getDimension(R.dimen.pop_x); 	 
			 window=new PopupWindow(v, x, LayoutParams.WRAP_CONTENT, true);	
		    } 
		 	window.setBackgroundDrawable(getResources().getDrawable(R.drawable.popover_background));
		 	window.getBackground().setAlpha(240);
		 	window.setOutsideTouchable(true); 
		    window.setOnDismissListener(new OnDismissListener() { 
		      public void onDismiss() { 
		        // TODO Auto-generated method stub 
		        isOpenPop = false; 
		        arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open));
		      } 
		    }); 
		    window.update(); 
		    window.showAtLocation(parent, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 
		        0, (int) getResources().getDimension(R.dimen.space_65)); 

		  } 
	 
	private void judgeisExistsData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LNOTICE, null);
			if(c.getCount()==0){
				loadData();
			}else{
				loadlocalData();
			}
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
	}
	
	private void loadData(){
		
		if(NetUtil.checkNetWorkStatus(this)){		
			getUnReadNoticeList=new GetUnReadNoticeList();
			getUnReadNoticeList.setUser(cEappApp.getUser());
			getUnReadNoticeList.setPass(cEappApp.getPass());
			if(notices.size()==0){
				getUnReadNoticeList.setId(-1);
			}else{
				getUnReadNoticeList.setId(notices.get(0).getId());				
			}
			NetUtil.post(Constant.BASE_URL, getUnReadNoticeList, handler, MessageWhat.GET_UNREADNOTICE_LIST_RESP);
			
		}else{
			loadlocalData();
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
	}
	private void loadlocalData(){
		if(notices!=null){
			notices.clear();
		}
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LNOTICE +" order by id desc", null);
		 	for(int i=0;i<c.getCount();i++){
		 		c.moveToPosition(i);
		 		notice_entity=new NoticeBase();
		 		notice_entity.setId(c.getInt(c.getColumnIndex("id")));
		 		notice_entity.setTitle(c.getString(c.getColumnIndex("title")));
		 		notice_entity.setIssuedDate(c.getString(c.getColumnIndex("time")));	 		
		 		notices.add(notice_entity);		 		
		 	}
		 	if(notices.size()==0){
				empty_text.setVisibility(View.VISIBLE);
				noticelistview.setVisibility(View.GONE);
				empty_text.setText("很抱歉,暂无数据！");
			}else{
				empty_text.setVisibility(View.GONE);
				noticelistview.setVisibility(View.VISIBLE);
				adapter=new ListViewNoticeAdapter(this, notices, R.layout.notice_item,inNotice);
				noticelistview.setAdapter(adapter);
			}
		 	c.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void loadSendNotice(){
		if(notices!=null){
			notices.clear();
		}		
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select id,title,time from "+TableName.NOTICE+" order by id desc", null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				notice_entity=new NoticeBase();
				notice_entity.setId(c.getInt(c.getColumnIndex("id")));
				notice_entity.setTitle(c.getString(c.getColumnIndex("title")));
				notice_entity.setIssuedDate(c.getString(c.getColumnIndex("time")));
				notices.add(notice_entity);
			}
			if(notices.size()==0){
				empty_text.setVisibility(View.VISIBLE);
				noticelistview.setVisibility(View.GONE);
				empty_text.setText("很抱歉,暂无数据！");
			}else{
				empty_text.setVisibility(View.GONE);
				noticelistview.setVisibility(View.VISIBLE);
				adapter=new ListViewNoticeAdapter(this, notices, R.layout.notice_item,inNotice);
				noticelistview.setAdapter(adapter);
			}
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			
			return true;
		}
		switch(msg.what){
			case MessageWhat.GET_UNREADNOTICE_LIST_RESP:
				if(null!=(String)msg.obj){
				
					Log.v("notices", ""+(String)msg.obj);
					getUnReadNoticeListResp=JsonUtil.fromJson((String)msg.obj, GetUnReadNoticeListResp.class);
					if(null==getUnReadNoticeListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getUnReadNoticeListResp.getStatus()){
						server_notices=getUnReadNoticeListResp.getNotices();
						if(isRefresh){
							
							noticelistview.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							Log.v("server_notices","=="+server_notices.size());
							if(server_notices==null||server_notices.size()==0){
								NewDataToast.makeText(NoticeActivity.this, getString(R.string.new_data_toast_none), false).show();
								isRefresh=false;
								break;
							}else{

							 	isRefresh=false;
							 	NewDataToast.makeText(this, getString(R.string.new_data_toast_message,server_notices.size()), false).show();
							 	for(int i=0;i<server_notices.size();i++){
							 			notice_entity=server_notices.get(i);
							 			ContentValues values=new  ContentValues();
								 		values.put("id", server_notices.get(i).getId());
								 		values.put("title", server_notices.get(i).getTitle());
								 		values.put("time", server_notices.get(i).getIssuedDate());
								 		cEappApp.dbManager.insertData(TableName.LNOTICE, values);
								 		notices.add(0, notice_entity);
								 		adapter.notifyDataSetChanged();
							 	}
							}
							
							
						}else{
							
						 	if(server_notices==null||server_notices.size()==0){
						 		empty_text.setVisibility(View.VISIBLE);
						 		noticelistview.setVisibility(View.GONE);
						 		empty_text.setText("很抱歉,暂无数据");
						 		break;
						 	}		

						 	StringBuilder sb=new StringBuilder();
						 	try{
						 		Cursor c=cEappApp.dbManager.queryData2Cursor("select id from  "+ TableName.LNOTICE, null);
						 		for(int i=0;i<c.getCount();i++){
						 			c.moveToPosition(i);
						 			sb.append(c.getInt(c.getColumnIndex("id"))).append(",");
						 		}
						 		
						 		c.close();
						 	}catch (Exception e) {
								// TODO: handle exception
						 		e.printStackTrace();
							}
						 	
						
						 	for(int i=0;i<server_notices.size();i++){
						 		if(!sb.toString().contains(""+server_notices.get(i).getId())){
						 			ContentValues values=new  ContentValues();
							 		values.put("id", server_notices.get(i).getId());
							 		values.put("title", server_notices.get(i).getTitle());
							 		values.put("time", server_notices.get(i).getIssuedDate());
							 		cEappApp.dbManager.insertData(TableName.LNOTICE, values);
						 		}
						 	}
							loadlocalData();
						}
						
						 		
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
