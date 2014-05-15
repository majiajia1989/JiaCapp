package com.e1858.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.BaseActivity;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.RoleType;
import com.e1858.network.NetUtil;
import com.e1858.protocol.ClassBase;
import com.e1858.protocol.TeaBase;
import com.e1858.protocol.http.GetClassList;
import com.e1858.protocol.http.GetClassListResp;
import com.e1858.protocol.http.GetTeacherList;
import com.e1858.protocol.http.GetTeacherListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.widget.FocusCheckBox;

public class AddSendMsgUsers extends BaseActivity {

	private EditText 			search_edit;
	private ListView 			content_list;
	
	private long[]   			ids;
	private long     			depID;
	private int      			roleType;
	private boolean  			allSelected;
	
	private GetTeacherList   	getTeacherList;
	private GetTeacherListResp  getTeacherListResp;
	
	private GetClassList        getClassList;
	private GetClassListResp    getClassListResp;
	
	private ListViewSelectContactAdapter adapter;
	private ListViewTSelectContactAdapter adaptert;
	
	private List<ClassBase> class_list=new ArrayList<ClassBase>();
	private List<TeaBase>   tea_list=new ArrayList<TeaBase>();
	private List<ClassBase> newclass_list=new ArrayList<ClassBase>();
	private List<TeaBase>   newtea_list=new ArrayList<TeaBase>();
	private ClassBase       class_entity;
	private TeaBase         tea_entity;
	
	private long[]       	 newids;
	private String[]    	 names;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);
		Intent intent=getIntent();
		ids=intent.getLongArrayExtra("ids");
		depID=intent.getLongExtra("depID", -1);
		roleType=intent.getIntExtra("roleType", -1);
		allSelected=intent.getBooleanExtra("allSelected", false);
		initView();
		if(roleType==RoleType.STUDENT){
			loadData();
		}else{
			loadTData();
		}
	}

	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("选择收件人");
		btn_back=(Button)findViewById(R.id.btn_back);
