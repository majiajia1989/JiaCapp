package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class ResetPass extends PacketRequest {
	
	@Expose
	private String userName;
	@Expose
	private long school;
	@Expose
	private String email;
	
	public ResetPass(){
		setCmd(HttpDefine.RESET_PASS);
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




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}



	


	

	
}
