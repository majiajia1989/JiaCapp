package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.SchoolBase;
import com.google.gson.annotations.Expose;

public class GetSchoolListResp extends PacketResp {

	@Expose
	private List<SchoolBase> schools;
	
	public GetSchoolListResp(){
		setCmd(HttpDefine.GET_SCHOOL_LIST_RESP);
	}
	public List<SchoolBase> getSchools() {
		return schools;
	}
	public void setSchools(List<SchoolBase> schools) {
		this.schools = schools;
	}
	
	
}
