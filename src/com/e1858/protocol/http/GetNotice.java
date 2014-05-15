package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;



public class GetNotice extends PacketRequest {

	@Expose
	private long notice;
	
	public GetNotice(){
		setCmd(HttpDefine.GET_NOTICE);
	}

	public long getNotice() {
		return notice;
	}

	public void setNotice(long notice) {
		this.notice = notice;
	}

	
	


	
	
}
