package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetTeacherList extends PacketRequest {

	@Expose
	private long dep;

	public GetTeacherList(){
		setCmd(HttpDefine.GET_TEACHER_LIST);
	}

	public long getDep() {
		return dep;
	}

	public void setDep(long dep) {
		this.dep = dep;
	}


	

	


}
