package com.e1858.protocol.http;

public class GetSchoolList extends PacketRequest {
	
	public GetSchoolList(){
		setCmd(HttpDefine.GET_SCHOOL_LIST);
	}
}
