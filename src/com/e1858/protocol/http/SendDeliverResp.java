package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class SendDeliverResp extends PacketResp {

	@Expose
	private long deliver;
	public SendDeliverResp(){
		setCmd(HttpDefine.SEND_DELIVER_RESP);
	}
	public long getDeliver() {
		return deliver;
	}
	public void setDeliver(long deliver) {
		this.deliver = deliver;
	}
	
}
