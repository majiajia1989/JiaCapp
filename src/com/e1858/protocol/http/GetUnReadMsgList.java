package com.e1858.protocol.http;



public class GetUnReadMsgList extends PacketRequest {




	public GetUnReadMsgList(){
		
		setCmd(HttpDefine.GET_UNREADMSG_LIST);
	}


}
