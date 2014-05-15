package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetCourse extends PacketRequest {



	@Expose
	private long course;

	
	public GetCourse(){
		setCmd(HttpDefine.GET_COURSE);
	}


	public long getCourse() {
		return course;
	}


	public void setCourse(long course) {
		this.course = course;
	}


	

	
	
}
