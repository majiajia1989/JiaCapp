package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class NoticeBase implements Serializable{

	@Expose
	private long id;
	@Expose
	private String title;
	@Expose
	private String issuedDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}
	
	
	
}
