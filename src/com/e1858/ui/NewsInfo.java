package com.e1858.ui;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.DownloadType;
import com.e1858.common.MessageWhat;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Library;
import com.e1858.protocol.http.GetLibrary;
import com.e1858.protocol.http.GetLibraryResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.AsyncImageLoader;
import com.e1858.utils.DateUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.ToastUtil;

public class NewsInfo extends BaseActivity {

	private int 				index;
	private long[] 				ids;
	private long 				contentID;
	private int  				srcMode;
	private long 				groupID;
	private String              feed_name;
	
	private LinearLayout 	web_lay;
	private LinearLayout 	content_lay;
//	private WebView  		news_web;
	private TextView        newsw_title;
	private TextView        newsw_time;
	private TextView        newsw_content;
	private Button          newsw_detail;
	
	private TextView        news_title;
	private TextView        news_time;
	private LinearLayout    news_content;
	
	private GetLibrary      getLibrary;
	private GetLibraryResp  getLibraryResp;
	
	private Library         entity;
	private AsyncImageLoader asynImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_info);
		Intent intent=getIntent();		
		index=intent.getIntExtra("index", -1);
		ids=intent.getLongArrayExtra("ids");
		contentID=intent.getLongExtra("contentID", -1);
		srcMode=intent.getIntExtra("srcMode", -1);
		groupID=intent.getLongExtra("groupID", -1);
		feed_name=intent.getStringExtra("feed_name");
		
		initView();
		
		asynImageLoader=new AsyncImageLoader(cEappApp, getResources().getDrawable(R.drawable.nav_abolish_bg));
		
		if(srcMode==0){
			web_lay.setVisibility(View.GONE);
			content_lay.setVisibility(View.VISIBLE);
			loadData(contentID);
			
		}else{
			web_lay.setVisibility(View.VISIBLE);
			content_lay.setVisibility(View.GONE);
			loadWeb(contentID,groupID);
		}
		
	}

	private void initView(){
		title=(TextView)findViewById(R.id.title);
		
		title.setText(feed_name);
		
		btn_back=(Button)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_right));
		btn_right.setVisibility(View.VISIBLE);
		
		btn_right_before=(Button)findViewById(R.id.btn_right_before);
		btn_right_before.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_left));
		btn_right_before.setVisibility(View.VISIBLE);
		
		web_lay=(LinearLayout)findViewById(R.id.web_lay);
		content_lay=(LinearLayout)findViewById(R.id.content_lay);
//		news_web=(WebView)findViewById(R.id.news_web);
		newsw_title=(TextView)findViewById(R.id.newsw_title);
		newsw_time=(TextView)findViewById(R.id.newsw_time);
		newsw_content=(TextView)findViewById(R.id.newsw_content);
		newsw_detail=(Button)findViewById(R.id.newsw_detail);
		
		news_title=(TextView)findViewById(R.id.news_title);
		news_time=(TextView)findViewById(R.id.news_title);
		news_content=(LinearLayout)findViewById(R.id.news_content);
		
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
					if(srcMode==0){
						loadData(contentID);	
					}else if(srcMode==2){
						loadWeb(contentID,groupID);
					}
					btn_right.setEnabled(true);
					btn_right_before.setEnabled(true);
				}else{
					btn_right.setEnabled(false);
					btn_right_before.setEnabled(true);
					ToastUtil.showShort(NewsInfo.this, "已经是最后一条");
				}
			}
		});
		btn_right_before.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(index>0){
					final long contentID=ids[index-1];
					index=index-1;
					if(srcMode==0){
						loadData(contentID);	
					}else if(srcMode==2){
						loadWeb(contentID,groupID);
					}
					btn_right_before.setEnabled(true);
					btn_right.setEnabled(true);
				}else{
					btn_right_before.setEnabled(false);
					btn_right.setEnabled(true);
					ToastUtil.showShort(NewsInfo.this, "已经是最新一条");
				}
			}
		});
		
	}
	private void loadWeb(long contentID,long groupID){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+TableName.LIBRARY+TableName.LIBWEB + " where id=? and groupID=?", new String[]{""+contentID,""+groupID});
			final String url=c.getString(c.getColumnIndex("linkurl"));
			String title=c.getString(c.getColumnIndex("title"));
			String time=c.getString(c.getColumnIndex("issuedDate"));
			String content=c.getString(c.getColumnIndex("content"));
			newsw_title.setText(title);
			newsw_time.setText(DateUtil.dateToZh(DateUtil.strToDatestrLong(time)));
//			newsw_time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(time))));
			newsw_content.setText(Html.fromHtml(content));
			newsw_detail.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri =Uri.parse(url); 
					Intent intent = new Intent(Intent.ACTION_VIEW,uri); 
			     	startActivity(intent); 
				}
			});
			c.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	/**
	private void loadWeb(long contentID,long groupID){
		//设置支持JavaScript脚本
		WebSettings webSettings = news_web.getSettings(); 
		webSettings.setJavaScriptEnabled(true);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		String url = null;
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select linkurl from "+TableName.LIBRARY+TableName.LIBWEB + " where id=? and groupID=?", new String[]{""+contentID,""+groupID});
			url=c.getString(c.getColumnIndex("linkurl"));
			Log.v("url", "========"+url);
		}catch(Exception e){
			e.printStackTrace();
		}
		//得到rss源的url
		news_web.loadUrl(url);
		news_web.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
            { 
                    view.loadUrl(url); 
                    return true; 
            } 
		});
	}
	*/
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
		switch(msg.what){
			case MessageWhat.GET_LIBRARY_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					getLibraryResp=JsonUtil.fromJson((String)msg.obj, GetLibraryResp.class);
					if(null==getLibraryResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getLibraryResp.getStatus()){
						entity=getLibraryResp.getLibrary();
						
						news_title.setText(entity.getTitle());
						try{
							news_time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(entity.getIssuedDate()))));
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						if(entity.getPicture()!=-1){
							ImageView  image=new ImageView(this);
							LinearLayout.LayoutParams p=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
							p.gravity=Gravity.CENTER;
							image.setLayoutParams(p);
							asynImageLoader.loadDrawable1(image, DownloadType.LIB_PICTURE, entity.getPicture());
							news_content.addView(image);
						}
						
						TextView text=new TextView(this);
						LinearLayout.LayoutParams p=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
						p.gravity=Gravity.FILL;
						text.setLayoutParams(p);
						text.setTextSize(getResources().getDimension(R.dimen.text_size_16));
						text.setText("\u3000\u3000"+entity.getContent());
						news_content.addView(text);
						
					}
				}
				break;
		}
		return false;
	}
	
	
	
}
