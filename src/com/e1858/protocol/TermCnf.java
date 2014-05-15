package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class TermCnf implements Serializable{

	@Expose
	private long id;
	@Expose
	private String name;
	@Expose
	private String start;
	@Expose
	private String end;
	@Expose
	private String firstDate;
	@Expose
	private int weekCnt;
	@Expose
	private int maxSession;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public int getWeekCnt() {
		return weekCnt;
	}
	public void setWeekCnt(int weekCnt) {
		this.weekCnt = weekCnt;
	}
	public int getMaxSession() {
		return maxSession;
	}
	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}
	
	
	

}
