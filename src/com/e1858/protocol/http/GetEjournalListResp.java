package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.EjournalBase;
import com.google.gson.annotations.Expose;

public class GetEjournalListResp extends PacketResp {

	
	@Expose
	private List<EjournalBase> ejournals;
	
	public GetEjournalListResp(){
		setCmd(HttpDefine.GET_EJOURNAL_LIST_RESP);
	}

	public List<EjournalBase> getEjournals() {
		return ejournals;
	}

	public void setEjournals(List<EjournalBase> ejournals) {
		this.ejournals = ejournals;
	}

	


	
}
