package com.e1858.ui;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import com.e1858.BaseMenuActivity;
import com.e1858.adapter.ListViewGroupAdapter;
import com.e1858.adapter.ListViewMessageAdapter;
import com.e1858.adapter.ListViewSendMsgAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.RoleType;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.LibGroup;
import com.e1858.protocol.Msg;
import com.e1858.protocol.SubmitMsg;
import com.e1858.protocol.UserBase;
import com.e1858.protocol.http.GetUnReadMsgList;
import com.e1858.protocol.http.GetUnReadMsgListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.protocol.socket.Packet;
import com.e1858.protocol.socket.PushMsg;
import com.e1858.protocol.socket.SocketDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.NotificationInfo;
import com.e1858.utils.NotificationUtil;
import com.e1858.utils.ToastUtil;
public class MessageActivity extends BaseMenuActivity{

	private ListView     			msglist;
	
	private LinearLayout       		write_newmsg;
	
	private LinearLayout       		delete; 
	private TextView                empty_text;
	
	private GetUnReadMsgList     	getUnreadMsgList_protocol;
	
	private GetUnReadMsgListResp  	getUnReadMsgListResp;
	 
	private ListViewMessageAdapter 	adapter;
	 		
	private ListViewSendMsgAdapter	send_adapter;
	
	private List<Msg> 				messages=new ArrayList<Msg>();
	 
	private List<Msg> 				mymsgs=new ArrayList<Msg>();
	
	private Msg       				msg_entity;
	 
	private List<SubmitMsg> 		submitMsgs=new ArrayList<SubmitMsg>();
	
	private SubmitMsg 		 		submitMsg_entity;
	 
	 
//	private List<ListID> 	 		ids=new ArrayList<ListID>();
//	private ListID       	 		listID;
	 
	private List<LibGroup>  		group_lists=new ArrayList<LibGroup>();
	
	private LibGroup 		 		libGroup;
	 
	private boolean  				inbox=true;
	 
	private ListViewGroupAdapter 	groupAdapter;
	
	protected Map<Integer, Packet>	socketSequenceList	= new ConcurrentHashMap<Integer, Packet>();
	
	private  List<Long>             msgids=new ArrayList<Long>();
	
