package com.e1858;
import java.util.ArrayList;
import java.util.List;

import com.e1858.adapter.ListViewMenuAdapter;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.common.RoleType;
import com.e1858.common.SelectorType;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Module;
import com.e1858.protocol.http.LoginOut;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BaseMenuActivity extends BaseActivity implements OnTouchListener{
	
	public 	Button            btn_top_left;	
	public 	Button            btn_top_right;
	public  Button            btn_top_right_before;
	public 	TextView          title_name;	
	public  TextView          menu_title;
	
	public 	static final int  SNAP_VELOCITY = 200;//滚动速度
	public 	int 			  screenWidth;
	public 	int 			  leftEdge;//menu最多可以滑动到的左边缘
	public 	int               rightEdge = 0;//menu最多可以滑动到的右边缘
	public 	int 			  menuPadding;//menu完全显示时，留给content的宽度值
	public  LinearLayout.LayoutParams menuParams;//menu布局的参数，通过此参数来更改leftMargin的值
	public  float             xDown;//按下时的横坐标
	public  float             xMove;//移动时的横坐标
	public  float             xUp;//抬起时的横坐标
	public  boolean           isMenuVisible;//menu当前是显示还是隐藏
	public  VelocityTracker   mVelocityTracker;//用于计算手指滑动的速度
	
	public  View              content;//主内容的布局
	public  View              menu;//menu的布局
	public  ListView          menulist;
	
	private List<Module> 		modules=new ArrayList<Module>();
	
	public  List<Module> 		localmodules=new ArrayList<Module>();
	private Module       		module_entity;
	private Module              person_module_entity;
	public boolean 				isFirstScroll=false;
	 public boolean 			isOpenPop = false; 
	 public PopupWindow 		window; 
	 public ListView 			group_list;
	 public ImageView 			arrow; 
	 public LinearLayout 		pop_menu;
	 
	 private ListViewMenuAdapter menu_adapter;
	 private Button              logout;
	 private LoginOut            loginOut;
	 public  SharedPreferences   sp;
	 private AlertDialog         exitdialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.base_menu);
		sp = getSharedPreferences(Constant.USER_INFO, 0);
		menuPadding=(int)getResources().getDimension(R.dimen.space_100);
		respBackButton=true;
		initValues();
		initBaseView();
		initData();
		content.setOnTouchListener(this);
		btn_top_left.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isFirstScroll){
					scrollToContent();
					isFirstScroll=false;
				}else{
					scrollToMenu();
					isFirstScroll=true;
				}		
			}
		});
		
	}
	public void initData(){
		
		modules=cEappApp.getModules();
		
		//先存入库，根据存入库设置
		/**
		StringBuilder module_sb=new StringBuilder();
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select id from "+TableName.MODULE, null);
			if(c.getCount()!=0){
				for(int i=0;i<c.getCount();i++){	
					c.moveToPosition(i);
					module_sb.append(c.getInt(c.getColumnIndex("id"))).append(",");	
				}
				module_sb.deleteCharAt(module_sb.length()-1);
				for(int j=0;j<modules.size();j++){
					if(!module_sb.toString().contains(""+modules.get(j).getId())){
						ContentValues values=new ContentValues();
						values.put("id", modules.get(j).getId());
						values.put("name", modules.get(j).getName());
						values.put("icon", modules.get(j).getIcon());
						values.put("home", ModuleInteger.MESSAGE);
					    values.put("isselect", SelectorType.SELETED);	
						cEappApp.dbManager.insertData(TableName.MODULE, values);
					}
				}
			
			}else{
				for(int i=0;i<modules.size();i++){
					ContentValues values=new ContentValues();
					values.put("id", modules.get(i).getId());
					values.put("name", modules.get(i).getName());
					values.put("icon", modules.get(i).getIcon());
					values.put("home", ModuleInteger.MESSAGE);		
					values.put("isselect", SelectorType.SELETED);				
					cEappApp.dbManager.insertData(TableName.MODULE, values);
					
				}
			}	
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
		*/
		cEappApp.dbManager.deleteData(TableName.MODULE, null, null);
		
		for(int i=0;i<modules.size();i++){
			ContentValues values=new ContentValues();
			values.put("id", modules.get(i).getId());
			values.put("name", modules.get(i).getName());
			values.put("icon", modules.get(i).getIcon());
			values.put("home", ModuleInteger.MESSAGE);		
			values.put("isselect", SelectorType.SELETED);				
			cEappApp.dbManager.insertData(TableName.MODULE, values);
			
		}
		
		try{
			Cursor c=cEappApp.dbManager.queryData2Cursor("select * from "+ TableName.MODULE+ " where isselect="+SelectorType.SELETED, null);
			for(int i=0;i<c.getCount();i++){
				c.moveToPosition(i);
				
				if(c.getInt(c.getColumnIndex("id"))==ModuleInteger.PERSONAL){
					person_module_entity=new Module();
					
					person_module_entity.setId(c.getInt(c.getColumnIndex("id")));
					person_module_entity.setName(c.getString(c.getColumnIndex("name")));
					person_module_entity.setIcon(c.getInt(c.getColumnIndex("icon")));
					person_module_entity.setHome(c.getInt(c.getColumnIndex("home")));
					person_module_entity.setSelect(c.getInt(c.getColumnIndex("isselect")));
					
				}else{
					module_entity=new Module();
					module_entity.setId(c.getInt(c.getColumnIndex("id")));
					module_entity.setName(c.getString(c.getColumnIndex("name")));
					module_entity.setIcon(c.getInt(c.getColumnIndex("icon")));
					module_entity.setHome(c.getInt(c.getColumnIndex("home")));
					module_entity.setSelect(c.getInt(c.getColumnIndex("isselect")));
					localmodules.add(module_entity);
				}
				
				
			}
			if(cEappApp.getRole()!=RoleType.MANAGER){
				localmodules.add(person_module_entity);
			}
			
			
			module_entity=new Module();
			module_entity.setId(100);
			module_entity.setIcon(-1);
			module_entity.setName("设置");
			module_entity.setHome(-1);
			module_entity.setSelect(SelectorType.SELETED);
			
			localmodules.add(module_entity);
			
			menu_adapter=new ListViewMenuAdapter(BaseMenuActivity.this, localmodules, R.layout.menu_item);
			menulist.setAdapter(menu_adapter);
			
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initBaseView(){

		
		menulist=(ListView)findViewById(R.id.menulist);
		btn_top_left=(Button)findViewById(R.id.btn_top_left);	
		btn_top_right=(Button)findViewById(R.id.btn_top_right);
		btn_top_right_before=(Button)findViewById(R.id.btn_right_before);
		title_name=(TextView)findViewById(R.id.title_name);
		menu_title=(TextView)findViewById(R.id.menu_title);
		arrow=(ImageView)findViewById(R.id.arrow);
		pop_menu=(LinearLayout)findViewById(R.id.pop_menu);
		logout=(Button)findViewById(R.id.logout);
		
		//设置menu标题
		menu_title.setText(cEappApp.getTermcnf().getName());
		
		
		logout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				exitdialog=new AlertDialog.Builder(BaseMenuActivity.this).setTitle("提示")
						.setMessage("确认注销吗？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								exitdialog.dismiss();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								exitdialog.dismiss();
								openProgressDialog("正在注销...");
								
								loginOut=new LoginOut();
								
								loginOut.setUser(cEappApp.getUser());
								
								loginOut.setPass(cEappApp.getPass());
								
								
								NetUtil.post(Constant.BASE_URL, loginOut, cEappApp.getCurrentHandler(), MessageWhat.LOGOUT_RESP);
							}
						}).create();
				exitdialog.show();
			}
		});
	}
	
	
	
	
	public void baseSetContentView(int layoutResId) {   
		
		LinearLayout llContent = (LinearLayout) findViewById(R.id.content);    
		LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);   
		llContent.addView(v);   
	} 
	public void initValues() {
		
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// 将menu的宽度设置为屏幕宽度减去menuPadding
		menuParams.width = screenWidth - menuPadding;
		// 左边缘的值赋值为menu宽度的负数
		leftEdge = -menuParams.width;
		// menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
		menuParams.leftMargin = leftEdge;
		// 将content的宽度设置为屏幕宽度
		content.getLayoutParams().width = screenWidth;
	}
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	public boolean wantToShowContent() {
		
		return xUp - xDown < 0 && isMenuVisible;
	}
	
	public boolean wantToShowMenu() {
		
		return xUp - xDown > 0 && !isMenuVisible;
	}
	
	public boolean shouldScrollToMenu() {
		
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}
	
	public boolean shouldScrollToContent() {
		
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}
	
	public void scrollToMenu() {
		
		new ScrollTask().execute(30);
	}
	
	public void scrollToContent() {
		
		new ScrollTask().execute(-30);
	}

	public void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	public int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}
	public void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}
	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
		
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}
	public void sleep(long millis) {
		
		try {
			
			Thread.sleep(millis);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	public void doScroll(int moduleid){
		if(cEappApp.getHomeID()==moduleid){
			if(cEappApp.isFirstScroll()){
				scrollToMenu();
				cEappApp.setFirstScroll(false);
			}
		}
	}

}
