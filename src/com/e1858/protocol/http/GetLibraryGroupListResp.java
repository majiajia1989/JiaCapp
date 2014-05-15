package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.LibGroup;
import com.google.gson.annotations.Expose;

public class GetLibraryGroupListResp extends PacketResp {

	@Expose
	private List<LibGroup> groups;
	public GetLibraryGroupListResp(){
		setCmd(HttpDefine.GET_LIBRARYGROUP_LIST_RESP);
	}
	public List<LibGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<LibGroup> groups) {
		this.groups = groups;
	}
	
	
}
