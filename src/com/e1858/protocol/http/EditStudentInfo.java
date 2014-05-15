package com.e1858.protocol.http;

import com.e1858.protocol.Student;
import com.google.gson.annotations.Expose;



public class EditStudentInfo extends PacketRequest {



	@Expose
	private Student student;

	public EditStudentInfo(){
		setCmd(HttpDefine.EDIT_STUDENTINFO);
	}
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	
}
