package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetTeacherInfo extends PacketRequest {

	@Expose
	private long teacher;

	public GetTeacherInfo(){
		setCmd(HttpDefine.GET_TEACHERINFO);
	}

	public long getTeacher() {
		return teacher;
	}

	public void setTeacher(long teacher) {
		this.teacher = teacher;
	}



	
}
