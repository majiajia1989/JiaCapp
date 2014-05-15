package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class SendMsgResp extends PacketResp {

	@Expose
	private long task;
	
	public SendMsgResp(){
		setCmd(HttpDefine.SEND_MSG_RESP);
	}

	public long getTask() {
		return task;
	}

	public void setTask(long task) {
		this.task = task;
	}

	
}
