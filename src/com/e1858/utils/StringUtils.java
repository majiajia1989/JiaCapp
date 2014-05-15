package com.e1858.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils { 

	public static List<Long>  ConvetDataType(String sourceData){
		
		List<Long> list=new ArrayList<Long>();
		String data[]=sourceData.split(",");
		for(int i=0;i<data.length;i++){
			long data_item=Long.parseLong(data[i]);
			list.add(data_item);
		}
		return list;
		
	}
	
	public static StringBuilder  ConvertDataType(List<Long> sourceData){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<sourceData.size();i++){
			sb.append(sourceData.get(i).toString()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb;
	}

	
	public static List<Long> ConvertDataType(long[] sourceData){
		List<Long> list=new ArrayList<Long>();
		for(int i=0;i<sourceData.length;i++){
			long data_item=sourceData[i];
			list.add(data_item);
		}	
		return list;
	}
	
	public static long[]  convertDataType(List<Long> sourceData){
		long[] ids=new long[sourceData.size()];
		for(int i=0;i<sourceData.size();i++){
			ids[i]=sourceData.get(i);
		}
		return ids;
	}

	
}
