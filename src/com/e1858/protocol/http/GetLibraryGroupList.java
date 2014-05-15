package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetLibraryGroupList extends PacketRequest {

	@Expose
	private long module;
	
	
	public GetLibraryGroupList(){
		setCmd(HttpDefine.GET_LIBRARYGROUP_LIST);
		
	}


	public long getModule() {
		return module;
	}


	public void setModule(long module) {
		this.module = module;
	}
	
	
}
