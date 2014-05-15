package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetClassList extends PacketRequest{


	@Expose
	private long dep;

	public GetClassList(){
		setCmd(HttpDefine.GET_CLASS_LIST);
	}

	public long getDep() {
		return dep;
	}

	public void setDep(long dep) {
		this.dep = dep;
	}

	


	
	
}
