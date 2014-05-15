package com.e1858.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import com.e1858.CEappApp;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.database.TableName;

public class FileUtil
{
	public static File UriToFile(Uri uri, Activity activity)
	{
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor.getString(actual_image_column_index);
		return new File(img_path);
	}

	public static String readFromAssets(String fileName, Context context)
	{
		String Result = "";
		try
		{
			InputStream inStream = context.getAssets().open(fileName);
			int size = inStream.available();
			byte[] buffer = new byte[size];
			inStream.read(buffer);

			inStream.close();
			Result = new String(buffer).replaceAll("\r", "");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return Result;
	}

	public static String getExtension(String filename)
	{
		String result = "";
		if ((filename != null) && (filename.length() > 0))
		{
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1)))
			{
				result = filename.substring(i + 1);
			}
		}
		if (result.length() > 0)
		{
			result = "." + result;
		}
		return result;
	}

	public static String getFileNameNoEx(String filename)
	{
		if ((filename != null) && (filename.length() > 0))
		{
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length())))
			{
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static int freeSpaceOnSdCard()
	{
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFree = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize());
		return (int) sdFree;
	}

	public static void updateFileTime(String dir, String fileName)
	{

		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();

		file.setLastModified(newModifiedTime);

	}

	public static void removeCache(String dirPath)
	{
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null)
		{
			return;
		}
		long dirSize = 0;

		for (int i = 0; i < files.length; i++)
		{
			// if (files[i].getName().contains(Constant.WHOLESALE_CONV))
			{
				dirSize += files[i].length();
			}
		}

		int i = 0;
		while (dirSize > Constant.CACHE_SIZE || Constant.FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSdCard())
		{
			dirSize = dirSize - files[i].length();
			files[i].delete();
			i++;
		}
	}

	public static class FileLastModifSort implements Comparator<File>
	{
		public int compare(File arg0, File arg1)
		{
			if (arg0.lastModified() > arg1.lastModified())
			{
				return 1;
			}
			else if (arg0.lastModified() == arg1.lastModified())
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
	}

	public static String convertUrlToFileName(String url)
	{
		String[] strs = url.split("/");
		return strs[strs.length - 1] + Constant.WHOLESALE_CONV;
	}

	public static String getCachDirectory()
	{
		String dir = getSDPath() + "/" + Constant.CACHDIR;
		String substr = dir.substring(0, 4);
		if (substr.equals("/mnt"))
		{
			dir = dir.replace("/mnt", "");
		}
		if (!(new File(dir)).exists())
		{
			(new File(dir)).mkdirs();
		}
		return dir;
	}

	public static String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory().getAbsoluteFile();// 获取根目录
		}
		if (sdDir != null)
		{
			return sdDir.toString();
		}
		else
		{
			return "";
		}
	}

	public File creatSDDir(String dir)
	{
		File dirFile = new File(getSDPath() + dir + File.separator);
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}

	public static void saveBmpToSd(Bitmap bm, String filename)
	{

		if (bm == null)
		{
			Log.i(Constant.TAG_UTILS_FILE, " 图片对象为null");
			return;
		}

		if (Constant.FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSdCard())
		{
			Log.i(Constant.TAG_UTILS_FILE, "SD卡空间不足！");
			return;
		}

		File file = new File(getCachDirectory() + "/" + filename);

		try
		{
			if (file.exists())
			{
				file.delete();
			}
			{
				file.createNewFile();
				OutputStream outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				// Log.i(Constant.TAG_UTILS_FILE, "图片保存到SD卡成功！");
			}
		}
		catch (Exception e)
		{
			Log.e(Constant.TAG_UTILS_FILE, e.getMessage());
		}
	}

	public static String readFileData(String fileName, Context context, String encoding)
	{
		String res = "";
		try
		{
			FileInputStream fin = context.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, encoding);
			fin.close();
		}
		catch (Exception e)
		{
		}
		return res;
	}

	public static void writeFileData(String fileName, String message, String encoding, Context context)
	{
		try
		{
			FileOutputStream fout = context.openFileOutput(fileName, context.MODE_APPEND);
			byte[] bytes = EncodingUtils.getBytes(message, encoding);
			fout.write(bytes);
			fout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public  static String SDPATH;   //用于存sd card的文件的路径
	 
 	public String getSDPATH() {
 		return SDPATH;
	 }
	 public void setSDPATH(String sDPATH) {
		 SDPATH = sDPATH;
	 }	 
	 
	 /**
	  * 构造方法
	  * 获取SD卡路径
	  */
	 public FileUtil() {
		 //获得当前外部存储设备的目录
		 SDPATH=Environment.getExternalStorageDirectory()+"/";
		 System.out.println("sd card's directory path:"+SDPATH);
	 }
	 
	 
	 /**
	  * 在SD卡上创建文件
	  * @throws IOException 
	  */
	 public File createSDFile(String fileName) throws IOException{
		 File file=new File(SDPATH+fileName);
		 Log.v("sdpath","========"+ SDPATH+fileName);
		 
		 try{
			 file.createNewFile();
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		
		 return file;
	 }
	 /**
	  * 在SD卡上创建目录
	  */
	 public File createSDDir(String dirName){
		 File dir=new File(SDPATH+dirName);
		 System.out.println("storage device's state :"+Environment.getExternalStorageState());
		 
		 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			 dir.mkdirs();
		 }
		 return dir;
	 }
	 
	 /**
	  * 判断SD卡上的文件夹是否存在
	  */
	 public boolean isFileExist(String fileName){
		 File file=new File(SDPATH+fileName);
		 return file.exists();
	 }
	 
	 public static StringBuffer readTxtFromSD(String path,long id){
		StringBuffer sb = new StringBuffer();
		try{
				
			File file = new File(path+id+".txt");
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file); 
				int c; 
				while ((c = fis.read()) != -1) { 
					sb.append((char) c); 
				} 
				fis.close(); 
			}
			
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return sb;
		}
	 
	 
	 /**
	  * 将一个inputSteam里面的数据写入到SD卡中
	  */
	 public File write2SDFromInput(CEappApp ceapp,int length,String path,String fileName,InputStream inputStream){
		 File file=null;
		 OutputStream output=null;
		 try {
			 //创建SD卡上的目录 
			 File tempf=createSDDir(path);	  
			 file=createSDFile(path+fileName);			 			 
			 if(file.exists()){	
				 output=new FileOutputStream(file);		 
				 byte buffer[]=new byte[1024];
				 int len=-1;  
				 int downSize=0;	  
				 while((len=(inputStream.read(buffer))) !=-1){  	  
					  output.write(buffer, 0, len);
					  Log.v("len", "=="+len);
                	  downSize+=len; 
                	  int progreeValue=(int) (((float) downSize /length) * 100);
                	  Log.v("progreeValue", "=="+progreeValue);
                	  if(progreeValue<0){
                		  ceapp.setProgress(0);
                	  }else{
                		  ceapp.setProgress((int) (((float) downSize /length) * 100));  
                	  }	 
                	  Message message = ceapp.getCurrentHandler().obtainMessage(MessageWhat.DOWN_UPDATE, "");
                	  ceapp.getCurrentHandler().sendMessage(message);    
                  }
				 if(downSize==length){
					  Message message_over = ceapp.getCurrentHandler().obtainMessage(MessageWhat.DOWN_OVER, "");
	  				  ceapp.getCurrentHandler().sendMessage(message_over);	
	  				  
				 }else if(downSize<length){
					 file.delete();
					 Message message_over = ceapp.getCurrentHandler().obtainMessage(MessageWhat.DOWNFIELD, "");
	  				 ceapp.getCurrentHandler().sendMessage(message_over);	 
				 }
  				  output.flush();
			 }
			 
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }finally{
			 try {
				 output.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }	  
		 return file;
	 }	
}
