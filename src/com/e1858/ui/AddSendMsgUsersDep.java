package com.e1858.ui;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.e1858.protocol.DepBase;
import com.e1858.protocol.http.GetDepartmentList;
import com.e1858.protocol.http.GetDepartmentListResp;
import com.e1858.protocol.http.HttpDefine;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.FocusCheckBox;

public class AddSendMsgUsersDep extends BaseActivity {

	private ListView 					contentlist;
	
	private EditText 					search_edit;
	private Button                      button_back;
	
	private int      					roleType;

	private GetDepartmentList 			getDepartmentList;
	
	private GetDepartmentListResp 		getDepartmentListResp;
	
	
	private List<DepBase>           	 deps=new ArrayList<DepBase>();
	private List<DepBase>           	 final_deps=new ArrayList<DepBase>();
	private List<DepBase>           	 newList=new ArrayList<DepBase>();
	private DepBase                      dep_entity;
	private ListViewSelectContactAdapter adapter;
	
	
	private long[]                       ids=null;
	private String[]				     names=null;
	private long                         depID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);
		Intent intent=getIntent();
		roleType=intent.getIntExtra("roleType", -1);
		initView();
		loadData();
	}

	private void initView(){
		title=(TextView)findViewById(R.id.title);
		title.setText("选择收件人");
		button_back=(Button)findViewById(R.id.btn_back);
//		btn_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_abolish_bg));
		button_back.setVisibility(View.VISIBLE);
		button_back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddSendMsgUsersDep.this,AddSendMsgUsersRole.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
				
			}
		});
		
		btn_right=(Button)findViewById(R.id.btn_right);
		btn_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_darkblue));
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText("完成");
		
		btn_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				for(int i=0;i<deps.size();i++){
					if(deps.get(i).isSelected()||deps.get(i).getUsers()!=""){
						dep_entity=deps.get(i);
						final_deps.add(dep_entity);
					}
				}				
				if(roleType==RoleType.STUDENT){
					
					cEappApp.setClasses(final_deps);
					
				}else{
					cEappApp.setTeas(final_deps);
				}
				Intent intent=new Intent(AddSendMsgUsersDep.this,AddNewMessage.class);
				startActivity(intent);
				finish();
				
				
			}
		});
		search_edit=(EditText)findViewById(R.id.search_edit);

		search_edit.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				newList.clear();
				if (search_edit.getText() != null) {
					String input_info = search_edit.getText().toString();
					newList = getNewData(input_info);
					adapter = new ListViewSelectContactAdapter(AddSendMsgUsersDep.this, newList, R.layout.select_contact_item);
					contentlist.setAdapter(adapter);
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
		
	
		
		contentlist=(ListView)findViewById(R.id.content_list);
		contentlist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, final int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddSendMsgUsersDep.this,AddSendMsgUsers.class);
				intent.putExtra("roleType", roleType);				
			    intent.putExtra("depID", deps.get(position).getId());  
			    
			    if(roleType==RoleType.STUDENT){
			    	 if(deps.get(position).isSelected()){
//				    	 intent.putExtra("allSelected",true);   
//				    	 intent.putExtra("ids", StringUtils.convertDataType(deps.get(position).getClass_ids()));
			    		 return;
			    	 }else{
				    	intent.putExtra("allSelected",false);   
				    	intent.putExtra("ids", StringUtils.convertDataType(deps.get(position).getClass_ids()));
				    }
			    }else{
			    	 if(deps.get(position).isSelected()){
//				    	 intent.putExtra("allSelected",true);   
//				    	 intent.putExtra("ids",StringUtils.convertDataType(deps.get(position).getTea_ids()));
			    		 return;
			    	 }else{
				    	intent.putExtra("allSelected",false);   
				    	intent.putExtra("ids",StringUtils.convertDataType(deps.get(position).getTea_ids()));
				    }
			    }
			   
				startActivityForResult(intent, 20);
				
			}
		});		
	}
	
	private List<DepBase> getNewData(String input_info){
		//遍历list
		for (int i = 0; i < deps.size(); i++) {
			DepBase entity = deps.get(i);
			//如果遍历到的名字包含所输入字符串
			if (entity.getName().contains(input_info)) {
				//将遍历到的元素重新组成一个list
				DepBase newData = new DepBase();
				newData.setName(entity.getName());
				newData.setId(entity.getId());
				newData.setTea_ids(entity.getTea_ids());
				newData.setUsers(entity.getUsers());
				newData.setClass_ids(entity.getClass_ids());
				newData.setSelected(entity.isSelected());
				newList.add(newData);
			}
		}
		return newList;
	}
	
	private void loadData(){
		
		if(NetUtil.checkNetWorkStatus(this)){
			openProgressDialog("加载中...");
			getDepartmentList=new GetDepartmentList();
			getDepartmentList.setUser(cEappApp.getUser());
			getDepartmentList.setPass(cEappApp.getPass());
			NetUtil.post(Constant.BASE_URL, getDepartmentList, handler, MessageWhat.GET_DEPARTMENT_LIST_RESP);
		
			
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
			case MessageWhat.GET_DEPARTMENT_LIST_RESP:
				if(null!=(String)msg.obj){
					closeProgressDialog();
					getDepartmentListResp=JsonUtil.fromJson((String)msg.obj, GetDepartmentListResp.class);
					if(null==getDepartmentListResp){
						break;
					}
					if(HttpDefine.RESPONSE_SUCCESS==getDepartmentListResp.getStatus()){
						deps=getDepartmentListResp.getDeps();
						if(deps==null||deps.size()==0){
							break;
						}
						
						adapter=new ListViewSelectContactAdapter(this, deps, R.layout.select_contact_item);
						contentlist.setAdapter(adapter);			
					}
				}
				break;
		}
		
		return false;
	}
	
	class ListViewSelectContactAdapter extends BaseAdapter{

		private Context context;
		private List<DepBase> list;
		private int itemResource;
		private LayoutInflater layInflater;
		public ListViewSelectContactAdapter(Context context,List<DepBase> list,int itemResource){
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
				vh.users=(TextView)convertView.findViewById(R.id.users);
				vh.selected=(FocusCheckBox)convertView.findViewById(R.id.selected);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder)convertView.getTag();
			}
			convertView.requestFocus();
			vh.title.setText(list.get(position).getName());
			if(list.get(position).isSelected()){
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
			}else{
				vh.selected.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_gray));
			}
			vh.users.setText(list.get(position).getUsers());
			vh.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						deps.get(position).setSelected(true);
						adapter.notifyDataSetChanged();
					}else{
						deps.get(position).setSelected(false);
						adapter.notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}
		
	}
	
	
	
	
	public static class ViewHolder{
		TextView title;
		TextView users;
		FocusCheckBox selected;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if(data==null){
			return;
		}else{
			if(roleType==RoleType.STUDENT){
				ids=data.getLongArrayExtra("ids");
				names=data.getStringArrayExtra("names");
				depID=data.getLongExtra("depID", -1);
				StringBuilder sb=new StringBuilder();
				if(names==null){
					sb.append("");
				}else{
					for(int i=0;i<names.length;i++){
						sb.append(names[i]).append(",");
					}
					sb.deleteCharAt(sb.length()-1);
				}
				if(depID!=-1){
					for(int i=0;i<deps.size();i++){
						if(depID==deps.get(i).getId()){
							deps.get(i).setUsers(sb.toString());
							deps.get(i).setClass_ids(StringUtils.ConvertDataType(ids));
						
							
						}
					}	
				}
				adapter.notifyDataSetChanged();
			}else{
				ids=data.getLongArrayExtra("ids");
				names=data.getStringArrayExtra("names");
				depID=data.getLongExtra("depID", -1);
				StringBuilder sb=new StringBuilder();
				if(names==null){
					sb.append("");
				}else{
					for(int i=0;i<names.length;i++){
						sb.append(names[i]).append(",");
					}
					sb.deleteCharAt(sb.length()-1);
				}
				if(depID!=-1){
					for(int i=0;i<deps.size();i++){
						if(depID==deps.get(i).getId()){
							deps.get(i).setUsers(sb.toString());
							deps.get(i).setTea_ids(StringUtils.ConvertDataType(ids));		
						}
					}	
				}
				adapter.notifyDataSetChanged();
			}
		}	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
