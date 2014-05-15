package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class Login extends PacketRequest{
	@Expose
	private String userName;
	@Expose
	private long school;
	
	public Login(){
		setCmd(HttpDefine.LOGIN);
	}
	
	
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public long getSchool() {
		return school;
	}
	public void setSchool(long school) {
		this.school = school;
	}
	
	
	

	
	
}
