package com.e1858.adapter;

import java.util.List;

import com.e1858.CEappApp;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.DownloadType;
import com.e1858.protocol.LibBase;
import com.e1858.ui.NewsActivity;
import com.e1858.ui.NewsInfo;
import com.e1858.utils.AsyncImageLoader;
import com.e1858.utils.DateUtil;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewNewsAdapter extends BaseAdapter {

	private CEappApp cEappApp;
	private NewsActivity context;
	private List<LibBase> list;
	private int itemResource;
	private long[] ids;
	private LayoutInflater layoutInflater;
	private AsyncImageLoader asynImageLoader;
	private long groupID;
	private int srcMode;
	private String feed_name;
	public ListViewNewsAdapter(CEappApp cEappApp,NewsActivity context,List<LibBase> list,int itemResource,int srcMode,long groupID,String feed_name){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;
		this.ids=new long[list.size()];
		this.layoutInflater=LayoutInflater.from(context);
		this.cEappApp=cEappApp;
		this.srcMode=srcMode;
		this.feed_name=feed_name;
		this.groupID=groupID;
		this.asynImageLoader=new AsyncImageLoader(cEappApp,context.getResources().getDrawable(R.drawable.book));
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if(convertView==null){
			vh=new  ViewHolder();
			convertView=layoutInflater.inflate(itemResource, null);
			vh.cover=(ImageView)convertView.findViewById(R.id.news_pic);
			vh.title=(TextView)convertView.findViewById(R.id.news_title);
			vh.time=(TextView)convertView.findViewById(R.id.news_time);
			vh.content=(TextView)convertView.findViewById(R.id.news_content);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		final long contentID=list.get(position).getId();
		final int index=position;

		ids[position]=list.get(position).getId();
		if(list.get(position).getPicture()!=-1){
			vh.cover.setVisibility(View.VISIBLE);
			asynImageLoader.loadDrawable1(vh.cover, DownloadType.LIB_PICTURE, list.get(position).getPicture());
		}else{
			vh.cover.setVisibility(View.GONE);
		}
		vh.title.setText(list.get(position).getTitle());
		try{
			vh.time.setText(DateUtil.dateToZh(DateUtil.strToDatestrLong(list.get(position).getIssuedDate())));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(list.get(position).getContent()==null){
			vh.content.setVisibility(View.GONE);
		}else{
			vh.content.setVisibility(View.VISIBLE);
			vh.content.setText(Html.fromHtml(list.get(position).getContent()));
		}
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,NewsInfo.class);
				intent.putExtra("ids", ids);
				intent.putExtra("contentID", contentID);
				intent.putExtra("index", index);
				intent.putExtra("srcMode", srcMode);
				intent.putExtra("groupID", groupID);
				intent.putExtra("feed_name", feed_name);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		return convertView;
	}
	static class ViewHolder{
		ImageView cover;
		TextView  content;
		TextView  time;
		TextView  title;
	}

}
