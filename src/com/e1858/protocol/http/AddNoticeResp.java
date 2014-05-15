package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class AddNoticeResp extends PacketResp {

	@Expose
	private long notice;
	
	public AddNoticeResp(){
		setCmd(HttpDefine.ADD_NOTICE_RESP);
	}

	public long getNotice() {
		return notice;
	}

	public void setNotice(long notice) {
		this.notice = notice;
	}

	

	

	
	
}
