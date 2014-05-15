package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;


public class GetEjournalList extends PacketRequest {

	@Expose
	private long id;
	
	public GetEjournalList(){
		setCmd(HttpDefine.GET_EJOURNAL_LIST);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


}
