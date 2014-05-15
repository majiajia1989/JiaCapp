package com.e1858.protocol.http;

import com.e1858.protocol.CourseTimes;
import com.google.gson.annotations.Expose;

public class AddCourseTimes extends PacketRequest {

	@Expose
	private CourseTimes courseTimes;

	public AddCourseTimes(){
		setCmd(HttpDefine.ADD_COURSETIMES);
	}


	public CourseTimes getCourseTimes() {
		return courseTimes;
	}

	public void setCourseTimes(CourseTimes courseTimes) {
		this.courseTimes = courseTimes;
	}
	
	
}
