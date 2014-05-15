package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.Module;
import com.google.gson.annotations.Expose;

public class GetSchoolModuleListResp extends PacketResp {

	@Expose
	private List<Module> modules;
	
	public GetSchoolModuleListResp(){
		setCmd(HttpDefine.GET_SCHOOLMODULE_LIST_RESP);
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	
}
