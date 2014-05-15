package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;



public class GetUnReadNoticeList extends PacketRequest {

	@Expose
	private long id;

	public GetUnReadNoticeList(){
		setCmd(HttpDefine.GET_UNREADNOTICE_LIST);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
}
