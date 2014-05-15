package com.e1858.adapter;

import java.util.List;

import com.e1858.CEappApp;
import com.e1858.ceapp.R;
import com.e1858.common.DownloadType;
import com.e1858.protocol.LibBase;
import com.e1858.ui.BookActivity;
import com.e1858.ui.BookInfo;
import com.e1858.utils.AsyncImageLoader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridViewBookAdapter extends BaseAdapter {

//	private CEappApp cEappApp;
	private BookActivity context;
	private List<LibBase> list;
	private int itemResource;
	private long[] ids;
	private LayoutInflater layoutInflater;
	private AsyncImageLoader asynImageLoader;
	public GridViewBookAdapter(CEappApp cEappApp,BookActivity context,List<LibBase> list,int itemResource){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;
		this.ids=new long[list.size()];
		this.layoutInflater=LayoutInflater.from(context);
//		this.cEappApp=cEappApp;
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
			vh.cover=(ImageView)convertView.findViewById(R.id.cover);
			vh.title=(TextView)convertView.findViewById(R.id.title);
			vh.author=(TextView)convertView.findViewById(R.id.author);
			vh.bar=(ProgressBar)convertView.findViewById(R.id.refresh_progress);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		final long contentID=list.get(position).getId();
		final int index=position;
		if(list.get(position).isHasMore()){
			vh.title.setVisibility(View.GONE);
			vh.author.setVisibility(View.GONE);	
			vh.cover.setVisibility(View.GONE);
			vh.bar.setVisibility(View.VISIBLE);
		}else{
			ids[position]=list.get(position).getId();
			if(list.get(position).getPicture()!=-1){
				asynImageLoader.loadDrawable1(vh.cover, DownloadType.LIB_PICTURE, list.get(position).getPicture());
			}
	
			vh.title.setText("书名:"+list.get(position).getTitle());
			vh.author.setText("作者:"+list.get(position).getAuthor());
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,BookInfo.class);
				intent.putExtra("ids", ids);
				intent.putExtra("contentID", contentID);
				intent.putExtra("index", index);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		return convertView;
	}
	static class ViewHolder{
		ImageView cover;
		TextView  title;
		TextView  author;
		ProgressBar bar;
	}

}
