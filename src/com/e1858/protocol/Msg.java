package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Msg implements Serializable{

	@Expose
	private long msgID;
	
	@Expose
	private  UserBase sender;
		
	@Expose
	private int contentType;
	
	@Expose
	private int contentCode;
	
	@Expose
	private String content;
	
	@Expose
	private long module;
	
	@Expose
	private String time;

	private int    unread;
	
	public long getMsgID() {
		return msgID;
	}

	public void setMsgID(long msgID) {
		this.msgID = msgID;
	}
	public UserBase getSender() {
		return sender;
	}

	public void setSender(UserBase sender) {
		this.sender = sender;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public int getContentCode() {
		return contentCode;
	}

	public void setContentCode(int contentCode) {
		this.contentCode = contentCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getModule() {
		return module;
	}

	public void setModule(long module) {
		this.module = module;
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	
	
	
	
}
