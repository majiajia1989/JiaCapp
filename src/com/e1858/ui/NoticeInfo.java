package com.e1858.ui;

import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Notice;
import com.e1858.protocol.http.DownLoad;
import com.e1858.protocol.http.GetNotice;
import com.e1858.protocol.http.GetNoticeResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.DateUtil;
import com.e1858.utils.FileUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.RoundProgressBar;

public class NoticeInfo extends BaseActivity {
	private TextView 		notice_title;
	private TextView 		notice_time;
	private TextView 		notice_content;	
	
	private Button   		download;
	
	private long    		noticeID;
	private boolean 		inNotice;
	private long[]  		noticeids;
	private int     		index;
	
	private GetNotice     getNotice;
	private GetNoticeResp getNoticeResp;
	
	private Notice        notice;
	private Dialog         			downdialog;
	private RoundProgressBar 		progressbar;
	private FileUtil                fileutil;
	private String                  filename;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_info);
		initView();
		fileutil=new FileUtil();
		Intent intent=getIntent();
		noticeID=intent.getLongExtra("noticeID", -1);
		inNotice=intent.getBooleanExtra("inNotice", true);
		noticeids=intent.getLongArrayExtra("noticeids");
		index=intent.getIntExtra("index", 0);
		if(inNotice){
			loadData(noticeID);
		}else{
			loadSendNotice(noticeID);
		}
		
	}

	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("公告内容");
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_right));
		btn_right.setVisibility(View.VISIBLE);
		
		btn_right_before=(Button)findViewById(R.id.btn_right_before);
		btn_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_left));
		btn_right_before.setVisibility(View.VISIBLE);
		
		notice_title=(TextView)findViewById(R.id.notice_title);
		notice_time=(TextView)findViewById(R.id.notice_time);
		notice_content=(TextView)findViewById(R.id.notice_content);
		download=(Button)findViewById(R.id.download);
		
		if(index==0){
			btn_right_before.setEnabled(false);
		}else if(index==noticeids.length-1){
			btn_right.setEnabled(false);
		}else{
			btn_right.setEnabled(true);
			btn_right_before.setEnabled(true);
		}
		
		download.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(download.getText().equals("下载附件")){
					downdialog=new Dialog(NoticeInfo.this,android.R.style.Theme_Translucent_NoTitleBar);
					downdialog.setContentView(R.layout.down_dialog);
					progressbar=(RoundProgressBar)downdialog.findViewById(R.id.roundProgressBar);
					progressbar.setProgress(cEappApp.getProgress());
					downdialog.show();			
					
					
					DownLoad downloads=new DownLoad();
					downloads.setUser(cEappApp.getUser());
					downloads.setPass(cEappApp.getPass());
					downloads.setId(Integer.valueOf(notice.getUrl()));
					NetUtil.downFile(cEappApp, Constant.BASE_URL, downloads.wrap(), Constant.PATH, Integer.valueOf(notice.getUrl()));
				}else{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					File f = new File(FileUtil.SDPATH+filename);
					String type = getMIMEType(f);
					intent.setDataAndType(Uri.fromFile(f), type);
					startActivity(intent);
				}
				
			}
		});
		
		
		
		btn_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(inNotice){
					if(index>0){
						final long noticeID=noticeids[index-1];
						index=index-1;
						loadData(noticeID);	
						btn_right_before.setEnabled(true);
						btn_right.setEnabled(true);
					}else{
						btn_right_before.setEnabled(false);
						btn_right.setEnabled(true);
						ToastUtil.showShort(NoticeInfo.this, "已经是最新一条");
					}
				}else{
					if(index>0){
						final long noticeID=noticeids[index-1];
						index=index-1;
						loadSendNotice(noticeID);	
						btn_right_before.setEnabled(true);
						btn_right.setEnabled(true);
					}else{
						btn_right_before.setEnabled(false);
						btn_right.setEnabled(true);
						ToastUtil.showShort(NoticeInfo.this, "已经是最新一条");
					}
				}
				
			}
		});
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(inNotice){
					if(index<noticeids.length-1){
						final long noticeID=noticeids[index+1];
						index=index+1;
						loadData(noticeID);	
						btn_right.setEnabled(true);
						btn_right_before.setEnabled(true);
					}else{
						ToastUtil.showShort(NoticeInfo.this, "已经是最后一条");
						btn_right.setEnabled(false);
						btn_right_before.setEnabled(true);
					}
				}else{
					if(index<noticeids.length-1){
						final long noticeID=noticeids[index+1];
						index=index+1;
						loadSendNotice(noticeID);	
						btn_right.setEnabled(true);
						btn_right_before.setEnabled(true);
					}else{
						ToastUtil.showShort(NoticeInfo.this, "已经是最后一条");
						btn_right.setEnabled(false);
						btn_right_before.setEnabled(true);
					}
				}
				
			}
		});
	}
	private String getMIMEType(File file) { 
	     
	    String type="*/*"; 
	    String fName = file.getName(); 
	    int dotIndex = fName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase(); 
	    if(end=="")return type; 
	    for(int i=0;i<Constant.MIME_MapTable.length;i++){
	        if(end.equals(Constant.MIME_MapTable[i][0])) 
	            type = Constant.MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 
	private void loadSendNotice(long noticeID){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.NOTICE +" where id=?", new String[]{""+noticeID});
			notice_title.setText(c.getString(c.getColumnIndex("title")));	
			String beginTime=DateUtil.strToDatestr(c.getString(c.getColumnIndex("begin")));
			
			String endTime;
			if(c.getString(c.getColumnIndex("end")).equals("")){
				endTime="今";
			}else{
				endTime=DateUtil.strToDatestr(c.getString(c.getColumnIndex("end")));			
			}

			notice_time.setText("有效期:"+beginTime+"至"+endTime);
			
			notice_content.setText(c.getString(c.getColumnIndex("content")));
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	private void loadData(long noticeID){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("加载中...");
			getNotice=new GetNotice();
			
			getNotice.setUser(cEappApp.getUser());
			
			getNotice.setPass(cEappApp.getPass());
			
			getNotice.setNotice(noticeID);
			
			NetUtil.post(Constant.BASE_URL, getNotice, handler, MessageWhat.GET_NOTICE_RESP);
		}else{
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
			case MessageWhat.GET_NOTICE_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					Log.v("noticeInfo", "========"+(String)msg.obj);
					getNoticeResp=JsonUtil.fromJson((String)msg.obj,GetNoticeResp.class);
					if(null==getNoticeResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getNoticeResp.getStatus()){
						
						notice=getNoticeResp.getNotice();
						
						notice_title.setText(notice.getTitle());
						String beginTime=DateUtil.strToDatestr(notice.getBegin().toString());
						String endTime=DateUtil.strToDatestr(notice.getEnd().toString());
						if(endTime==null){
							notice_time.setText("有效期:"+beginTime+"至"+"今");
						}else{
							notice_time.setText("有效期:"+beginTime+"至"+endTime);
						}
						notice_content.setText("\u3000\u3000"+notice.getContent());
						
						if(Integer.valueOf(notice.getUrl())==-1){
							download.setVisibility(View.GONE);
						}else{
							download.setVisibility(View.VISIBLE);
							DownLoad downLoad=new DownLoad();
							downLoad.setId(Integer.valueOf(notice.getUrl()));
							downLoad.setUser(cEappApp.getUser());
							downLoad.setPass(cEappApp.getPass());
							String extension=NetUtil.getExtension(Constant.BASE_URL, downLoad.wrap());
							if(extension==null){
								break;
							}else{
								filename=Constant.PATH+notice.getUrl()+"."+extension;
								if(fileutil.isFileExist(filename)){
									download.setText("查看附件");
								}else{
									download.setText("下载附件");
								}
							}
						}

					}
					msg.obj=null;
				}
				break;
			case MessageWhat.DOWN_UPDATE:
				progressbar.setProgress(cEappApp.getProgress());
				
				break;
			case MessageWhat.DOWN_OVER:
				downdialog.dismiss();
				download.setText("查看附件");
				ToastUtil.showShort(NoticeInfo.this, "已下载完成！");
				break;
			case MessageWhat.ISEXISTS:
				downdialog.dismiss();
				download.setText("已下载");
				break;
			case MessageWhat.DOWNFIELD:
				downdialog.dismiss();
				ToastUtil.showShort(NoticeInfo.this, "下载失败！");
				break;
		}
		return false;
	}
	
	
}
