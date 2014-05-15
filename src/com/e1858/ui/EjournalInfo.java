package com.e1858.ui;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.ContentType;
import com.e1858.common.DownloadType;
import com.e1858.common.MessageWhat;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Ejournal;
import com.e1858.protocol.MMSAttach;
import com.e1858.protocol.http.DownLoad;
import com.e1858.protocol.http.GetEjournal;
import com.e1858.protocol.http.GetEjournalResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.AsyncImageLoader;
import com.e1858.utils.FileUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;

public class EjournalInfo extends BaseActivity {

	private long   				ejournalID;
	private long[] 				ejournalids;
	private int   				index;
	
	private List<MMSAttach>		attachs=new ArrayList<MMSAttach>();
	private MMSAttach       	entity;
	private Ejournal        	ejournal;
	
	private GetEjournal 		getEjournal;
	private GetEjournalResp 	getEjournalResp;
	
//	private TextView        	e_title;
	private LinearLayout    	content;
	
	private AsyncImageLoader  	asyncImageLoader;
	private FileUtil            fileUtil;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ejournal_info);
		Intent intent=getIntent();
		ejournalID=intent.getLongExtra("ejournalID", -1);
		ejournalids=intent.getLongArrayExtra("ejournalids");
		index=intent.getIntExtra("index", 0);
		fileUtil=new FileUtil();
		asyncImageLoader=new AsyncImageLoader(cEappApp,getResources().getDrawable(R.drawable.book));
		initView();
		loadData(ejournalID);
	}
	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("校刊详细");
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_right));
		btn_right.setVisibility(View.VISIBLE);
		
		btn_right_before=(Button)findViewById(R.id.btn_right_before);
		btn_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_left));
		btn_right_before.setVisibility(View.VISIBLE);
		
		if(index==0){
			btn_right_before.setEnabled(false);
		}else if(index==ejournalids.length-1){
			btn_right.setEnabled(false);
		}else{
			btn_right.setEnabled(true);
			btn_right_before.setEnabled(true);
		}
		
		
		
//		e_title=(TextView)findViewById(R.id.e_title);
		content=(LinearLayout)findViewById(R.id.content);
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index<ejournalids.length-1){
					final long ejournalID=ejournalids[index+1];
					index=index+1;
					loadData(ejournalID);
					btn_right.setEnabled(true);
					btn_right_before.setEnabled(true);
				}else{
					btn_right.setEnabled(false);
					btn_right_before.setEnabled(true);
					ToastUtil.showShort(EjournalInfo.this, "已经是最后一条");
				}
			}
		});
		btn_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index>0){
					final long ejournalID=ejournalids[index-1];
					index=index-1;
					loadData(ejournalID);	
					btn_right_before.setEnabled(true);
					btn_right.setEnabled(true);
				}else{
					ToastUtil.showShort(EjournalInfo.this, "已经是最新一条");
					btn_right_before.setEnabled(false);
					btn_right.setEnabled(true);
				}
			}
		});
	}
	
	private void loadData(long ejournalID){
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("加载中...");
			getEjournal=new GetEjournal();
			getEjournal.setUser(cEappApp.getUser());
			getEjournal.setPass(cEappApp.getPass());
			getEjournal.setEjournal(ejournalID);
			NetUtil.post(Constant.BASE_URL, getEjournal, handler, MessageWhat.GET_EJOURNAL_RESP);
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
		case MessageWhat.GET_EJOURNAL_RESP:
			if(null!=(String)msg.obj){
				closeProgressDialog();
				Log.v("ejournalinfo", (String)msg.obj);
				getEjournalResp=JsonUtil.fromJson((String)msg.obj, GetEjournalResp.class);
			
				if(null==getEjournalResp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==getEjournalResp.getStatus()){
					ejournal=getEjournalResp.getEjournal();
					setContent(ejournal);
				}
				msg.obj=null;
			}
			break;
		}
		return false;
	}
	
	//设置内容
	private void setContent(Ejournal ejournal){
		
		title.setText(ejournal.getTitle());
//		e_title.setText(ejournal.getTitle());
		
		attachs=ejournal.getAttachs();
		
		content.removeAllViews();
		
		//根据附件内容往content中添加内容
//		Collections.sort(attachs, new MyComparator());	
		for(int i=0;i<attachs.size();i++){
			entity=attachs.get(i);
			//根据id把文件下载下来读取展示出来
			if(entity.getType()==ContentType.TEXT){
				TextView text=new TextView(this);
				LinearLayout.LayoutParams p=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
				text.setLayoutParams(p);
				text.setTextSize(getResources().getDimension(R.dimen.text_size_10));
				/**
				DownLoad download=new DownLoad();
				download.setId(entity.getId());
				download.setUser(cEappApp.getUser());
				download.setPass(cEappApp.getPass());			
				byte[] b=NetUtil.download(Constant.BASE_URL, download.wrap());	
				String s="";
				try{
					 s=new String(b,Constant.ENCODING);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				text.setText("\u3000\u3000"+s);	
				*/
				asyncImageLoader.getText(text, entity.getId());
				
				content.addView(text);
			}else if(entity.getType()==ContentType.PICTURE){
				ImageView image=new ImageView(this);
				LinearLayout.LayoutParams p=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				image.setLayoutParams(p);
				image.setScaleType(ScaleType.CENTER_CROP);
				asyncImageLoader.loadDrawable(image, DownloadType.LIB_PICTURE, entity.getId(), true, false, true);
				content.addView(image);
			}
		}
		
	}
	
	
	 class MyComparator implements Comparator<MMSAttach>
     {
         //这里的o1和o2就是list里任意的两个对象，然后按需求把这个方法填完整就行了
         public int compare(MMSAttach o1, MMSAttach o2)
         {
        	int object1=o1.getSort();
        	int objece2=o1.getSort();
        	return object1 > objece2 ? 1: -1; 	
         }
     }
	
}
