package com.e1858.protocol.socket;

import com.google.gson.annotations.Expose;

public class PacketResp extends Packet
{
	@Expose
	private int		status	= 0;
	@Expose
	private String	errText	= "成功";

	public PacketResp()
	{
		name = "PacketResp";
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getErrText()
	{
		return errText;
	}

	public void setErrText(String errText)
	{
		this.errText = errText;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		StringBuilder body = new StringBuilder();
		body.append(getName()).append(":").append(System.getProperty("line.separator"));
		body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty("line.separator"));
		body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty("line.separator"));
		body.append("\t").append("status:").append(getStatus()).append(System.getProperty("line.separator"));
		body.append("\t").append("errText:").append(getErrText()).append(System.getProperty("line.separator"));
		return body.toString();
	}

}
