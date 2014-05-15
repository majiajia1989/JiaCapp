package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public abstract class PacketRequest extends Packet
{
	@Expose
	private long	user	= 0;
	@Expose
	private String	pass	= "";

	public PacketRequest()
	{
		setCmd(0);
	}

	
	public long getUser() {
		return user;
	}


	public void setUser(long user) {
		this.user = user;
	}



	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

}
