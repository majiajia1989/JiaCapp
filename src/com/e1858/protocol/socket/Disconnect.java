package com.e1858.protocol.socket;


public class Disconnect extends PacketRequest
{
	public Disconnect()
	{
		setName("Disconnect");
		setCmd(SocketDefine.DISCONNECT);
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		StringBuilder body = new StringBuilder();
		body.append(getName()).append(":").append(System.getProperty("line.separator"));
		body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty("line.separator"));
		body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty("line.separator"));
		return body.toString();
	}

}
