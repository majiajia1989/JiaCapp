package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class ChangePass extends PacketRequest {

	@Expose
	private String newPass;
	
	public ChangePass(){
		setCmd(HttpDefine.CHANGE_PASS);
	}
	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	
	
}
