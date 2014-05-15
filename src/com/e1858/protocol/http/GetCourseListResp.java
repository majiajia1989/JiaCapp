package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.Course;
import com.google.gson.annotations.Expose;

public class GetCourseListResp extends PacketResp {

	@Expose
	private List<Course> courses; 
	
	public GetCourseListResp(){
		setCmd(HttpDefine.GET_COURSE_LIST_RESP);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}


	
}
