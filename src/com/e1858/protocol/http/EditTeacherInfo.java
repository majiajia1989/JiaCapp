package com.e1858.protocol.http;

import com.e1858.protocol.Teacher;
import com.google.gson.annotations.Expose;


public class EditTeacherInfo extends PacketRequest {
	

	@Expose
	private Teacher teacher;

	
	public EditTeacherInfo(){
		setCmd(HttpDefine.EDIT_TEACHERINFO);
	}
	public Teacher getTeacher() {
		return teacher;
	}


	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	

}
