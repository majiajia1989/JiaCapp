package com.e1858.protocol.socket;

import com.e1858.protocol.Msg;
import com.google.gson.annotations.Expose;

public class PushMsg extends PacketRequest {

	@Expose
	private Msg msg;
	
	public PushMsg(){
		setName("GetPushMessage");
		setCmd(SocketDefine.PUSH_MSG);
	}
	public Msg getMsg() {
		return msg;
	}
	public void setMsg(Msg msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		  StringBuilder body = new StringBuilder();
	        body.append(getName()).append(":").append(System.getProperty( "line.separator"));
	        body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty( "line.separator"));
	        body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty( "line.separator"));
	        if(null != msg)
	        {
	        body.append("\t").append("msg:").append(msg).append(System.getProperty( "line.separator"));
	        }
	        return body.toString();
	}
	
	
	
	
}
