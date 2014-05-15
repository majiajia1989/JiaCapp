package com.e1858.protocol.http;

public class ChangePassResp extends PacketResp {

	public ChangePassResp(){
		setCmd(HttpDefine.CHANGE_PASS_RESP);
	}
}
