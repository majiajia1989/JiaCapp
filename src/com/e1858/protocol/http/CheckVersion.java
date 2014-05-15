package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class CheckVersion extends PacketRequest {

	@Expose
	private String mobileOS;
	@Expose
	private long currentVer;	
	@Expose
	private long school;
	
	public CheckVersion(){
		setCmd(HttpDefine.CHECK_VERSION);
	}

	public String getMobileOS() {
		return mobileOS;
	}
	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}
	public long getCurrentVer() {
		return currentVer;
	}
	public void setCurrentVer(long currentVer) {
		this.currentVer = currentVer;
	}

	public long getSchool() {
		return school;
	}

	public void setSchool(long school) {
		this.school = school;
	}
	
}
