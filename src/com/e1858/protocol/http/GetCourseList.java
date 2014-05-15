package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;



public class GetCourseList extends PacketRequest {

	@Expose
	private long id;

	public GetCourseList(){
		setCmd(HttpDefine.GET_COURSE_LIST);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	

	
}
