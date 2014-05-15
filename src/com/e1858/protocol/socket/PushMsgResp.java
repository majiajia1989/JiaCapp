package com.e1858.protocol.socket;

import com.google.gson.annotations.Expose;

public class PushMsgResp extends PacketResp {

	@Expose
	private long msgID;
	public PushMsgResp(){
		setName("GetPushMessageResp");
		setCmd(SocketDefine.PUSH_MSG_RESP);
	}
	public long getMsgID() {
		return msgID;
	}
	public void setMsgID(long msgID) {
		this.msgID = msgID;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		 StringBuilder body = new StringBuilder();
	        
		 	body.append(getName()).append(":").append(System.getProperty( "line.separator"));
	        
	        body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty( "line.separator"));
	        
	        body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty( "line.separator"));
	        
	        body.append("\t").append("status:").append(getStatus()).append(System.getProperty( "line.separator"));
	        
	        body.append("\t").append("errText:").append(getErrText()).append(System.getProperty( "line.separator"));
	        
	        body.append("\t").append("msgID:").append(msgID).append(System.getProperty( "line.separator"));
			
	        return body.toString();
	}
	
	
}
