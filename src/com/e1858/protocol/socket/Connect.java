package com.e1858.protocol.socket;

import android.util.Log;

import com.google.gson.annotations.Expose;

public class Connect extends PacketRequest {

	@Expose
	private long user;
	@Expose
	private String pass;
	
	public Connect(){
		setName("Connect");
		setCmd(SocketDefine.CONNECT);
	}

	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder body = new StringBuilder();
        body.append(getName()).append(":").append(System.getProperty( "line.separator"));
        body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty( "line.separator"));
        body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty( "line.separator"));
        body.append("\t").append("user:").append(user).append(System.getProperty( "line.separator"));
        body.append("\t").append("pass:").append(pass).append(System.getProperty( "line.separator"));
      
        Log.v("connect", body.toString());
        
        return body.toString();
	}
	
	
}
