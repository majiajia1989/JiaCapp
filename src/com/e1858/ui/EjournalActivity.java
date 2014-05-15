package com.e1858.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.e1858.BaseMenuActivity;
import com.e1858.adapter.ListViewEjournalAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.NewDataToast;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.EjournalBase;
import com.e1858.protocol.http.GetEjournalList;
import com.e1858.protocol.http.GetEjournalListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.protocol.http.LoginOutResp;
import com.e1858.protocol.socket.Disconnect;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;

public class EjournalActivity extends BaseMenuActivity {

	private List<EjournalBase> 		ejournals=new ArrayList<EjournalBase>();
	private List<EjournalBase>      server_ejournals=new ArrayList<EjournalBase>();
	
	private PullToRefreshListView   ejournallistview;
	private TextView                empty_text;
	private EjournalBase            ejournal;

	private GetEjournalList     	getEjournalList;
	private GetEjournalListResp 	getEjournalListResp;
	
	private ListViewEjournalAdapter adapter;
	private boolean                 isRefresh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.ejournal);
		doScroll(ModuleInteger.EJOURNAL);
		initView();
		judgeData();
	}
	
	
	private void initView(){
		
		title_name.setText("电子校刊");
		ejournallistview=(PullToRefreshListView)findViewById(R.id.ejournallist);
		empty_text=(TextView)findViewById(R.id.empty_text);
		ejournallistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener(){

			public void onRefresh() {
				// TODO Auto-generated method stub
				isRefresh=true;
				loadData();
			}
			
		});
		ejournallistview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				long[] ejournalidss=new long[ejournals.size()];
				for(int i=0;i<ejournals.size();i++){
					ejournalidss[i]=ejournals.get(i).getId();
				}
				long ejournalID=ejournals.get(position-1).getId();
				Intent intent=new Intent(EjournalActivity.this,EjournalInfo.class);
				intent.putExtra("ejournalID", ejournalID);
				intent.putExtra("ejournalids",ejournalidss);
				intent.putExtra("index", position-1);
				startActivity(intent);
			}
		});
		
		
	}
	private void judgeData(){
		try{
			
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.EJOURNAL, null);
			if(c.getCount()==0){
				loadData();
			}else{
				loadlocalData();
			}
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	private void loadData(){
		if(NetUtil.checkNetWorkStatus(this)){
			
			getEjournalList=new GetEjournalList();
			getEjournalList.setUser(cEappApp.getUser());
			getEjournalList.setPass(cEappApp.getPass());
			if(ejournals.size()==0){
				getEjournalList.setId(-1);
			}else{
				getEjournalList.setId(ejournals.get(0).getId());
			}
			NetUtil.post(Constant.BASE_URL, getEjournalList, handler, MessageWhat.GET_EJOURNAL_LIST_RESP);
		}else{
			loadlocalData();
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
	}
	private void loadlocalData(){
		ejournals.clear();
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.EJOURNAL +" order by id desc", null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				ejournal=new EjournalBase();
				ejournal.setId(c.getInt(c.getColumnIndex("id")));
				
				ejournal.setTitle(c.getString(c.getColumnIndex("title")));
				
				ejournals.add(ejournal);	
				
			}
			adapter=new ListViewEjournalAdapter(this, ejournals, R.layout.ejournal_item);
			ejournallistview.setAdapter(adapter);
			c.close();
		}catch(Exception e){
			
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			
			return true;
		}
		switch(msg.what){
			case MessageWhat.GET_EJOURNAL_LIST_RESP:
				if(null!=(String)msg.obj){
					
					getEjournalListResp=JsonUtil.fromJson((String)msg.obj, GetEjournalListResp.class);
					Log.v("ejournal", (String)msg.obj);
					if(null==getEjournalListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getEjournalListResp.getStatus()){
						
						server_ejournals=getEjournalListResp.getEjournals();
						if(isRefresh){
							ejournallistview.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							if(server_ejournals==null||server_ejournals.size()==0){
								NewDataToast.makeText(EjournalActivity.this, getString(R.string.new_data_toast_none), false).show();
								isRefresh=false;
								break;
							}else{
								NewDataToast.makeText(this, getString(R.string.new_data_toast_message,server_ejournals.size()), false).show();
								isRefresh=false;
								for(int i=0;i<server_ejournals.size();i++){
										ejournal=server_ejournals.get(i);
										ContentValues values=new ContentValues();
										values.put("id", server_ejournals.get(i).getId());
										values.put("title", server_ejournals.get(i).getTitle());
										
										cEappApp.dbManager.insertData(TableName.EJOURNAL, values);
										
										ejournals.add(0, ejournal);
										adapter.notifyDataSetChanged();
									
									
								}
							}
						}else{
							
							
							if(null==server_ejournals||server_ejournals.size()==0){
								empty_text.setVisibility(View.VISIBLE);
								ejournallistview.setVisibility(View.GONE);
								empty_text.setText("很抱歉,暂无数据！");
								break;
							}	
							StringBuilder sb=new StringBuilder();
							try{
								Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.EJOURNAL, null);
								for(int i=0;i<c.getCount();i++){
									c.moveToPosition(i);
									sb.append(c.getInt(c.getColumnIndex("id"))).append(",");
								}
								
								c.close();
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							for(int i=0;i<server_ejournals.size();i++){
								if(!sb.toString().contains(""+server_ejournals.get(i).getId())){

									ContentValues values=new ContentValues();
									
									values.put("id", server_ejournals.get(i).getId());
									
									values.put("title", server_ejournals.get(i).getTitle());
									
									cEappApp.dbManager.insertData(TableName.EJOURNAL, values);
								}
								
								
							}
							loadlocalData();
		
							
						}
						
						
						
					
					}
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
			
		}
		return false;
	}
	
	
}

