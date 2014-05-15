package com.e1858.protocol.http;

import com.e1858.protocol.SubmitMsg;
import com.google.gson.annotations.Expose;

public class SendMsg extends PacketRequest {

	@Expose
	private SubmitMsg submitMsg;
	
	public SendMsg(){
		setCmd(HttpDefine.SEND_MSG);
	}

	public SubmitMsg getSubmitMsg() {
		return submitMsg;
	}

	public void setSubmitMsg(SubmitMsg submitMsg) {
		this.submitMsg = submitMsg;
	}

	
	
}
