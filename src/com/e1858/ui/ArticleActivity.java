package com.e1858.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.e1858.BaseMenuActivity;
import com.e1858.adapter.ListViewArticleAdapter;
import com.e1858.adapter.ListViewGroupAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.DirectionType;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.NewDataToast;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.LibBase;
import com.e1858.protocol.LibGroup;
import com.e1858.protocol.http.GetLibraryGroupList;
import com.e1858.protocol.http.GetLibraryGroupListResp;
import com.e1858.protocol.http.GetLibraryList;
import com.e1858.protocol.http.GetLibraryListResp;
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

public class ArticleActivity extends BaseMenuActivity {	
	private PullToRefreshListView 		article_list;	
	private TextView                    empty_text;
	private GetLibraryGroupList   		getLibraryGroupList;
	private GetLibraryGroupListResp 	getLibraryGroupListResp;	
	private GetLibraryList          	getLibraryList;
	private GetLibraryListResp      	getLibraryListResp;
	private List<LibGroup>   	    	grouplist=new ArrayList<LibGroup>();
	private ListViewGroupAdapter 		groupAdapter;
	private List<LibBase>           	serverlibs=new ArrayList<LibBase>();
	private LinkedList<LibBase>         libs=new LinkedList<LibBase>();
	private LibBase                     entity;
	private LibGroup                    group_entity;	
	private ListViewArticleAdapter  	adapter;	
	private long                  		groupID;
	private long                    	libID=-1;
	private int                         direction=DirectionType.FIRST;
	private int                         pageSize =20;
	private View                        footer_view;
	private ProgressBar                 footer_bar;
	private TextView                    footer_more;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.article);	
		doScroll(ModuleInteger.ARTICLE);	
		initView();
		loadGroupData();
	}
	private void initView(){
		
		cEappApp.dbManager.createLibrary(TableName.LIBRARY+ModuleInteger.ARTICLE);
		cEappApp.dbManager.createLibraryGroup(TableName.LIBRARY_GROUP+ModuleInteger.ARTICLE);
		title_name.setText("素质提升");
		empty_text=(TextView)findViewById(R.id.empty_text);
		arrow.setVisibility(View.VISIBLE);
		arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_title_open));
		initknowlegeListView();
		pop_menu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(grouplist==null||grouplist.size()==0){
					return;
				}else{
					changPopState(v);
				}
				
			}
		});
	
	}
	private void initknowlegeListView(){
		article_list=(PullToRefreshListView)findViewById(R.id.article_list);
		footer_view=getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar=(ProgressBar)footer_view.findViewById(R.id.footer_bar);
		footer_more=(TextView)footer_view.findViewById(R.id.foot_more);
		
		article_list.addFooterView(footer_view);
		
		
		article_list.setOnScrollListener(new AbsListView.OnScrollListener() {
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					article_list.onScrollStateChanged(view, scrollState);
					//数据为空--不用继续下面代码了
					if(libs.isEmpty()) return;	
					//判断是否滚动到底部
					boolean scrollEnd = false;
					try {
						if(view.getPositionForView(footer_view) == view.getLastVisiblePosition())
							scrollEnd = true;
					} catch (Exception e) {
						scrollEnd = false;
					}
					if(scrollEnd)
					{
						if(view.getCount()>=pageSize){
							footer_more.setText(R.string.pull_to_refresh_refreshing_label);
							footer_bar.setVisibility(View.VISIBLE);
							Log.v("view.getCount", "===="+view.getCount());
							
							libID=libs.get(view.getCount()-3).getId();
							direction=DirectionType.OLD;
							loadData(groupID, libID, direction);
						}else{
							footer_bar.setVisibility(View.GONE);
							footer_more.setText(getResources().getString(R.string.loading_full));
						}
						
						
					}
				}
				public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
					article_list.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				}
			});
			article_list.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
	            public void onRefresh() {
	            	libID=libs.get(0).getId();
					direction=DirectionType.NEW;
					loadData(groupID, libID, direction);
	            }
	        });						
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
			 groupAdapter=new ListViewGroupAdapter(this, grouplist,R.layout.pop_menu_item);			
			 group_list.setAdapter(groupAdapter); 
			 group_list.setItemsCanFocus(false); 
			 group_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
			 group_list.setOnItemClickListener(new OnItemClickListener() {

				 public void onItemClick(AdapterView<?> arg0, View view,
						 int position, long arg3) {
					// TODO Auto-generated method stub
					 //对分类的处理
					 groupID=grouplist.get(position).getId();
					 title_name.setText(grouplist.get(position).getName());
					 try{
						 Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.LIBRARY+ModuleInteger.ARTICLE +" where groupID=?", new String[]{""+groupID});
						 if(c.getCount()==0){
							 if(NetUtil.checkNetWorkStatus(ArticleActivity.this)){
								 direction=DirectionType.FIRST;
								 libs.clear();
								 loadData(groupID,-1,direction); 
							 }else{
								 libs.clear();
								
								 loadlocalLibData(groupID);
							 } 
						 }else{
							 libs.clear();
							 loadlocalLibData(groupID);
						 }
						 c.close();
					 }catch(Exception e){
						 e.printStackTrace();
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
	 private void loadlocalData(){
		 try{
			 Cursor c=cEappApp.dbManager.queryData2Cursor("select *  from "+TableName.LIBRARY_GROUP+ModuleInteger.ARTICLE, null);
			 if(c.getCount()==0){
				 Cursor cursor=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LIBRARY+ModuleInteger.ARTICLE, null);
				 if(cursor.getCount()==0){
					 return;
				 }else{
					 for(int i=0;i<cursor.getCount();i++){
						cursor.moveToPosition(i);
						entity=new LibBase();
						entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
						entity.setGroupID(cursor.getInt(cursor.getColumnIndex("groupID")));
						entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
						entity.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
						entity.setPicture(cursor.getInt(cursor.getColumnIndex("picture")));
						entity.setIssuedDate(cursor.getString(cursor.getColumnIndex("issuedDate")));
						libs.add(entity);
					 }
					 cursor.close();
				 }
			 }else{
				for(int i=0;i<c.getCount();i++){
					c.moveToPosition(i);
					group_entity=new LibGroup();
					group_entity.setId(c.getInt(c.getColumnIndex("id")));
					group_entity.setName(c.getString(c.getColumnIndex("name")));
					group_entity.setPicture(c.getInt(c.getColumnIndex("picture")));
					group_entity.setSrcMode(c.getInt(c.getColumnIndex("srcMode")));
					group_entity.setSrcUrl(c.getString(c.getColumnIndex("srcUrl")));
					grouplist.add(group_entity);
				}
				title_name.setText(grouplist.get(0).getName());
				loadlocalLibData(grouplist.get(0).getId());
			 }
			 c.close();
		 }catch(Exception e){
			 
		 }
	 }
	 private void loadlocalLibData(long groupID){
		 try{
			 Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LIBRARY+ModuleInteger.ARTICLE+" where groupID=? order by id desc ", new String[]{""+groupID});
			 
			 Log.v("c.getcount","=="+c.getCount()); 
			 for(int i=0;i<c.getCount();i++){
				 c.moveToPosition(i);
				 Log.v("id", "===="+c.getInt(c.getColumnIndex("id")));
				 entity=new LibBase();
				 entity.setId(c.getInt(c.getColumnIndex("id")));
				 entity.setGroupID(c.getInt(c.getColumnIndex("groupID")));
				 entity.setTitle(c.getString(c.getColumnIndex("title")));
				 entity.setAuthor(c.getString(c.getColumnIndex("author")));
				 entity.setPicture(c.getInt(c.getColumnIndex("picture")));
				 entity.setIssuedDate(c.getString(c.getColumnIndex("issuedDate")));
				 libs.add(entity);
			 }
			 if(libs.size()<pageSize){
				 footer_bar.setVisibility(View.GONE);
				 footer_more.setText(getResources().getString(R.string.loading_full));
			 }
			 if(libs.size()==0){
				 article_list.setVisibility(View.GONE);
				 empty_text.setVisibility(View.VISIBLE);
				 empty_text.setText("很抱歉,暂无数据！");
			 }else{
				 article_list.setVisibility(View.VISIBLE);
				 empty_text.setVisibility(View.GONE);
				 adapter=new ListViewArticleAdapter(this, libs, R.layout.article_item);
				 article_list.setAdapter(adapter);
			 }
			 
			 
			c.close(); 
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
	 }
	 
	 
	private void loadGroupData(){
		if(NetUtil.checkNetWorkStatus(this)){
			getLibraryGroupList=new GetLibraryGroupList();
			getLibraryGroupList.setUser(cEappApp.getUser());
			getLibraryGroupList.setPass(cEappApp.getPass());
			getLibraryGroupList.setModule(ModuleInteger.ARTICLE);
			NetUtil.post(Constant.BASE_URL, getLibraryGroupList, handler, MessageWhat.GET_LIBRARYGROUP_LIST_RESP);
		}else{
			loadlocalData();
			ToastUtil.showShort(this, getResources().getString(R.string.network_fail));
		}
	}
	
	private void loadData(long groupID,long libID,int direction){
//		openProgressDialog("加载中...");
		getLibraryList=new GetLibraryList();
		getLibraryList.setUser(cEappApp.getUser());
		getLibraryList.setPass(cEappApp.getPass());
	    getLibraryList.setId(libID);
	    getLibraryList.setCnt(pageSize);
		getLibraryList.setDirection(direction);
		getLibraryList.setGroup(groupID);
		NetUtil.post(Constant.BASE_URL, getLibraryList, handler, MessageWhat.GET_LIBRARY_LIST_RESP);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			return true;
		}
		switch(msg.what){
			case MessageWhat.GET_LIBRARYGROUP_LIST_RESP:
				if(null!=(String)msg.obj){
					Log.v("knowlegegroup", "===="+(String)msg.obj);
					getLibraryGroupListResp=JsonUtil.fromJson((String)msg.obj, GetLibraryGroupListResp.class);
					if(null==getLibraryGroupListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getLibraryGroupListResp.getStatus()){
						grouplist=getLibraryGroupListResp.getGroups();	
						if(grouplist==null||grouplist.size()==0){
							direction=DirectionType.FIRST;
							loadData(-1, 0,direction);
							break;
						}
						cEappApp.dbManager.deleteData(TableName.LIBRARY_GROUP+ModuleInteger.ARTICLE, null, null);
						for(int i=0;i<grouplist.size();i++){	
							ContentValues values=new ContentValues();
							values.put("id", grouplist.get(i).getId());
							values.put("name", grouplist.get(i).getName());	
							values.put("picture",grouplist.get(i).getPicture());
							values.put("srcMode", grouplist.get(i).getSrcMode());
							values.put("srcUrl", grouplist.get(i).getSrcUrl());
							cEappApp.dbManager.insertData(TableName.LIBRARY_GROUP+ModuleInteger.ARTICLE, values);
						}
						groupID=grouplist.get(0).getId();
						title_name.setText(grouplist.get(0).getName());
						try{
							Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LIBRARY+ModuleInteger.ARTICLE +" where groupID=?", new String[]{""+groupID});
							if(c.getCount()==0){
								direction=DirectionType.FIRST;
								libID=-1;
								loadData(groupID,libID,direction);
							}else{
								loadlocalLibData(groupID);
							}
							c.close();
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}	
					}
					msg.obj=null;
				}
				break;
			case MessageWhat.GET_LIBRARY_LIST_RESP:
				if(null!=(String)msg.obj){
					Log.v("knowlege", "===="+(String)msg.obj);
					getLibraryListResp=JsonUtil.fromJson((String)msg.obj,GetLibraryListResp.class);
					if(getLibraryListResp==null){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getLibraryListResp.getStatus()){
						serverlibs=getLibraryListResp.getLibrarys();	
						if((serverlibs==null||serverlibs.size()==0)&&direction==DirectionType.OLD){	
							footer_bar.setVisibility(View.GONE);
							footer_more.setText(getResources().getString(R.string.loading_full));
							break;
						}
						if((serverlibs==null||serverlibs.size()==0)&&direction==DirectionType.NEW){
							article_list.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							NewDataToast.makeText(this, getString(R.string.new_data_toast_none), false).show();
							break;
						}
						if((serverlibs==null||serverlibs.size()==0)&&direction==DirectionType.FIRST){
							article_list.setVisibility(View.GONE);
							empty_text.setVisibility(View.VISIBLE);
							empty_text.setText("很抱歉,暂无数据!");
							break;
						}
						
						if(direction==DirectionType.NEW){
							article_list.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							NewDataToast.makeText(this, getString(R.string.new_data_toast_message,serverlibs.size()), false).show();	
							for(int i=serverlibs.size()-1;i>=0;i--){
								entity=serverlibs.get(i);		
								libs.addFirst(entity);	
								adapter.notifyDataSetChanged();
							}
							if(serverlibs.size()==pageSize){
								try{
									cEappApp.dbManager.deleteData(TableName.LIBRARY+ModuleInteger.ARTICLE , "groupID=?", new String[]{""+groupID});
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							for(int i=0;i<serverlibs.size();i++){
								ContentValues values=new ContentValues();
								values.put("id", serverlibs.get(i).getId());
								values.put("groupID",serverlibs.get(i).getGroupID());
								values.put("title", serverlibs.get(i).getTitle());
								values.put("issuedDate", serverlibs.get(i).getIssuedDate());
								values.put("author", serverlibs.get(i).getAuthor());
								values.put("picture", serverlibs.get(i).getPicture());					
								cEappApp.dbManager.insertData(TableName.LIBRARY+ModuleInteger.ARTICLE, values);
							}
							
						}else if(direction==DirectionType.OLD){
	
							for(int i=0;i<serverlibs.size();i++){
								entity=serverlibs.get(i);
								libs.addLast(entity);	
							}
							if(serverlibs.size()==pageSize){
								footer_bar.setVisibility(View.GONE);
								footer_more.setText(getResources().getString(R.string.loading_more));
							}else{
								footer_bar.setVisibility(View.GONE);
								footer_more.setText(getResources().getString(R.string.loading_full));
							}
							
							
							
							
						}else if(direction==DirectionType.FIRST){
							for(int i=0;i<serverlibs.size();i++){
								entity=serverlibs.get(i);
								libs.addLast(entity);	
							}
							if(serverlibs.size()==pageSize){
								footer_bar.setVisibility(View.GONE);
								footer_more.setText(getResources().getString(R.string.loading_more));
							}else{
								footer_bar.setVisibility(View.GONE);
								footer_more.setText(getResources().getString(R.string.loading_full));
							}
							
							cEappApp.dbManager.deleteData(TableName.LIBRARY+ModuleInteger.ARTICLE, "groupID=?", new String[]{""+groupID});
							for(int i=0;i<serverlibs.size();i++){
								ContentValues values=new ContentValues();
								values.put("id", serverlibs.get(i).getId());
								values.put("groupID",serverlibs.get(i).getGroupID());
								values.put("title", serverlibs.get(i).getTitle());
								values.put("issuedDate", serverlibs.get(i).getIssuedDate());
								values.put("author", serverlibs.get(i).getAuthor());
								values.put("picture", serverlibs.get(i).getPicture());					
								cEappApp.dbManager.insertData(TableName.LIBRARY+ModuleInteger.ARTICLE, values);
							}
						}
						
						 if(libs.size()==0){
							 article_list.setVisibility(View.GONE);
							 empty_text.setVisibility(View.VISIBLE);
							 empty_text.setText("很抱歉,暂无数据！");
						 }else{
							 article_list.setVisibility(View.VISIBLE);
							 empty_text.setVisibility(View.GONE);
							 adapter=new ListViewArticleAdapter(this, libs, R.layout.article_item);
							 article_list.setAdapter(adapter);
							 if(direction==DirectionType.OLD){
								 article_list.setSelection(article_list.getCount()-serverlibs.size()+1);
								}
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
