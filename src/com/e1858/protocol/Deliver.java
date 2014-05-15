package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Deliver implements Serializable{

	@Expose
	private long deliverID;
	
	@Expose
	private long msgID;
	
	@Expose
	private String imei;
	
	@Expose
	private String mobile;
	
	@Expose
	private int contentType;
	
	@Expose
	private int contentCode;
	
	@Expose
	private String content;
	
	
	public long getDeliverID() {
		return deliverID;
	}
	public void setDeliverID(long deliverID) {
		this.deliverID = deliverID;
	}
	public long getMsgID() {
		return msgID;
	}
	public void setMsgID(long msgID) {
		this.msgID = msgID;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	
	

}
