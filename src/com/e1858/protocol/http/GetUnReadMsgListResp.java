package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.Msg;
import com.google.gson.annotations.Expose;

public class GetUnReadMsgListResp extends PacketResp {

	@Expose
	private List<Msg> msgs;
	
	public GetUnReadMsgListResp(){
		setCmd(HttpDefine.GET_UNREADMSG_LIST_RESP);
	}

	public List<Msg> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Msg> msgs) {
		this.msgs = msgs;
	}

	
	
	
	
}
