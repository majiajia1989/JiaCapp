package com.e1858.adapter;

import java.util.List;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.protocol.LibBase;
import com.e1858.ui.ArticleActivity;
import com.e1858.ui.ArticleInfo;
import com.e1858.utils.DateUtil;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewArticleAdapter extends BaseAdapter {

	private ArticleActivity context;
	private List<LibBase> list;
	private int itemResource;
	private long[] ids;
	private LayoutInflater layoutInflater;
	public ListViewArticleAdapter(ArticleActivity context,List<LibBase> list,int itemResource){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;
		this.ids=new long[list.size()];
		this.layoutInflater=LayoutInflater.from(context);
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
			vh.title=(TextView)convertView.findViewById(R.id.article_title);
			vh.time=(TextView)convertView.findViewById(R.id.article_time);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		final long contentID=list.get(position).getId();
		final int index=position;
		ids[position]=list.get(position).getId();
		vh.title.setText(list.get(position).getTitle());
		try{
			vh.time.setText(DateUtil.getAccurateTime(Constant.SIMPLE_DATE_FORMAT.parse(DateUtil.strToDatestrLong(list.get(position).getIssuedDate()))));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ArticleInfo.class);
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
		TextView  title;
		TextView  time;

	}

}
