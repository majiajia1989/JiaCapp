package com.e1858.protocol.http;

import com.e1858.protocol.Teacher;
import com.google.gson.annotations.Expose;

public class GetTeacherInfoResp extends PacketResp {

	@Expose
	private Teacher teacher;
	
	public GetTeacherInfoResp(){
		setCmd(HttpDefine.GET_TEACHERINFO_RESP);
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	
}
