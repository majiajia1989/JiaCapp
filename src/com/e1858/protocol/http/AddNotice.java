package com.e1858.protocol.http;

import com.e1858.protocol.Notice;
import com.google.gson.annotations.Expose;

public class AddNotice extends PacketRequest {

	@Expose
	private Notice notice;
	public AddNotice(){
		setCmd(HttpDefine.ADD_NOTICE);
	}
	public Notice getNotice() {
		return notice;
	}
	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	
}
