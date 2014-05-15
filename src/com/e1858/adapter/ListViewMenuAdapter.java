package com.e1858.adapter;
import java.util.List;

import com.e1858.BaseMenuActivity;
import com.e1858.CEappApp;
import com.e1858.ceapp.R;
import com.e1858.common.UIHelper;
import com.e1858.protocol.Module;
import com.e1858.utils.AsyncImageLoader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class ListViewMenuAdapter extends BaseAdapter{

	private BaseMenuActivity context;
	private LayoutInflater layInflater;
	private List<Module> list;
	private int itemViewResource;
	private AsyncImageLoader asyncImageLoader;
	private CEappApp cEappApp;

	
	static  class ListItemView{
		 TextView  menu_name;
//		 ImageView menu_icon;
	}
	
	public ListViewMenuAdapter(BaseMenuActivity context,List<Module> list,int resource){
		this.context=context;
		this.list=list;
		this.cEappApp=(CEappApp)context.getApplication();
		this.itemViewResource=resource;
		this.layInflater=LayoutInflater.from(context);
		this.asyncImageLoader=new AsyncImageLoader(cEappApp,context.getResources().getDrawable(R.drawable.icon));
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView=null;
		if(convertView==null){
			convertView=layInflater.inflate(itemViewResource, null);
			listItemView=new ListItemView();
//			listItemView.menu_icon=(ImageView)convertView.findViewById(R.id.menu_icon);
			listItemView.menu_name=(TextView)convertView.findViewById(R.id.menu_name);
			convertView.setTag(listItemView);
		}else{
			listItemView=(ListItemView)convertView.getTag();
		}
		listItemView.menu_name.setText(list.get(position).getName());	
//		asyncImageLoader.loadDrawable1(listItemView.menu_icon, DownloadType.APK_FILE, list.get(position).getIcon());
		final int moduleid=(int)list.get(position).getId();
		
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				switch(moduleid){		
				case 100:
					intent.setClassName(context, UIHelper.doIntent(100));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 0:				
					intent.setClassName(context, UIHelper.doIntent(0));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 1:
					intent.setClassName(context, UIHelper.doIntent(1));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
					
				case 2:
					intent.setClassName(context, UIHelper.doIntent(2));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 3:
					intent.setClassName(context, UIHelper.doIntent(3));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 4:
					intent.setClassName(context, UIHelper.doIntent(4));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 5:
					intent.setClassName(context, UIHelper.doIntent(5));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 6:
					intent.setClassName(context, UIHelper.doIntent(6));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 7:
					intent.setClassName(context, UIHelper.doIntent(7));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				case 8:
					intent.setClassName(context, UIHelper.doIntent(8));
					context.startActivity(intent);
					context.finish();
					context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					break;
				}
			}
		});
	
		return convertView;
	}
	
	public  void setSelectItem(int selectItem) {  
             this.selectItem = selectItem;  
	}  
    private int  selectItem=-1;

}
