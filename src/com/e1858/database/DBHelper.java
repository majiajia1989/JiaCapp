package com.e1858.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库帮助类
 * @author jia
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	protected Context context;
	 // 用户数据库文件的版本
    protected static final int DB_VERSION = 1;
    
    protected static String DB_PATH = "/data/data/com.e1858.ceapp/databases/";
    /*
     * //如果把数据库文件存放在SD卡的话 private static String DB_PATH =
     * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
     * "/arthurcn/drivertest/packfiles/";       
     */
    protected static String DB_NAME = "ceapp.db";
    
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	public DBHelper(Context context,String name,int version){
		
		this(context,name,null,version);
	}

	public DBHelper(Context context,String name){
		
		this(context,name,DB_VERSION);
	}
	
	public DBHelper(Context context){
		
		this(context,DB_PATH+DB_NAME);
	}
	
	//创建数据库
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/**
		 * 创建数据库        
		 */
		//收件箱短消息
		db.execSQL("CREATE TABLE IF NOT EXISTS  "+ TableName.SHORT_MSG+"(id INTEGER PRIMARY KEY," +
							"sender TEXT,contentType INTEGER," +
							"contentCode INTEGER,content TEXT," +
							"time TEXT,unread INTEGER)");		
		//短消息回复 
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.RESHORT_MSG+"(id INTEGER PRIMARY KEY," +
							"msgID INTEGER,contentType INTEGER," +
							"contentCode INTEGER,content TEXT," +
							"time TEXT,status INTEGER)");
	
		//已发送消息
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.SUBMIT_MSG+"(id INTEGER PRIMARY KEY," +
							"mode INTEGER,receiver TEXT,contentType INTEGER," +
							"contentCode INTEGER,content TEXT," +
							"time TEXT," +
							"status INTEGER)");
		//已发送公告 ----加时间
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.NOTICE+"(id INTEGER PRIMARY KEY," +
							"title TEXT,content TEXT," +
							"url TEXT,begin TEXT," +
							"end TEXT,time TEXT,status INTEGER)");
		
		//公告
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.LNOTICE+"(id INTEGER PRIMARY KEY," +
				"title TEXT,time TEXT)");
		
		//电子校刊
		db.execSQL("CREATE TABLE IF NOT EXISTS  "+ TableName.EJOURNAL+"(id INTEGER PRIMARY KEY,"+
							"title TEXT)");		
		//班级课程表
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.CLASS_SHEDULE+"(courseID INTEGER PRIMARY KEY," +
				"classID INTEGER,termID INTEGER)");	
		//课程信息
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.COURSE+"(id INTEGER PRIMARY KEY," +
				"name TEXT,teacher TEXT,comefrom INTEGER)");
		//课程安排
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.COURSE_TIMES+"(id INTEGER," +
							"classroom TEXT," +
							"weeks TEXT," +
							"weekday INTEGER," +
							"start INGEGER,end INTEGER)");
		//模块
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableName.MODULE +"(id INTEGER PRIMARY KEY," +
							"name TEXT," +
							"icon INTEGER," +
							"home INTEGER," +
							"isselect INTEGER)");
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//删除数据库之后重建
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.SHORT_MSG);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.RESHORT_MSG);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.SUBMIT_MSG);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.NOTICE);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.EJOURNAL);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.CLASS_SHEDULE);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.COURSE);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.COURSE_TIMES);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.LNOTICE);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.MODULE);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.LIBRARY);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.LIBRARY_GROUP);
		db.execSQL("DROP TABLE IF EXISTS "+ TableName.LIBWEB);
		onCreate(db);
	}
}
