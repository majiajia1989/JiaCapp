package com.e1858.protocol.http;

public class UploadResp extends PacketResp {

	
	public UploadResp(){
		setCmd(HttpDefine.UPLOAD_RESP);
	}
}
