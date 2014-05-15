package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.StuBase;
import com.google.gson.annotations.Expose;

public class GetStudentListResp extends PacketResp {

	@Expose
	private List<StuBase> students;
	
	public GetStudentListResp(){
		setCmd(HttpDefine.GET_STUDENT_LIST_RESP);
	}

	public List<StuBase> getStudents() {
		return students;
	}

	public void setStudents(List<StuBase> students) {
		this.students = students;
	}

	
	
	
}
