package com.e1858.protocol.http;

import com.e1858.protocol.Student;
import com.google.gson.annotations.Expose;

public class GetStudentInfoResp extends PacketResp {

	@Expose
	private Student student;
	
	public GetStudentInfoResp(){
		setCmd(HttpDefine.GET_STUDENTINFO_RESP);
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	
	
}
