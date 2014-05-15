package com.e1858.protocol.http;



public class LoginOut extends PacketRequest {


	public LoginOut(){
		setCmd(HttpDefine.LOGOUT);
	}
}
