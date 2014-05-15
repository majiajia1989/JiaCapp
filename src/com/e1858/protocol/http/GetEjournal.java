package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetEjournal extends PacketRequest {


	@Expose
	private long ejournal;
	public GetEjournal(){
		setCmd(HttpDefine.GET_EJOURNAL);
	}
	public long getEjournal() {
		return ejournal;
	}
	public void setEjournal(long ejournal) {
		this.ejournal = ejournal;
	}
	
	
	
}
