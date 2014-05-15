package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;



public class AddCourseTimesResp extends PacketResp {

	@Expose
	private long newID;
	
	public AddCourseTimesResp(){
		setCmd(HttpDefine.ADD_COURSETIMES_RESP);
	}

	public long getNewID() {
		return newID;
	}

	public void setNewID(long newID) {
		this.newID = newID;
	}

	

	
	
}
