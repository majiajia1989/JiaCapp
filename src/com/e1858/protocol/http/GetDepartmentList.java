package com.e1858.protocol.http;



public class GetDepartmentList extends PacketRequest {

	
	public GetDepartmentList(){
		setCmd(HttpDefine.GET_DEPARTMENT_LIST);
	}
	
	
	
}