	private AlertDialog             deleteDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.message);
		doScroll(ModuleInteger.MESSAGE);	
		initView();
		initlocalData();
		if(cEappApp.getRole()==RoleType.STUDENT)
		{
			loadData();
		}else if(cEappApp.getRole()==RoleType.MANAGER){
			title_name.setText(getResources().getString(R.string.outbox_str));
//			loadSendMsg();
			loadData();
		}else{
			title_name.setText(getResources().getString(R.string.inbox_str));
			loadData();
		}		
	}
	
	private void loadlocalData(){
		mymsgs.clear();
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from " +TableName.SHORT_MSG+"  order by id desc", null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				msg_entity=new Msg();				
				msg_entity.setMsgID(c.getLong(c.getColumnIndex("id")));
				String[] sender_str=c.getString(c.getColumnIndex("sender")).split(",");
				UserBase user_base=new UserBase();
				user_base.setId(Long.valueOf(sender_str[0]));
				user_base.setNickname(sender_str[1]);
				user_base.setRealName(sender_str[2]);
				user_base.setUserName(sender_str[3]);
				msg_entity.setSender(user_base);
				msg_entity.setContentType(c.getInt(c.getColumnIndex("contentType")));
				msg_entity.setContentCode(c.getInt(c.getColumnIndex("contentCode")));
				msg_entity.setContent(c.getString(c.getColumnIndex("content")));
				msg_entity.setTime(c.getString(c.getColumnIndex("time")));
				msg_entity.setUnread(c.getInt(c.getColumnIndex("unread")));			
				mymsgs.add(msg_entity);
			};	

			
			if(mymsgs.size()==0){
				msglist.setVisibility(View.GONE);
				empty_text.setVisibility(View.VISIBLE);
				empty_text.setText("很抱歉,暂无数据！");
			}else{
				msglist.setVisibility(View.VISIBLE);
				empty_text.setVisibility(View.GONE);
				adapter=new ListViewMessageAdapter(this, mymsgs, R.layout.message_item);
				msglist.setAdapter(adapter);
			}
			
			
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	
	//加载已发送信息
	public void loadSendMsg(){
		submitMsgs.clear();
		try{		
			Cursor c=cEappApp.dbManager.queryData2Cursor("select id,receiver,content,time from "+ TableName.SUBMIT_MSG +" order by id desc", null);
			for(int i=0;i<c.getCount();i++){	
				c.moveToPosition(i);
				submitMsg_entity=new SubmitMsg();
				submitMsg_entity.setId(c.getInt(c.getColumnIndex("id")));
				submitMsg_entity.setContent(c.getString(c.getColumnIndex("content")));
				submitMsg_entity.setTime(c.getString(c.getColumnIndex("time")));
				submitMsg_entity.setReceiver(c.getString(c.getColumnIndex("receiver")));
				submitMsgs.add(submitMsg_entity);		
			}
			if(submitMsgs.size()==0){
				msglist.setVisibility(View.GONE);
				empty_text.setVisibility(View.VISIBLE);
				empty_text.setText("很抱歉,暂无数据！");
			}else{
				msglist.setVisibility(View.VISIBLE);
				empty_text.setVisibility(View.GONE);
				send_adapter=new ListViewSendMsgAdapter(this, submitMsgs, R.layout.message_item);
				msglist.setAdapter(send_adapter);
			}
			c.close();
		}catch(Exception e){	
		}
	}
	private void initlocalData(){
		
		
		libGroup=new LibGroup();
		libGroup.setId(0);
		libGroup.setName("收件箱");
		libGroup.setPicture(0);
		libGroup.setSrcMode(0);
		libGroup.setSrcUrl("");
		
		group_lists.add(libGroup);
	
		libGroup=new LibGroup();
		libGroup.setId(1);
		libGroup.setName("发件箱");
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
	      arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open)); 
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
						 inbox=true;
						 title_name.setText("收件箱");
						 loadData();
					 }else{
						 inbox=false;
						 title_name.setText("发件箱");
						 loadSendMsg();
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
	
	private void initView(){
		
		empty_text=(TextView)findViewById(R.id.empty_text);
		msglist=(ListView)findViewById(R.id.msglist);
		write_newmsg=(LinearLayout)findViewById(R.id.write_newmsg);	
		btn_top_right.setText("编辑");		
		btn_top_right.setVisibility(View.VISIBLE);

		if(cEappApp.getRole()==RoleType.MANAGER){
		
			title_name.setText("收件箱");
			arrow.setVisibility(View.VISIBLE);
			arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open));
			pop_menu.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					changPopState(v);
				}
			});
		}else if(cEappApp.getRole()==RoleType.TEACHER){
			title_name.setText("收件箱");
			write_newmsg.setVisibility(View.GONE);
		}else if(cEappApp.getRole()==RoleType.STUDENT){
			title_name.setText("收件箱");
			write_newmsg.setVisibility(View.GONE);
		}
		
		
		delete=(LinearLayout)findViewById(R.id.delete);		
		delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteDialog=new AlertDialog.Builder(MessageActivity.this).setTitle("提示")
						.setMessage("确认删除吗？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								deleteDialog.dismiss();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								deleteDialog.dismiss();
								
								for(int i=0;i<msgids.size();i++){
									if(inbox){
										try{
											cEappApp.dbManager.deleteDataBySql("delete from "+TableName.SHORT_MSG +" where id=?",new String[]{""+msgids.get(i)});		
											loadlocalData();
											adapter=new ListViewMessageAdapter(MessageActivity.this, mymsgs, R.layout.message_item);
											msglist.setAdapter(adapter);
											btn_top_right.setText("编辑");
											delete.setVisibility(View.GONE);
											btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_index_bg));
										}catch(Exception e){
											e.printStackTrace();
										}
										
									}else{
										
										try{
											cEappApp.dbManager.deleteDataBySql("delete from "+TableName.SUBMIT_MSG +" where id=?",new String[]{""+msgids.get(i)});
											loadSendMsg();
											send_adapter=new ListViewSendMsgAdapter(MessageActivity.this, submitMsgs, R.layout.message_item);
											msglist.setAdapter(send_adapter);
											delete.setVisibility(View.GONE);
											btn_top_right.setText("编辑");
											btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_index_bg));
										}catch (Exception e) {
											// TODO: handle exception
											e.printStackTrace();
										}
									}
								}
								
								
							}
						}).create();
				
				deleteDialog.show();
			
			}
		});
		
		btn_top_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_top_right.getText().equals("编辑")){
					if(mymsgs.size()==0&&inbox){
						return;
					}else if(submitMsgs.size()==0&&!inbox){
						return;
					}else{
						delete.setVisibility(View.VISIBLE);
						
						btn_top_right.setText("全选");
						btn_top_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_back_bg));
					}	
				}else if(btn_top_right.getText().equals("全选")){
					
					if(inbox){
						msgids.clear();
						for(int i=0;i<mymsgs.size();i++){
							msgids.add(mymsgs.get(i).getMsgID());
						}
						adapter.setAllSelect(true);
						adapter.setType(0);
						adapter.notifyDataSetChanged();
					}else{
						msgids.clear();
						
						for(int i=0;i<submitMsgs.size();i++){
							msgids.add(submitMsgs.get(i).getId());
						}
						send_adapter.setType(0);
						send_adapter.setAllSelect(true);
						send_adapter.notifyDataSetChanged();
					}
					
				}
			}
		});
				
		btn_top_left.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_top_right.getText().equals("全选")){
					if(inbox){
						adapter.setAllSelect(false);
						adapter.setType(1);
						adapter.notifyDataSetInvalidated();
					}else{
						send_adapter.setAllSelect(false);
						send_adapter.setType(1);
						send_adapter.notifyDataSetInvalidated();
					}
					
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
			
		write_newmsg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageActivity.this, AddNewMessage.class);
				startActivity(intent);
				finish();
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				overridePendingTransition(R.anim.market_slide_in_from_bottom, 0);
			}
		});
		
		
		/**
		msglist.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if(inbox){
					ids.clear();
					adapter.setType(0);
					adapter.setSelectedPosition(position,0);
					adapter.notifyDataSetInvalidated();
					delete.setVisibility(View.VISIBLE);
					listID=new ListID();
					listID.setId(position);
					listID.setLongclick(0);
					ids.add(listID);	
				}else{
					ids.clear();
					send_adapter.setType(0);
					send_adapter.setSelectedPosition(position,0);
					send_adapter.notifyDataSetInvalidated();
					delete.setVisibility(View.VISIBLE);
					listID=new ListID();
					listID.setId(position);
					listID.setLongclick(0);
					ids.add(listID);	
				}
				return false;
			}
		});
		*/
		msglist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				Log.v("btn_top_right", btn_top_right.getText().toString());
				
				if(btn_top_right.getText().equals("编辑")){
					if(inbox){
						long msgID=mymsgs.get(position).getMsgID();
						Intent intent=new Intent(MessageActivity.this,MessageInfo.class);
						intent.putExtra("msgID", msgID);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}else{
						long sendID=submitMsgs.get(position).getId();
						Intent intent=new Intent(MessageActivity.this,SendMsgInfo.class);
						intent.putExtra("sendID", sendID);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
					
				}else{
					if(inbox){
						adapter.setAllSelect(false);
						for(int i=0;i<msgids.size();i++){
							if(mymsgs.get(position).getMsgID()==msgids.get(i)){
								adapter.setType(0);
								adapter.setSelectedPosition(position, 1);
								adapter.notifyDataSetInvalidated();
								msgids.remove(i);
								return;
							}
						}
						adapter.setType(0);
						adapter.setSelectedPosition(position, 0);
						adapter.notifyDataSetInvalidated();
						msgids.add(mymsgs.get(position).getMsgID());	
					}else{
						
						send_adapter.setAllSelect(false);
						
						for(int i=0;i<msgids.size();i++){
							if(submitMsgs.get(position).getId()==msgids.get(i)){
								send_adapter.setType(0);
								send_adapter.setSelectedPosition(position, 1);
								send_adapter.notifyDataSetInvalidated();
								msgids.remove(i);
								return;
							}
						}
						send_adapter.setType(0);
						send_adapter.setSelectedPosition(position, 0);
						send_adapter.notifyDataSetInvalidated();
						msgids.add(submitMsgs.get(position).getId());
					}	
				}
				
				
				/**
				if(inbox){
					if(ids.size()>0){
						for(int i=0;i<ids.size();i++){
							if(position==ids.get(i).getId()){
								if(ids.get(i).getLongclick()==0){
									ids.clear();
									adapter.setType(1);
									delete.setVisibility(View.GONE);
							    	adapter.notifyDataSetInvalidated();
							    	return;
								}else{
									adapter.setType(0);
									adapter.setSelectedPosition(position, 1);
									adapter.notifyDataSetInvalidated();
									ids.remove(i);
									return;
								}
								
							}
						}
						adapter.setType(0);
						adapter.setSelectedPosition(position, 0);	
						adapter.notifyDataSetInvalidated();
						listID=new ListID();
						listID.setId(position);
						listID.setLongclick(1);
						ids.add(listID);
						
					}else{
						//指向详细信息 可回复页面
						long msgID=mymsgs.get(position).getMsgID();
						Intent intent=new Intent(MessageActivity.this,MessageInfo.class);
						intent.putExtra("msgID", msgID);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				}else{
					if(ids.size()>0){
						for(int i=0;i<ids.size();i++){
							if(position==ids.get(i).getId()){
								if(ids.get(i).getLongclick()==0){
									ids.clear();
									send_adapter.setType(1);
									delete.setVisibility(View.GONE);
									send_adapter.notifyDataSetInvalidated();
							    	return;
								}else{
									send_adapter.setType(0);
									send_adapter.setSelectedPosition(position, 1);
									send_adapter.notifyDataSetInvalidated();
									ids.remove(i);
									return;
								}
								
							}
						}
						send_adapter.setType(0);
						send_adapter.setSelectedPosition(position, 0);	
						send_adapter.notifyDataSetInvalidated();
						listID=new ListID();
						listID.setId(position);
						listID.setLongclick(1);
						ids.add(listID);
						
					}else{
						//指向发送页面
						long sendID=submitMsgs.get(position).getId();
						Intent intent=new Intent(MessageActivity.this,SendMsgInfo.class);
						intent.putExtra("sendID", sendID);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				}
				*/
			}
		});
	}

	public void loadData(){
		
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("正在获取未读消息...");
			getUnreadMsgList_protocol=new GetUnReadMsgList();
			getUnreadMsgList_protocol.setUser(cEappApp.getUser());
			getUnreadMsgList_protocol.setPass(cEappApp.getPass());
			NetUtil.post(Constant.BASE_URL, getUnreadMsgList_protocol, handler, MessageWhat.GET_UNREADMSG_LIST_RESP);
		
		}else{
			loadlocalData();
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
			case MessageWhat.GET_UNREADMSG_LIST_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					getUnReadMsgListResp=JsonUtil.fromJson((String)msg.obj,GetUnReadMsgListResp.class);
					Log.v("getunreadmsg", (String)msg.obj);
					if(null==getUnReadMsgListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getUnReadMsgListResp.getStatus()){
						messages=getUnReadMsgListResp.getMsgs();
						if(messages==null||messages.size()==0){
							loadlocalData();
							
						}else{
							for(int i=0;i<messages.size();i++){
								ContentValues content=new ContentValues();
								content.put("id", messages.get(i).getMsgID());
								StringBuilder sender_sb=new StringBuilder();
								UserBase sender=messages.get(i).getSender();
								sender_sb.append(sender.getId()).append(",").append(sender.getNickname()).append(",")
								.append(sender.getRealName()).append(",").append(sender.getUserName());	
								content.put("sender", sender_sb.toString());
								content.put("contentType", messages.get(i).getContentType());
								content.put("contentCode", messages.get(i).getContentCode());
								content.put("content", messages.get(i).getContent());
								content.put("time", messages.get(i).getTime());
								
								content.put("unread", IntentValue.UNREAD);
								cEappApp.dbManager.insertData(TableName.SHORT_MSG, content);
							}												
							loadlocalData();		
						}
															
						
					}else{
						//加载不成功
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
				if(mymsgs.size()==0){
					empty_text.setVisibility(View.GONE);
					msglist.setVisibility(View.VISIBLE);
					mymsgs.add(pushMsg.getMsg());
					adapter=new ListViewMessageAdapter(this, mymsgs, R.layout.message_item);
					msglist.setAdapter(adapter);
				}else{
					mymsgs.add(0, pushMsg.getMsg());
					adapter.notifyDataSetChanged();
				}

                break;
		}
		return false;
	}
	
	
}
