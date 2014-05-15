package com.e1858.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.e1858.BaseActivity;
import com.e1858.adapter.ListViewMsgInfoAdapter;
import com.e1858.bean.MsgInfo;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentCoding;
import com.e1858.common.ContentType;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.SendStatusTyp;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Deliver;
import com.e1858.protocol.UserBase;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.SendDeliver;
import com.e1858.protocol.http.SendDeliverResp;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.TelephoneUitl;
import com.e1858.utils.ToastUtil;
public class MessageInfo extends BaseActivity {
	
	private ListView 				msginfo_list;
	private EditText 				sendmsg_edit;
	private Button   				btn_send;
	private Button                  button_back;
	
	private long 					msgID;
	private boolean    				reReply=false;
	
	private List<MsgInfo>  			msgs=new ArrayList<MsgInfo>();
	private MsgInfo 				entity;
	
	private ListViewMsgInfoAdapter 	adapter;
	
	private SendDeliver 			senddeliver_protocol;	
	private SendDeliverResp 		sendDeliverResp;
	
	private Deliver 				deliver;
	private String                  deliver_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_info);
		Intent intent=getIntent();
		msgID=intent.getLongExtra("msgID", 0);		
		initView();	
		loadData();		
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);
		msginfo_list=(ListView)findViewById(R.id.msginfo_list);
		sendmsg_edit=(EditText)findViewById(R.id.sendmsg_edit);
		btn_send=(Button)findViewById(R.id.btn_send);
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MessageInfo.this,MessageActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			}
		});
		
		msginfo_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(msgs.get(position).getStatus()==SendStatusTyp.FAILED){
					
					AlertDialog dialog=new AlertDialog.Builder(MessageInfo.this).setTitle("重发").setMessage("确定重发该消息？").
							setNegativeButton("取消", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
								
							}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									//重发
									reReply=true;
									if(NetUtil.checkNetWorkStatus(MessageInfo.this)){
										deliver=new Deliver();
										
										deliver.setDeliverID(-1);
									
										deliver.setMsgID(msgID);
										
										deliver.setImei(TelephoneUitl.getDeviceId(MessageInfo.this));
										
										deliver.setMobile(cEappApp.getStudent().getMobile());
										
										deliver.setContent(msgs.get(position).getContent());
										
										deliver.setContentCode(ContentCoding.UTF8);
										
										deliver.setContentType(ContentType.TEXT);				
										
										senddeliver_protocol=new SendDeliver();									
										senddeliver_protocol.setDeliver(deliver);
										senddeliver_protocol.setUser(cEappApp.getUser());
										senddeliver_protocol.setPass(cEappApp.getPass());	
										
										NetUtil.post(Constant.BASE_URL, senddeliver_protocol, handler, MessageWhat.SEND_DELIVER_RESP);
									}else{
										ToastUtil.showShort(MessageInfo.this, getResources().getString(R.string.network_fail));
									}
									
								}
							}).create();
					dialog.show();
							
				}else{
					return;
				}
			}
		
		});
		
		btn_send.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendmsg_edit.getText().toString().equals("")){
					ToastUtil.showShort(MessageInfo.this, "回复消息不能为空");
				}else{
					
					entity=new MsgInfo();
					
					entity.setId(-1);
					
					entity.setComeMsg(false);
					
					deliver_content=sendmsg_edit.getText().toString().trim();
					entity.setContent(sendmsg_edit.getText().toString().trim());	
					try{
						entity.setTime(DateUtil.getStringToday());
					}catch (Exception e) {
						// TODO: handle exception
					}
					entity.setStatus(SendStatusTyp.WAIT_SEND);
					
					msgs.add(entity);	
					
					adapter=new ListViewMsgInfoAdapter(MessageInfo.this, msgs, R.layout.message_info_left, R.layout.message_info_right);				
					msginfo_list.setAdapter(adapter);									
					sendmsg_edit.setText("");				
					msginfo_list.setSelection(msgs.size());				
					replyRequest();
				}
			}
		});
	}
	
	private void loadData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.SHORT_MSG +" where id=?", new String[]{""+msgID});
			
			String title_str=c.getString(c.getColumnIndex("sender"));
			String[] sender=title_str.split(",");
			UserBase user=new UserBase();
			user.setId(Integer.valueOf(sender[0]));
			user.setNickname(sender[1]);
			user.setRealName(sender[2]);
			user.setUserName(sender[3]);
			title.setText(user.getRealName());
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				entity=new MsgInfo();
				entity.setId(msgID);
				entity.setComeMsg(true);
				entity.setContent(c.getString(c.getColumnIndex("content")));
				entity.setTime(c.getString(c.getColumnIndex("time")));
				entity.setStatus(SendStatusTyp.SUCCESS);
				msgs.add(entity);
			}
			c.close();
			
			Cursor rec=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.RESHORT_MSG+" where msgID=?", new String[]{""+msgID});
			for(int j=0;j<rec.getCount();j++){
				rec.moveToPosition(j);
				entity=new MsgInfo();
				entity.setId(rec.getInt(rec.getColumnIndex("id")));
				entity.setContent(rec.getString(rec.getColumnIndex("content")));
				entity.setTime(rec.getString(rec.getColumnIndex("time")));
				entity.setComeMsg(false);	
				entity.setStatus(rec.getInt(rec.getColumnIndex("status")));
				msgs.add(entity);
			}
			rec.close();
			
			ContentValues values=new ContentValues();
			
			values.put("unread", IntentValue.READ);
			
			cEappApp.dbManager.updataData(TableName.SHORT_MSG, values, "id=?", new String[]{""+msgID});		
			
			adapter=new ListViewMsgInfoAdapter(this, msgs, R.layout.message_info_left, R.layout.message_info_right);
			
			msginfo_list.setAdapter(adapter);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void replyRequest(){
			deliver=new Deliver();		
			deliver.setDeliverID(-1);
			deliver.setMsgID(msgID);
			deliver.setContent(deliver_content);
			deliver.setContentCode(ContentCoding.UTF8);
			deliver.setContentType(ContentType.TEXT);				
			senddeliver_protocol=new SendDeliver();
			
			senddeliver_protocol.setDeliver(deliver);
			senddeliver_protocol.setUser(cEappApp.getUser());
			senddeliver_protocol.setPass(cEappApp.getPass());	
			
			NetUtil.post(Constant.BASE_URL, senddeliver_protocol, handler, MessageWhat.SEND_DELIVER_RESP);
	}
	
	
	

	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			return true;
		}
		switch(msg.what){
			case MessageWhat.SEND_DELIVER_RESP:
				if(null!=(String)msg.obj){
					
					sendDeliverResp=JsonUtil.fromJson((String)msg.obj, SendDeliverResp.class);
					Log.v("sendDeliverResp", (String)msg.obj);
					
					if(sendDeliverResp==null){
						break;
					}
					if (HttpDefine.RESPONSE_SUCCESS == sendDeliverResp.getStatus()){
						//标记成功状态
						long deliverID=sendDeliverResp.getDeliver();			
						msgs.get(msgs.size()-1).setStatus(SendStatusTyp.SUCCESS);
						//更新adapter
						adapter.notifyDataSetChanged();		
						//展示最后一条
						msginfo_list.setSelection(msgs.size()-1);	
						btn_send.setEnabled(true);
						if(reReply){
							try{
								cEappApp.dbManager.updateDataBySql("update "+TableName.RESHORT_MSG+" set id="+deliverID +"where msgID="+msgID +"and id=-1", null);
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}	
						}else{
							//入库
							ContentValues values=new ContentValues();
							values.put("id", deliverID);
							
							values.put("msgID", msgID);
							
							values.put("contentType", ContentType.TEXT);
							
							values.put("contentCode", ContentCoding.UTF8);
							
							values.put("content",msgs.get(msgs.size()-1).getContent());
							
							values.put("time", DateUtil.getStringToday());
							
							values.put("status", SendStatusTyp.SUCCESS);
							
							cEappApp.dbManager.insertData(TableName.RESHORT_MSG, values);
						}	
					}else{
						//发送失败
						msgs.get(msgs.size()-1).setStatus(SendStatusTyp.FAILED);
						
						adapter.notifyDataSetChanged();		
					
						msginfo_list.setSelection(msgs.size()-1);
						
						btn_send.setEnabled(false);
						
						ContentValues values=new ContentValues();
						
						values.put("id", -1);	
						
						values.put("msgID", msgID);
						
						values.put("contentType", ContentType.TEXT);
						
						values.put("contentCode", ContentCoding.UTF8);
						
						values.put("content",msgs.get(msgs.size()-1).getContent());
						
						values.put("time", DateUtil.getStringDate());
						
						values.put("status", SendStatusTyp.FAILED);
						
						cEappApp.dbManager.insertData(TableName.RESHORT_MSG, values);
			
					}
				}
				msg.obj=null;
				break;
		}
		return false;
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
