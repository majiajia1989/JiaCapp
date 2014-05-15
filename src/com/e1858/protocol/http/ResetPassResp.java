package com.e1858.protocol.http;


public class ResetPassResp extends PacketResp {
	public ResetPassResp(){
		setCmd(HttpDefine.RESET_PASS_RESP);
	}
}
