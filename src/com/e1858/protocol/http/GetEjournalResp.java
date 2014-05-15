package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.Ejournal;
import com.google.gson.annotations.Expose;

public class GetEjournalResp extends PacketResp {

	@Expose
	private Ejournal ejournal;
	public GetEjournalResp(){
		
		setCmd(HttpDefine.GET_EJOURNAL_RESP);
	}
	public Ejournal getEjournal() {
		return ejournal;
	}
	public void setEjournal(Ejournal ejournal) {
		this.ejournal = ejournal;
	}
	
	
	
	
}
