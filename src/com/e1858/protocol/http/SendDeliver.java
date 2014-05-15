package com.e1858.protocol.http;

import com.e1858.protocol.Deliver;
import com.google.gson.annotations.Expose;

public class SendDeliver extends PacketRequest {

	@Expose
	private Deliver deliver;
	public SendDeliver(){
		setCmd(HttpDefine.SEND_DELIVER);
	}
	public Deliver getDeliver() {
		return deliver;
	}
	public void setDeliver(Deliver deliver) {
		this.deliver = deliver;
	}
	
}
