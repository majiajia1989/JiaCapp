package com.e1858.protocol.socket;

import android.util.Log;

import com.google.gson.annotations.Expose;

public class ConnectResp extends PacketResp {
	
	@Expose
	private int msgCnt;
	
	public ConnectResp(){
		setName("ConnectResp");
		setCmd(SocketDefine.CONNECT_RESP);
	}



	public int getMsgCnt() {
		return msgCnt;
	}



	public void setMsgCnt(int msgCnt) {
		this.msgCnt = msgCnt;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
			StringBuilder body = new StringBuilder();
		    body.append(getName()).append(":").append(System.getProperty("line.separator"));
	        body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty("line.separator"));
	        body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty("line.separator"));
	        body.append("\t").append("status:").append(getStatus()).append(System.getProperty("line.separator"));
	        body.append("\t").append("errText:").append(getErrText()).append(System.getProperty("line.separator"));	  
	        body.append("\t").append("msgCnt:").append(msgCnt).append(System.getProperty("line.separator"));
	        Log.v("ConnectResp", body.toString());
	        return body.toString();
	}
	
	
	
	
}
