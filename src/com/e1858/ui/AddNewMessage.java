package com.e1858.ui;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentCoding;
import com.e1858.common.ContentType;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.SendModeType;
import com.e1858.common.SendStatusTyp;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.DepBase;
import com.e1858.protocol.SubmitMsg;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.SendMsg;
import com.e1858.protocol.http.SendMsgResp;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.FixGridLayout;

public class AddNewMessage extends BaseActivity {
	
	private Button   		addusers;
	
	private EditText		content;	
	private RadioGroup      mode_radio_group;
	private SendMsg 		send_protocol;
	
	private SendMsgResp 	sendMsgResp;
	
	private FixGridLayout 	layout_add;
	
	private SubmitMsg 		subMsg;
	
	private Button          button_back;
	
	private int 			index=1;

	private int 			indextag=0;
	
	private boolean 		isSelected=true;
	
	private List<DepBase>   classes=new ArrayList<DepBase>();
	
	private List<DepBase>   teas   =new ArrayList<DepBase>();
	
	private List<Long>      classID=new ArrayList<Long>();
	
	private List<Long>      teaID=new ArrayList<Long>();
	
	private List<Long>      classDepID=new ArrayList<Long>();
	
	private List<Long>      teaDepID=new ArrayList<Long>();
	
	private List<Long>      teaProID=new ArrayList<Long>();
	
	private List<Long>      userID=new ArrayList<Long>();
	
	private StringBuilder   sendUsers=new StringBuilder();
	private StringBuilder   sendUsersID=new StringBuilder();

	private String[]        usersname;
	private String[]        ids;
	
	private boolean        app_mode=false;
	
	private boolean        mobile_mode=true;
	

	private boolean        isDouble=false;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnew_message);
		initView();
		loadSendUsers();
	}
	
	private void loadSendUsers(){
		classes=cEappApp.getClasses();	
		teas=cEappApp.getTeas();	
		
		if(classes!=null){
	
			for(int i=0;i<classes.size();i++){
				if(classes.get(i).isSelected()){
					sendUsers.append(classes.get(i).getName()+"(学生)").append(",");
					sendUsersID.append(classes.get(i).getId()).append(",");
				}else{
					sendUsers.append(classes.get(i).getUsers()).append(",");
					for(int j=0;j<classes.get(i).getClass_ids().size();j++){
						sendUsersID.append(classes.get(i).getClass_ids().get(j)).append(",");
					}
				}	
			}
		}
		if(teas!=null){
	
			for(int j=0;j<teas.size();j++){
				if(teas.get(j).isSelected()){
					sendUsers.append(teas.get(j).getName()+"(教师)").append(",");
					sendUsersID.append("99"+teas.get(j).getId()+"99").append(",");
				}else{
					sendUsers.append(teas.get(j).getUsers()).append(",");
					for(int i=0;i<teas.get(j).getTea_ids().size();i++){
						sendUsersID.append("99"+teas.get(j).getTea_ids().get(i)+"99").append(",");
					}
				}

			}
		}
		if(classes!=null||teas!=null){

			sendUsers.deleteCharAt(sendUsers.length()-1);
			sendUsersID.deleteCharAt(sendUsersID.length()-1);
			
			usersname=sendUsers.toString().split(",");	
			ids=sendUsersID.toString().split(",");
			
			for(int i=0;i<usersname.length;i++){
				final TextView text=new TextView(AddNewMessage.this);
				text.setText(usersname[i]);
				text.setTag(Integer.valueOf(ids[i]));
			
				
				text.setTextSize(getResources().getDimension(R.dimen.text_size_10));
				text.setTextColor(getResources().getColor(R.color.black));
				text.setBackgroundColor(getResources().getColor(R.color.text_bg_light_gray));			
				text.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
						// TODO Auto-generated method stub
					if(isSelected){
						text.setTextColor(getResources().getColor(R.color.white));
						text.setBackgroundColor(getResources().getColor(R.color.text_bg_dark_gray));
						indextag=(Integer)text.getTag();
						isSelected=false;
					}else{
						text.setTextColor(getResources().getColor(R.color.black));
						text.setBackgroundColor(getResources().getColor(R.color.text_bg_light_gray));
						isSelected=true;
					}
				}
				});
				layout_add.addView(text,index);
				index++;
			}
	
		}
	
	}
	
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("编辑信息");
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		button_back.setVisibility(View.VISIBLE);

		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddNewMessage.this,MessageActivity.class);
				startActivity(intent);
				AddNewMessage.this.finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
			}
		});
		
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setText("发送");
		btn_right.setVisibility(View.VISIBLE);		
		layout_add=(FixGridLayout)findViewById(R.id.layout_add);	
		
