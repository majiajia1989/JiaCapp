package com.e1858.protocol.http;

import com.e1858.protocol.Notice;
import com.google.gson.annotations.Expose;

public class GetNoticeResp extends PacketResp {

	@Expose
	private Notice notice;
	
	public GetNoticeResp(){
		setCmd(HttpDefine.GET_NOTICE_RESP);
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	
	
	
}
