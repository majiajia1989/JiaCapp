package com.e1858.utils;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 
 * @author jia
 * 返回键按2次，退出
 */

public class Exit {


	private boolean isExit = false;  

	
	private Runnable task = new Runnable() {  
		
		public void run() {  
			
			isExit = false;  	
		}  

	}; 
	
 

	public void doExitInOneSecond() {  

		isExit = true;  
		
		HandlerThread thread = new HandlerThread("doTask");  

		thread.start();  
		new Handler(thread.getLooper()).postDelayed(task, 5000);  
		 
	}  

	public boolean isExit() {  
		return isExit;  	
	}  
	public void setExit(boolean isExit) {  
		this.isExit = isExit;  
	}  


}
