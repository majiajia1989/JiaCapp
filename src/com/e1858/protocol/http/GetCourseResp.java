package com.e1858.protocol.http;
import com.e1858.protocol.Course;
import com.google.gson.annotations.Expose;

public class GetCourseResp extends PacketResp {

	
	@Expose
	private Course course;
	
	public GetCourseResp(){
		setCmd(HttpDefine.GET_COURSE_RESP);
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
}
