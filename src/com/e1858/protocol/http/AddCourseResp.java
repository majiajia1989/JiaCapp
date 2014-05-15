package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class AddCourseResp extends PacketResp {

	
	@Expose
	private long newID;
	
	public AddCourseResp(){
		setCmd(HttpDefine.ADD_COURSE_RESP);
	}

	public long getNewID() {
		return newID;
	}

	public void setNewID(long newID) {
		this.newID = newID;
	}

	
	
	
}
