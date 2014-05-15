package com.e1858.protocol.http;

import com.e1858.protocol.Course;
import com.google.gson.annotations.Expose;

public class AddCourse extends PacketRequest {
	@Expose
	private Course course;

	public AddCourse(){
		setCmd(HttpDefine.ADD_COURSE);
	}
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
}
