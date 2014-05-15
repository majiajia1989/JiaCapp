package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetStudentInfo extends PacketRequest {


	@Expose
	private long student;

	
	public GetStudentInfo(){
		setCmd(HttpDefine.GET_STUDENTINFO);
	}


	public long getStudent() {
		return student;
	}


	public void setStudent(long student) {
		this.student = student;
	}




	

	
}
