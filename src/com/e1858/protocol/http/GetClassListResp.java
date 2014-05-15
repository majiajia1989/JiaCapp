package com.e1858.protocol.http;
import java.util.List;

import com.e1858.protocol.ClassBase;
import com.google.gson.annotations.Expose;

public class GetClassListResp extends PacketResp {

	@Expose
	private List<ClassBase> classes;
	public GetClassListResp(){
		setCmd(HttpDefine.GET_CLASS_LIST_RESP);
	}
	public List<ClassBase> getClasses() {
		return classes;
	}
	public void setClasses(List<ClassBase> classes) {
		this.classes = classes;
	}
	
	
}
