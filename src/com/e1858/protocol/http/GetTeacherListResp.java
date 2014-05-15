package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.TeaBase;
import com.google.gson.annotations.Expose;

public class GetTeacherListResp extends PacketResp {

	@Expose
	private List<TeaBase> teachers;
	
	public GetTeacherListResp(){
		setCmd(HttpDefine.GET_TEACHER_LIST_RESP);
	}

	public List<TeaBase> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeaBase> teachers) {
		this.teachers = teachers;
	}
	
	
}
