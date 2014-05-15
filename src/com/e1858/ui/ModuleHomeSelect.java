package com.e1858.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.database.TableName;
import com.e1858.protocol.Module;

public class ModuleHomeSelect extends BaseActivity {
	private List<Module> 			modules=new ArrayList<Module>();
	private Module       			module;
	
	private ListView        		module_select_list;
	private ModuleAapter            adapter;
	private Button                  button_back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.module_setting);
		initView();
		loadData();
	}

	private void initView(){	
		title=(TextView)findViewById(R.id.title);
		title.setText("设置首页");
		module_select_list=(ListView)findViewById(R.id.module_select_list);
		
		button_back=(Button)findViewById(R.id.btn_back);
		button_back.setVisibility(View.VISIBLE);

		
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data=new Intent();
				try{
					Cursor c=cEappApp.dbManager.queryData2Cursor("select id,name from "+TableName.MODULE +" where home!=-1", null);
					cEappApp.setHomeID(c.getInt(c.getColumnIndex("id")));
					cEappApp.setHomeName(c.getString(c.getColumnIndex("name")));	
					c.close();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				data.putExtra("homename", cEappApp.getHomeName());
				setResult(10,data);
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
				
			}
		});
		
	}
	private void loadData(){
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.MODULE+ " where isselect=1 and id!=100", null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				module=new Module();
				module.setId(c.getInt(c.getColumnIndex("id")));
				module.setName(c.getString(c.getColumnIndex("name")));
				module.setIcon(c.getInt(c.getColumnIndex("icon")));
				module.setHome(c.getInt(c.getColumnIndex("home")));
				module.setSelect(c.getInt(c.getColumnIndex("isselect")));
				modules.add(module);
			}
			adapter=new ModuleAapter(this, modules, R.layout.module_select_item);
			module_select_list.setAdapter(adapter);
			
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	class ModuleAapter extends BaseAdapter{
		
		private Context 		context;
		private List<Module> 	list;
		private int          	itemResource;
		private LayoutInflater 	layoutInflater;
		private long            seleted=-1;
		public ModuleAapter(Context context,List<Module> list,int itemResource){
			this.context=context;
			this.list=list;
			this.itemResource=itemResource;
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
		

		public long getSeleted() {
			return seleted;
		}
		public void setSeleted(long seleted) {
			this.seleted = seleted;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh=null;
			if(convertView==null){
				vh=new ViewHolder();
				convertView=layoutInflater.inflate(itemResource, null);
				vh.module_name=(TextView)convertView.findViewById(R.id.module_title);
				vh.module_select=(CheckBox)convertView.findViewById(R.id.module_select);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder)convertView.getTag();
			}
			vh.module_name.setText(list.get(position).getName());
			if(list.get(position).getHome()==list.get(position).getId()){
				vh.module_select.setVisibility(View.VISIBLE);
				vh.module_select.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
			}else{
//				vh.module_select.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_gray));
				vh.module_select.setVisibility(View.GONE);
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setSeleted(list.get(position).getId());
					modules.get(position).setHome(list.get(position).getId());
					for(int i=0;i<modules.size();i++){
						if(seleted!=modules.get(i).getId()){
							modules.get(i).setHome(-1);
						}
					}
					try{
						cEappApp.dbManager.updateDataBySql("update  "+TableName.MODULE +" set home="+list.get(position).getId()+" where id=?", new String[]{""+list.get(position).getId()});
						cEappApp.dbManager.updateDataBySql("update  "+TableName.MODULE +" set home=-1 where id!=?", new String[]{""+list.get(position).getId()});
					}catch(Exception e){
						e.printStackTrace();
					}
					adapter.notifyDataSetChanged();
				
				}
			});
		/**
			vh.module_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						setSeleted(list.get(position).getId());
						modules.get(position).setHome(list.get(position).getId());
						for(int i=0;i<modules.size();i++){
							if(seleted!=modules.get(i).getId()){
								modules.get(i).setHome(-1);
							}
						}
						try{
							cEappApp.dbManager.updateDataBySql("update  "+TableName.MODULE +" set home="+list.get(position).getId()+" where id=?", new String[]{""+list.get(position).getId()});
							cEappApp.dbManager.updateDataBySql("update  "+TableName.MODULE +" set home=-1 where id!=?", new String[]{""+list.get(position).getId()});
						}catch(Exception e){
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
					}
				}
			});
			*/
			return convertView;
		}
		
	}
	static class ViewHolder{
		public TextView module_name;
		public CheckBox module_select;
	}

}
