package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.DepBase;
import com.google.gson.annotations.Expose;

public class GetDepartmentListResp extends PacketResp{

	@Expose
	private List<DepBase> deps;
	public GetDepartmentListResp(){
		setCmd(HttpDefine.GET_DEPARTMENT_LIST_RESP);
	}
	public List<DepBase> getDeps() {
		return deps;
	}
	public void setDeps(List<DepBase> deps) {
		this.deps = deps;
	}
	
	
}
