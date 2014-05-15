package com.e1858.protocol.socket;


public class ActiveTestResp extends PacketResp
{

	public ActiveTestResp()
	{
		setName("ActiveTestResp");
		setCmd(SocketDefine.ACTIVE_TEST_RESP);
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
