package com.e1858.protocol.http;

public class LoginOutResp extends PacketResp {

	public LoginOutResp(){
		setCmd(HttpDefine.LOGOUT_RESP);
	}
	
}
