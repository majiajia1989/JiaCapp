package com.e1858.adapter;

import java.util.List;
import com.e1858.ceapp.R;
import com.e1858.protocol.EjournalBase;
import com.e1858.ui.EjournalActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewEjournalAdapter extends BaseAdapter {

	private EjournalActivity context;
	private List<EjournalBase> list;
	private int itemResource;
	private LayoutInflater layiInflater;
	private long[]         ejournalids;
	public ListViewEjournalAdapter(EjournalActivity context,List<EjournalBase> list,int itemResource){
		this.context=context;
		this.list=list;
		this.itemResource=itemResource;	
		this.layiInflater=LayoutInflater.from(context);
		ejournalids=new long[list.size()];
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
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=layiInflater.inflate(itemResource, null);
			vh.ejournal_title=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
//		final long ejournalID=list.get(position).getId();
//		final int  index=position;
		
		
		
		vh.ejournal_title.setText(list.get(position).getTitle());
		/**
		ejournalids[position]=list.get(position).getId();
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,EjournalInfo.class);
				intent.putExtra("ejournalID", ejournalID);
				intent.putExtra("ejournalids", ejournalids);
				intent.putExtra("index", index);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		*/
		
		return convertView;
		
	}
	static class  ViewHolder{
		TextView ejournal_title;
	}

}
