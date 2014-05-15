package com.e1858.common;

import android.app.ActivityManager;
import android.content.Context;

public class UIHelper {
		
		public final static String TAG="UIHelper";
		
		
		public final static String packageName="com.e1858.ui";
		//根据传递的值不同，进入到不同的页面
		
		public static String  doIntent(int id){
			String intent="";
			switch(id){		
			case ModuleInteger.SETTING:
				intent=packageName+".Setting";
				break;
			case ModuleInteger.MESSAGE:
				intent=packageName+".MessageActivity";
				break;
			case ModuleInteger.PERSONAL:
				intent=packageName+".PersonalActivity";
				break;
			case ModuleInteger.NOTICE:
				intent=packageName+".NoticeActivity";
				break;
			case ModuleInteger.EJOURNAL:
				intent=packageName+".EjournalActivity";
				break;
			case ModuleInteger.SHEDULE:
//				intent=packageName+".Schedules";//
				intent=packageName+".SchoolTimeTable";
				break;
			case ModuleInteger.KNOWLEGE:
				intent=packageName+".KnowlegeActivity";
				break;
			case ModuleInteger.BOOK:
				intent=packageName+".BookActivity";
				break;
			case ModuleInteger.NEWS:
				intent=packageName+".NewsActivity";
				break;
			case ModuleInteger.ARTICLE:
				intent=packageName+".ArticleActivity";
				break;
				
			default:
				break;
			}
			return intent;
		}
	
}
