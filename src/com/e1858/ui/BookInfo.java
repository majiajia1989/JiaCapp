package com.e1858.ui;
import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.DownloadType;
import com.e1858.common.MessageWhat;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Library;
import com.e1858.protocol.http.DownLoad;
import com.e1858.protocol.http.GetLibrary;
import com.e1858.protocol.http.GetLibraryResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.AsyncImageLoader;
import com.e1858.utils.FileUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.RoundProgressBar;

public class BookInfo extends BaseActivity {

	private int 				index;
	private long[] 				ids;
	private long 				contentID;
	
	private ImageView 			cover;
	private TextView  			name;
	private TextView  			author;
	private TextView  			publisher;
	private TextView  			content;
	private Button    			downLoad;
	
	private GetLibrary 			getLibrary;
	private GetLibraryResp 		getLibraryResp;
	
	private AsyncImageLoader 	asynImageLoader;
	private Library        		entity;
	private FileUtil       		fileutil;
	private RoundProgressBar 	progressbar;
	private Dialog     			downdialog; 
	private String              filename;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info);
		fileutil=new FileUtil();
		Intent intent=getIntent();		
		index=intent.getIntExtra("index", -1);
		ids=intent.getLongArrayExtra("ids");
		contentID=intent.getLongExtra("contentID", -1);
		asynImageLoader=new AsyncImageLoader(cEappApp, getResources().getDrawable(R.drawable.book_info));
		initView();
		loadData(contentID);
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("详细内容");
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_right));
		btn_right.setVisibility(View.VISIBLE);
		
		btn_right_before=(Button)findViewById(R.id.btn_right_before);
		btn_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_left));
		btn_right_before.setVisibility(View.VISIBLE);
		
		cover=(ImageView)findViewById(R.id.cover);
		
		name=(TextView)findViewById(R.id.name);
		
		author=(TextView)findViewById(R.id.author);
		
		publisher=(TextView)findViewById(R.id.publisher);
		
		content=(TextView)findViewById(R.id.content);
		
		
		downLoad=(Button)findViewById(R.id.download);
		if(index==0){
			btn_right_before.setEnabled(false);
		}else if(index==ids.length-1){
			btn_right.setEnabled(false);
		}else{
			btn_right.setEnabled(true);
			btn_right_before.setEnabled(true);
		}
		
		
		
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index<ids.length-1){
					final long contentID=ids[index+1];
					index++;
					loadData(contentID);
					
					btn_right.setEnabled(true);
					btn_right_before.setEnabled(true);
				}else{
					btn_right.setEnabled(false);
					btn_right_before.setEnabled(true);
					ToastUtil.showShort(BookInfo.this, "已经是最后一条");
				}
			}
		});
		btn_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index>0){
					final long contentID=ids[index-1];
					index=index-1;
					loadData(contentID);
					btn_right_before.setEnabled(true);
					btn_right.setEnabled(true);
				}else{
					btn_right_before.setEnabled(false);
					btn_right.setEnabled(true);
					ToastUtil.showShort(BookInfo.this, "已经是最新一条");
				}
			}
		});
		
		downLoad.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//下载
				
				
				
				if(downLoad.getText().equals("下载附件")){
					downdialog=new Dialog(BookInfo.this,android.R.style.Theme_Translucent_NoTitleBar);
					downdialog.setContentView(R.layout.down_dialog);
					progressbar=(RoundProgressBar)downdialog.findViewById(R.id.roundProgressBar);
					progressbar.setProgress(cEappApp.getProgress());
					downdialog.show();		
				
					DownLoad download=new DownLoad();
					download.setId(entity.getAttach());
					download.setUser(cEappApp.getUser());
					download.setPass(cEappApp.getPass());
					
					
					NetUtil.downFile(cEappApp,Constant.BASE_URL, download.wrap(),  Constant.PATH ,entity.getAttach());

					
				}else{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					File f = new File(FileUtil.SDPATH+filename);
					String type=getMIMEType(f);
					intent.setDataAndType(Uri.fromFile(f), type);//图片
					startActivity(intent);
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
	    for(int i=0;i<Constant.MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？ 
	        if(end.equals(Constant.MIME_MapTable[i][0])) 
	            type = Constant.MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 
	private void loadData(long contentID){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("加载中...");
			getLibrary=new GetLibrary();
			getLibrary.setUser(cEappApp.getUser());
			getLibrary.setPass(cEappApp.getPass());
			getLibrary.setLibrary(contentID);
			NetUtil.post(Constant.BASE_URL, getLibrary, handler, MessageWhat.GET_LIBRARY_RESP);
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
		switch (msg.what) {
		case MessageWhat.GET_LIBRARY_RESP:
			if(null!=(String)msg.obj){
				closeProgressDialog();
				getLibraryResp=JsonUtil.fromJson((String)msg.obj,GetLibraryResp.class);
				Log.v("getLibraryResp", (String)msg.obj);
				if(null==getLibraryResp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==getLibraryResp.getStatus()){
					entity=getLibraryResp.getLibrary();
					if(entity==null){
						ToastUtil.showShort(this, getLibraryResp.getErr());
						break;
					}
					if(entity.getPicture()!=-1){
						asynImageLoader.loadDrawable1(cover, DownloadType.LIB_PICTURE, entity.getPicture());
					}
					
					name.setText(entity.getTitle());
					if(entity.getAuthor().equals("")){
						author.setVisibility(View.GONE);
					}else{
						author.setText("作者:"+entity.getAuthor());
					}
					if(entity.getPublisher().endsWith("")){
						publisher.setVisibility(View.GONE);
					}else{
						publisher.setText("出版社:"+entity.getPublisher());
					}
					
					
					content.setText("\u3000\u3000"+entity.getSummary());
					
					if(entity.getAttach()==-1){
						downLoad.setVisibility(View.GONE);
					}else{
						DownLoad download=new DownLoad();
						download.setId(entity.getAttach());
						download.setUser(cEappApp.getUser());
						download.setPass(cEappApp.getPass());
						String extension=NetUtil.getExtension(Constant.BASE_URL, download.wrap());
						if(extension==null){
							break;
						}else{
							filename=Constant.PATH+entity.getAttach()+"."+extension;
							if(fileutil.isFileExist(filename)){
								downLoad.setText("查看附件");
							}else{
								downLoad.setText("下载附件");
							}
						}
						
					}	
					
				}else if(getLibraryResp.getStatus()==1){
					entity=getLibraryResp.getLibrary();
					if(entity==null){
						ToastUtil.showShort(this, getLibraryResp.getErr());
						break;
					}
				}
			}
			break;
		case MessageWhat.DOWN_UPDATE:
			progressbar.setProgress(cEappApp.getProgress());
			break;
		case MessageWhat.DOWN_OVER:
			downdialog.dismiss();
			downLoad.setText("查看附件");
			ToastUtil.showShort(BookInfo.this, "已下载完成！");
			break;
		case MessageWhat.ISEXISTS:
			downdialog.dismiss();
			downLoad.setText("查看附件");
			break;
		case MessageWhat.DOWNFIELD:
			downdialog.dismiss();
			ToastUtil.showShort(BookInfo.this, "下载失败！");
			break;

		}

		return false;
	}
	
}
