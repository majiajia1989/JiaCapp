package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public abstract class PacketResp extends Packet
{
	@Expose
	private int		status	= 0;
	@Expose
	private String	err	= "";

	public PacketResp()
	{
		setCmd(0);
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	

}