//		sendusers=(EditText)findViewById(R.id.sendusers);
		
		addusers=(Button)findViewById(R.id.add_sender);
		content=(EditText)findViewById(R.id.content);
		
		mode_radio_group=(RadioGroup)findViewById(R.id.mode_radio_group);
		mode_radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio0:
					mobile_mode=true;
					break;
				case R.id.radio1:
					app_mode=true;
					break;
				}
			}
		});
		
		/**
		mobile_msg=(CheckBox)findViewById(R.id.mobile_msg);		
		app_msg=(CheckBox)findViewById(R.id.app_msg);
		
		
		mobile_msg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mobile_mode=true;
				}
				
			}
		});
	
		app_msg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					app_mode=true;
				}	
				
			}
		});
	
		 */
		layout_add.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode==event.KEYCODE_DEL&&isSelected==false){
					
					layout_add.removeView(layout_add.findViewWithTag(indextag));
					
					index--;
					
					String  tag=String.valueOf(indextag);
					
					if(tag.length()>4&&tag.substring(0, 2).equals("99")){
						if(teas!=null){

							Log.v("tag", tag);
							
							tag=tag.substring(2, tag.length()-2);

							for(int i=0;i<teas.size();i++){
								if(Long.valueOf(tag)==teas.get(i).getId()){
									teas.remove(i);
									break;
								}else{
									for(int j=0;j<teas.get(i).getTea_ids().size();j++){
										if(Long.valueOf(tag)==teas.get(i).getTea_ids().get(j)){
											teas.get(i).getTea_ids().remove(j);
											break;
										}
									}
								}
							}
						}
					}else{
						if(classes!=null){
							for(int i=0;i<classes.size();i++){
								if(indextag==classes.get(i).getId()){
									classes.remove(i);
									break;
								}else{
									for(int j=0;j<classes.get(i).getClass_ids().size();j++){
										if(indextag==classes.get(i).getClass_ids().get(j)){
											classes.get(i).getClass_ids().remove(j);
											break;
										}
									}
								}
							}	
						}
					}
					
					if(layout_add.getChildCount()==0){
						cEappApp.setClasses(null);
						cEappApp.setTeas(null);
					}
				}
				return false;
			}
		});
		/**
		sendusers.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, final int keyCode, final KeyEvent event) {
				// TODO Auto-generated method stub
				
				if(keyCode==event.KEYCODE_ENTER){
					final TextView text=new TextView(AddNewMessage.this);
					text.setText(sendusers.getText());
					text.setTag(indextag);
					text.setTextSize(getResources().getDimension(R.dimen.text_size_10));
					text.setTextColor(getResources().getColor(R.color.black));
					text.setBackgroundColor(getResources().getColor(R.color.listitem_transparent));			
					text.setOnClickListener(new OnClickListener() {		
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(isSelected){
								text.setTextColor(getResources().getColor(R.color.white));
								text.setBackgroundColor(getResources().getColor(R.color.text_bg_dark_gray));
								indextag=(Integer)text.getTag();
								isSelected=false;
							}else{
								text.setTextColor(getResources().getColor(R.color.black));
								text.setBackgroundColor(getResources().getColor(R.color.listitem_transparent));
								isSelected=true;
							}
							
						}
					});
				
					layout_add.addView(text,index);
					indextag++;
					index++;
					sendusers.setText("");
				}
				if(keyCode==event.KEYCODE_DEL&&isSelected==false){
					layout_add.removeView(layout_add.findViewWithTag(indextag));
					index--;
				}
				return false;
			}
		});
		*/
		
		addusers.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddNewMessage.this,AddSendMsgUsersRole.class);
				startActivity(intent);
				AddNewMessage.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(layout_add.getChildCount()<=1){
					ToastUtil.showShort(AddNewMessage.this, "收件人为空");
				}else if(content.getText().toString().trim().equals("")){
					ToastUtil.showShort(AddNewMessage.this, "发送内容不能为空");
				}else if(!app_mode&&!mobile_mode){
					ToastUtil.showShort(AddNewMessage.this, "请至少选择一个发送模式！");
				}else{
					if(app_mode&&!mobile_mode){
						sendRequest(SendModeType.PUSHMSG);
					}else if(mobile_mode&&!app_mode){
						sendRequest(SendModeType.MOBILESMS);
					}else if(app_mode&&mobile_mode){
						isDouble=true;
						sendRequest(SendModeType.MOBILESMS);
						sendRequest(SendModeType.PUSHMSG);
						
					}
					Intent intent=new  Intent(AddNewMessage.this,MessageActivity.class);
					startActivity(intent);
					AddNewMessage.this.finish();
				}
				
			}
		});
	}
	private void sendRequest(int mode){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("发送中...");
			subMsg=new SubmitMsg();			
			
			subMsg.setId(-1);			
			
			subMsg.setMode(mode);					
			
			
			subMsg.setModule(ModuleInteger.MESSAGE);			
			
			List<Long> teadepID=new ArrayList<Long>();	
			
			List<Long>  teaID=new ArrayList<Long>();
			if(teas!=null){
				for(int i=0;i<teas.size();i++){
					if(teas.get(i).isSelected()){
						teadepID.add(teas.get(i).getId());
					}else{
						for(int j=0;j<teas.get(i).getTea_ids().size();j++){
							//得到teachers的id
							teaID.add(teas.get(i).getTea_ids().get(j));
						}
					}
					
					
				}
			}
			List<Long> studepID=new ArrayList<Long>();
			List<Long>  classesID=new ArrayList<Long>();
			if(classes!=null){
				for(int i=0;i<classes.size();i++){
					if(classes.get(i).isSelected()){
						studepID.add(classes.get(i).getId());
					}else{
						for(int j=0;j<classes.get(i).getClass_ids().size();j++){
							classesID.add(classes.get(i).getClass_ids().get(j));
						}
					}
					
				}
			}
			subMsg.setTeaDeps(teadepID);
			subMsg.setTeaPros(null);
			subMsg.setStuDeps(studepID);
			subMsg.setStuClasses(classesID);	
			subMsg.setUsers(teaID);	
			subMsg.setContentType(ContentType.TEXT);	
			subMsg.setContentCode(ContentCoding.UTF8);
			subMsg.setContent(content.getText().toString().trim());
			subMsg.setTime(DateUtil.getStringToday());		
			
			send_protocol=new SendMsg();			
			send_protocol.setUser(cEappApp.getUser());	
			send_protocol.setPass(cEappApp.getPass());		
			send_protocol.setSubmitMsg(subMsg);
	
			NetUtil.post(Constant.BASE_URL, send_protocol, handler, MessageWhat.SEND_MSG_RESP);
		
			
	
		}else{
		
			ToastUtil.showShort(AddNewMessage.this, getResources().getString(R.string.network_fail));
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
		case MessageWhat.SEND_MSG_RESP:
			if(null!=(String)msg.obj){
				closeProgressDialog();
				sendMsgResp=JsonUtil.fromJson((String)msg.obj,SendMsgResp.class);
				Log.v("sendMsgResp", (String)msg.obj);
				if(null==sendMsgResp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==sendMsgResp.getStatus()){
					cEappApp.setClasses(null);
					cEappApp.setTeas(null);
					
					long newTask=sendMsgResp.getTask();	
					/**
					try{
						Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.SUBMIT_MSG+ " where id=?", new String[]{""+newTask});
						if(c.getCount()!=0){
							cEappApp.dbManager.deleteData(TableName.SUBMIT_MSG, "id=?", new String[]{newTask+""});
						}
						c.close();
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					*/
					
	
					ContentValues values=new ContentValues();
					values.put("id", newTask);
					Log.v("sendUsers", sendUsers.toString());
					if(app_mode&&!mobile_mode){
						values.put("mode", SendModeType.PUSHMSG);
					}else if(mobile_mode&&!app_mode){
						values.put("mode", SendModeType.MOBILESMS);
					}else if(isDouble&&app_mode&&mobile_mode){
						values.put("mode", SendModeType.MOBILESMS);
					}else if(!isDouble&&app_mode&&mobile_mode){
						values.put("mode", SendModeType.PUSHMSG);
					}
					values.put("receiver", sendUsers.toString());
					
					values.put("contentType", ContentType.TEXT);
					
					values.put("contentCode", ContentCoding.UTF8);
					
					values.put("content", content.getText().toString().trim());
					
					values.put("time",DateUtil.getStringToday());
					
					values.put("status", SendStatusTyp.SUCCESS);
					
					cEappApp.dbManager.insertData(TableName.SUBMIT_MSG, values);
					
					isDouble=false;
					
					
				}
			}
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
