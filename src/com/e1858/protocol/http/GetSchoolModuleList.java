package com.e1858.protocol.http;


public class GetSchoolModuleList extends PacketRequest {



	public GetSchoolModuleList(){
		setCmd(HttpDefine.GET_SCHOOLMODULE_LIST);
	}
	
}