//		btn_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.));
		btn_back.setVisibility(View.VISIBLE);
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText("完成");
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data=new Intent();
				if(roleType==RoleType.STUDENT){
					StringBuilder sbid=new StringBuilder();
					StringBuilder sbname=new StringBuilder();
					for(int i=0;i<class_list.size();i++){
						if(class_list.get(i).isSelected()){
							sbid.append(class_list.get(i).getId()).append(",");
							sbname.append(class_list.get(i).getName()+"(学生)").append(",");
						}
					}
					if(sbname.length()==0){
						sbid.append(",");
						sbname.append(",");
					}
					sbid.deleteCharAt(sbid.length()-1);
					sbname.deleteCharAt(sbname.length()-1);
					
					String news[]=sbid.toString().split(",");
					newids=new long[news.length];
					for(int i=0;i<news.length;i++){
						if(news[i].equals("")){
							break;
						}else{
							newids[i]=Long.parseLong(news[i]);
						}
						
					}	
					names=sbname.toString().split(",");
					
				}else{
					
					StringBuilder sbid=new StringBuilder();
					StringBuilder sbname=new StringBuilder();
					for(int i=0;i<tea_list.size();i++){
						if(tea_list.get(i).isSelected()){
							sbid.append(tea_list.get(i).getId()).append(",");
							sbname.append(tea_list.get(i).getName()+"(教师)").append(",");
						}
					}
					if(sbname.length()==0){
						sbid.append(",");
						sbname.append(",");
					}
					
					sbid.deleteCharAt(sbid.length()-1);
					sbname.deleteCharAt(sbname.length()-1);				
					String news[]=sbid.toString().split(",");
					newids=new long[news.length];
					for(int i=0;i<news.length;i++){
						if(news[i].equals("")){
							break;
						}else{
							newids[i]=Long.parseLong(news[i]);
						}
					}	
					names=sbname.toString().split(",");
				}
				
				data.putExtra("ids", newids);
				data.putExtra("names", names);
				data.putExtra("depID", depID);
				
				setResult(20,data);
				
				finish();
				

			}
		});
			search_edit=(EditText)findViewById(R.id.search_edit);
		
			search_edit.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(roleType==RoleType.STUDENT){
					newclass_list.clear();
					if (search_edit.getText() != null) {
						String input_info = search_edit.getText().toString();
						newclass_list = getNewData(input_info);
						adapter = new ListViewSelectContactAdapter(AddSendMsgUsers.this, newclass_list, R.layout.select_contact_item);
						content_list.setAdapter(adapter);
					}
				}else{
					newtea_list.clear();
					if (search_edit.getText() != null) {
						String input_info = search_edit.getText().toString();
						newtea_list = getNewTData(input_info);
						adaptert = new ListViewTSelectContactAdapter(AddSendMsgUsers.this, newtea_list, R.layout.select_contact_item);
						content_list.setAdapter(adaptert);
					}
				
				}
			}	
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		content_list=(ListView)findViewById(R.id.content_list);

		content_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				return;
			}
		});
	}
	private List<ClassBase> getNewData(String input_info){
		//遍历list
		for (int i = 0; i < class_list.size(); i++) {
			ClassBase entity = class_list.get(i);
			//如果遍历到的名字包含所输入字符串
			if (entity.getName().contains(input_info)) {
				//将遍历到的元素重新组成一个list
				ClassBase newData = new ClassBase();
				newData.setName(entity.getName());
				newData.setId(entity.getId());
				newData.setSelected(entity.isSelected());
				newclass_list.add(newData);
			}
		}
		return newclass_list;
	}
	
	private List<TeaBase> getNewTData(String input_info){
		//遍历list
		for (int i = 0; i < tea_list.size(); i++) {
			TeaBase entity = tea_list.get(i);
			//如果遍历到的名字包含所输入字符串
			if (entity.getName().contains(input_info)) {
				//将遍历到的元素重新组成一个list
				TeaBase newData = new TeaBase();
				newData.setName(entity.getName());
				newData.setId(entity.getId());
				newData.setSelected(entity.isSelected());
				newtea_list.add(newData);
			}
		}
		return newtea_list;
	}
	
	
	private void loadData(){
		
		openProgressDialog("加载中...");
		getClassList=new GetClassList();
		getClassList.setUser(cEappApp.getUser());
		getClassList.setPass(cEappApp.getPass());
		getClassList.setDep(depID);

		NetUtil.post(Constant.BASE_URL, getClassList, handler, MessageWhat.GET_CLASS_LIST_RESP);
	
		
	}
	private void loadTData(){
		
		openProgressDialog("加载中...");
		
		getTeacherList=new GetTeacherList();
		
		getTeacherList.setUser(cEappApp.getUser());
		
		getTeacherList.setPass(cEappApp.getPass());
		
		getTeacherList.setDep(depID);
	
		NetUtil.post(Constant.BASE_URL, getTeacherList, handler, MessageWhat.GET_TEACHER_LIST_RESP);	
	
		
	}
	

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
			case MessageWhat.GET_CLASS_LIST_RESP:
				if((String)msg.obj!=null){
					closeProgressDialog();
					getClassListResp=JsonUtil.fromJson((String)msg.obj, GetClassListResp.class);
					if(null==getClassListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getClassListResp.getStatus()){
						class_list=getClassListResp.getClasses();
						if(class_list==null||class_list.size()==0){
							break;
						}
						adapter=new ListViewSelectContactAdapter(this, class_list, R.layout.select_contact_item);
						content_list.setAdapter(adapter);
					}
					msg.obj=null;
				}
				break;
			case MessageWhat.GET_TEACHER_LIST_RESP:
				if((String)msg.obj!=null){
					closeProgressDialog();
					getTeacherListResp=JsonUtil.fromJson((String)msg.obj, GetTeacherListResp.class);
					if(null==getTeacherListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getTeacherListResp.getStatus()){
						tea_list=getTeacherListResp.getTeachers();
						if(tea_list==null||tea_list.size()==0){
							break;
						}
						adaptert=new ListViewTSelectContactAdapter(this, tea_list, R.layout.select_contact_item);
						content_list.setAdapter(adaptert);
					}
				}
				break;
		}	
		return false;
	}
	
	class ListViewSelectContactAdapter extends BaseAdapter{

		private Context context;
		private List<ClassBase> list;
		private int itemResource;
		private LayoutInflater layInflater;
		public ListViewSelectContactAdapter(Context context,List<ClassBase> list,int itemResource){
			this.context=context;
			this.list=list;
			this.itemResource=itemResource;
			this.layInflater=LayoutInflater.from(context);
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
			ViewHolder vh=null;
			if(convertView==null){
				vh=new ViewHolder();
				convertView=layInflater.inflate(itemResource, null);
				vh.title=(TextView)convertView.findViewById(R.id.title);
				vh.selected=(FocusCheckBox)convertView.findViewById(R.id.selected);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder)convertView.getTag();
			}
			vh.title.setText(list.get(position).getName());
			if(list.get(position).isSelected()){
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
			}else{
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_gray));
			}	
			if(ids!=null){
				for(int i=0;i<ids.length;i++){
					if(ids[i]==list.get(position).getId()){
						vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
					}
				}
			}
			vh.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						class_list.get(position).setSelected(true);
						adapter.notifyDataSetChanged();
					}else{
						class_list.get(position).setSelected(false);
						adapter.notifyDataSetChanged();
					}
				}
			});
			
			
			return convertView;
		}
		
	}
  public static class ViewHolder{
		TextView title;
		FocusCheckBox selected;
	}
	
	class ListViewTSelectContactAdapter extends BaseAdapter{

		private Context context;
		private List<TeaBase> list;
		private int itemResource;
		private LayoutInflater layInflater;
		public ListViewTSelectContactAdapter(Context context,List<TeaBase> list,int itemResource){
			this.context=context;
			this.list=list;
			this.itemResource=itemResource;
			this.layInflater=LayoutInflater.from(context);
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
			ViewHolder vh=null;
			if(convertView==null){
				vh=new ViewHolder();
				convertView=layInflater.inflate(itemResource, null);
				vh.title=(TextView)convertView.findViewById(R.id.title);
				vh.selected=(FocusCheckBox)convertView.findViewById(R.id.selected);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder)convertView.getTag();
			}
			
			vh.title.setText(list.get(position).getName());
			if(list.get(position).isSelected()){
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
			}else{
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_gray));
			}	
			
			if(ids!=null){
				for(int i=0;i<ids.length;i++){
					if(ids[i]==list.get(position).getId()){
						vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
					}
				}
			}
			vh.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
					if(isChecked){
						tea_list.get(position).setSelected(true);
						adaptert.notifyDataSetChanged();
					}else{
						tea_list.get(position).setSelected(false);
						adaptert.notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}
		
	}

	
}
